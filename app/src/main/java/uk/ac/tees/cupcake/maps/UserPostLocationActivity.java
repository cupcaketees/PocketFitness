package uk.ac.tees.cupcake.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class UserPostLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        android.location.LocationListener {

    private GoogleMap mMap;
    private static final String TAG = "GymMapActivity";
    private LocationManager mLocationManager;
    private Marker currentUserLocationMarker;
    private GetCloseGyms getCloseGyms;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_location);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        initialise();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initialise() {
        ImageButton backButton = findViewById(R.id.backArrow);
        EditText address = findViewById(R.id.search_location);
        ImageButton shareText = findViewById(R.id.search_address);
        Button submit = findViewById(R.id.submitLocation);
        backButton.setOnClickListener(v -> finish());
        shareText.setOnClickListener(v -> showData(address.getText().toString()));
        submit.setOnClickListener(v -> submitLocation());
    }

    private void submitLocation() {
        if (getCloseGyms.getSelectedItem() != null) {
            Intent intent = new Intent();
            intent.putExtra("Location", getCloseGyms.getSelectedItem());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "No Option Selected", Toast.LENGTH_SHORT).show();
        }
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
            IntentUtils.invokeBaseView(getApplicationContext(), MainActivity.class);
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


        mMap.animateCamera(cameraUpdate);
        mLocationManager.removeUpdates(this);
    }


    private void showData(String input) {
        Object transferData[] = new Object[2];
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?");
        googleURL.append("&input=").append(input);
        googleURL.append("&inputtype=textquery&fields=name,geometry,reference&locationbias=circle:2000@" + lat + "," + lng + "&");
        googleURL.append("&key=").append(getString(R.string.google_places_key));

        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());


        transferData[0] = mMap;
        transferData[1] = googleURL.toString();
        getCloseGyms = new GetCloseGyms();
        getCloseGyms.execute(transferData);

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