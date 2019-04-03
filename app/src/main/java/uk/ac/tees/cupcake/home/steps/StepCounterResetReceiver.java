package uk.ac.tees.cupcake.home.steps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import uk.ac.tees.cupcake.ApplicationConstants;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class StepCounterResetReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        
        final int yesterdaysSteps = preferences.getInt(ApplicationConstants.STEPS_PREFERENCE_KEY, 0);
    
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        
        if (user != null) {
            firestore.collection("UserStats")
                    .document(user.getUid())
                    .collection("StepCounts")
                    .add(new StepCountMeasurement(tomorrow, yesterdaysSteps))
                    .addOnFailureListener(x -> Log.e("StepCounterResetReceive", "onReceive: Failed to add step count to database", x));
        }
        
        preferences.edit().putInt("steps", 0).apply();
    }
    
}