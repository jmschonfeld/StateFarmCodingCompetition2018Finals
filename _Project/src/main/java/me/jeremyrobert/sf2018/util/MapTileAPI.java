package me.jeremyrobert.sf2018.util;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import me.jeremyrobert.sf2018.Config;

public class MapTileAPI {
	public static final String BASE_URL = "https://image.maps.api.here.com/mia/1.6/mapview?app_id=lGq9E35cPQxaLBmQrL2M&app_code=RN5XR00Ibz1QjLlB_d4LGA&bbox=";
	
	
	public static Image getImageForBoundingBox(BoundingBox box) throws MalformedURLException, IOException {
		String markersText = box.getBottomLeft().getLatitude() + "," + box.getBottomLeft().getLongitude() + ","
				+ box.getTopRight().getLatitude() + "," + box.getTopRight().getLongitude();
		HttpsURLConnection conn = (HttpsURLConnection) (new URL(BASE_URL + markersText).openConnection());
		Image img = ImageIO.read(conn.getInputStream());
		return img;
	}
}