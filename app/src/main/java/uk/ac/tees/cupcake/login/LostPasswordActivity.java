package uk.ac.tees.cupcake.login;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/**
 * Lost Password Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class LostPasswordActivity extends AppCompatActivity {

    private ConstraintLayout mLostPasswordBackground;
    private AnimationDrawable mBackgroundAnimation;

    private EditText mEmailEditText;
    private Button mResetPasswordButton, mSignInButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_password);

        // Initialise fields
        mAuth = FirebaseAuth.getInstance();
        mEmailEditText = findViewById(R.id.lost_password_email_edit_text);
        mResetPasswordButton = findViewById(R.id.lost_password_reset_password_button);
        mSignInButton = findViewById(R.id.lost_password_sign_in_button);

        // Call method to start background animation.
        initBackground();

        /*
         * Checks if user is logged in and updates UI accordingly.
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    sendUserToHomeActivity();
                }
            }
        };

        /*
         * Sends user to Login Activity
         */
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetUserPassword();
            }
        });
    }
    /*
     * Validates user input and attempts sends password instructions to email.
     */
    private void resetUserPassword(){
        String userInputEmail = mEmailEditText.getText().toString().trim();
        // Checks input field is not empty
        if(TextUtils.isEmpty(userInputEmail)){
            Toast.makeText(LostPasswordActivity.this, "You must enter your email address", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.sendPasswordResetEmail(userInputEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LostPasswordActivity.this,"Password reset instructions have been sent to your email", Toast.LENGTH_SHORT).show();
                        sendUserToLoginActivity();
                    }else{
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(LostPasswordActivity.this,"Password Reset Failed : " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /*
    * Send user to login activity.
    */
    private void sendUserToLoginActivity(){
        Intent loginIntent = new Intent(LostPasswordActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    /*
     * Send user to home activity.
     */
    private void sendUserToHomeActivity(){
        Intent loginIntent = new Intent(LostPasswordActivity.this, HomeActivity.class);
        startActivity(loginIntent);
        finish();
    }

    /*
     * Starts background animation for lost password activity.
     */
    private void initBackground(){
        // Initialise values.
        mLostPasswordBackground = findViewById(R.id.lost_password_background);
        mBackgroundAnimation = (AnimationDrawable) mLostPasswordBackground.getBackground();
        // Set duration.
        mBackgroundAnimation.setEnterFadeDuration(4500);
        mBackgroundAnimation.setExitFadeDuration(4500);
        //Start animation.
        mBackgroundAnimation.start();
    }
}
