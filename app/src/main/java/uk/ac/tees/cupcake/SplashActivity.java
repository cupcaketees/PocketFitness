package uk.ac.tees.cupcake;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import uk.ac.tees.cupcake.home.MainActivity;

/**
 * Created by HugoT on 13/02/2019.
 * Splash screen Page
 */
public class SplashActivity extends AppCompatActivity {
    
    private static final String TAG = "SplashActivity";

    /**
     * Starts HomeActivity/LoginActivity depending on user SystemClock.sleep runs the activity but
     * only opens it after 1 second gives everything time to load.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onStart");
        super.onCreate(savedInstanceState);
        
        //Replace with brads login page will need verification if logged in
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        
        SystemClock.sleep(1000);
        finish();
        Log.d(TAG, "onCreate: onEnd");
    }
}