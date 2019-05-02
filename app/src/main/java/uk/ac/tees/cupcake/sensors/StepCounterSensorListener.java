package uk.ac.tees.cupcake.sensors;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import uk.ac.tees.cupcake.ApplicationConstants;
import uk.ac.tees.cupcake.R;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class StepCounterSensorListener implements SensorEventListener {
    
    private SharedPreferences preferences;
    
    private boolean firstEvent = true;
    
    private int referenceStepCount;
    
    private long referenceTime;
    
    private final Context context;
    
    private int lastEventCount;
    
    public StepCounterSensorListener(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(ApplicationConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String desc = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1","step_count_notification_channel", importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        final int eventValue = (int) event.values[0];
        final int storedSteps = preferences.getInt("steps", 0);
        final int storedTime = preferences.getInt("steps_time", 0);
        final int delta = eventValue - referenceStepCount;
        final int stepCount = storedSteps + delta;

        if (!firstEvent) {
            if (delta <= ApplicationConstants.STEP_COUNTING_EVENT_START_THRESHOLD) {
                int time = (int) (System.currentTimeMillis() - referenceTime);

                if (lastEventCount > 0) {
                    time *= lastEventCount;
                    lastEventCount = 0;
                }

                preferences.edit().putInt("steps_time", referenceTime > 0 ? storedTime + time : 0).apply();
            } else {
                lastEventCount = delta;
            }

            referenceTime = System.currentTimeMillis();
            preferences.edit().putInt("steps", stepCount).apply();

            if (stepCount == 100 || (storedSteps < 100 && stepCount >= 100)) {
                Intent intent = new Intent(context, SensorActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context, "1")
                                .setSmallIcon(R.drawable.ic_step_counter_icon)
                                .setContentTitle("Step Milestone Reached")
                                .setContentText("You have reached 100 steps, keep it up!")
                                .setPriority(Notification.PRIORITY_MAX)
                                .setContentIntent(pIntent);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, mBuilder.build());
            }

        } else {
            firstEvent = false;

            createNotificationChannel();
        }
        context.sendBroadcast(new Intent(ApplicationConstants.STEP_COUNT_BROADCAST_INTENT_ACTION));

        referenceStepCount = eventValue;
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}