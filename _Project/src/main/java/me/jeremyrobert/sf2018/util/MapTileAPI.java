package me.jeremyrobert.sf2018.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import me.jeremyrobert.sf2018.Config;

public class MapTileAPI {
	public static final String BASE_URL = "https://image.maps.api.here.com/mia/1.6/mapview?" + Config.HERE_LOGON + "&h=650&w=1100&bbox=";
	
	public static class MapTile {
		public Image img;
		public BoundingBox bbox;
	}
	
	public static MapTile getImageForBoundingBox(BoundingBox box) throws MalformedURLException, IOException {
		String markersText = box.getBottomLeft().getLatitude() + "," + box.getBottomLeft().getLongitude() + ","
				+ box.getTopRight().getLatitude() + "," + box.getTopRight().getLongitude();
		HttpsURLConnection conn = (HttpsURLConnection) (new URL(BASE_URL + markersText).openConnection());
		BufferedImage img = ImageIO.read(conn.getInputStream());
		String topRight = conn.getHeaderField("Viewport-Top-Right");
		String bottomLeft = conn.getHeaderField("Viewport-Bottom-Left");
		MapTile tile = new MapTile();
		tile.img = img;
		tile.bbox = new BoundingBox(parse(bottomLeft), parse(topRight));
		return tile;
	}
	
	private static Location parse(String header) {
		String[] parts = header.split("\\,");
		String[] lats = parts[0].split("\\:");
		String[] lons = parts[1].split("\\:");
		double lat = Double.parseDouble(lats[1].trim());
		double lon = Double.parseDouble(lons[1].trim());
		return new Location(lat, lon);
	}
}
