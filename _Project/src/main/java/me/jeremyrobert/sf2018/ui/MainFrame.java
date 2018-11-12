package me.jeremyrobert.sf2018.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 5391024478019540407L;
	
	private static final Dimension WINDOW_BOUNDS = new Dimension(1500, 750);
	
	public MainFrame() {
		super("State Farm Coding Competition 2018 Finals - Robert Pooley and Jeremy Schonfeld");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle(screen.width / 2 - WINDOW_BOUNDS.width / 2, screen.height / 2 - WINDOW_BOUNDS.height / 2, WINDOW_BOUNDS.width, WINDOW_BOUNDS.height);
		this.setBounds(bounds);
		this.setResizable(false);
		this.setContentPane(new MainContentPane());
	}

}
