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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;

/**
 * Login Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout mLoginBackground;
    private AnimationDrawable mBackgroundAnimation;

    private final static int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailEditText, mPasswordEditText;
    private Button mSignInButton, mSignUpButton, mForgotPasswordButton;
    private SignInButton mSignInGoogleButton;

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialise fields.
        mAuth = FirebaseAuth.getInstance();
        mEmailEditText = findViewById(R.id.login_email_edit_text);
        mPasswordEditText = findViewById(R.id.login_password_edit_text);
        mSignInButton = findViewById(R.id.login_sign_in_button);
        mSignInGoogleButton = findViewById(R.id.sign_in_google_button);
        mSignUpButton = findViewById(R.id.login_sign_up_button);
        mForgotPasswordButton = findViewById(R.id.login_forgot_password_button);

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
         * Configures google sign in.
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                         .requestIdToken(getString(R.string.default_web_client_id))
                                                         .requestEmail()
                                                         .build();
        /*
         * Build Api client with options specified by gso.
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                              .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                                                  @Override
                                                  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                                      String errorMessage = connectionResult.getErrorMessage();
                                                      Toast.makeText(LoginActivity.this, "Sign in failed: "+ errorMessage , Toast.LENGTH_LONG).show();
                                                  }
                                              })
                                              .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                                              .build();

        // Initialise on click listeners.

        /*
         * Calls method to sign in with email and password.
         */
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUserEmailAndPassword();
            }
        });
        /*
         * Calls method to sign in with Google account.
         */
        mSignInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        /*
         * Send user to register activity.
         */
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        /*
         * Send user to forgot password activity.
         */
        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add intent for forgot password activity when created.
            }
        });
    }

    /*
     * Sign in with Google account.
     */
    private void signInGoogle(){
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result returned from launching the intent from GoogleSignInApi.
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                //Google sign in was successful, calls method to authenticate with FireBase.
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }else{
                // Prompt user with error message on unsuccessful login.
                Toast.makeText(LoginActivity.this,"Google Sign in has failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * Uses google account to authenticate with FireBase.
     */
    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                        }else{
                            // Prompt user with error message on unsuccessful login.
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Sign in failed : " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
            // Attempt to sign in with email and password input.
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
     * Starts background animation for login activity.
     */
    private void initBackground(){
        // Initialise values.
        mLoginBackground = findViewById(R.id.login_background);
        mBackgroundAnimation = (AnimationDrawable) mLoginBackground.getBackground();
        // Set duration.
        mBackgroundAnimation.setEnterFadeDuration(4500);
        mBackgroundAnimation.setExitFadeDuration(4500);
        //Start animation.
        mBackgroundAnimation.start();
    }
}
