package uk.ac.tees.cupcake.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.EditProfileActivity;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.feed.FeedAdapter;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.friends.SearchUserFriendsActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class ProfileFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;

    private FirebaseUser mCurrentUser;
    private DocumentReference mDocumentRef;
    private ListenerRegistration profileListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDocumentRef = FirebaseFirestore.getInstance().collection("Users/").document(mCurrentUser.getUid());

        initialise();
        getPosts();

        return rootView;
    }

    private void initialise() {
        recyclerView = rootView.findViewById(R.id.my_profile_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        Button editProfile = rootView.findViewById(R.id.profile_edit_profile_button);

        editProfile.setOnClickListener(v -> IntentUtils.invokeBaseView(getContext(), EditProfileActivity.class));
    }

    private void getPosts() {
        List<Post> posts = new ArrayList<>();
        FeedAdapter feedAdapter = new FeedAdapter(posts);
        recyclerView.setAdapter(feedAdapter);

        mDocumentRef.collection("User Posts")
                .orderBy("timeStamp", Query.Direction.DESCENDING).limit(100)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    for(DocumentSnapshot documentSnapshot : documentSnapshots){
                        Post currentItem = documentSnapshot.toObject(Post.class);
                        posts.add(currentItem);
                        feedAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Add profile listener on start
     */
    @Override
    public void onStart() {
        super.onStart();

        profileListener = mDocumentRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e == null && documentSnapshot.exists()){
                // Initialise
                TextView profileNameTextView = rootView.findViewById(R.id.profile_name_text_view);
                TextView dateJoinedTextView = rootView.findViewById(R.id.profile_date_joined_text_view);
                TextView emailAddressTextView = rootView.findViewById(R.id.profile_email_text_view);
                TextView bioTextView = rootView.findViewById(R.id.profile_bio_text_view);
                TextView textFollowing = rootView.findViewById(R.id.profile_following_count_text_view);
                TextView textFollowers = rootView.findViewById(R.id.profile_followers_count_text_view);
                TextView numberFollowers = rootView.findViewById(R.id.profile_followers_title_text_view);
                TextView numberFollowing = rootView.findViewById(R.id.profile_following_title_text_view);

                CircleImageView profilePictureImageView = rootView.findViewById(R.id.profile_profile_picture_image_view);
                ImageView coverPhotoImageView = rootView.findViewById(R.id.profile_cover_photo_image_view);

                // Set Values
                UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);

                if (userProfile.getBio() != null) {
                    bioTextView.setText(userProfile.getBio());
                }

                if (userProfile.getProfilePictureUrl() != null) {
                    Picasso.with(rootView.getContext()).load(userProfile.getProfilePictureUrl()).into(profilePictureImageView);
                }

                if (userProfile.getCoverPhotoUrl() != null) {
                    Picasso.with(rootView.getContext()).load(userProfile.getCoverPhotoUrl()).into(coverPhotoImageView);
                }

                String profileName = userProfile.getFirstName() + " " + userProfile.getLastName();
                String dateJoined = "Joined " + userProfile.getAccountCreated();

                profileNameTextView.setText(profileName);
                emailAddressTextView.setText(mCurrentUser.getEmail());
                dateJoinedTextView.setText(dateJoined);

                CollectionReference followingPath = FirebaseFirestore.getInstance().collection("Users/" + mCurrentUser.getUid() + "/Following/");
                CollectionReference followerPath = FirebaseFirestore.getInstance().collection("Users/" + mCurrentUser.getUid() + "/Followers/");

                followingPath.addSnapshotListener(((documentSnapshots, t) -> {
                    if (documentSnapshots == null || documentSnapshots.isEmpty()) {
                        textFollowing.setText("0");
                    } else {
                        textFollowing.setText(String.valueOf(documentSnapshots.size()));
                    }
                }));

                followerPath.addSnapshotListener(((documentSnapshots, t) -> {
                    if (documentSnapshots == null || documentSnapshots.isEmpty()) {
                        textFollowers.setText("0");
                    } else {
                        textFollowers.setText(String.valueOf(documentSnapshots.size()));
                    }

                }));

                numberFollowers.setOnClickListener(v -> IntentUtils.invokeFollowers(getContext(), SearchUserFriendsActivity.class, "Followers",textFollowers.getText().toString(),"Following",textFollowing.getText().toString(),"intent","1"));
                numberFollowing.setOnClickListener(v -> IntentUtils.invokeFollowers(getContext(), SearchUserFriendsActivity.class, "Followers",textFollowers.getText().toString(),"Following",textFollowing.getText().toString(), "intent","0"));
            }
        });
    }

    /**
     * Removes profile listener on stop
     */
    @Override
    public void onStop() {
        super.onStop();
        profileListener.remove();
    }
}