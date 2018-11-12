package me.jeremyrobert.sf2018.model;

import me.jeremyrobert.sf2018.util.DataField;
import me.jeremyrobert.sf2018.util.Location;

public class FatalVehicleCrash {
	@DataField(colName = "YEAR")
	public int year;
	
	@DataField(colName = "LATITUDE")
	public double latitude;
	
	@DataField(colName = "LONGITUD")
	public double longitude;
	
	public Location getLocation() {
		return new Location(latitude, longitude);
	}
}
