package me.jeremyrobert.sf2018;

import me.jeremyrobert.sf2018.osm.OpenStreetMap;
import me.jeremyrobert.sf2018.util.Location;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        Location box = OpenStreetMap.getLocation("3246 Front Rd");
        System.out.println(box);
    }

}
