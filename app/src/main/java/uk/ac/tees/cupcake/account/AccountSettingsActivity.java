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
     * Takes user to Delete Account activity
     */
    public void sendUserToDeleteAccount(View view){
        Intent intent = new Intent(this, DeleteAccountActivity.class);
        startActivity(intent);
    }

    /*
     * Takes user to Change Email Activity
     */
    public void sendUserToChangeEmail(View view){
        Intent intent = new Intent(this, ChangeEmailActivity.class);
        startActivity(intent);
    }

    /*
     * Takes user to Change Password Activity
     */
    public void sendUserToChangePassword(View view){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }


}
