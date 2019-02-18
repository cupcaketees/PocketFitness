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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/**
 * Login Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout mLoginBackground;
    private AnimationDrawable mBackgroundAnimation;

    private FirebaseAuth mAuth;
    private EditText mEmailEditText, mPasswordEditText;
    private Button mSignInButton, mSignUpButton, mForgotPasswordButton, mSignInGoogleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialise Fields
        mAuth = FirebaseAuth.getInstance();
        mEmailEditText = findViewById(R.id.login_email_edit_text);
        mPasswordEditText = findViewById(R.id.login_password_edit_text);
        mSignInButton = findViewById(R.id.login_sign_in_button);

        initBackground();

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUserEmailAndPassword();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            // User is already logged in.
            sendUserToHomeActivity();
        }
    }

    /*
     * Validates user email and password input and attempts to login.
     */
    private void signInUserEmailAndPassword(){
        String userInputEmail = mEmailEditText.getText().toString().trim();
        String userInputPassword = mPasswordEditText.getText().toString().trim();

        // Check user input values are not empty.
        if(TextUtils.isEmpty(userInputEmail)){
            Toast.makeText(LoginActivity.this, "You must enter your email address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userInputPassword)){
            Toast.makeText(LoginActivity.this, "You must enter your password", Toast.LENGTH_SHORT).show();
        }else{
            // Attempt to sign in with email and password input
            mAuth.signInWithEmailAndPassword(userInputEmail,userInputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendUserToHomeActivity();
                    }else{
                        // Prompt user with error message on unsuccessful login.
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Sign in failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /*
     * Send user to home activity.
     */
    private void sendUserToHomeActivity(){
        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }
    /*
     * Starts background animation for login screen.
     */
    private void initBackground(){
        // Initialise values
        mLoginBackground = findViewById(R.id.login_background);
        mBackgroundAnimation = (AnimationDrawable) mLoginBackground.getBackground();
        // Set duration
        mBackgroundAnimation.setEnterFadeDuration(4500);
        mBackgroundAnimation.setExitFadeDuration(4500);
        //Start animation
        mBackgroundAnimation.start();
    }
}
