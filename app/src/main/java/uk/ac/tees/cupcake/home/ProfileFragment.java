
package uk.ac.tees.cupcake.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.EditProfileActivity;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);
        mAuth = FirebaseAuth.getInstance();

        Button editProfile = rootView.findViewById(R.id.profile_edit_profile_button);

        editProfile.setOnClickListener(v -> {
            IntentUtils.invokeBaseView(getContext(), EditProfileActivity.class);
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        String currentUserUid = mAuth.getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUserUid)
                .addSnapshotListener(getActivity(), (documentSnapshot, e) -> {
                    // In case something went wrong while loading
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


