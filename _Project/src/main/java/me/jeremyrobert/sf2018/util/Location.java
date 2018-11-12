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
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "[" + latitude + ", " + longitude + "]";
    }
}
