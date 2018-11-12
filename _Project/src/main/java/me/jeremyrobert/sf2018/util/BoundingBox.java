package me.jeremyrobert.sf2018.util;

import java.util.List;
import java.util.stream.Collectors;

public class BoundingBox {

    private Location bottomLeft;

    private Location topRight;

    public BoundingBox(Location bottomLeft, Location topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public Location getTopRight() {
        return topRight;
    }

    public Location getBottomLeft() {
        return bottomLeft;
    }

    public List<Location> getContainedLocations(List<Location> locs) {
        return locs.stream().filter(loc -> loc.getLatitude() >= bottomLeft.getLatitude()
                && loc.getLongitude() >= bottomLeft.getLongitude()
                && loc.getLatitude() <= topRight.getLatitude()
                && loc.getLongitude() <= topRight.getLongitude()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "[" + bottomLeft + ", " + topRight + "]";
    }
}
