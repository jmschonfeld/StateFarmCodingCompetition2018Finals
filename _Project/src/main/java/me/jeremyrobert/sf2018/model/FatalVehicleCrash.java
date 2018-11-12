package me.jeremyrobert.sf2018.model;

import me.jeremyrobert.sf2018.util.DataField;

public class FatalVehicleCrash {
	@DataField(colName = "YEAR")
	public int year;
	
	@DataField(colName = "LATITUDE")
	public double latitude;
	
	@DataField(colName = "LONGITUD")
	public double longitude;
}
