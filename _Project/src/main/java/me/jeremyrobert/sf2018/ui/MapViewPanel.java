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

import me.jeremyrobert.sf2018.osm.OpenStreetMap;
import me.jeremyrobert.sf2018.util.BoundingBox;
import me.jeremyrobert.sf2018.util.GoogleMapsStaticAPI;
import me.jeremyrobert.sf2018.util.InsuranceType;

public class MapViewPanel extends JPanel {
	private static final long serialVersionUID = 3948060070912799213L;
	
	private JTextField searchBar;
	private JButton searchButton;
	private JComboBox<String> insuranceSelector;
	private ImagePanel imageView;

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
		
		imageView = new ImagePanel(null);
		this.add(imageView, BorderLayout.CENTER);
	}
	
	private void onSearch(String term) {
		System.out.println("Searched for '" + term + "'");
		BoundingBox boundBox;
		try {
			boundBox = OpenStreetMap.getBoundingBox(term);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error ocurred while generating the map. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (boundBox == null) {
			JOptionPane.showMessageDialog(null, "Location not found. Please try another search term.", "Location Not Found", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Image tile;
		try {
			tile = GoogleMapsStaticAPI.getImageForBoundingBox(boundBox);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error ocurred while displaying the map. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.imageView.setImage(tile);
		System.out.println("Updated map");
	}
	
	private void onChangeInsuranceType(InsuranceType type) {
		System.out.println("Changed insurance to '" + type.name() + "'");
	}
}
