package me.jeremyrobert.sf2018.util;

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

    @Override
    public String toString() {
        return "[" + bottomLeft + ", " + topRight + "]";
    }
}
