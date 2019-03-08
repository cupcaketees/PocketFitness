package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/*
 * Setup Profile Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class SetupProfileActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private FirebaseAuth mAuth;
    private StorageReference profileImageStorageReference;
    private String userProfileImageURL = "";
    private String currentUserId;

    private Button mAddPhotoButton;
    private CircleImageView mProfilePicture;

    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";

    private static final int IMAGE_FROM_GALLERY = 1;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        mFirstNameEditText = findViewById(R.id.setup_profile_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.setup_profile_last_name_edit_text);
        mAddPhotoButton = findViewById(R.id.setup_profile_add_photo_button);
        mProfilePicture = findViewById(R.id.setup_profile_profile_image);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        profileImageStorageReference = FirebaseStorage.getInstance().getReference().child("Profile Picture Images");

        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null){

            mImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();

                StorageReference userFilePath = profileImageStorageReference.child(currentUserId + ".jpg");

                userFilePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            mProfilePicture.setImageURI(resultUri);
                            Toast.makeText(SetupProfileActivity.this, "Profile picture saved successfully", Toast.LENGTH_SHORT).show();
                            userProfileImageURL = task.getResult().getDownloadUrl().toString();
                        }else{
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(SetupProfileActivity.this, "Error Saving image: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    /*
     * Checks required input fields are not empty, adds data to database, return user to homepage.
     */

    public void saveProfileInformation(View view){
        String userInputFirstName = mFirstNameEditText.getText().toString().trim();
        String userInputLastName = mLastNameEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userInputFirstName)){
            Toast.makeText(SetupProfileActivity.this, "You must enter your first name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userInputLastName)){
            Toast.makeText(SetupProfileActivity.this, "You must enter your last name", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile userProfile = new UserProfile(userInputFirstName, userInputLastName, userProfileImageURL);

        firebaseFirestore.collection("Users")
                         .document(currentUserId).set(userProfile)
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(SetupProfileActivity.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(SetupProfileActivity.this, HomeActivity.class));
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 String errorMessage = e.getMessage();
                                 Toast.makeText(SetupProfileActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                             }
                         });
    }

}
