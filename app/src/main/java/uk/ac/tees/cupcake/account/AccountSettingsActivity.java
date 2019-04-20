package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;

/**
 * Account Settings Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class AccountSettingsActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        setTitle("Account Settings");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Loads current user on start
     * On success changes mVerifyEmailStatus text and colour appropriately. on failure prompts user with appropriate message.
     */
    @Override
    protected void onStart() {
        super.onStart();

        mCurrentUser.reload()
                    .addOnSuccessListener(aVoid -> {
                        TextView verifyEmailTextView = findViewById(R.id.account_verify_email_status_text_view);

                        int textValue = mCurrentUser.isEmailVerified() ? R.string.account_settings_verified_email : R.string.account_settings_not_verified_email;
                        int colorValue = mCurrentUser.isEmailVerified() ? Color.GREEN : Color.RED;

                        verifyEmailTextView.setText(textValue);
                        verifyEmailTextView.setTextColor(colorValue);
                    })
                    .addOnFailureListener(e -> Toast.makeText(AccountSettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Intent to delete account activity
     */
    public void deleteAccountOnClick(View view){
        sendUserToActivity(DeleteAccountActivity.class);
    }

    /**
     * Intent to change email activity
     */
    public void changeEmailOnClick(View view){
        sendUserToActivity(ChangeEmailActivity.class);
    }

    /**
     * Intent to change password activity
     */
    public void changePasswordOnClick(View view){ sendUserToActivity(ChangePasswordActivity.class); }

    /**
     * Intent to verify email activity if current user account is not verified, else prompts user with appropriate message.
     */
    public void verifyEmailOnClick(View view){
        if(mCurrentUser.isEmailVerified()){
            Toast.makeText(AccountSettingsActivity.this, "Your email address is already verified.", Toast.LENGTH_SHORT).show();
        }else{
            sendUserToActivity(VerifyEmailActivity.class);
        }
    }

    /**
     * Helper method to create intent to destination passed through params
     */
    private void sendUserToActivity(Class dest){ startActivity(new Intent(AccountSettingsActivity.this, dest)); }
}
