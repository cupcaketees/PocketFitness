package uk.ac.tees.cupcake.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/**
 * Verify Email Address Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class VerifyEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        setTitle("Verify Email Address");

        mAuth = FirebaseAuth.getInstance();
    }

    /*
     * On success sends instructions to verify email address to current user registered email address.
     * On failure prompts user with appropriate message.
     */
    public void verifyEmail(View view){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(VerifyEmailActivity.this, "Instructions have been sent to your registered email address", Toast.LENGTH_LONG).show();
                startActivity(new Intent(VerifyEmailActivity.this, HomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerifyEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
