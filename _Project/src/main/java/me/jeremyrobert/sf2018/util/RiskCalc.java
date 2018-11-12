package me.jeremyrobert.sf2018.util;

import me.jeremyrobert.sf2018.osm.OpenStreetMap;
import me.jeremyrobert.sf2018.task.Task;
import me.jeremyrobert.sf2018.task.TaskRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiskCalc {

    public static class RiskData {
        public double risk;
        public String description;

        public RiskData(double risk, String description) {
            this.risk = risk;
            this.description = description;
        }
    }

    public static Map<InsuranceType, RiskData> calculateRisks(Location location) {
        BoundingBox box = new BoundingBox(location.addMileOffset(-5, -5), location.addMileOffset(5, 5));

        InsuranceType[] types = InsuranceType.values();
        /*Task[] tasks = new Task[types.length];
        for (int i = 0; i < types.length; i++) {
            tasks[i] = getRiskTask(types[i], box);
        }

        Double[] risks = TaskRunner.runTasks(tasks, Double.class);*/

        Map<InsuranceType, RiskData> riskMap = new HashMap<>();
        /*for (int i = 0; i < types.length; i++) {
            riskMap.put(types[i], risks[i]);
        }*/
        try {
            riskMap.put(InsuranceType.AUTO, calculateAutoRisk(box));
            riskMap.put(InsuranceType.HOME, calculateHomeRisk(box));
            riskMap.put(InsuranceType.LIFE, calculateLifeRisk(box));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return riskMap;
    }

    private static RiskData calculateAutoRisk(BoundingBox box) throws IOException {
        List<Location> locs = OpenStreetMap.fetchLocations(box, "bar");
        return new RiskData(0.5 + locs.size() / 5, locs.size() + " nearby bar" + s(locs.size()));
    }

    private static RiskData calculateHomeRisk(BoundingBox box) throws IOException {
        /*List<Location>[] arrs = TaskRunner.runTasks(new Task[]{
                getLocationsTask(box, "school"),
                getLocationsTask(box, "fire_station")
        }, List.class);

        List<Location> schools = arrs[0];
        List<Location> fireDepts = arrs[1];*/

        List<Location> schools = OpenStreetMap.fetchLocations(box, "school");
        List<Location> fireDepts = OpenStreetMap.fetchLocations(box, "fire_station");

        return new RiskData(Math.max(0, (0.5 + schools.size() / 5 - fireDepts.size() / 5) / 3.0), schools.size() +
                " nearby school" + s(schools.size()) + ", " + fireDepts.size() + " nearby fire station" + s(fireDepts.size()));
    }

    private static String s(int num) {
        return num == 1 ? "" : "s";
    }

    private static RiskData calculateLifeRisk(BoundingBox box) throws IOException {
        List<Location> restaurants = OpenStreetMap.fetchLocations(box, "restaurant");

        return new RiskData((0.5 + restaurants.size() / 5) / 7.0, restaurants.size() + " nearby restaurant" + s(restaurants.size()));
    }
/*
    private static Task<List<Location>> getLocationsTask(BoundingBox box, String amenity) {
        return new Task<List<Location>>() {
            @Override
            public List<Location> run() {
                try {
                    return OpenStreetMap.fetchLocations(box, amenity);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    private static Task<Double> getRiskTask(InsuranceType type, BoundingBox box) {
        return new Task<Double>() {
            @Override
            public Double run() {
                try {
                    switch(type) {
                        case AUTO:
                            return calculateAutoRisk(box);
                        case HOME:
                            return calculateHomeRisk(box);
                        case LIFE:
                            return calculateLifeRisk(box);
                        default:
                            throw new RuntimeException("Insurance type not implemented: " + type);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1.0;
                }
            }
        };
    }*/
}
