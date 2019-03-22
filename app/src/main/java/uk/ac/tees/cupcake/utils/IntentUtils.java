package uk.ac.tees.cupcake.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Intent Utility
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 * @author Sam Hammersley <q5315908@live.tees.ac.uk>
 */
public class IntentUtils {
    
    private IntentUtils() {
        // prevent instantiation.
    }

    public static void invokeVideoView(Context context, Class<?> selectedClass, String title, String extra) {
        Map<String, String> extras = new HashMap<>();
        extras.put(title, extra);
        
        invokeViewWithExtras(context, selectedClass, extras);
    }

    public static void invokePhotoView(Context context, Class<?> selectedClass, Bitmap bitmap) {
        Intent intent = new Intent(context, selectedClass);
        intent.putExtra("selected_bitmap", bitmap);
        context.startActivity(intent);
    }

    public static void invokeDietPlan(Context context, Class<?> selectedClass, String title, Parcelable extra) {
        Intent intent = new Intent(context, selectedClass);
        intent.putExtra(title, extra);
        context.startActivity(intent);
    }


    public static void invokeBaseView(Context context, Class<?> selectedClass) {
        Intent intent = new Intent(context, selectedClass);
        context.startActivity(intent);
    }

    public static void invokeFromFragmentView(Activity activity, Class<?> selectedClass) {
        Intent intent = new Intent(activity, selectedClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void invokeToURL(Context context,String url) {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url));

        context.startActivity(browserIntent);
    }

    /**
     * Invoke a view with extra data to pass to the invoked view.
     *
     * @param context the context in which the view is invoked.
     * @param selectedClass the selected activity to invoke.
     * @param extras extra data to pass to the invoked view.
     */
    public static void invokeViewWithExtras(Context context, Class<?> selectedClass, Map<String, ? extends Serializable> extras) {
        Intent intent = new Intent(context, selectedClass);

        for (Map.Entry<String, ? extends Serializable> e : extras.entrySet()) {
            intent.putExtra(e.getKey(), e.getValue());
        }
        
        context.startActivity(intent);
    }
}