package uk.ac.tees.cupcake.home.uk.ac.tees.cupcake.home.steps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */

public class StepCounterReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("CupcakePrefs", Context.MODE_PRIVATE);
        
        int yesterdaysSteps = preferences.getInt("steps", 0);
    
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        
        firestore.collection("UserStats")
                .document(auth.getCurrentUser().getUid())
                .collection("StepCounts")
                .add(new StepCountMeasurement(yesterday, yesterdaysSteps))
                .addOnFailureListener(x -> {
                    Log.e("StepCounterReceiver", "onReceive: Failed to add step count to database", x);
                });
        
        preferences.edit().putInt("steps", 0).apply();
    }
    
}