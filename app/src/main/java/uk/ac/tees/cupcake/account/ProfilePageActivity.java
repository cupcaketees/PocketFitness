package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;

/*
 * Profile Page
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class ProfilePageActivity extends AppCompatActivity {

    private TextView mProfileNameTextView;
    private CircleImageView mProfilePictureImageView;
    private ImageView mCoverPhotoImageView;

    private TextView mEmailAddressTextView;
    private TextView mAccountCreatedTextView;

    private FirebaseAuth mAuth;
    private String mCurrentUserUid;
    private ListenerRegistration profilePageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mProfileNameTextView = findViewById(R.id.profile_name_text_view);
        mProfilePictureImageView = findViewById(R.id.profile_profile_image_image_view);
        mEmailAddressTextView = findViewById(R.id.profile_email_text_view);
        mAccountCreatedTextView = findViewById(R.id.profile_account_created_text_view);
        mCoverPhotoImageView = findViewById(R.id.edit_profile_cover_image_view);

        mAuth = FirebaseAuth.getInstance();

        mCurrentUserUid = mAuth.getCurrentUser().getUid();
        mEmailAddressTextView.setText(mAuth.getCurrentUser().getEmail());
    }

    /*
     * Updates profile page to any changes made to current user document while user is on the page.
     */
    @Override
    protected void onStart(){
        super.onStart();

        profilePageListener = FirebaseFirestore.getInstance().collection("Users").document(mCurrentUserUid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(ProfilePageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                if(documentSnapshot.exists()){
                    UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                    String firstName = userProfile.getFirstName();
                    String lastName = userProfile.getLastName();
                    String profileImage = userProfile.getProfileImageUrl();
                    String coverImage = userProfile.getCoverPhotoUrl();

                    mProfileNameTextView.setText(firstName + " " + lastName);
                    mAccountCreatedTextView.setText(userProfile.getAccountCreated());

                    if(profileImage != null){
                        Picasso.with(ProfilePageActivity.this).load(profileImage).into(mProfilePictureImageView);
                    }

                    if(coverImage != null){
                        //Picasso.with(ProfilePageActivity.this).load(coverImage).into(mCoverPhotoImageView);
                    }

                }
            }
        });
    }

    /*
     * Sends user to edit profile activity
     */
    public void editProfile(View view){
        startActivity(new Intent(ProfilePageActivity.this,EditProfileActivity.class));
    }

    /*
     * Removes profile page listener
     */
    @Override
    protected void onStop(){
        super.onStop();
        profilePageListener.remove();
    }

}
