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

public class GetCloseGyms extends AsyncTask<Object, String, String> implements GoogleMap.OnMarkerClickListener {
    private static final String TAG = "GetCloseGyms";
    private String googleplaceData;
    private GoogleMap mMap;
    private String selectedItem;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        String url = (String) objects[1];

        DownloadURL downloadUrl = new DownloadURL();
        try {
            googleplaceData = downloadUrl.ReadURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleplaceData;
    }


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
