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
 * Change Email Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class ChangeEmailActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        mAuth = FirebaseAuth.getInstance();
        mPasswordEditText = findViewById(R.id.change_email_password_edit_text);
        mEmailEditText = findViewById(R.id.change_email_email_edit_text);
        setTitle("Change Email");
    }


    /*
     * Authenticate current user using password input, on success attempts to change email with input provided.
     */
    public void changeEmail(View view){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userInputPassword = mPasswordEditText.getText().toString().trim();
        String userInputEmail = mEmailEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userInputPassword)){
            Toast.makeText(ChangeEmailActivity.this, "You must enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userInputEmail)){
            Toast.makeText(ChangeEmailActivity.this, "You must enter your new email address", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), userInputPassword);

        currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    currentUser.updateEmail(userInputEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ChangeEmailActivity.this, "Email hass been updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ChangeEmailActivity.this, HomeActivity.class));
                                    }else{
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(ChangeEmailActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else{
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(ChangeEmailActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
