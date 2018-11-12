package me.jeremyrobert.sf2018.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JPanel;

import me.jeremyrobert.sf2018.util.BoundingBox;
import me.jeremyrobert.sf2018.util.HeatMap;
import me.jeremyrobert.sf2018.util.Location;

public class MapImagePanel extends JPanel{
	private static final long serialVersionUID = 8438814766386530335L;
	
	private Image image;
	private HeatMap heatMap;
	private BoundingBox bbox;
	
	private int width, height;

    public MapImagePanel(Image img, int width, int height) {
       this.image = img;
       this.width = width;
       this.height = height;
    }
    
    public HeatMap getHeatMap() {
    	return heatMap;
    }
    
    public void setImage(Image image, BoundingBox bbox) {
    	this.bbox = bbox;
    	this.image = image;
    	this.heatMap = new HeatMap(bbox);
    	this.update(this.getGraphics());
    }
    
    public Point locationToPixel(Location loc) {
    	/*double siny = Math.sin(loc.getLatitude() * Math.PI / 180.0);

        // Truncating to 0.9999 effectively limits latitude to 89.189. This is
        // about a third of a tile past the edge of the world tile.
        siny = Math.min(Math.max(siny, -0.9999), 0.9999);

        return new Point((int)Math.round(width * (0.5 + loc.getLongitude() / 360.0)), (int)Math.round(height * (0.5 - Math.log((1 + siny) / (1 - siny)) / (4 * Math.PI))));
    	*/
    	
    	double pixelsPerLat = (height * 1.0 / (bbox.getTopRight().getLatitude() - bbox.getBottomLeft().getLatitude()));
		double pixelsPerLong = (width * 1.0 / (bbox.getTopRight().getLongitude() - bbox.getBottomLeft().getLongitude()));

    	int x = (int)((loc.getLongitude()-bbox.getBottomLeft().getLongitude()) * pixelsPerLong);
    	int y = (int)(Math.abs(loc.getLatitude()-bbox.getTopRight().getLatitude()) * pixelsPerLat);
    	return new Point(x, y + 10);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
        	g.drawImage(image, 0, 0, width, height, this);
        	
        	if (heatMap != null) {
	        	HashMap<Location, Double> heat = heatMap.getColoring();
	        	for (Location loc : heat.keySet()) {
	        		double locHeat = heat.get(loc);
	        		Point p = locationToPixel(loc);
	        		//image.getGraphics().get
	        		g.setColor(new Color(255, 0, 0, (int)(locHeat * 255.0)));
	        		g.fillOval(p.x - 5, p.y - 5, 10, 10);
	        	}
        	}
        	
        } else {
        	g.clearRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

}