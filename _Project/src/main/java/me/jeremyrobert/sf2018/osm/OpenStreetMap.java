package me.jeremyrobert.sf2018.osm;

import me.jeremyrobert.sf2018.util.BoundingBox;
import me.jeremyrobert.sf2018.util.Location;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OpenStreetMap {

    private static class QueryData {
        BoundingBox box;
        Location loc;

        QueryData(BoundingBox box, Location loc) {
            this.box = box;
            this.loc = loc;
        }

        QueryData() {}
    }
    
    public Point getTile(BoundingBox box) {
    	int n = 2 ^ 15; // calculate zoom, not always 15
    	double lat = (box.getTopRight().getLatitude() + box.getBottomLeft().getLatitude()) / 2;
    	double lon = (box.getTopRight().getLongitude() + box.getBottomLeft().getLongitude()) / 2;
    	int xtile = n * (((int)lon + 180) / 360);
    	int lat_rad = (int)(lat * Math.PI) / 180;
    	int ytile = (int) (n * (1 - (Math.log(Math.tan(lat_rad) + 1/Math.cos(lat_rad)) / Math.PI)) / 2);
    	return new Point(xtile, ytile);
    }

    public static BoundingBox getBoundingBox(String query) throws IOException {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, "UTF-8") + "&format=xml";
        String result = getRequest(url);

        return parseQueryXML(result).box;
    }

    public static Location getLocation(String query) throws IOException {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, "UTF-8") + "&format=xml";
        String result = getRequest(url);

        return parseQueryXML(result).loc;
    }

    public static List<Location> fetchLocations(BoundingBox box, String amenity) throws IOException {
        Location bottomLeft = box.getBottomLeft();
        Location topRight = box.getTopRight();

        String url = "http://www.overpass-api.de/api/xapi_meta?node" +
                "[bbox=" + bottomLeft.getLongitude() + "," +
                bottomLeft.getLatitude() + "," +
                topRight.getLongitude() + "," +
                topRight.getLatitude() + "]" +
                "[amenity=" + amenity + "]";

        String result = getRequest(url);

        return parseLocationXML(result);
    }

    private static Document getXMLDocument(String xml) throws IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder build = factory.newDocumentBuilder();
            return build.parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Location> parseLocationXML(String xml) throws IOException {
        Document document = getXMLDocument(xml);
        if (document == null) {
            return new ArrayList<>();
        }

        // Loop through all <node> tags
        NodeList nodes = document.getElementsByTagName("node");
        List<Location> locations = new ArrayList<>();
        int len = nodes.getLength();

        for (int i = 0; i < len; i++) {
            Node n = nodes.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) { // Only look at element nodes
                continue;
            }
            Element node = (Element) n;

            // Store the lattitude and longitude attributes
            NamedNodeMap attr = node.getAttributes();
            Node latNode = attr.getNamedItem("lat"), lonNode = attr.getNamedItem("lon");
            String lat = null, lon = null;
            if (latNode != null && lonNode != null) {
                lat = latNode.getNodeValue();
                lon = lonNode.getNodeValue();
            }

            if (lat == null || lon == null) continue;

            locations.add(new Location(Double.parseDouble(lat), Double.parseDouble(lon)));
        }

        return locations;
    }

    private static QueryData parseQueryXML(String xml) throws IOException {
        Document document = getXMLDocument(xml);

        if (document == null) {
            return new QueryData();
        }

        NodeList places = document.getElementsByTagName("place");
        int len = places.getLength();

        if (len == 0) {
            return new QueryData();
        }

        Node node = places.item(0);
        NamedNodeMap attr = node.getAttributes();
        Node boundingBox = attr.getNamedItem("boundingbox");
        Node lat = attr.getNamedItem("lat");
        Node lon = attr.getNamedItem("lon");
        if (boundingBox == null || lat == null || lon == null) {
            return new QueryData();
        }

        String bbValue = boundingBox.getNodeValue();
        String latValue = lat.getNodeValue();
        String lonValue = lon.getNodeValue();
        if (bbValue == null || latValue == null || lonValue == null) {
            return new QueryData();
        }

        Node displayName = attr.getNamedItem("display_name");
        String displayNameStr = "[Unavailable]";
        if (displayName != null) {
            String value = displayName.getNodeValue();
            if (value != null) {
                displayNameStr = value;
            }
        }

        String[] vals = bbValue.split(",");
        return new QueryData(
                new BoundingBox(
                        new Location(Double.parseDouble(vals[0]), Double.parseDouble(vals[2])),
                        new Location(Double.parseDouble(vals[1]), Double.parseDouble(vals[3]))
                ),
                new Location(Double.parseDouble(latValue), Double.parseDouble(lonValue), displayNameStr)
        );
    }

    private static String getRequest(String urlStr) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
