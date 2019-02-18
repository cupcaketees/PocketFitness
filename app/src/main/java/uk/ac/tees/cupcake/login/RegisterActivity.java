package uk.ac.tees.cupcake.login;

import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import uk.ac.tees.cupcake.R;

/**
 * Register Activity
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class RegisterActivity extends AppCompatActivity {

    private ConstraintLayout mRegisterBackground;
    private AnimationDrawable mBackgroundAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initBackground();
    }

    /*
    * Starts background animation for register activity.
    */
    private void initBackground(){
        // Initialise values
        mRegisterBackground = findViewById(R.id.register_background);
        mBackgroundAnimation = (AnimationDrawable) mRegisterBackground.getBackground();
        // Set duration
        mBackgroundAnimation.setEnterFadeDuration(4500);
        mBackgroundAnimation.setExitFadeDuration(4500);
        //Start animation
        mBackgroundAnimation.start();
    }
}
