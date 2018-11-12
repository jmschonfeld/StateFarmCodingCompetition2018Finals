package me.jeremyrobert.sf2018.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainContentPane extends JPanel {
	private static final long serialVersionUID = 1751629436927990392L;
	
	private JTabbedPane tabs;
	
	public MainContentPane() {
		tabs = new JTabbedPane();
		tabs.addTab("Risk Map", new MapViewPanel());
		tabs.addTab("Location Reports", new GenerateReportsPanel());
		this.setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		this.add(new JLabel(" © OpenStreetMap contributors, some data retrieved from Data.gov (https://www.data.gov/)"), BorderLayout.SOUTH);
	}

}
