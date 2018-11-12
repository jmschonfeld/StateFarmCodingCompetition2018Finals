package me.jeremyrobert.sf2018.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import me.jeremyrobert.sf2018.util.InsuranceType;

public class MapViewPanel extends JPanel {
	private static final long serialVersionUID = 3948060070912799213L;
	
	private JTextField searchBar;
	private JButton searchButton;
	private JComboBox<String> insuranceSelector;

	public MapViewPanel() {
		this.setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel();
		JPanel searchPanel = new JPanel();
		searchBar = new JTextField(20);
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			onSearch(searchBar.getText());
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
	}
	
	private void onSearch(String term) {
		System.out.println("Searched for '" + term + "'");
	}
	
	private void onChangeInsuranceType(InsuranceType type) {
		System.out.println("Changed insurance to '" + type.name() + "'");
	}
}
