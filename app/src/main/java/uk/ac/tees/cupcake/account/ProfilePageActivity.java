package uk.ac.tees.cupcake.account;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import uk.ac.tees.cupcake.R;

/*
 * Profile Page
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class ProfilePageActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private TextView mProfileNameTextView;
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mProfileNameTextView = findViewById(R.id.profile_name_text_view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        loadProfileData();
    }

    /*
     * Gets current user profile data and updates activity accordingly
     */
    public void loadProfileData(){

        String currentUserId = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users")
                .document(currentUserId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            //String firstName = documentSnapshot.getString(KEY_FIRST_NAME);
                            //String lastName = documentSnapshot.getString(KEY_LAST_NAME);

                            UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                            String firstName = userProfile.getFirstName();
                            String lastName = userProfile.getLastName();
                            mProfileNameTextView.setText(firstName + " " + lastName);
                        }else{
                            Toast.makeText(ProfilePageActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.getMessage();
                        Toast.makeText(ProfilePageActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
