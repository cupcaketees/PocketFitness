package uk.ac.tees.cupcake.utils;

import android.Manifest;

public class Permissions {

    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    public static final String[] CAMERA_PERMISSSION = {
            Manifest.permission.CAMERA
    };

    public static final String[] WRITE_STORAGE_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] READ_STORAGE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static final String[] BODY_SENSORS = {
            Manifest.permission.BODY_SENSORS
    };
    public static final String[] ACCESS_FINE_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public static final String[] ACCESS_COARSE_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static final String[] ACCESS_NETWORK_STATE = {
            Manifest.permission.ACCESS_NETWORK_STATE
    };


}
