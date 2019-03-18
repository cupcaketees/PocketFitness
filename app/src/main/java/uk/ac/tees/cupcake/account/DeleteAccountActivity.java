package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.login.LoginActivity;

/**
 * Delete Account Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class DeleteAccountActivity extends AppCompatActivity {

    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        setTitle("Delete My Account");

        mPasswordEditText = findViewById(R.id.delete_account_password_edit_text);
    }

    /**
     * Attempts to authenticate current user with mPasswordEditText input value from user.
     * On success deletes current user account sends user to login activity.
     * On failure prompts user with appropriate message.
     */
    public void deleteAccount(View view){
        String userInputCurrentPassword = mPasswordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userInputCurrentPassword)) {
            Toast.makeText(DeleteAccountActivity.this, "You must enter your current password.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), userInputCurrentPassword);

        currentUser.reauthenticate(credential)
                   .addOnSuccessListener(aVoid -> {
                       currentUser.delete()
                                  .addOnSuccessListener(aVoid1 -> {
                                      Toast.makeText(DeleteAccountActivity.this, "Your account has been deleted successfully", Toast.LENGTH_SHORT).show();
                                      startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));
                                      finish();
                                  })
                                  .addOnFailureListener(e -> Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
                   })
                   .addOnFailureListener(e -> Toast.makeText(DeleteAccountActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }
}

