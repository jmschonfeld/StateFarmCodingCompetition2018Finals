package me.jeremyrobert.sf2018.util;

public class Util {

    private static final double MINUTES_TO_METERS = 1852d;

    private static final double DEGREE_TO_MINUTES = 60d;

    public static Location getLocationOffset(Location start, final double course, double distance) {
        distance *= 1609.34;

        double startPointLat = start.getLatitude();
        double startPointLon = start.getLongitude();

        final double crs = Math.toRadians(course);
        final double d12 = Math.toRadians(distance / MINUTES_TO_METERS / DEGREE_TO_MINUTES);

        final double lat1 = Math.toRadians(startPointLat);
        final double lon1 = Math.toRadians(startPointLon);

        final double lat = Math.asin(Math.sin(lat1) * Math.cos(d12)
                + Math.cos(lat1) * Math.sin(d12) * Math.cos(crs));
        final double dlon = Math.atan2(Math.sin(crs) * Math.sin(d12) * Math.cos(lat1),
                Math.cos(d12) - Math.sin(lat1) * Math.sin(lat));
        final double lon = (lon1 + dlon + Math.PI) % (2 * Math.PI) - Math.PI;

        return new Location(Math.toDegrees(lat), Math.toDegrees(lon));
    }
}
