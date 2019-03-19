package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.MainActivity;

/**
 * Verify Email Address Activity
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class VerifyEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        setTitle("Verify Email Address");
    }

    /**
     * On success sends instructions to verify email address to current user registered email address.
     * On failure prompts user with appropriate message.
     */
    public void verifyEmail(View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        currentUser.sendEmailVerification()
                   .addOnSuccessListener(aVoid -> {
                       Toast.makeText(VerifyEmailActivity.this, "Instructions have been sent to your registered email address", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(VerifyEmailActivity.this, MainActivity.class));
                   })
                   .addOnFailureListener(e -> Toast.makeText(VerifyEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
