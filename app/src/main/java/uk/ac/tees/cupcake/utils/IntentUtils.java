package uk.ac.tees.cupcake.utils;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Map;

/**
 * Intent Utility
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class IntentUtils {

    public static void invokeVideoView(Context context, Class<?> selectedClass, String title, String extra) {
        Intent intent = new Intent(context, selectedClass);
        intent.putExtra(title, extra);
        context.startActivity(intent);
    }


    public static void invokeBaseView(Context context, Class<?> selectedClass) {
        Intent intent = new Intent(context, selectedClass);
        context.startActivity(intent);
    }
}
