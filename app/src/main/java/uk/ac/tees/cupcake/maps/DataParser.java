package uk.ac.tees.cupcake.maps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Data Parser - parses the json data to HashMap.
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
class DataParser {


    /**
     * @param googleSingleLocationJSON - retrieves a single location to be stored in a object
     * @return - the Hashmap with name,vicinity, lat,lng,ref stored.
     */
    private HashMap<String, String> singleLocation(JSONObject googleSingleLocationJSON) {
        MapLocation mapLocation = new MapLocation();

        try {
            if (!googleSingleLocationJSON.isNull("name")) {
                mapLocation.setNameOfLocation(googleSingleLocationJSON.getString("name"));
            }
            if (!googleSingleLocationJSON.isNull("vicinity")) {
                mapLocation.setVicinity(googleSingleLocationJSON.getString("vicinity"));
            }
            mapLocation.setLatitude(googleSingleLocationJSON.getJSONObject("geometry").getJSONObject("location").getString("lat"));
            mapLocation.setLongitude(googleSingleLocationJSON.getJSONObject("geometry").getJSONObject("location").getString("lng"));
            mapLocation.setReference(googleSingleLocationJSON.getString("reference"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getSingleNearbyPlace: " + mapLocation.toString());

        return mapLocation.setGooglePlaceMap();
    }


    /**
     * @param jsonArray - Retrieves all locations into an array
     *                  it calls method above retrieving the data of each individual location and storing it in a variable
     * @return the list of all locations but location data stored in hashmaps
     */
    private List<HashMap<String, String>> getAllNearbyGyms(JSONArray jsonArray) {
        int totalGyms = jsonArray.length();

        List<HashMap<String, String>> nearbyGymsList = new ArrayList<>();

        HashMap<String, String> nearbyGymMap;

        for (int i = 0; i < totalGyms; i++) {
            try {
                nearbyGymMap = singleLocation((JSONObject) jsonArray.get(i));
                nearbyGymsList.add(nearbyGymMap);
                Log.d(TAG, "getSingleNearbyPlace: " + nearbyGymMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return nearbyGymsList;
    }


    /**
     * @param JSONData - All the information retrieved from the URL
     * @param type     - due to the retrieval of the two apis returning different object names it passes the name so it knows what json array to retrieve.
     * @return if empty return null otherwise passes to method to start retrieving the data.
     */
    List<HashMap<String, String>> parse(String JSONData, String type) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        Log.d(TAG, "parse: " + JSONData);
        try {
            jsonObject = new JSONObject(JSONData);
            jsonArray = jsonObject.getJSONArray(type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            return getAllNearbyGyms(jsonArray);
        }

        return null;

    }
}
