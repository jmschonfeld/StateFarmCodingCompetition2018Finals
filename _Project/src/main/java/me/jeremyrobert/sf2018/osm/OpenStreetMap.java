package me.jeremyrobert.sf2018.osm;

import me.jeremyrobert.sf2018.util.BoundingBox;
import me.jeremyrobert.sf2018.util.Location;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    public static BoundingBox getBoundingBox(String query) throws IOException {
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, "UTF-8") + "&format=xml";
        String result = getRequest(url);
        System.out.println(result);
        return null;
    }

    public static Location getLocation(String query) throws IOException {
        return null;
    }

    public static List<Location> fetchLocations(Location bottomLeft, Location topRight, String amenity) throws IOException {
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

    private static BoundingBox parseBoundingBoxXML(String xml) throws IOException {
        Document document = getXMLDocument(xml);

        BoundingBox empty = new BoundingBox(new Location(0, 0), new Location(0, 0));

        if (document == null) {
            return empty;
        }

        NodeList places = document.getElementsByTagName("place");
        int len = places.getLength();

        if (len == 0) {
            return empty;
        }

        Node node = places.item(0);
        NamedNodeMap attr = node.getAttributes();
        Node boundingBox = attr.getNamedItem("boundingbox");
        if (boundingBox == null) {
            return empty;
        }
        //String value =
        return null;
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
