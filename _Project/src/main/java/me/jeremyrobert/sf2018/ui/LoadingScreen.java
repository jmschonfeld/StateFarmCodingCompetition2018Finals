package me.jeremyrobert.sf2018.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class LoadingScreen extends JFrame {
	private static final long serialVersionUID = 2800935210829232967L;

	private static final Dimension WINDOW_BOUNDS = new Dimension(500, 50);
	
	private LoadingScreen() {
		super("Loading...");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle(screen.width / 2 - WINDOW_BOUNDS.width / 2, screen.height / 2 - WINDOW_BOUNDS.height / 2, WINDOW_BOUNDS.width, WINDOW_BOUNDS.height);
		this.setBounds(bounds);
		this.setResizable(false);
		
		JProgressBar prog = new JProgressBar();
		prog.setIndeterminate(true);
		prog.setStringPainted(true);
		prog.setString("Loading...");
		
		this.setContentPane(prog);
	}
	
	private static LoadingScreen instance;
	
	public static void start() {
		instance = new LoadingScreen();
		instance.setVisible(true);
	}
	
	public static void end() {
		if (instance == null) {
			return;
		}
		instance.setVisible(false);
		instance = null;
	}
	
}
