package uk.ac.tees.cupcake;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.home.steps.StepCounterResetStarterJobService;
import uk.ac.tees.cupcake.home.steps.StepCounterService;
import uk.ac.tees.cupcake.login.LoginActivity;
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

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            IntentUtils.invokeBaseView(getApplicationContext(), LoginActivity.class);
        } else {
            IntentUtils.invokeBaseView(getApplicationContext(), MainActivity.class);
        }
        
        startService(new Intent(getApplicationContext(), StepCounterService.class));
        scheduleStepCounterResetJob();
        
        SystemClock.sleep(1000);
        finish();
        Log.d(TAG, "onCreate: onEnd");
    }
    
    private void scheduleStepCounterResetJob() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
    
        final long initialDelay = c.getTimeInMillis() - System.currentTimeMillis();
        
        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), StepCounterResetStarterJobService.class);
    
        JobInfo.Builder builder = new JobInfo.Builder(ApplicationConstants.STEP_COUNT_RESET_STARTER_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setMinimumLatency(initialDelay)
                .setOverrideDeadline((int) (initialDelay * 1.01));
        
        jobScheduler.schedule(builder.build());
    }

}