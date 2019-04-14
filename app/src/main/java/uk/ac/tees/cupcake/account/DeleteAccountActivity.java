package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.login.LoginActivity;

/**
 * Delete Account Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class DeleteAccountActivity extends AppCompatActivity {

    private EditText mPasswordEditText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mCurrentUser;
    String mProvider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        setTitle("Delete My Account");

        mPasswordEditText = findViewById(R.id.delete_account_password_edit_text);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        mProvider = mCurrentUser.getProviders().get(0);

        if(mProvider.equalsIgnoreCase("password")){
            mPasswordEditText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Requires re authentication by user. Google acc / email pw
     *
     * Deletes all images; this includes profile pictures, cover photos, todo/ images used on posts from storage.
     * Deletes all posts made by user; includes record of all likes from each post.
     * User account will be removed from previous followers list, and record of all users followers will be deleted.
     * Deletes user account.
     * On success takes user to login activity, On failure prompts user with appropriate message.
     */
    public void deleteAccount(View view){

        // Reference to current users document.
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                                                               .collection("Users")
                                                               .document(mCurrentUser.getUid());

        // Deletes users posts including each posts like collection.
        documentReference.collection("User Posts")
                         .get()
                         .addOnSuccessListener(documentSnapshots -> {
                             // Iterate through current user posts documents.
                             for(DocumentSnapshot documentSnapshot : documentSnapshots){

                                 // Iterate through Likes collection in each user post document and deletes each document.
                                 documentSnapshot.getReference()
                                                 .collection("Likes")
                                                 .get()
                                                 .addOnSuccessListener(documentSnapshots1 -> {
                                                     for(DocumentSnapshot documentSnapshot1 : documentSnapshots1){
                                                         documentSnapshot1.getReference().delete();
                                                     }
                                                 });

                                 // Deletes each document in the current user posts collection.
                                 documentSnapshot.getReference().delete();
                             }
                         });

        // Storage ref
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        // Delete user profile picture from storage
        storageReference.child("profile pictures/" + mCurrentUser.getUid())
                .delete();

        // Delete cover photo image from storage
        storageReference.child("cover photos/" + mCurrentUser.getUid())
                .delete();

        // Deletes all images used in posts from user
        // TODO

        // Delete current users followers collection
        documentReference.collection("Followers")
                         .get()
                         .addOnSuccessListener(documentSnapshots -> {
                             // Iterate through each follower document and deletes.
                             for(DocumentSnapshot documentSnapshot : documentSnapshots){
                                 documentSnapshot.getReference().delete();
                             }
                         });

        // Delete current users following collection
        documentReference.collection("Following")
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    // Iterate through each following document and deletes.
                    for(DocumentSnapshot documentSnapshot : documentSnapshots){
                        documentSnapshot.getReference().delete();
                    }
                });

        // Deletes current user database document
        documentReference.delete()
                         .addOnSuccessListener(aVoid -> Toast.makeText(DeleteAccountActivity.this, "User information has been deleted successfully", Toast.LENGTH_SHORT).show())
                         .addOnFailureListener(e -> Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());


        AuthCredential credential = null;

        switch(mProvider){
            case "google.com":
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                if(account != null){
                    credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                }else{
                    //todo re auth user - not required atm.
                    return;
                }

                break;
            case "email":
                String userInputCurrentPassword = mPasswordEditText.getText().toString().trim();

                if(TextUtils.isEmpty(userInputCurrentPassword)) {
                    Toast.makeText(DeleteAccountActivity.this, "You must enter your current password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                credential = EmailAuthProvider.getCredential(mCurrentUser.getEmail(), userInputCurrentPassword);
                break;
            default:
                break;
        }

        // Authenticate user,  on success deletes their account.
        mCurrentUser.reauthenticate(credential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mCurrentUser.delete()
                                        .addOnSuccessListener(aVoid1 -> Toast.makeText(DeleteAccountActivity.this, "Your account has been deleted.", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    /**
     * Adds listener on activity
     */
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Removes listener on activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}

