package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;

/**
 * Profile Page Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class ProfilePageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ListenerRegistration mProfilePageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Listener immediately gets data from current user document and sets profile page to values
     */
    @Override
    protected void onStart(){
        super.onStart();
        String currentUserUid = mAuth.getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                         .collection("Users")
                         .document(currentUserUid)
                         .addSnapshotListener(this, (documentSnapshot, e) -> {
                             // In case something went wrong while loading
                             if(e != null){
                                 Toast.makeText(ProfilePageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                 return;
                             }

                             if(documentSnapshot.exists()){
                                 TextView profileNameTextView = findViewById(R.id.profile_name_text_view);
                                 TextView accountCreatedTextView = findViewById(R.id.profile_account_created_text_view);
                                 TextView emailAddressTextView = findViewById(R.id.profile_email_text_view);
                                 CircleImageView profileImageView = findViewById(R.id.profile_profile_image_image_view);
                                 ImageView coverPhotoImageView = findViewById(R.id.profile_cover_image_image_view);

                                 UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);

                                 if(userProfile.getProfilePictureUrl() != null){
                                     Picasso.with(ProfilePageActivity.this).load(userProfile.getProfilePictureUrl()).into(profileImageView);
                                 }

                                 if(userProfile.getCoverPhotoUrl() != null){
                                     Picasso.with(ProfilePageActivity.this).load(userProfile.getCoverPhotoUrl()).into(coverPhotoImageView);
                                 }

                                 profileNameTextView.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
                                 emailAddressTextView.setText(mAuth.getCurrentUser().getEmail());
                                 accountCreatedTextView.setText("Member since " + userProfile.getAccountCreated());
                             }
                         });
    }

    /**
     * Intent to edit profile activity
     */
    public void editProfileOnClick(View view){ startActivity(new Intent(ProfilePageActivity.this,EditProfileActivity.class)); }
}
