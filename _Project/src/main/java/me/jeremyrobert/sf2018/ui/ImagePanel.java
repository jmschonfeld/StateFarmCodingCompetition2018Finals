package me.jeremyrobert.sf2018.ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private static final long serialVersionUID = 8438814766386530335L;
	
	private Image image;

    public ImagePanel(Image img) {
       this.image = img;
    }
    
    public void setImage(Image image) {
    	this.image = image;
    	this.update(this.getGraphics());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
        	g.drawImage(image, 0, 0, this);    
        } else {
        	g.clearRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

}