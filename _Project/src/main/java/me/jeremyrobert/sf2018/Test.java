package me.jeremyrobert.sf2018;

import me.jeremyrobert.sf2018.osm.OpenStreetMap;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        OpenStreetMap.getBoundingBox("3246 Front Rd");
    }

}
