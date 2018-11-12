package me.jeremyrobert.sf2018;

import me.jeremyrobert.sf2018.osm.OpenStreetMap;
import me.jeremyrobert.sf2018.util.BoundingBox;
import me.jeremyrobert.sf2018.util.Location;

import java.io.IOException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {
        Location l = OpenStreetMap.getLocation("3246 Front Rd");
        System.out.println(l.addMileOffset(-2, -4));
    }

}
