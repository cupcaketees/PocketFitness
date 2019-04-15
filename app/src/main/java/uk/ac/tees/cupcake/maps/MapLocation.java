package uk.ac.tees.cupcake.maps;

import java.util.HashMap;

class MapLocation {

    private HashMap<String, String> googlePlaceMap;
    private String nameOfLocation;
    private String vicinity;
    private String latitude;
    private String longitude;
    private String reference;

    MapLocation() {
        googlePlaceMap = new HashMap<>();
        nameOfLocation = "-NA-";
        vicinity = "-NA-";
        latitude = "";
        longitude = "";
        reference = "";
    }

    HashMap<String, String> setGooglePlaceMap() {
        googlePlaceMap.put("place_name", nameOfLocation);
        googlePlaceMap.put("vicinity", vicinity);
        googlePlaceMap.put("lat", latitude);
        googlePlaceMap.put("lng", longitude);
        googlePlaceMap.put("reference", reference);

        return googlePlaceMap;
    }

    void setNameOfLocation(String nameOfLocation) {
        this.nameOfLocation = nameOfLocation;
    }

    void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    void setReference(String reference) {
        this.reference = reference;
    }
}
