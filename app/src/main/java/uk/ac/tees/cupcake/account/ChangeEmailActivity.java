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
 * Change Email Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class ChangeEmailActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        setTitle("Change Email");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mPasswordEditText = findViewById(R.id.change_email_password_edit_text);
        mEmailEditText = findViewById(R.id.change_email_email_edit_text);
    }

    /**
     * Attempts to authenticate current user with mPasswordEditText input value from user.
     * On success changes current user email address to mEmailEditText value and sends user to home activity.
     * On failure prompts user with appropriate message.
     */
    public void changeEmail(View view){
        String userInputCurrentPassword = mPasswordEditText.getText().toString().trim();
        String userInputNewEmail = mEmailEditText.getText().toString().trim();

        String result = validateUserInput(userInputCurrentPassword, userInputNewEmail);

        if(!result.isEmpty()){
            Toast.makeText(ChangeEmailActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(mCurrentUser.getEmail(), userInputCurrentPassword);

        mCurrentUser.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> {

                        mCurrentUser.updateEmail(userInputNewEmail)
                                    .addOnSuccessListener(aVoid1 -> {
                                        Toast.makeText(ChangeEmailActivity.this, "Your email has been changed successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ChangeEmailActivity.this, MainActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ChangeEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(ChangeEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
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
            sb.append("You must enter your new email address.");
        }
        return sb.toString();
    }
}