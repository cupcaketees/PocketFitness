package uk.ac.tees.cupcake;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.home.steps.StepCounterResetStarterJobService;
import uk.ac.tees.cupcake.login.LoginActivity;
import uk.ac.tees.cupcake.sensors.SensorAdapter;
import uk.ac.tees.cupcake.sensors.StepCounterSensorListener;
import uk.ac.tees.cupcake.utils.IntentUtils;

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
        setContentView(R.layout.activity_splashscreen);
        StepCounterSensorListener eventListener = new StepCounterSensorListener(getApplicationContext());
        SensorAdapter sensorAdapter = new SensorAdapter(getApplicationContext());
        sensorAdapter.addSensorWithListener(Sensor.TYPE_STEP_COUNTER, SensorManager.SENSOR_DELAY_NORMAL, eventListener);


        scheduleStepCounterResetJob();
        (new Handler()).postDelayed(this::checkLoggedIn, 2000);
    }

    private void checkLoggedIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Checks users current auth state and directs to them to appropriate activity.
        if(currentUser == null){
            // not logged in
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }else{
            FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(!documentSnapshot.exists()){
                                // new account not setup.

                                startActivity(new Intent(SplashActivity.this, SetupProfileActivity.class));
                            }else {
                                // already setup account.
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }
                            finish();
                        }
                    });
        }

    }
    
    private void scheduleStepCounterResetJob() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 0);
    
        final long initialDelay = c.getTimeInMillis() - System.currentTimeMillis();
        
        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), StepCounterResetStarterJobService.class);
    
        JobInfo.Builder builder = new JobInfo.Builder(ApplicationConstants.STEP_COUNT_RESET_STARTER_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setMinimumLatency(initialDelay)
                .setOverrideDeadline(initialDelay + TimeUnit.SECONDS.toMillis(59));
        
        jobScheduler.schedule(builder.build());
    }

}