package uk.ac.tees.cupcake.home.health.exercise;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import uk.ac.tees.cupcake.ApplicationConstants;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.posts.CreatePostActivity;
import uk.ac.tees.cupcake.utils.HealthUtility;

public class ExerciseMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    
    private static final int LOCATION_UPDATE_INTERVAL = 100;
    
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    
    /**
     * The {@link GoogleMap} to display route and current location.
     */
    private GoogleMap googleMap;
    
    /**
     * Provides location updates.
     */
    private FusedLocationProviderClient locationProviderClient;
    
    /**
     * For tracking the exercise route.
     */
    private ExerciseRouteTracker exerciseRouteTracker;
    
    /**
     * The type of exercise.
     */
    private Exercise currentExercise;
    
    /**
     * Denotes whether the user has permissions.
     */
    private boolean hasPermissions;
    
    /**
     * Chronometer used for timing the workout.
     */
    private Chronometer chronometer;
    
    private TextView paceTextView;
    private TextView caloriesTextView;
    private TextView exerciseTextView;
    
    private float userBmr;
    
    private long caloriesBurned;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.activity_exercise_map);
    
        if (ActivityCompat.checkSelfPermission(ExerciseMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            hasPermissions = true;
        } else {
            ActivityCompat.requestPermissions(ExerciseMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    
        locationProviderClient = LocationServices.getFusedLocationProviderClient(ExerciseMapActivity.this);
        currentExercise = (Exercise) getIntent().getSerializableExtra("exercise");
        
        chronometer = findViewById(R.id.exercise_map_chronometer);
        paceTextView = findViewById(R.id.exercise_map_current_pace);
        caloriesTextView = findViewById(R.id.exercise_map_calories_burned);
    
        exerciseTextView = findViewById(R.id.exercise_map_exercise_label);
        exerciseTextView.setText(currentExercise.getName());
    
        userBmr = getSharedPreferences(ApplicationConstants.PREFERENCES_NAME, Context.MODE_PRIVATE).getFloat("user_bmr", 0);
    
        startCountdownAnimation();
        
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.exercise_map_map);
        mapFragment.getMapAsync(ExerciseMapActivity.this);
    }
    
    /**
     * Starts periodic location requests.
     *
     * @throws SecurityException if necessary permissions are not granted.
     */
    private void requestLocationUpdates() throws SecurityException {
        LocationRequest locationRequest = buildLocationRequest(LOCATION_UPDATE_INTERVAL, LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationProviderClient.requestLocationUpdates(locationRequest, exerciseRouteTracker, locationProviderClient.getLooper());
    }
    
    /**
     * Adds appropriate listener to and starts {@link #chronometer}
     */
    private void startChronometer(long base) {
        chronometer.setBase(SystemClock.elapsedRealtime() + base);
    
        chronometer.setOnChronometerTickListener(c -> {
        
            float distance = exerciseRouteTracker.getDistanceTravelled();
            long elapsedMillis = SystemClock.elapsedRealtime() - c.getBase();
        
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);
            if (seconds > 0) {
                String pace = String.format("%.2f", (distance / seconds));
                paceTextView.setText("Current pace: " + pace + " m/s");
            }
        
            caloriesBurned = HealthUtility.calculateCaloriesBurnedMovement(userBmr, distance, elapsedMillis);
            caloriesTextView.setText("Calories burned: " + caloriesBurned + " kcal");
        
            exerciseTextView.setText(currentExercise.getName() + " for " + distance / 1000 + "km");
        });
        
        chronometer.start();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        hasPermissions = false;
        
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                }
                break;
        }
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.exerciseRouteTracker = new ExerciseRouteTracker(googleMap, currentExercise.getMaxDisplacement());
        
        try {
            if (hasPermissions) {
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(ExerciseMapActivity.this, R.raw.exercise_map_style));
                googleMap.setMyLocationEnabled(false);
    
                locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    requestLocationUpdates();
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
    
        locationProviderClient.removeLocationUpdates(exerciseRouteTracker);
    }
    
    /**
     * Builds a {@link LocationRequest} with the given interval, accuracy and minimum displacement.
     *
     * @return a {@link LocationRequest}
     */
    private LocationRequest buildLocationRequest(int interval, int accuracy) {
        return new LocationRequest()
                .setInterval(interval)
                .setPriority(accuracy)
                .setSmallestDisplacement(0);
    }
    
    private Animation countdownAnimation(TextView view, AtomicInteger countDown) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        fadeIn.setRepeatCount(3);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                
                View overlay = findViewById(R.id.exercise_map_countdown_overlay);
                AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setDuration(1000);
                fadeOut.setFillAfter(true);
                overlay.startAnimation(fadeOut);
                
                startChronometer(0);
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                if (countDown.get() == 1) {
                    view.setText("Go!");
                    
                } else {
                    
                    view.setText(countDown.decrementAndGet() + "");
                }
            }
        });
        
        return fadeIn;
    }
    
    private void startCountdownAnimation() {
        AtomicInteger countDownValue = new AtomicInteger(3);
        TextView countDownText = findViewById(R.id.exercise_map_countdown);
        countDownText.startAnimation(countdownAnimation(countDownText, countDownValue));
    }
    
    /**
     * Called upon pressing finish. Stops the timer and stops receiving location updates.
     */
    public void finishButtonOnClick(View view) {
        chronometer.stop();
        locationProviderClient.removeLocationUpdates(exerciseRouteTracker);
        
        LatLngBounds.Builder bldr = LatLngBounds.builder();
        for (LatLng coordinates : exerciseRouteTracker.getVisitedCoordinates()) {
            bldr.include(coordinates);
        }
        
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bldr.build(), 150));
        
        googleMap.setOnMapLoadedCallback(() -> googleMap.snapshot(bitmap -> {
            
            Intent intent = new Intent(ExerciseMapActivity.this, CreatePostActivity.class);
            long elapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
            
            intent.putExtra("create_post_description", "I walked "
                    + (float) (exerciseRouteTracker.getDistanceTravelled() / 1000) + "km in "
                    + DateUtils.formatElapsedTime(elapsed) + " and burned " + caloriesBurned);
            
            try {
                File outputFile = File.createTempFile(System.nanoTime() + "", ".jpg", getCacheDir());
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                }
                
                intent.putExtra("create_post_image_uri", Uri.fromFile(outputFile).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            startActivity(intent);
            finish();
        }));
    }
    
}