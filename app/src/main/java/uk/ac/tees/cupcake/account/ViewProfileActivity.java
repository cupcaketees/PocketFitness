package uk.ac.tees.cupcake.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
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
        collectionReference = FirebaseFirestore.getInstance().collection("Users");
        mFollowButton = findViewById(R.id.profile_edit_profile_button);


        if (extra != null) {
            mProfilePageUid = extra.getString("profileId");
        }

        mFollowButton.setOnClickListener(v -> followButton());
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
                                        }
                                    });
                        } else {
                            collectionReference.document(mProfilePageUid + "/Followers/" + mCurrentUser.getUid())
                                    .get()
                                    .addOnSuccessListener(documentSnapshotE -> {
                                        if (documentSnapshotE.exists()) {
                                            mFollowButton.setText(documentSnapshotE.exists() ? "Unfollow" : "Follow");
                                        }
                                    });
                        }

                    }
                });

        FirebaseFirestore.getInstance().collection("Users")
                .document(mProfilePageUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserProfile currentProfile = documentSnapshot.toObject(UserProfile.class);
                        collectionReference.document(mProfilePageUid + "/FolloweRequests/" + mCurrentUser.getUid())
                                .get()
                                .addOnSuccessListener(d -> {
                                    if (d.exists()) {
                                        mFollowButton.setText("Follow Requested");
                                    }
                                });

                        collectionReference.document(mProfilePageUid + "/Followers/" + mCurrentUser.getUid())
                                .get()
                                .addOnSuccessListener(documentSnapshotE -> {
                                    if (documentSnapshotE.exists()) {
                                        mFollowButton.setText("Following");
                                    } else {
                                        mFollowButton.setText("Follow");
                                    }
                                });
                    }
                });

        followerCountTextView.setOnClickListener(v -> IntentUtils.invokeFollowers(ViewProfileActivity.this, SearchUserFriendsActivity.class, "Followers", followerCountTextView.getText().toString(), "Following", followingCountTextView.getText().toString(), "intent", "1", "id", mProfilePageUid));
        followingCountTextView.setOnClickListener(v -> IntentUtils.invokeFollowers(ViewProfileActivity.this, SearchUserFriendsActivity.class, "Followers", followerCountTextView.getText().toString(), "Following", followingCountTextView.getText().toString(), "intent", "0", "id", mProfilePageUid));
    }

    private void followButton() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(mProfilePageUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                        if (mFollowButton.getText().toString().equals("Following")) {

                            collectionReference.document(mProfilePageUid + "/Followers/" + mCurrentUser.getUid())
                                    .get()
                                    .addOnSuccessListener(documentSnapshotT -> {

                                        if (documentSnapshotT.exists()) {

                                            // Deletes current user from viewed profile followers collection.
                                            documentSnapshotT.getReference()
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(ViewProfileActivity.this, "You have stopped following", Toast.LENGTH_SHORT).show();

                                                        // Deletes viewed profile uid from current user followers collection.
                                                        collectionReference.document(mCurrentUser.getUid() + "/Following/" + mProfilePageUid)
                                                                .delete();

                                                        mFollowButton.setText("Follow");
                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                                        }
                                    });
                        } else if (mFollowButton.getText().toString().equals("Follow Requested")) {
                            collectionReference.document(mProfilePageUid + "/FollowerRequest/" + mCurrentUser.getUid())
                                    .get()
                                    .addOnSuccessListener(documentSnapshotT -> {

                                        if (documentSnapshotT.exists()) {

                                            // Deletes current user from viewed profile followers collection.
                                            documentSnapshotT.getReference()
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(ViewProfileActivity.this, "You have stopped following", Toast.LENGTH_SHORT).show();

                                                        // Deletes viewed profile uid from current user followers collection.
                                                        collectionReference.document(mCurrentUser.getUid() + "/FollowingRequest/" + mProfilePageUid)
                                                                .delete();

                                                        mFollowButton.setText("Follow");
                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                                        }
                                    });
                        } else {
                            if (profile.isPrivateProfile()) {
                                Map<String, Object> followTimeStamp = new HashMap<>();
                                followTimeStamp.put("timestamp", FieldValue.serverTimestamp());
                                collectionReference.document(mProfilePageUid + "/FollowerRequests/" + mCurrentUser.getUid())
                                        .get()
                                        .addOnSuccessListener(documentSnapshotU -> {

                                            if (!documentSnapshotU.exists()) {
                                                documentSnapshotU.getReference()
                                                        .set(followTimeStamp)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d(TAG, "followButton: ");


                                                            Toast.makeText(ViewProfileActivity.this, "Request has been sent", Toast.LENGTH_SHORT).show();

                                                            // Adds viewed profile uid to current users following collection
                                                            collectionReference.document(mCurrentUser.getUid() + "/FollowingRequests/" + mProfilePageUid)
                                                                    .set(followTimeStamp);

                                                            mFollowButton.setText("Follow Requested");
                                                        })
                                                        .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                                            }
                                        });
                            } else {
                                collectionReference.document(mProfilePageUid + "/Followers/" + mCurrentUser.getUid())
                                        .get()
                                        .addOnSuccessListener(documentSnapshotT -> {

                                            if (documentSnapshotT.exists()) {
                                                Log.d(TAG, "followButton: hello" );

                                                // Deletes current user from viewed profile followers collection.
                                                documentSnapshotT.getReference()
                                                        .delete()
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(ViewProfileActivity.this, "You have stopped following", Toast.LENGTH_SHORT).show();

                                                            // Deletes viewed profile uid from current user followers collection.
                                                            collectionReference.document(mCurrentUser.getUid() + "/Following/" + mProfilePageUid)
                                                                    .delete();

                                                            mFollowButton.setText("Follow");
                                                        })
                                                        .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

                                            } else {
                                                Log.d(TAG, "followButton: hello2");
                                                // Adds current user to viewed profile followers collection.
                                                Map<String, Object> followTimeStamp = new HashMap<>();
                                                followTimeStamp.put("timestamp", FieldValue.serverTimestamp());

                                                documentSnapshotT.getReference()
                                                        .set(followTimeStamp)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d(TAG, "followButton: ");


                                                            Toast.makeText(ViewProfileActivity.this, "You are now following", Toast.LENGTH_SHORT).show();

                                                            // Adds viewed profile uid to current users following collection
                                                            collectionReference.document(mCurrentUser.getUid() + "/Following/" + mProfilePageUid)
                                                                    .set(followTimeStamp);

                                                            mFollowButton.setText("Unfollow");
                                                        })
                                                        .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                                            }
                                        });
                            }
                        }

                    }

