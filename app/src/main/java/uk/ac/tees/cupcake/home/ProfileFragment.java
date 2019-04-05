
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import uk.ac.tees.cupcake.utils.IntentUtils;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;

    private View rootView;
    private String currentUserUid;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();

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


        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUserUid)
                .collection("User Posts")
                .orderBy("date", Query.Direction.DESCENDING).limit(100)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot imageSnapshots : documentSnapshots) {
                        Post currentItem = imageSnapshots.toObject(Post.class);
                        posts.add(currentItem);
                        feedAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUserUid)
                .addSnapshotListener(getActivity(), (documentSnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (documentSnapshot.exists()) {
                        TextView profileNameTextView = rootView.findViewById(R.id.profile_name_text_view);
                        TextView dateJoinedTextView = rootView.findViewById(R.id.profile_date_joined_text_view);
                        TextView emailAddressTextView = rootView.findViewById(R.id.profile_email_text_view);
                        TextView bioTextView = rootView.findViewById(R.id.profile_bio_text_view);
                        CircleImageView profilePictureImageView = rootView.findViewById(R.id.profile_profile_picture_image_view);
                        ImageView coverPhotoImageView = rootView.findViewById(R.id.profile_cover_photo_image_view);

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

                        profileNameTextView.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
                        emailAddressTextView.setText(mAuth.getCurrentUser().getEmail());
                        dateJoinedTextView.setText("Joined " + userProfile.getAccountCreated());
                    }
                });
    }
}