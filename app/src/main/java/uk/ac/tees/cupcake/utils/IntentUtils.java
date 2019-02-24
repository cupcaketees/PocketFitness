package uk.ac.tees.cupcake.utils;

import android.app.Activity;
import android.content.Intent;

public class IntentUtils {


    public static void invokeVideoView(Activity activity, Class<?> selectedClass, String title, String extra)
    {
        Intent intent = new Intent(activity, selectedClass);
        intent.putExtra(title,extra);
        activity.startActivity(intent);
    }

    public static void invokeBaseView(Activity activity, Class<?> selectedClass)
    {
        Intent intent = new Intent(activity, selectedClass);
        activity.startActivity(intent);
    }
}
