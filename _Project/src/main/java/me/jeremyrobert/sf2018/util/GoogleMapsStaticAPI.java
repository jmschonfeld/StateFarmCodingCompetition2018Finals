package me.jeremyrobert.sf2018.util;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import me.jeremyrobert.sf2018.Config;

public class GoogleMapsStaticAPI {
	public static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?size=640x640&key=" + Config.MAPS_STATIC_API_KEY + "&markers=size:tiny|";
	
	
	public static Image getImageForBoundingBox(BoundingBox box) throws MalformedURLException, IOException {
		String markersText = box.getBottomLeft().getLatitude() + "," + box.getBottomLeft().getLongitude() + "|"
				+ box.getTopRight().getLatitude() + "," + box.getTopRight().getLongitude();
		HttpsURLConnection conn = (HttpsURLConnection) (new URL(BASE_URL + markersText).openConnection());
		Image img = ImageIO.read(conn.getInputStream());
		return img;
	}
}
