package me.jeremyrobert.sf2018.util;

import java.awt.Point;
import java.util.HashMap;

public class HeatMap {
	
	private static final int HEATMAP_WIDTH = 500, HEATMAP_HEIGHT = 500;
	
	private double[][] data;
	private BoundingBox bbox;
	
	private boolean hasUpdates = true;
	private HashMap<Location, Double> cache;
	
	public HeatMap(BoundingBox bbox) {
		this.bbox = bbox;
		data = new double[HEATMAP_HEIGHT][HEATMAP_WIDTH];
	}
	
	public void addPoint(Location loc, int value) {
		Point index = getIndiciesForLocation(loc);
		data[index.x][index.y] += value;
		this.hasUpdates = true;
	}
	
	public HashMap<Location, Double> getColoring() {
		if (this.hasUpdates) {
			this.normalize();
			
			cache = new HashMap<>();
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					cache.put(getLocationForIndicies(i, j), data[i][j]);
				}
			}
			this.hasUpdates = false;
		}
		
		
		return cache;
	}
	
	private void normalize() {
		double currentMax = Double.MIN_VALUE;
		for (int i = 0; i < data.length; i++) {
			double[] row = data[i];
			for (int j = 0; j < row.length; j++) {
				currentMax = Math.max(currentMax, row[j]);
			}
		}
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = data[i][j] / currentMax;
			}
		}
	}
	
	private Location getLocationForIndicies(int x, int y) {
		double latStep = Math.abs(bbox.getBottomLeft().getLatitude() - bbox.getTopRight().getLatitude()) / (double)HEATMAP_HEIGHT;
		double lat = latStep * x + (latStep / 2.0);
		double lonStep = Math.abs(bbox.getBottomLeft().getLongitude() - bbox.getTopRight().getLongitude()) / (double)HEATMAP_WIDTH;
		double lon = lonStep * y + (lonStep / 2.0);
		return new Location(lat + bbox.getBottomLeft().getLatitude(), lon + bbox.getBottomLeft().getLongitude());
	}
	
	private Point getIndiciesForLocation(Location loc) {
		double latStep = Math.abs(bbox.getBottomLeft().getLatitude() - bbox.getTopRight().getLatitude()) / (double)HEATMAP_HEIGHT;
		int heightIndex = (int) Math.floor((loc.getLatitude() - bbox.getBottomLeft().getLatitude()) / latStep);
		double lonStep = Math.abs(bbox.getBottomLeft().getLongitude() - bbox.getTopRight().getLongitude()) / (double)HEATMAP_WIDTH;
		int widthIndex = (int) Math.floor((loc.getLongitude() - bbox.getBottomLeft().getLongitude()) / lonStep);
		return new Point(heightIndex, widthIndex);
	}
}
