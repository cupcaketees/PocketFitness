package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;

/*
 * Account Settings Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView mVerifyEmailStatus;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        setTitle("Account Settings");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mVerifyEmailStatus = findViewById(R.id.account_verify_email_status_text_view);
    }

    /*
     * Reloads current user information
     * on success changes mVerifyEmailStatus text and colour appropriately. on failure prompts user with appropriate message.
     */

    @Override
    protected void onStart() {
        super.onStart();

        currentUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    mVerifyEmailStatus.setText(R.string.account_settings_verified_email);
                    mVerifyEmailStatus.setTextColor(Color.GREEN);
                } else {
                    mVerifyEmailStatus.setText(R.string.account_settings_not_verified_email);
                    mVerifyEmailStatus.setTextColor(Color.RED);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountSettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


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
     * Sends user to verify email activity if email address is not verified otherwise displays appropriate message.
     */
    public void verifyEmailActivity(View view){
        if(currentUser.isEmailVerified()){
            Toast.makeText(AccountSettingsActivity.this, "Your email address is already verified.", Toast.LENGTH_SHORT).show();
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
