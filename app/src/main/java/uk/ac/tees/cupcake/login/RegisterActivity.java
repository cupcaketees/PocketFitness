package uk.ac.tees.cupcake.login;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.SetupProfileActivity;

/**
 * Register Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Method call to start background animation.
        initBackground();
    }

    /**
     * On click method call to send user to login activity when go to login activity is pressed.
     */
    public void loginActivityOnClick(View view){
        sendUserToActivity(LoginActivity.class);
    }

    /**
     * On click method call to start sign up account process when sign up button is pressed.
     */
    public void signUpAccountOnClick(View view){
        signUpAccount();
    }

    /**
     * Attempts to create a new user account with the input values for email and password.
     * Prompts user with appropriate message on failure.
     * Sends user to setup profile page on success.
     */
    private void signUpAccount(){
        EditText emailEditText = findViewById(R.id.register_email_edit_text);
        EditText passwordEditText = findViewById(R.id.register_password_edit_text);
        EditText confirmPasswordEditText = findViewById(R.id.register_confirm_password);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userInputEmail = emailEditText.getText().toString().trim();
        String userInputPassword = passwordEditText.getText().toString().trim();
        String userInputConfirmPassword = confirmPasswordEditText.getText().toString().trim();

        String result = validateUserInput(userInputEmail, userInputPassword, userInputConfirmPassword);

        if(!result.isEmpty()){
            Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        if(userInputPassword.equals(userInputConfirmPassword)){

            auth.createUserWithEmailAndPassword(userInputEmail, userInputPassword)
                .addOnSuccessListener(authResult -> sendUserToActivity(SetupProfileActivity.class))
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
        }else{
            Toast.makeText(RegisterActivity.this, "Your password and confirm password must match.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method to create intent to destination passed through params.
     */
    private void sendUserToActivity(Class dest){
        startActivity(new Intent(RegisterActivity.this, dest));
    }

    /**
     * Validates input values passed through params are not empty.
     * @return empty string or appended message.
     */
    private String validateUserInput(String userInputEmail, String userInputPassword, String userInputNewPassword){
        StringBuilder sb = new StringBuilder();

        if(TextUtils.isEmpty(userInputEmail)){
            sb.append("You must enter your email address. ");
        }
        if(TextUtils.isEmpty(userInputPassword)) {
            sb.append("You must enter a password. ");
        }
        if(TextUtils.isEmpty(userInputNewPassword)) {
            sb.append("You must enter a new password.");
        }
        return sb.toString();
    }

    /**
     * Starts background animation for activity.
     */
    private void initBackground(){
        ConstraintLayout background = findViewById(R.id.login_background);
        AnimationDrawable animationDrawable = (AnimationDrawable) background.getBackground();

        // Duration.
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        //Start.
        animationDrawable.start();
    }
}
