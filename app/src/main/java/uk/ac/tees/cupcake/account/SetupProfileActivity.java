package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/**
 * Setup Profile Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class SetupProfileActivity extends AppCompatActivity {

    private CircleImageView mProfilePictureImageView;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private String mProfileImageUrl;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFireStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mProfilePictureImageView = findViewById(R.id.setup_profile_profile_picture_image_view);
        mFirstNameEditText = findViewById(R.id.setup_profile_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.setup_profile_last_name_edit_text);

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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                StorageReference profilePicturesRef = mStorage.getReference()
                                                              .child("profile pictures")
                                                              .child(mAuth.getCurrentUser().getUid());
                profilePicturesRef.putFile(resultUri)
                                  .addOnSuccessListener(taskSnapshot -> {
                                      mProfilePictureImageView.setImageURI(resultUri);
                                      mProfileImageUrl = taskSnapshot.getDownloadUrl().toString();
                                      Toast.makeText(SetupProfileActivity.this, "Your profile picture has been saved successfully.", Toast.LENGTH_SHORT).show();
                                  })
                                  .addOnFailureListener(e -> Toast.makeText(SetupProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());

            }  else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                String error = result.getError().getMessage();
                Toast.makeText(SetupProfileActivity.this, error, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void finishSetupOnClick(View view){
        saveData();
    }

    private void saveData(){
        String firstNameUserInput = mFirstNameEditText.getText().toString().trim();
        String lastNameUserInput = mLastNameEditText.getText().toString().trim();

        String result = validateUserInput(firstNameUserInput, lastNameUserInput);

        if(!result.isEmpty()){
            Toast.makeText(SetupProfileActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(mAuth.getCurrentUser().getMetadata().getCreationTimestamp());

        UserProfile profile = new UserProfile(firstNameUserInput, lastNameUserInput, mProfileImageUrl,date);

        mFireStore.collection("Users")
                  .document(mAuth.getCurrentUser().getUid())
                  .set(profile)
                  .addOnSuccessListener(aVoid -> {
                      Toast.makeText(SetupProfileActivity.this, "Profile information saved successfully", Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(SetupProfileActivity.this, HomeActivity.class));
                  })
                  .addOnFailureListener(e -> Toast.makeText(SetupProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    /**
     * Validates input values passed through params are not empty.
     * @return string with appropriate message.
     */
    private String validateUserInput(String userInputFirstName, String userInputLastName){

        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(userInputFirstName)) {
            sb.append("You must enter your first name. ");
        }
        if(TextUtils.isEmpty(userInputLastName)) {
            sb.append("You must enter your last name.");
        }
        return sb.toString();
    }
}