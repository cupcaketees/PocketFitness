package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.login.LoginActivity;

/*
 * Delete Account Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class DeleteAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        setTitle("Delete My Account");

        mAuth = FirebaseAuth.getInstance();
        mPasswordEditText = findViewById(R.id.delete_account_password_edit_text);
    }

    /*
     * Attempts to re authenticate current user, requires current user password.
     * On success deletes current user account and returns user to login screen.
     * On failure prompts user with appropriate message.
     */

    public void deleteAccount(View view){

        String userInputCurrentPassword = mPasswordEditText.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(TextUtils.isEmpty(userInputCurrentPassword)) {
            Toast.makeText(DeleteAccountActivity.this, "You must enter your current password.", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), userInputCurrentPassword);

        currentUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                currentUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DeleteAccountActivity.this, "Your account has been deleted successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

