package me.jeremyrobert.sf2018.util;

public class Location {

    private double latitude, longitude;

    private String displayName;
    
    public Location(double latitude, double longitude) {
    	this(latitude, longitude, null);
    }

    public Location(double latitude, double longitude, String displayName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayName = displayName;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        String str = "[" + latitude + ", " + longitude;

        if (displayName != null) {
            str += ", " + displayName;
        }

        return str + "]";
    }

    public String getDisplayName() {
        return displayName;
    }
}
