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
import com.google.android.gms.tasks.Task;
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

    FirebaseAuth mAuth;
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
     * Authenticate current user using password input, on success attempts to change user password with input provided.
     */
    public void changePassword(View view){

        String userInputPassword = mPasswordEditText.getText().toString().trim();
        String userInputNewPassword = mNewPasswordEditText.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(TextUtils.isEmpty(userInputPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "You must enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userInputNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "You must enter your new password", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), userInputPassword);

        currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    currentUser.updatePassword(userInputNewPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ChangePasswordActivity.this, "Your password has been updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
                                    }else{
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(ChangePasswordActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else{
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(ChangePasswordActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
