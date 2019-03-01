package uk.ac.tees.cupcake.account;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import uk.ac.tees.cupcake.R;

/*
 * Profile Page
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class ProfilePageActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private TextView mProfileNameTextView;
    private TextView mLocationTextView;

    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private String currentUserId;
    private ListenerRegistration profileListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mProfileNameTextView = findViewById(R.id.profile_name_text_view);
        mLocationTextView = findViewById(R.id.profile_location_text_view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();

        TextView memberSinceTextView = findViewById(R.id.profile_member_since_text_view);
        TextView emailTextView = findViewById(R.id.profile_email_text_view);

        //String accountCreated = String.valueOf(mAuth.getCurrentUser().getMetadata().getCreationTimestamp());
        //memberSinceTextView.setText();

        emailTextView.setText(mAuth.getCurrentUser().getEmail());

        loadProfileData();
    }

    /*
     * Listener update profile page if any changes are made to the database
     */
    @Override
    protected void onStart(){
        super.onStart();
        profileListener = firebaseFirestore.collection("Users")
                         .document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(e != null){
                    String errorMessage = e.getMessage();
                    Toast.makeText(ProfilePageActivity.this, "Error " + errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(documentSnapshot.exists()){
                    UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                    String firstName = userProfile.getFirstName();
                    String lastName = userProfile.getLastName();
                    String location = userProfile.getLocation();
                    mProfileNameTextView.setText(firstName + " " + lastName);
                    mLocationTextView.setText(location);
                }
            }
        });
    }

    /*
     * Removes profile page listener on stop to reduce bandwidth
     */
    @Override
    protected void onStop(){
        super.onStop();
        profileListener.remove();
    }

    /*
     * Gets current user profile data and updates activity accordingly (currently only gets first and last name)
     */
    public void loadProfileData(){

        firebaseFirestore.collection("Users")
                .document(currentUserId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                            String firstName = userProfile.getFirstName();
                            String lastName = userProfile.getLastName();
                            String location = userProfile.getLocation();
                            mProfileNameTextView.setText(firstName + " " + lastName);
                            mLocationTextView.setText(location);
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

    public void followButton(){
        // TODO
    }

}
