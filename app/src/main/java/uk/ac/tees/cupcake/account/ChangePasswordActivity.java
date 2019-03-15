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
 * Change Password Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mPasswordEditText;
    private EditText mNewPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");

        mAuth = FirebaseAuth.getInstance();
        mPasswordEditText = findViewById(R.id.change_password_password_edit_text);
        mNewPasswordEditText = findViewById(R.id.change_password_new_password_edit_text);
    }

    /*
     * Attempts to re authenticate current user, requires current user password and new password input value from user.
     * On success changes current user password to mNewPasswordEditText value and returns user to home activity.
     * On failure prompts user with appropriate message.
     */
    public void changePassword(View view){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userInputCurrentPassword = mPasswordEditText.getText().toString().trim();
        String userInputNewPassword = mNewPasswordEditText.getText().toString().trim();

        String result = validateUserInput(userInputCurrentPassword, userInputNewPassword);

        if(!result.isEmpty()){
            Toast.makeText(ChangePasswordActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), userInputCurrentPassword);

        currentUser.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                currentUser.updatePassword(userInputNewPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangePasswordActivity.this, "Your password has been changed successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            sb.append("You must enter your new password.");
        }
        return sb.toString();
    }
}
