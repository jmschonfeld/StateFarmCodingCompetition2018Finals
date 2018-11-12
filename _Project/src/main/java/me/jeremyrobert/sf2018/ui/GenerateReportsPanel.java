package me.jeremyrobert.sf2018.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.BorderFactory;import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import me.jeremyrobert.sf2018.osm.OpenStreetMap;
import me.jeremyrobert.sf2018.util.InsuranceType;
import me.jeremyrobert.sf2018.util.Location;
import me.jeremyrobert.sf2018.util.RiskCalc;

public class GenerateReportsPanel extends JPanel {
	private static final long serialVersionUID = 4799016462897119767L;
	
	private JTextField locationField;
	private JPanel resPanel;
	private JLabel rLocation, rAuto, rAutoContrib, rHome, rHomeContrib, rLife, rLifeContrib;
	
	public GenerateReportsPanel() {
		this.setLayout(new GridBagLayout());
		
		JPanel inputPanel = new JPanel();
		JPanel outputPanel = new JPanel();
		
		inputPanel.setLayout(new BorderLayout());
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new SpringLayout());
		
		formPanel.add(new JLabel("Location:"));
		locationField = new JTextField();
		formPanel.add(locationField);

		SpringUtilities.makeCompactGrid(formPanel, 1, 2, 20, 20, 50, 6);
		
		inputPanel.add(formPanel, BorderLayout.NORTH);

		JButton generateButton = new JButton("Generate Report");
		generateButton.addActionListener(e -> {
			generate();
		});
		inputPanel.add(generateButton, BorderLayout.SOUTH);
		
		// -----
		
		outputPanel.setLayout(new BorderLayout());
		
		resPanel = new JPanel();
		resPanel.setLayout(new SpringLayout());
		
		resPanel.add(new JLabel("Location:"));
		rLocation = new JLabel();
		resPanel.add(rLocation);
		resPanel.add(new JLabel("Auto Insurance Risk:"));
		rAuto = new JLabel();
		resPanel.add(rAuto);
		resPanel.add(new JLabel("Auto Insurance Factors:"));
		rAutoContrib = new JLabel();
		resPanel.add(rAutoContrib);
		resPanel.add(new JLabel("Home Insurance Risk:"));
		rHome = new JLabel();
		resPanel.add(rHome);
		resPanel.add(new JLabel("Home Insurance Factors:"));
		rHomeContrib = new JLabel();
		resPanel.add(rHomeContrib);
		resPanel.add(new JLabel("Life Insurance Risk:"));
		rLife = new JLabel();
		resPanel.add(rLife);
		resPanel.add(new JLabel("Life Insurance Factors:"));
		rLifeContrib = new JLabel();
		resPanel.add(rLifeContrib);
		resPanel.add(new JPanel());
		resPanel.add(Box.createHorizontalGlue());

		formatReportPanel();
		
		outputPanel.add(resPanel, BorderLayout.NORTH);
		
		
		inputPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));
		outputPanel.setBorder(BorderFactory.createTitledBorder("Generated Report"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(inputPanel, c);
		c.gridx = 1;
		c.gridy = 0;
		this.add(outputPanel, c);
	}
	
	private void formatReportPanel() {
		SpringUtilities.makeCompactGrid(resPanel, 8, 2, 20, 20, 50, 20);
	}
	
	private void generate() {
		LoadingScreen.start();
		System.out.println("Generating report for '" + locationField.getText() + "'");
		rLocation.setText("");
		rAuto.setText("");
		rAutoContrib.setText("");
		rHome.setText("");
		rHomeContrib.setText("");
		rLife.setText("");
		rLifeContrib.setText("");
		
		try {
			Location location = OpenStreetMap.getLocation(locationField.getText());
			if (location == null) {
				LoadingScreen.end();
				JOptionPane.showMessageDialog(null, "Location not found. Please try another search term.", "Location Not Found", JOptionPane.WARNING_MESSAGE);
				return;
			}

			Map<InsuranceType, Double> risks = RiskCalc.calculateRisks(location);
			
			rLocation.setText(location.getDisplayName());
			rAuto.setText(formatRisk(risks.get(InsuranceType.AUTO)));
			rHome.setText(formatRisk(risks.get(InsuranceType.HOME)));
			rLife.setText(formatRisk(risks.get(InsuranceType.LIFE)));
			formatReportPanel();
		} catch (IOException e) {
			e.printStackTrace();
			LoadingScreen.end();
			JOptionPane.showMessageDialog(null, "An error ocurred while generating the report. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		LoadingScreen.end();
	}

	private String formatRisk(double risk) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(risk);
	}
}
