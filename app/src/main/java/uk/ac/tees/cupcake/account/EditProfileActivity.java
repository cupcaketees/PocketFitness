package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import uk.ac.tees.cupcake.R;
/**
 * Edit Profile Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView mProfilePictureImageView;
    private ImageView mCoverPhotoImageView;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mBioEditText;

    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;

    private final String KEY_COVER_PHOTO = "coverPhotoUrl";
    private final String KEY_PROFILE_PICTURE = "profilePictureUrl";

    private final int PROFILE_PICTURE_REQUEST_CODE = 1;
    private final int COVER_PHOTO_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");

        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mProfilePictureImageView = findViewById(R.id.edit_profile_profile_picture_image_view);
        mCoverPhotoImageView = findViewById(R.id.edit_profile_cover_photo_image_view);
        mFirstNameEditText = findViewById(R.id.edit_profile_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.edit_profile_last_name_edit_text);
        mBioEditText = findViewById(R.id.edit_profile_bio_edit_text);

        setValues();
    }

    public void changeProfilePictureOnClick(View view){
        cropImageIntent(1,1, PROFILE_PICTURE_REQUEST_CODE);
    }

    public void changeCoverPhotoOnClick(View view) {
        cropImageIntent(21,10, COVER_PHOTO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);

        if(resultCode == RESULT_OK){
            Uri resultUri = result.getUri();

            if(requestCode == PROFILE_PICTURE_REQUEST_CODE){
                saveImage("profile pictures", resultUri, KEY_PROFILE_PICTURE);
                mProfilePictureImageView.setImageURI(resultUri);

            }else if (requestCode == COVER_PHOTO_REQUEST_CODE) {
                saveImage("cover photos", resultUri, KEY_COVER_PHOTO);
                mCoverPhotoImageView.setImageURI(resultUri);
            }

        }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
            Toast.makeText(EditProfileActivity.this, result.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void saveChangesOnClick(View view){
        String firstNameUserInput = mFirstNameEditText.getText().toString().trim();
        String lastNameUserInput = mLastNameEditText.getText().toString().trim();
        String bioUserInput = mBioEditText.getText().toString().trim();

        String result = validateUserInput(firstNameUserInput, lastNameUserInput, bioUserInput);

        if(!result.isEmpty()){
            Toast.makeText(EditProfileActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> value = new HashMap<>();
        value.put("firstName" , firstNameUserInput);
        value.put("lastName", lastNameUserInput);
        value.put("bio", bioUserInput);

        mFireStore.collection("Users")
                  .document(mAuth.getCurrentUser().getUid())
                  .set(value, SetOptions.merge())
                  .addOnSuccessListener(aVoid -> {
                      Toast.makeText(EditProfileActivity.this, "Profile information saved successfully", Toast.LENGTH_SHORT).show();
                      onBackPressed();
                  })
                  .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void setValues() {
        mFireStore.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                        mFirstNameEditText.setText(profile.getFirstName());
                        mLastNameEditText.setText(profile.getLastName());
                        mBioEditText.setText(R.string.profile_bio_temp_value_text_view_text);

                        if(profile.getBio() != null){
                            mBioEditText.setText(profile.getBio());
                        }

                        if(profile.getProfilePictureUrl() != null){
                            Picasso.with(EditProfileActivity.this)
                                   .load(profile.getProfilePictureUrl())
                                   .into(mProfilePictureImageView);
                        }
                        if(profile.getCoverPhotoUrl() != null){
                            Picasso.with(EditProfileActivity.this)
                                   .load(profile.getCoverPhotoUrl())
                                   .into(mCoverPhotoImageView);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void cropImageIntent(int x, int y, int requestCode){
        Intent intent = CropImage.activity()
                                 .setGuidelines(CropImageView.Guidelines.ON)
                                 .setMultiTouchEnabled(true)
                                 .setAspectRatio(x,y)
                                 .getIntent(EditProfileActivity.this);

        startActivityForResult(intent, requestCode);
    }

    private void saveImage(String reference, Uri imageUri, final String key){
        StorageReference storageRef = mStorage.getReference()
                                              .child(reference)
                                              .child(mAuth.getCurrentUser().getUid());
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Map<String, Object> value = new HashMap<>();
                    value.put(key , taskSnapshot.getDownloadUrl().toString());

                    mFireStore.collection("Users")
                             .document(mAuth.getCurrentUser().getUid())
                             .set(value, SetOptions.merge())
                             .addOnSuccessListener(aVoid -> {
                                 Toast.makeText(EditProfileActivity.this, "Your image been saved successfully.", Toast.LENGTH_SHORT).show();
                             })
                             .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private String validateUserInput(String userInputFirstName, String userInputLastName, String userInputBio){
        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(userInputFirstName)) {
            sb.append("You cant leave first name field empty. ");
        }
        if(TextUtils.isEmpty(userInputLastName)) {
            sb.append("You cant leave last name field empty. ");
        }
        if(TextUtils.isEmpty(userInputBio)) {
            sb.append("You cant leave bio field empty.");
        }
        return sb.toString();
    }
}
