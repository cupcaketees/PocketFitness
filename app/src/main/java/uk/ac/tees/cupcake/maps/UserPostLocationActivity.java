package uk.ac.tees.cupcake.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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


/**
 * UserPostLocationActivity - Shows requested location
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class UserPostLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        android.location.LocationListener {
    private static final String TAG = "UserPostLocationActivit";
    private GoogleMap mMap;
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

        getCloseGyms = new GetCloseGyms();
        initialise();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Initialise all onClickListeners
     */
    private void initialise() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ImageButton backButton = findViewById(R.id.backArrow);
        EditText address = findViewById(R.id.search_location);
        ImageButton shareText = findViewById(R.id.search_address);
        Button submit = findViewById(R.id.submitLocation);
        backButton.setOnClickListener(v -> finish());
        shareText.setOnClickListener(v -> showData(address.getText().toString()));
        submit.setOnClickListener(v -> submitLocation());
    }

    /**
     * When user clicks submit it'll check if they've selected an area and
     * if they have it'll pass it through as a putExtra so it can be retrieved in the post class.
     */
    private void submitLocation() {
        if (getCloseGyms.getSelectedItem() != null) {
            Log.d(TAG, "submitLocation: ");
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

    /**
     * @param location - current location
     *                 takes camera to current location
     */
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
        Toast.makeText(this, "Moving to current location", Toast.LENGTH_SHORT).show();

    }


    /**
     * @param input - the user entered location
     *              creates a URL string which leads to a page with json data to retrieve.
     */
    private void showData(String input) {
        Object transferData[] = new Object[2];
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?")
                .append("&input=").append(input)
                .append("&inputtype=textquery&fields=name,geometry,reference&locationbias=circle:2000@")
                .append(lat)
                .append(",")
                .append(lng)
                .append("&")
                .append("&key=")
                .append(getString(R.string.google_places_key));

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

    /**
     * On connection it'll request the location update to be called.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}