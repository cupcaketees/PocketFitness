package uk.ac.tees.cupcake.account;

import android.content.Intent;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;


public class EditProfileActivity extends AppCompatActivity {

    private StorageReference profileImageStorageReference;
    private StorageReference coverPhotoStorageReference;
    private String mProfileImageURL;
    private String mCoverPhotoURL;
    private CircleImageView mProfilePicture;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private ImageView mCoverPhoto;
    private String mCurrentUserUid;

    private final int PROFILE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");

        mProfilePicture = findViewById(R.id.edit_profile_profile_circle_image);
        mCoverPhoto = findViewById(R.id.edit_profile_cover_image_view);
        mFirstNameEditText = findViewById(R.id.edit_profile_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.edit_profile_first_name_edit_text);
        mCurrentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        profileImageStorageReference = FirebaseStorage.getInstance().getReference().child("Users profile picture");
        coverPhotoStorageReference = FirebaseStorage.getInstance().getReference().child("Users cover photo");
    }

    public void editProfilePicture(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PROFILE_PICTURE);
    }

    public void editCoverPhoto(View view){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16,9)
                .start(EditProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PROFILE_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri resultUri = data.getData();

            StorageReference path = profileImageStorageReference.child(mCurrentUserUid + ".jpg");

            path.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProfilePicture.setImageURI(resultUri);
                    Toast.makeText(EditProfileActivity.this, "Your profile picture has been saved successfully", Toast.LENGTH_LONG).show();
                    mProfileImageURL = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resultUri = result.getUri();

            StorageReference path = coverPhotoStorageReference.child(mCurrentUserUid + ".jpg");

            path.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mCoverPhoto.setImageURI(resultUri);
                    Toast.makeText(EditProfileActivity.this, "Your cover photo been saved successfully", Toast.LENGTH_LONG).show();
                    mCoverPhotoURL = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void saveChanges(View view){

        String userInputFirstName = mFirstNameEditText.getText().toString().trim();
        String userInputLastName = mLastNameEditText.getText().toString().trim();

        UserProfile userProfile = new UserProfile(mProfileImageURL, mCoverPhotoURL, userInputFirstName, userInputLastName);

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(mCurrentUserUid).set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileActivity.this, "Your profile has been saved successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProfileActivity.this, ProfilePageActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
