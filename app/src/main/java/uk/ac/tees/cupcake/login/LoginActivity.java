package uk.ac.tees.cupcake.login;

import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import uk.ac.tees.cupcake.R;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout loginBackground;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // background animation
        loginBackground = findViewById(R.id.login_background);
        animationDrawable = (AnimationDrawable) loginBackground.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();


    }
}
