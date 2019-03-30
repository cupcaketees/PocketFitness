package uk.ac.tees.cupcake;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.login.LoginActivity;
import uk.ac.tees.cupcake.posts.PostActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

/**
 * Created by HugoT on 13/02/2019.
 * Splash screen Page
 */
public class SplashActivity extends AppCompatActivity {
    
    private static final String TAG = "SplashActivity";

    /**
     * Starts LoginActivity depending on user SystemClock.sleep runs the activity but
     * only opens it after 1 second gives everything time to load.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onStart");
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            IntentUtils.invokeBaseView(getApplicationContext(), LoginActivity.class);
        } else {
            IntentUtils.invokeBaseView(getApplicationContext(), MainActivity.class);
        }
        
        SystemClock.sleep(1000);
        finish();
        Log.d(TAG, "onCreate: onEnd");
    }
}