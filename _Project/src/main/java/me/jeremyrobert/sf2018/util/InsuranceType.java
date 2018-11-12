package me.jeremyrobert.sf2018.util;

public enum InsuranceType {
	AUTO("Auto"), HOME("Home"), LIFE("Life");
	
	private String name;
	
	InsuranceType(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return name;
	}
}
