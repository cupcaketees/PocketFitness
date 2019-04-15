package uk.ac.tees.cupcake.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class GymMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        android.location.LocationListener, GoogleMap.OnMarkerClickListener {


    private static final String TAG = "GymMapActivity";
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Marker currentUserLocationMarker;
    private double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        initialise();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initialise() {
        ImageView backButton = findViewById(R.id.backArrow);
        TextView shareText = findViewById(R.id.postFinalise);
        shareText.setVisibility(View.GONE);
        backButton.setOnClickListener(v -> IntentUtils.invokeBaseView(getApplicationContext(), MainActivity.class));
    }


    /**
     * If API is greater than 23 need to ask the user for permission before opening the app if they haven't accepted it
     */
    private void checkUserLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        }
    }

    /**
     * Gets API Key and setups the client to be used within methods.
     */
    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    /**
     * @param googleMap - sets user variable to the map
     *                  checks that all permissions are setup if not notifies to change in settings
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "Permissions Not enabled change in settings", Toast.LENGTH_SHORT).show();
//            IntentUtils.invokeBaseView(getApplicationContext(), MainActivity.class);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.animateCamera(cameraUpdate);
        mLocationManager.removeUpdates(this);
        findGyms();
    }

    private void findGyms() {
        Object transferData[] = new Object[2];
        GetCloseGyms getCloseGyms = new GetCloseGyms();

        String url = showData(lat, lng);
        transferData[0] = mMap;
        transferData[1] = url;

        getCloseGyms.execute(transferData);
        Toast.makeText(this, "Showing nearby gyms", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this, marker.getTitle() + "hi", Toast.LENGTH_SHORT).show();
        return false;
    }

    private String showData(double lat, double lng) {
//        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=YOUR_API_KEY
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=").append(lat).append(",").append(lng);
        googleURL.append("&radius=" + 5000);
        googleURL.append("&type=gym");
        googleURL.append("&sensor=true");
        googleURL.append("&key=").append(getString(R.string.google_places_key));

        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                Log.i(TAG, "onLocationResult: " + location.getLatitude() + " " + location.getLongitude());
            }
        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
