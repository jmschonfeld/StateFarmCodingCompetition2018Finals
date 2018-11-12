package me.jeremyrobert.sf2018.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import me.jeremyrobert.sf2018.DataManager;
import me.jeremyrobert.sf2018.model.FatalVehicleCrash;
import me.jeremyrobert.sf2018.osm.OpenStreetMap;
import me.jeremyrobert.sf2018.util.BoundingBox;
import me.jeremyrobert.sf2018.util.MapTileAPI;
import me.jeremyrobert.sf2018.util.InsuranceType;
import me.jeremyrobert.sf2018.util.Location;

public class MapViewPanel extends JPanel {
	private static final long serialVersionUID = 3948060070912799213L;
	
	private JTextField searchBar;
	private JButton searchButton;
	private JComboBox<String> insuranceSelector;
	private MapImagePanel imageView;
	
	private InsuranceType insuranceType = InsuranceType.AUTO;
	private String currentTerm = null;

	public MapViewPanel() {
		this.setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel();
		JPanel searchPanel = new JPanel();
		searchBar = new JTextField(20);
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			new Thread() {
				@Override
				public void run() {
					onSearch(searchBar.getText());
				}
			}.start();
		});
		
		searchPanel.setLayout(new FlowLayout());
		searchPanel.add(searchBar);
		searchPanel.add(searchButton);
		
		JPanel iSPanel = new JPanel();
		iSPanel.setLayout(new FlowLayout());
		iSPanel.add(new JLabel("Insurance Type:"));
		insuranceSelector = new JComboBox<String>(Arrays.stream(InsuranceType.values()).map(t -> {
			return t.getTitle();
		}).toArray(value -> {
			return new String[value];
		}));
		insuranceSelector.addActionListener(e -> {
			onChangeInsuranceType(InsuranceType.valueOf(((String)insuranceSelector.getSelectedItem()).toUpperCase()));
		});
		iSPanel.add(insuranceSelector);
		
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		menuPanel.add(searchPanel);
		menuPanel.add(iSPanel);
		menuPanel.add(Box.createVerticalStrut(500));
		
		menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
		this.add(menuPanel, BorderLayout.EAST);
		
		imageView = new MapImagePanel(null, 1100, 650);
		this.add(imageView, BorderLayout.CENTER);
	}
	
	private void onSearch(String term) {
		if (term == null) {
			return;
		}
		currentTerm = term;
		LoadingScreen.start();
		System.out.println("Searched for '" + term + "'");
		BoundingBox boundBox;
		try {
			boundBox = OpenStreetMap.getBoundingBox(term);
		} catch (IOException e) {
			e.printStackTrace();
			LoadingScreen.end();
			JOptionPane.showMessageDialog(null, "An error ocurred while generating the map. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (boundBox == null) {
			LoadingScreen.end();
			JOptionPane.showMessageDialog(null, "Location not found. Please try another search term.", "Location Not Found", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Image tile;
		try {
			tile = MapTileAPI.getImageForBoundingBox(boundBox);
		} catch (IOException e) {
			e.printStackTrace();
			LoadingScreen.end();
			JOptionPane.showMessageDialog(null, "An error ocurred while displaying the map. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.imageView.setImage(tile, boundBox);
		
		if (insuranceType == InsuranceType.AUTO) {
			FatalVehicleCrash[] crashes = DataManager.getFatalVehicleCrashes();
			for (FatalVehicleCrash crash : crashes) {
				this.imageView.getHeatMap().addPoint(crash.getLocation(), 1);
			}
			this.imageView.getHeatMap().getColoring();
			this.imageView.repaint();
			this.imageView.setVisible(true);
		} else if (insuranceType == InsuranceType.HOME) {
			try {
				for (Location loc : OpenStreetMap.fetchLocations(boundBox, "school")) {
					this.imageView.getHeatMap().addPoint(loc);
				}
				for (Location loc : OpenStreetMap.fetchLocations(boundBox, "fire_station")) {
					this.imageView.getHeatMap().addPoint(loc, -1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (insuranceType == InsuranceType.LIFE) {
			try {
				for (Location loc : OpenStreetMap.fetchLocations(boundBox, "restaurant")) {
					this.imageView.getHeatMap().addPoint(loc);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		LoadingScreen.end();
	}
	
	private void onChangeInsuranceType(InsuranceType type) {
		this.insuranceType = type;
		new Thread() {
			@Override
			public void run() {
				onSearch(currentTerm);
			}
		}.start();
	}
}
