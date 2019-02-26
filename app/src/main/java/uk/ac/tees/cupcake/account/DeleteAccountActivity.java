package uk.ac.tees.cupcake.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.login.LoginActivity;

public class DeleteAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        mAuth = FirebaseAuth.getInstance();
        mPasswordEditText = findViewById(R.id.delete_account_password_edit_text);
    }

    /*
     * Sends user to Account Settings Activity
     */
    public void sendUserToAccountSettings(View view){
        Intent intent = new Intent(this, AccountSettingsActivity.class );
        startActivity(intent);
    }

    /*
     * Sends user to Login Activity
     */
    public void sendUserToLogin(){
        Intent loginIntent = new Intent(this, LoginActivity.class );
        startActivity(loginIntent);
        finish();
    }

    /*
     * Deletes user account
     * TODO Add check to see if account is signed in using google or email
     */
    public void deleteUserAccount(View view){

        String userInputPassword = mPasswordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userInputPassword)){
            Toast.makeText(DeleteAccountActivity.this, "You must enter a password.", Toast.LENGTH_SHORT).show();
        }else{
            AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), userInputPassword);

            mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DeleteAccountActivity.this, "Your account has been deleted.", Toast.LENGTH_SHORT).show();
                                sendUserToLogin();
                            }else{
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(DeleteAccountActivity.this, "Error:" + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
    }

}
