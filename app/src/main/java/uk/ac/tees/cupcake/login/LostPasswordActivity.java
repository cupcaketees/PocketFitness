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

/**
 * Lost Password Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */
public class LostPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_password);

        // Call method to start background animation.
        initBackground();
    }

    /**
     * On click method call to send user to login activity when go to login activity is pressed.
     */
    public void loginActivityOnClick(View view){
        sendUserToActivity(LoginActivity.class);
    }

    /**
     * On click call method call to start recovering lost password process.
     */
    public void resetUserPasswordOnClick(View view){
        resetUserPassword();
    }
    /**
     * Checks input is not null, attempts to send password reset instructions.
     * On success return user to login activity
     * On failure prompts user with appropriate message.
     */
    private void resetUserPassword(){
        EditText emailEditText = findViewById(R.id.lost_password_email_edit_text);
        String userInputEmail = emailEditText.getText().toString().trim();

        if(TextUtils.isEmpty(userInputEmail)) {
            Toast.makeText(LostPasswordActivity.this, "You must enter your email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(userInputEmail)
            .addOnCompleteListener(task -> {
                Toast.makeText(LostPasswordActivity.this, "Reset instructions have been sent to your email address.", Toast.LENGTH_SHORT).show();
                sendUserToActivity(LoginActivity.class);
            })
            .addOnFailureListener(e -> Toast.makeText(LostPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    /**
     * Helper method to create intent to destination passed through params.
     */
    private void sendUserToActivity(Class dest){
        startActivity(new Intent(LostPasswordActivity.this, dest));
        finish();
    }

    /**
     * Starts background animation for activity.
     */
    private void initBackground(){
        ConstraintLayout background = findViewById(R.id.lost_password_background);
        AnimationDrawable animationDrawable = (AnimationDrawable) background.getBackground();

        // Duration.
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        //Start.
        animationDrawable.start();
    }
}
