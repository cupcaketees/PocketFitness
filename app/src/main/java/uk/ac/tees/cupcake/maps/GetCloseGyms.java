package uk.ac.tees.cupcake.maps;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * getCloseGyms - Places markers on the requested location
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class GetCloseGyms extends AsyncTask<Object, String, String> implements GoogleMap.OnMarkerClickListener {
    private static final String TAG = "GetCloseGyms";
    private String googlePlaceData;
    private GoogleMap mMap;
    private String selectedItem;

    /**
     * @param objects - Retrieves the map location and the url to make the api call.
     *                It calls {@link DownloadURL} to retrieve the json data from the url.
     * @return - returns the JSON data from the URL
     */
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        String url = (String) objects[1];

        DownloadURL downloadUrl = new DownloadURL();
        try {
            googlePlaceData = downloadUrl.ReadURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlaceData;
    }


    /**
     * @param s - Retrieved JSON Data
     *          val - refers to which API call was made.
     *          stores the results in nearByPlaces and then calls the method depending on the API call
     */
    @Override
    protected void onPostExecute(String s) {
        String val = "results";

        if (s.toLowerCase().contains("candidates".toLowerCase())) {
            val = "candidates";
        }

        Log.d(TAG, "onPostExecute: " + s);
        List<HashMap<String, String>> nearByPlacesList;
        DataParser dataParser = new DataParser();
        nearByPlacesList = dataParser.parse(s, val);

        if (val.equals("results")) {
            DisplayNearbyGyms(nearByPlacesList);
        } else {
            DisplaySearchedLocation(nearByPlacesList);
        }
    }


    /**
     * @param nearByGymList - all the map locations
     *                      used with {@link GymMapActivity} - placing markers with gym locations nearby
     */
    private void DisplayNearbyGyms(List<HashMap<String, String>> nearByGymList) {
        if (nearByGymList.size() != 0) {
            for (int i = 0; i < nearByGymList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googleNearbyPlace = nearByGymList.get(i);
                String nameOfPlace = googleNearbyPlace.get("place_name");
                String vicinity = googleNearbyPlace.get("vicinity");
                double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
                double lng = Double.parseDouble(googleNearbyPlace.get("lng"));


                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(nameOfPlace + " : " + vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                mMap.addMarker(markerOptions);
                mMap.setOnMarkerClickListener(this);
            }
        } else {
            Log.d(TAG, "DisplayNearbyGyms: No Gyms in your area");
        }

    }

    /**
     * @param nearByGymList - all the map locations
     *                      used with {@link UserPostLocationActivity} - placing markers from the results searched for.
     */
    private void DisplaySearchedLocation(List<HashMap<String, String>> nearByGymList) {
        if (nearByGymList.size() != 0) {
            for (int i = 0; i < nearByGymList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();

                HashMap<String, String> googleNearbyPlace = nearByGymList.get(i);
                String nameOfPlace = googleNearbyPlace.get("place_name");
                double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
                double lng = Double.parseDouble(googleNearbyPlace.get("lng"));


                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(nameOfPlace);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                mMap.clear();
                mMap.addMarker(markerOptions);
                mMap.setOnMarkerClickListener(this);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            }
        } else {
            Log.d(TAG, "DisplayNearbyGyms: Location Not Found");
        }

    }

    /**
     * sets the selected item so it can be retrieved for the post.
     *
     * @param marker - the marker selected
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        setSelectedItem(marker.getTitle());

        Log.d(TAG, "onMarkerClick - selected Item: " + marker.getTitle());
        return false;
    }

    String getSelectedItem() {
        return selectedItem;
    }

    private void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }
}
