package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.MainActivity;

/**
 * Change Password Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private EditText mPasswordEditText;
    private EditText mNewPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");

        mPasswordEditText = findViewById(R.id.change_password_password_edit_text);
        mNewPasswordEditText = findViewById(R.id.change_password_new_password_edit_text);
    }

    /**
     * Attempts to authenticate current user with mPasswordEditText input value from user.
     * On success changes current user password to mNewPasswordEditText value and sends user to home activity.
     * On failure prompts user with appropriate message.
     */
    public void changePassword(View view){
        String userInputCurrentPassword = mPasswordEditText.getText().toString().trim();
        String userInputNewPassword = mNewPasswordEditText.getText().toString().trim();

        String result = validateUserInput(userInputCurrentPassword, userInputNewPassword);

        if(!result.isEmpty()){
            Toast.makeText(ChangePasswordActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), userInputCurrentPassword);

        currentUser.reauthenticate(credential)
                   .addOnSuccessListener(aVoid -> {

                       currentUser.updatePassword(userInputNewPassword)
                                  .addOnSuccessListener(aVoid1 -> {
                                      Toast.makeText(ChangePasswordActivity.this, "Your password has been changed successfully", Toast.LENGTH_SHORT).show();
                                      startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                                      finish();
                                  })
                                  .addOnFailureListener(e -> Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
                   })
                   .addOnFailureListener(e -> Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    /**
     * Validates input values passed through params are not empty.
     * @return empty string or appended message.
     */
    private String validateUserInput(String userInputCurrentPassword, String userInputNewPassword){
        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(userInputCurrentPassword)) {
            sb.append("You must enter your current password.\n");
        }
        if(TextUtils.isEmpty(userInputNewPassword)) {
            sb.append("You must enter your new password.");
        }
        return sb.toString();
    }
}
