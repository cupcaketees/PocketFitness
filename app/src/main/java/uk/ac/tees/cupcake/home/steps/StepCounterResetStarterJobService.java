package uk.ac.tees.cupcake.home.steps;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import uk.ac.tees.cupcake.ApplicationConstants;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class StepCounterResetStarterJobService extends JobService {
    
    @Override
    public boolean onStartJob(JobParameters params) {
        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), StepCounterResetJobService.class);
    
        JobInfo.Builder builder = new JobInfo.Builder(ApplicationConstants.STEP_COUNT_RESET_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(TimeUnit.DAYS.toMillis(1))
                .setPersisted(false);
        
        jobScheduler.schedule(builder.build());
        jobFinished(params, false);
        return false;
    }
    
    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}