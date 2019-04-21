package uk.ac.tees.cupcake.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        Button editProfile = rootView.findViewById(R.id.profile_edit_profile_button);

        editProfile.setOnClickListener(v -> IntentUtils.invokeBaseView(rootView.getContext(), EditProfileActivity.class));
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

    @Override
    public void onStart() {
        super.onStart();

        TextView followerCountTextView = rootView.findViewById(R.id.profile_followers_count_text_view);
        TextView followingCountTextView = rootView.findViewById(R.id.profile_following_count_text_view);

        // Profile information snapshot listener
        mDocumentRef.addSnapshotListener(getActivity(), (documentSnapshot, e) -> {
            if(e != null){
                e.printStackTrace();
                return;
            }

            if(documentSnapshot.exists()){
                UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                initAndSetProfileData(userProfile);
            }

        });

        // Followers count snapshot listener.
        mDocumentRef.collection("Followers")
                .addSnapshotListener(getActivity(), (documentSnapshots, e) -> {
                    if(e != null){
                        e.printStackTrace();
                        return;
                    }
                    String output = "Followers " + documentSnapshots.size();
                    followerCountTextView.setText(output);
                });

        // Following count snapshot listener.
        mDocumentRef.collection("Following")
                .addSnapshotListener(getActivity(), (documentSnapshots, e) -> {
                    if(e != null){
                        e.printStackTrace();
                        return;
                    }
                    String output = "Following " + documentSnapshots.size();
                    followingCountTextView.setText(output);
                });

        followerCountTextView.setOnClickListener(v -> IntentUtils.invokeFollowers(getContext(), SearchUserFriendsActivity.class, "Followers",followerCountTextView.getText().toString(),"Following",followingCountTextView.getText().toString(),"intent","1"));
        followingCountTextView.setOnClickListener(v -> IntentUtils.invokeFollowers(getContext(), SearchUserFriendsActivity.class, "Followers",followerCountTextView.getText().toString(),"Following",followingCountTextView.getText().toString(), "intent","0"));
    }

    private void initAndSetProfileData(UserProfile profile){

        // Initialise
        TextView profileNameTextView = rootView.findViewById(R.id.profile_name_text_view);
        TextView dateJoinedTextView = rootView.findViewById(R.id.profile_date_joined_text_view);
        TextView emailAddressTextView = rootView.findViewById(R.id.profile_email_text_view);
        TextView bioTextView = rootView.findViewById(R.id.profile_bio_text_view);

        CircleImageView profilePictureImageView = rootView.findViewById(R.id.profile_profile_picture_image_view);
        ImageView coverPhotoImageView = rootView.findViewById(R.id.profile_cover_photo_image_view);

        // Set
        if (profile.getBio() != null) {
            bioTextView.setText(profile.getBio());
        }

        if (profile.getProfilePictureUrl() != null) {
            Picasso.with(rootView.getContext()).load(profile.getProfilePictureUrl()).into(profilePictureImageView);
        }

        if (profile.getCoverPhotoUrl() != null) {
            Picasso.with(rootView.getContext()).load(profile.getCoverPhotoUrl()).into(coverPhotoImageView);
        }

        String profileName = profile.getFirstName() + " " + profile.getLastName();
        String dateJoined = "Joined " + profile.getAccountCreated();

        profileNameTextView.setText(profileName);
        emailAddressTextView.setText(profile.getEmailAddress());
        dateJoinedTextView.setText(dateJoined);
    }
}