package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.healthstats.HealthStatsSetupActivity;

/**
 * Setup Profile Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class SetupProfileActivity extends AppCompatActivity {

    private CircleImageView mProfilePictureImageView;
    private String mProfileImageUrl;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        
        mProfilePictureImageView = findViewById(R.id.setup_profile_profile_picture_image_view);
    }

    public void addPhotoOnClick(View view){
        CropImage.activity()
                 .setGuidelines(CropImageView.Guidelines.ON)
                 .setAspectRatio(1,1)
                 .start(SetupProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    
        ProgressBar progressBar = findViewById(R.id.setup_profile_loading_view);
        progressBar.setVisibility(View.VISIBLE);
        mProfilePictureImageView.setVisibility(View.INVISIBLE);
        
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                StorageReference profilePicturesRef = mStorage.getReference()
                                                              .child("profile pictures")
                                                              .child(mAuth.getCurrentUser().getUid());
                profilePicturesRef.putFile(resultUri)
                                  .addOnSuccessListener(taskSnapshot -> {
                                      progressBar.setVisibility(View.GONE);
                                      mProfilePictureImageView.setVisibility(View.VISIBLE);
                                      AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
                                      alphaAnim.setDuration(1000);
                                      mProfilePictureImageView.startAnimation(alphaAnim);
                                      mProfilePictureImageView.setImageURI(resultUri);
                                      mProfileImageUrl = taskSnapshot.getDownloadUrl().toString();
                                      Toast.makeText(SetupProfileActivity.this, "Your profile picture has been saved successfully.", Toast.LENGTH_SHORT).show();
                                  })
                                  .addOnFailureListener(e -> Toast.makeText(SetupProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());

            }  else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(SetupProfileActivity.this, result.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    
    /**
     * Invoked upon clicking the next button.
     */
    public void nextOnClick(View view) {
        EditText firstName = findViewById(R.id.setup_profile_first_name_edit_text);
        EditText lastName = findViewById(R.id.setup_profile_last_name_edit_text);
        
        String firstNameUserInput = firstName.getText().toString().trim();
        String lastNameUserInput = lastName.getText().toString().trim();
    
        String result = validateUserInput(firstNameUserInput, lastNameUserInput);

        if(!result.isEmpty()){
            Toast.makeText(SetupProfileActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }
    
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Calendar.getInstance().getTime());
    
        UserProfile profile = new UserProfile(firstNameUserInput, lastNameUserInput, mProfileImageUrl, date);
    
        startActivity(new Intent(SetupProfileActivity.this, HealthStatsSetupActivity.class)
                .putExtra("user_profile", profile));
    }

    /**
     * Validates input values passed through params are not empty.
     * @return empty string or appended message.
     */
    private String validateUserInput(String userInputFirstName, String userInputLastName){

        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(userInputFirstName)) {
            sb.append("You must enter your first name.\n");
        }
        if(TextUtils.isEmpty(userInputLastName)) {
            sb.append("You must enter your last name.");
        }
        return sb.toString();
    }
}