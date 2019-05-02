package uk.ac.tees.cupcake.account;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.ProfileAdapter;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.friends.SearchUserFriendsActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;


public class ViewProfileActivity extends AppCompatActivity {
    private static final String TAG = "ViewProfileActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mProfilePageUid;
    private CollectionReference collectionReference;
    private Button mFollowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_page);

        // Initialise
        Bundle extra = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        collectionReference = FirebaseFirestore.getInstance().collection("Users/");
        mFollowButton = findViewById(R.id.profile_edit_profile_button);


        if (extra != null) {
            mProfilePageUid = extra.getString("profileId");
        }

        mFollowButton.setOnClickListener(v -> followButton());

        FirebaseFirestore.getInstance().collection("Users")
                .document(mProfilePageUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                UserProfile profile = documentSnapshot.toObject(UserProfile.class);
                                if (profile.isPrivateProfile()) {
                                    collectionReference.document(mProfilePageUid + "/Followers/" + mCurrentUser.getUid())
                                            .get()
                                            .addOnSuccessListener(documentSnapshotT -> {
                                                if (documentSnapshotT.exists()) {
                                                    addPosts();
                                                }
                                            });
                                } else {
                                    addPosts();
                                }
                            }
                        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        TextView followerCountTextView = findViewById(R.id.profile_followers_count_text_view);
        TextView followingCountTextView = findViewById(R.id.profile_following_count_text_view);

        // Profile information snapshot listener.
        collectionReference.document(mProfilePageUid)
                .addSnapshotListener(ViewProfileActivity.this, (documentSnapshot, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    if (documentSnapshot.exists()) {
                        // Set profile information
                        UserProfile profile = documentSnapshot.toObject(UserProfile.class);
                        initAndSetProfileValues(profile);
                    }
                });

        // Followers count snapshot listener.
        collectionReference.document(mProfilePageUid)
                .collection("Followers")
                .addSnapshotListener(ViewProfileActivity.this, (documentSnapshots, e) -> {
                    String output = "Followers " + documentSnapshots.size();
                    followerCountTextView.setText(output);
                });

        // Following count snapshot listener.
        collectionReference.document(mProfilePageUid)
                .collection("Following")
                .addSnapshotListener(ViewProfileActivity.this, (documentSnapshots, e) -> {
                    String output = "Following " + documentSnapshots.size();
                    followingCountTextView.setText(output);
                });

        // Sets button text to correct value on start.

        FirebaseFirestore.getInstance().collection("Users")
                .document(mProfilePageUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserProfile profile = documentSnapshot.toObject(UserProfile.class);
                        if (profile.isPrivateProfile()) {
                            collectionReference.document(mProfilePageUid + "/FollowerRequests/" + mCurrentUser.getUid())
                                    .get()
                                    .addOnSuccessListener(documentSnapshotE -> {
                                        if (documentSnapshotE.exists()) {
                                            mFollowButton.setText("Follow Requested");
                                        } else {
                                            checkFollowing();
                                        }
                                    });
                        } else {
                            checkFollowing();
                        }

                    }
                });

        followerCountTextView.setOnClickListener(v -> IntentUtils.invokeFollowers(ViewProfileActivity.this, SearchUserFriendsActivity.class, "Followers", followerCountTextView.getText().toString(), "Following", followingCountTextView.getText().toString(), "intent", "1", "id", mProfilePageUid));
        followingCountTextView.setOnClickListener(v -> IntentUtils.invokeFollowers(ViewProfileActivity.this, SearchUserFriendsActivity.class, "Followers", followerCountTextView.getText().toString(), "Following", followingCountTextView.getText().toString(), "intent", "0", "id", mProfilePageUid));
    }

    private void checkFollowing() {
        collectionReference.document(mProfilePageUid + "/Followers/" + mCurrentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshotT -> {
                    if (documentSnapshotT.exists()) {
                        mFollowButton.setText("Following");
                    } else {
                        mFollowButton.setText("Follow");
                    }
                });
    }

    private void followButton() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(mProfilePageUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                        switch (mFollowButton.getText().toString()) {
                            case "Following":
                                getDatabaseInfo("/Followers/", "/Following/");
                                break;
                            case "Follow Requested":
                                getDatabaseInfo("/FollowerRequests/", "/FollowingRequests/");
                                break;
                            case "Follow":
                                Log.d(TAG, "followButton: " + profile.isPrivateProfile());
                                if (profile.isPrivateProfile()) {
                                    addDbInfo("/FollowerRequests/", "/FollowingRequests/", "Follow Requested");
                                    break;
                                }
                                addDbInfo("/Followers/", "/Following/", "Following");
                                break;
                            default:
                                Toast.makeText(this, "Error With this Request", Toast.LENGTH_SHORT).show();


                        }

                    }
                });
    }

    private void getDatabaseInfo(String followerType, String followingType) {
        collectionReference.document(mProfilePageUid + followerType + mCurrentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshotT -> {

                    if (documentSnapshotT.exists()) {

                        documentSnapshotT.getReference()
                                .delete()
                                .addOnSuccessListener(aVoid -> {

                                    Toast.makeText(ViewProfileActivity.this, "You have stopped following", Toast.LENGTH_SHORT).show();
                                    collectionReference.document(mCurrentUser.getUid() + followingType + mProfilePageUid)
                                            .delete();
                                    mFollowButton.setText("Follow");

                                })
                                .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

                    }
                });
    }

    private void addDbInfo(String followerType, String followingType, String message) {

        Map<String, Object> followTimeStamp = new HashMap<>();
        followTimeStamp.put("timestamp", FieldValue.serverTimestamp());

        collectionReference.document(mProfilePageUid + followerType + mCurrentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshotU -> {

                    if (!documentSnapshotU.exists()) {
                        documentSnapshotU.getReference()
                                .set(followTimeStamp)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "addDbInfo: hello world");
                                    Toast.makeText(ViewProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    collectionReference.document(mCurrentUser.getUid() + followingType + mProfilePageUid)
                                            .set(followTimeStamp);

                                    mFollowButton.setText(message);
                                })
                                .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
    }


    private void addPosts() {
        RecyclerView recyclerView = findViewById(R.id.my_profile_recycler_view);
        recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        List<Post> posts = new ArrayList<>();
        TreeMap<Date, Post> allPosts = new TreeMap<>();
        ProfileAdapter feedAdapter = new ProfileAdapter(posts);
        recyclerView.setAdapter(feedAdapter);

        FirebaseFirestore.getInstance().collection("Users/").document(mProfilePageUid).collection("User Posts")
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    for(DocumentSnapshot documentSnapshot : documentSnapshots){
                        Post currentItem = documentSnapshot.toObject(Post.class);
                        allPosts.put(currentItem.getTimeStamp(),currentItem);
                    }
                    posts.clear();
                    posts.addAll(allPosts.descendingMap().values());
                    feedAdapter.notifyDataSetChanged();

                });
    }

    private void initAndSetProfileValues(UserProfile profile) {
        // Initialise
        TextView profileNameTextView = findViewById(R.id.profile_name_text_view);
        TextView dateJoinedTextView = findViewById(R.id.profile_date_joined_text_view);
        TextView emailAddressTextView = findViewById(R.id.profile_email_text_view);
        TextView bioTextView = findViewById(R.id.profile_bio_text_view);

        CircleImageView profilePictureImageView = findViewById(R.id.profile_profile_picture_image_view);
        ImageView coverPhotoImageView = findViewById(R.id.profile_cover_photo_image_view);

        // Set Values

        if (profile.getBio() != null) {
            bioTextView.setText(profile.getBio());
        }

        if (profile.getProfilePictureUrl() != null) {
            Picasso.with(ViewProfileActivity.this).load(profile.getProfilePictureUrl()).into(profilePictureImageView);
        }

        if (profile.getCoverPhotoUrl() != null) {
            Picasso.with(ViewProfileActivity.this).load(profile.getCoverPhotoUrl()).into(coverPhotoImageView);
        }

        String profileName = profile.getFirstName() + " " + profile.getLastName();
        String dateJoined = "Joined " + profile.getAccountCreated();

        setTitle(profileName);
        profileNameTextView.setText(profileName);
        emailAddressTextView.setText(profile.getEmailAddress());
        dateJoinedTextView.setText(dateJoined);
    }
}



