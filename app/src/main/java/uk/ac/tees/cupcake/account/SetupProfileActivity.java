package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        mFirstNameEditText = findViewById(R.id.setup_profile_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.setup_profile_last_name_edit_text);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
    /*
     * Checks required input fields are not empty, adds data to database, return user to homepage.
     */

    public void saveProfileData(View view){
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

        String currentUserId = mAuth.getCurrentUser().getUid();

        UserProfile userProfile = new UserProfile(userInputFirstName, userInputLastName);

        //Map<String, Object> profile = new HashMap<>();
        //profile.put(KEY_FIRST_NAME, userInputFirstName);
        //profile.put(KEY_LAST_NAME, userInputLastName);

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
