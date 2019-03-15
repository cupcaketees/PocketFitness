package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/*
 * Setup Profile Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class SetupProfileActivity extends AppCompatActivity {

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private CircleImageView mProfilePicture;
    private String mCurrentUserUid;

    private StorageReference profileImageStorageReference;
    private String mProfileImageURL;
    private String mAccountCreationDate;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        mFirstNameEditText = findViewById(R.id.setup_profile_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.setup_profile_last_name_edit_text);
        mProfilePicture = findViewById(R.id.setup_profile_profile_image);

        mCurrentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        profileImageStorageReference = FirebaseStorage.getInstance().getReference().child("Users profile picture");

        Date date = new Date(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp());
        mAccountCreationDate = "Member since " + DATE_FORMAT.format(date);

    }

    public void addPhoto(View view){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(SetupProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            StorageReference path = profileImageStorageReference.child(mCurrentUserUid + ".jpg");
            Uri resultUri = result.getUri();

            path.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProfilePicture.setImageURI(resultUri);
                    Toast.makeText(SetupProfileActivity.this, "Your profile picture has been saved successfully", Toast.LENGTH_LONG).show();
                    mProfileImageURL = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetupProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /*
     * On success saves user input values to current user document in user collections, return user to home activity.
     * On failure prompts user with appropriate message .
     */
    public void saveProfileInformation(View view){

        String userInputFirstName = mFirstNameEditText.getText().toString().trim();
        String userInputLastName = mLastNameEditText.getText().toString().trim();

        String result = validateUserInput(userInputFirstName, userInputLastName);

        if(!result.isEmpty()){
            Toast.makeText(SetupProfileActivity.this, result, Toast.LENGTH_LONG).show();
            return;
        }

        UserProfile userProfile = new UserProfile(userInputFirstName, userInputLastName, mProfileImageURL, mAccountCreationDate);

        FirebaseFirestore.getInstance()
                         .collection("Users")
                         .document(mCurrentUserUid).set(userProfile)
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(SetupProfileActivity.this, "Your profile has been saved successfully.", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(SetupProfileActivity.this, HomeActivity.class));
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(SetupProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         });
    }

    /*
     * Validates input values passed are not empty.
     * Returns empty string on success, otherwise a string with appropriate message.
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
