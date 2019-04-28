package uk.ac.tees.cupcake.home.health.exercise;

import android.location.Location;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.cupcake.utils.ColourUtility;

public final class ExerciseRouteTracker extends LocationCallback {
    
    /**
     * The hex rgb colour to draw the route overlay in.
     */
    private static final int ROUTE_COLOUR = 0xFFCAE8DC;
    
    /**
     * The coordinates of the previous call.
     */
    private LatLng previousCoordinates;
    
    /**
     * {@link List} of visited coordinates.
     */
    private final List<LatLng> visitedCoordinates = new ArrayList<>();
    
    /**
     * Map to track the route on.
     */
    private final GoogleMap googleMap;
    
    /**
     * Maximum distance that can be travelled for one call.
     */
    private final int maxDisplacement;
    
    /**
     * The distance travelled.
     */
    private int distanceTravelled;
    
    /**
     * The current position on the map.
     */
    private Circle currentPosition;
    
    /**
     * Filters the locations to produce a smoother route.
     */
    private final KalmanFilter filter = new KalmanFilter();
    
    public ExerciseRouteTracker(GoogleMap googleMap, int maxDisplacement) {
        this.googleMap = googleMap;
        this.maxDisplacement = maxDisplacement;
    }
    
    @Override
    public void onLocationResult(LocationResult result) {
        Location location = result.getLastLocation();
        
        if (location == null) {
            return;
        }
    
        LatLng coordinates = filter.apply(location);
        visitedCoordinates.add(coordinates);
        
        if (visitedCoordinates.size() == 1) {
            googleMap.addCircle(
                    new CircleOptions()
                            .center(visitedCoordinates.get(0))
                            .radius(10)
                            .strokeColor(ROUTE_COLOUR)
                            .fillColor(ColourUtility.setAlpha(0x55, ROUTE_COLOUR)));
            
            currentPosition = googleMap.addCircle(new CircleOptions()
                    .strokeColor(ROUTE_COLOUR)
                    .fillColor(ColourUtility.setAlpha(0x55, ROUTE_COLOUR))
                    .radius(1)
                    .center(coordinates));
        }
        
        if (previousCoordinates != null) {
            LatLng startCoordinates = visitedCoordinates.get(0);
            
            float[] distanceFromStart = new float[3];
            Location.distanceBetween(
                    startCoordinates.latitude, startCoordinates.longitude,
                    coordinates.latitude, coordinates.longitude, distanceFromStart);
            
            float[] distance = new float[3];
            Location.distanceBetween(
                    previousCoordinates.latitude, previousCoordinates.longitude,
                    coordinates.latitude, coordinates.longitude, distance);
            
            if (distance[0] < maxDisplacement) {
                //todo: cheaty boy
            }
            
            if (distanceFromStart[0] > 10) {
                distanceTravelled += distance[0];
                
                currentPosition.remove();
                currentPosition = googleMap.addCircle(new CircleOptions()
                        .strokeColor(ROUTE_COLOUR)
                        .fillColor(ColourUtility.setAlpha(0x55, ROUTE_COLOUR))
                        .radius(1)
                        .center(coordinates));
                
                googleMap.addPolyline(new PolylineOptions().add(previousCoordinates, coordinates).width(5).color(ROUTE_COLOUR));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
            }
        }
        
        previousCoordinates = coordinates;
    }
    
    public int getDistanceTravelled() {
        return distanceTravelled;
    }
    
    public List<LatLng> getVisitedCoordinates() {
        return visitedCoordinates;
    }
    
}