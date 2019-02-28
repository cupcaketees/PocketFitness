package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.ac.tees.cupcake.R;

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        setTitle("Account Settings");
    }

    /*
     * Sends user to delete account activity.
     */
    public void deleteAccountActivity(View view){
        Intent intent = new Intent(this, DeleteAccountActivity.class);
        startActivity(intent);
    }

    /*
     * Sends user to change email activity.
     */
    public void changeEmailActivity(View view){
        Intent intent = new Intent(this, ChangeEmailActivity.class);
        startActivity(intent);
    }

    /*
     * Sends user to change password activity.
     */
    public void changePasswordActivity(View view){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    /*
     * Sends user to verify email activity.
     */
    public void verifyEmailActivity(View view){
        Intent intent = new Intent(this, VerifyEmailActivity.class);
        startActivity(intent);
    }


}