//                    if (documentSnapshotT.exists()) {
//                        UserProfile profile = documentSnapshotT.toObject(UserProfile.class);
//
//                        if (profile.isPrivateProfile()) {
//                            collectionReference.document(mProfilePageUid + "/FollowerRequests/" + mCurrentUser.getUid())
//                                    .get()
//                                    .addOnSuccessListener(documentSnapshotU -> {
//
//                                        if (documentSnapshotU.exists()) {
//                                            // Deletes current user from viewed profile followers collection.
//                                            documentSnapshotU.getReference()
//                                                    .delete()
//                                                    .addOnSuccessListener(aVoid -> {
//                                                        Toast.makeText(ViewProfileActivity.this, "Follow Request cancelled", Toast.LENGTH_SHORT).show();
//
//                                                        // Deletes viewed profile uid from current user followers collection.
//                                                        collectionReference.document(mCurrentUser.getUid() + "/FollowingRequests/" + mProfilePageUid)
//                                                                .delete();
//
//                                                        mFollowButton.setText("Follow");
//                                                    })
//                                                    .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//
//                                        }
//                                        else {
//                                            // Adds current user to viewed profile followers collection.
//                                            Map<String, Object> followTimeStamp = new HashMap<>();
//                                            followTimeStamp.put("timestamp", FieldValue.serverTimestamp());
//
//                                            documentSnapshotU.getReference()
//                                                    .set(followTimeStamp)
//                                                    .addOnSuccessListener(aVoid -> {
//                                                        Log.d(TAG, "followButton: ");
//
//
//                                                        Toast.makeText(ViewProfileActivity.this, "Request has been sent", Toast.LENGTH_SHORT).show();
//
//                                                        // Adds viewed profile uid to current users following collection
//                                                        collectionReference.document(mCurrentUser.getUid() + "/FollowingRequests/" + mProfilePageUid)
//                                                                .set(followTimeStamp);
//
//                                                        mFollowButton.setText("Follow Requested");
//                                                    })
//                                                    .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//                                        }
//                                    });
//
//
//                        } else {
//                            collectionReference.document(mProfilePageUid + "/Followers/" + mCurrentUser.getUid())
//                                    .get()
//                                    .addOnSuccessListener(documentSnapshot -> {
//
//                                        if (documentSnapshot.exists()) {
//
//                                            // Deletes current user from viewed profile followers collection.
//                                            documentSnapshot.getReference()
//                                                    .delete()
//                                                    .addOnSuccessListener(aVoid -> {
//                                                        Toast.makeText(ViewProfileActivity.this, "You have stopped following", Toast.LENGTH_SHORT).show();
//
//                                                        // Deletes viewed profile uid from current user followers collection.
//                                                        collectionReference.document(mCurrentUser.getUid() + "/Following/" + mProfilePageUid)
//                                                                .delete();
//
//                                                        mFollowButton.setText("Follow");
//                                                    })
//                                                    .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//
//                                        } else {
//                                            // Adds current user to viewed profile followers collection.
//                                            Map<String, Object> followTimeStamp = new HashMap<>();
//                                            followTimeStamp.put("timestamp", FieldValue.serverTimestamp());
//
//                                            documentSnapshot.getReference()
//                                                    .set(followTimeStamp)
//                                                    .addOnSuccessListener(aVoid -> {
//                                                        Log.d(TAG, "followButton: ");
//
//
//                                                        Toast.makeText(ViewProfileActivity.this, "You are now following", Toast.LENGTH_SHORT).show();
//
//                                                        // Adds viewed profile uid to current users following collection
//                                                        collectionReference.document(mCurrentUser.getUid() + "/Following/" + mProfilePageUid)
//                                                                .set(followTimeStamp);
//
//                                                        mFollowButton.setText("Unfollow");
//                                                    })
//                                                    .addOnFailureListener(e -> Toast.makeText(ViewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//                                        }
//                                    });
//                        }
//                    }
//                });

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



