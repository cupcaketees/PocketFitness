package uk.ac.tees.cupcake.home.health.exercise;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Predicts the lat and long from the best estimate of previous estimation.
 */
public class KalmanFilter {
    
    private static final float MINIMUM_ACCURACY = 1;
    
    /**
     * The current estimate used to predict the next estimate
     */
    private Location currentEstimate;
    
    /**
     * Joint variance in accuracy and velocity.
     */
    private float variance;

    public KalmanFilter() {
        this(null);
    }
    
    public KalmanFilter(Location previousEstimate) {
        this.currentEstimate = previousEstimate;
    }
    
    /**
     * Takes another location estimates the actual position based on previous estimations of
     * positions and updates the current estimation.
     *
     * @param location the new location.
     * @return A {@link LatLng} with predicted values for a more smooth route of visited positions.
     */
    public LatLng apply(Location location) {
        
        float accuracy = location.getAccuracy();
        if (accuracy < MINIMUM_ACCURACY) {
            accuracy = MINIMUM_ACCURACY;
        }
        
        if (currentEstimate == null) {
            currentEstimate = location;
            
            //No previous best estimate.
            
            // variance of the horizontal position (lat,lng)
            variance = accuracy * accuracy;
            
        } else {
            long timeDelta = location.getTime() - currentEstimate.getTime();
            if (timeDelta > 0) {
                // pk = pk−1 + Δt*vk−1,
                // current position = previous best estimated position + change in time * velocity.
                variance += (timeDelta / 1000) * (location.getSpeed() * location.getSpeed());
            }

            // calculate gain which will be some variance as a percentage to apply to the change in horizontal coordinates.
            float K = variance / (variance + accuracy * accuracy);

            double lat = currentEstimate.getLatitude();
            currentEstimate.setLatitude(lat + (location.getLatitude() - lat) * K);
    
            double lng = currentEstimate.getLongitude();
            currentEstimate.setLongitude(lng + (location.getLongitude() - lng) * K);
            
            variance *= (1 - K);
        }
        
        return new LatLng(currentEstimate.getLatitude(), currentEstimate.getLongitude());
    }
}