package uk.ac.tees.cupcake.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.home.MainActivity;

/**
 * Login Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>1
 */

public class LoginActivity extends AppCompatActivity {

    private final static int GOOGLE_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private VideoView mBackgroundVideoView;
    private MediaPlayer mMediaPlayer;
    private int mCurrentVideoPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialise
        mAuth = FirebaseAuth.getInstance();
        mBackgroundVideoView = findViewById(R.id.login_video_view);
        CheckBox backgroundCheckBox = findViewById(R.id.login_check_box);

        // Uri path to mp4 file
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.running_video);

        //Set
        mBackgroundVideoView.setVideoURI(uri);
        mBackgroundVideoView.start();

        mBackgroundVideoView.setOnPreparedListener(mp -> {
            mMediaPlayer = mp;
            mMediaPlayer.setLooping(true);
            if(mCurrentVideoPos != 0){
                // goes to prev pos
                mMediaPlayer.seekTo(mCurrentVideoPos);
                mMediaPlayer.start();
            }
        });


        //Auth listener listening for any changes in auth state. On change directs user them to appropriate activity.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){

                    FirebaseFirestore.getInstance()
                                     .collection("Users")
                                     .document(firebaseAuth.getCurrentUser().getUid())
                                     .get()
                                     .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                         @Override
                                         public void onSuccess(DocumentSnapshot documentSnapshot) {
                                             if(documentSnapshot.exists()){
                                                 // Account that does not require to be setup.
                                                 sendUserToActivity(MainActivity.class);
                                                 finish();
                                             }else{
                                                 // New account that requires setup
                                                 sendUserToActivity(SetupProfileActivity.class);
                                                 finish();
                                             }
                                         }
                                     })
                                     .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        };

        //Configures google sign in.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                         .requestIdToken(getString(R.string.default_web_client_id))
                                                         .requestEmail()
                                                         .build();
        //Build Api client with options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                              .enableAutoManage(LoginActivity.this, connectionResult -> {
                                                  Toast.makeText(LoginActivity.this,connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
                                              })
                                              .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                                              .build();


        // pauses or plays video depending on value of checkbox.
        backgroundCheckBox.setOnClickListener(v -> {
            boolean checked = ((CheckBox) v).isChecked();

            if(checked){
                mBackgroundVideoView.pause();
            }else{
                mBackgroundVideoView.start();
            }
        });
    }

    /**
     * Adds listener on activity
     */
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Removes listener on activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentVideoPos = mMediaPlayer.getCurrentPosition();
        mBackgroundVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBackgroundVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }



    /**
     * On click method call to start sign in process with Google account.
     */
    public void googleSignInOnClick(View view){
        signInGoogle();
    }

    /**
     * On click call method call to start sign in process with email and password.
     */
    public void emailSignInOnClick(View view){
        signInUserEmailAndPassword();
    }

    /**
     * Send user to forgot password activity.
     */
    public void forgotPasswordOnClick(View view){
        sendUserToActivity(LostPasswordActivity.class);
    }

    /**
     * Send user to register activity.
     */
    public void registerOnClick(View view){
        sendUserToActivity(RegisterActivity.class);
    }

    /**
     * Helper method to create intent to destination passed through params.
     */
    private void sendUserToActivity(Class dest){
        startActivity(new Intent(LoginActivity.this, dest));
    }

    /**
     * Create intent to start sign in with Google account.
     */
    private void signInGoogle(){
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN);
    }

    /**
     * On activity result currently on used for Google sign in intent.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the intent from GoogleSignInApi.
        if(requestCode == GOOGLE_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                //Google sign in was successful, calls method to authenticate with Firebase.
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }else{
                Toast.makeText(LoginActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Uses users google account credentials passed through param to authenticate with Firebase. Prompts user with appropriate message on failure.
     */
    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    /**
     * Uses sign in with email and password credentials. On failure prompts user with appropriate message.
     */
    private void signInUserEmailAndPassword(){
        EditText emailEditText = findViewById(R.id.login_email_edit_text);
        EditText passwordEditText = findViewById(R.id.login_password_edit_text);

        String userInputEmail = emailEditText.getText().toString().trim();
        String userInputPassword = passwordEditText.getText().toString().trim();

        String result = validateUserInput(userInputEmail, userInputPassword);
        if(!result.isEmpty()){
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(userInputEmail, userInputPassword)
             .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Validates input values passed through params are not empty.
     * @return empty string or appended message.
     */
    private String validateUserInput(String userInputEmailAddress, String userInputPassword){
        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(userInputEmailAddress)) {
            sb.append("You must enter your email address.\n");
        }
        if(TextUtils.isEmpty(userInputPassword)) {
            sb.append("You must enter your password.");
        }
        return sb.toString();
    }
}