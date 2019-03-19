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
import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.home.MainActivity;

/**
 * Register Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class RegisterActivity extends AppCompatActivity {

    private ConstraintLayout mRegisterBackground;
    private AnimationDrawable mBackgroundAnimation;

    private EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    private Button mSignUpButton, mSignInButton;

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
        setContentView(R.layout.activity_register);

        // Initialise Fields.
        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = findViewById(R.id.register_email_edit_text);
        mPasswordEditText = findViewById(R.id.register_password_edit_text);
        mConfirmPasswordEditText = findViewById(R.id.register_confirm_password);

        mSignInButton = findViewById(R.id.register_sign_in_button);
        mSignUpButton = findViewById(R.id.register_sign_up_button);

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

        // Initialise on click listeners.

        /*
         * Sends user to login activity.
         */
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        /*
         * Calls sign up account method
         */
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAccount();
            }
        });
    }

    /*
     * Validates user input and creates a new user account.
     */
    private void signUpAccount(){
        String userInputEmail = mEmailEditText.getText().toString().trim();
        String userInputPassword = mPasswordEditText.getText().toString().trim();
        String userInputConfirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        // Check input fields are not not empty.
        if(TextUtils.isEmpty(userInputEmail)){
            Toast.makeText(RegisterActivity.this, "You must enter your email address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userInputPassword)){
            Toast.makeText(RegisterActivity.this, "You must enter your password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userInputConfirmPassword)){
            Toast.makeText(RegisterActivity.this, "You must enter your confirm password", Toast.LENGTH_SHORT).show();
        }else{
            // Check password and confirm password match.
            if(userInputPassword.equals(userInputConfirmPassword)){
                mAuth.createUserWithEmailAndPassword(userInputEmail,userInputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, SetupProfileActivity.class));
                            finish();
                        }else{
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Sign up failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(RegisterActivity.this, "You password and confirm password must match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * Send user to home activity.
     */
    private void sendUserToHomeActivity(){
        Intent homeIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }

    /*
     * Send user to login activity.
     */
    private void sendUserToLoginActivity(){
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    /*
     * Starts background animation for register activity.
     */
    private void initBackground(){
        // Initialise values.
        mRegisterBackground = findViewById(R.id.register_background);
        mBackgroundAnimation = (AnimationDrawable) mRegisterBackground.getBackground();
        // Set duration.
        mBackgroundAnimation.setEnterFadeDuration(4500);
        mBackgroundAnimation.setExitFadeDuration(4500);
        //Start animation.
        mBackgroundAnimation.start();
    }
}
