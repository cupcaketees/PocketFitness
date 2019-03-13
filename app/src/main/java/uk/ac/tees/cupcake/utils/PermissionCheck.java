package uk.ac.tees.cupcake.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionCheck {

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    public static boolean checkPermissionsList(String[] postPermissions, Context context) {
        for (String permissionCheck : postPermissions) {
            if (!checkPermissions(permissionCheck, context)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermissions(String permissions, Context context) {
        int permissionRequest = ActivityCompat.checkSelfPermission(context, permissions);
        return permissionRequest == PackageManager.PERMISSION_GRANTED;
    }


    public static void verifyPermissions(String[] permissions, Activity activity) {

        ActivityCompat.requestPermissions(
                activity,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }


}
