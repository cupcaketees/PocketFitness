package uk.ac.tees.cupcake.utils;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Map;

public final class IntentUtils {
    
    private IntentUtils() {
        // prevent instantiation.
    }

    public static void invokeBaseView(Context context, Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }
    
    public static void invokeViewWithExtras(Context context, Class<?> activityClass, Map<String, ? extends Serializable> extras) {
        Intent intent = new Intent(context, activityClass);
        
        for (Map.Entry<String, ? extends Serializable> e : extras.entrySet()) {
            intent.putExtra(e.getKey(), e.getValue());
        }
        
        context.startActivity(intent);
    }
    
}