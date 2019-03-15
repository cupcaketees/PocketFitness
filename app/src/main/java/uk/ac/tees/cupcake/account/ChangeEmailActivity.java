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
import uk.ac.tees.cupcake.home.HomeActivity;

/*
 * Change Email Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class ChangeEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        setTitle("Change Email");

        mAuth = FirebaseAuth.getInstance();
        mPasswordEditText = findViewById(R.id.change_email_password_edit_text);
        mEmailEditText = findViewById(R.id.change_email_email_edit_text);
    }
    /*
     * Attempts to re authenticate current user, requires current user password and new password input value from user.
     * On success changes current user password to mNewPasswordEditText value and returns user to home activity.
     * On failure prompts user with appropriate message.
     */
    public void changeEmail(View view){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userInputCurrentPassword = mPasswordEditText.getText().toString().trim();
        String userInputNewEmail = mEmailEditText.getText().toString().trim();

        String result = validateUserInput(userInputCurrentPassword, userInputNewEmail);

        if(!result.isEmpty()){
            Toast.makeText(ChangeEmailActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), userInputCurrentPassword);

        currentUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                currentUser.updateEmail(userInputNewEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangeEmailActivity.this, "Email hass been updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangeEmailActivity.this, HomeActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangeEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
     * Validates input values passed are not empty.
     * Returns empty string on success, otherwise a string with appropriate message.
     */
    private String validateUserInput(String userInputCurrentPassword, String userInputNewPassword){
        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(userInputCurrentPassword)) {
            sb.append("You must enter your current password. ");
        }
        if(TextUtils.isEmpty(userInputNewPassword)) {
            sb.append("You must enter your new email address.");
        }
        return sb.toString();
    }
}
