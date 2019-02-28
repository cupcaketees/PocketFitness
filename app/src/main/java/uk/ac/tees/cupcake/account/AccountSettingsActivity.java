package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;

/**
 * Account Settings Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        setTitle("Account Settings");
        mAuth = FirebaseAuth.getInstance();

        /*
         * Reload auth on create and updates verify email status accordingly
         */
        mAuth.getCurrentUser().reload()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            TextView verifyEmailStatus = findViewById(R.id.account_verify_email_status_text_view);

                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                verifyEmailStatus.setText(R.string.account_settings_verified_email);
                                verifyEmailStatus.setTextColor(Color.GREEN);
                            } else {
                                verifyEmailStatus.setText(R.string.account_settings_not_verified_email);
                                verifyEmailStatus.setTextColor(Color.RED);
                            }
                        }
                    }
                });
    }


    /*
     * Sends user to delete account activity.
     */
    public void deleteAccountActivity(View view){
        sendUserToActivity(DeleteAccountActivity.class);
    }

    /*
     * Sends user to change email activity.
     */
    public void changeEmailActivity(View view){
        sendUserToActivity(ChangeEmailActivity.class);
    }

    /*
     * Sends user to change password activity.
     */
    public void changePasswordActivity(View view){
        sendUserToActivity(ChangePasswordActivity.class);
    }

    /*
     * Sends user to verify email activity if email address is not verified.
     */
    public void verifyEmailActivity(View view){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser.isEmailVerified()){
            Toast.makeText(AccountSettingsActivity.this, "Your email address is already verified", Toast.LENGTH_SHORT).show();
        }else{
            sendUserToActivity(VerifyEmailActivity.class);
        }
    }

    /*
     * Helper method that sends user to destination passed through params
     */
    private void sendUserToActivity(Class dest){
        startActivity(new Intent(this, dest));
    }

}
