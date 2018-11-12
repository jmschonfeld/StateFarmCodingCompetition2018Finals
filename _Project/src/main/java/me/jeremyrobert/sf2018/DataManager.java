package me.jeremyrobert.sf2018;

import java.io.FileNotFoundException;

import me.jeremyrobert.sf2018.model.FatalVehicleCrash;
import me.jeremyrobert.sf2018.util.CSVParser;

public class DataManager {

	private static FatalVehicleCrash[] fatalVehicleCrashes;
	
	public static FatalVehicleCrash[] getFatalVehicleCrashes() {
		return fatalVehicleCrashes;
	}
	
	public static void loadAllData() throws FileNotFoundException {
		CSVParser<FatalVehicleCrash> parser = new CSVParser<>("datasets/accident.csv");
		fatalVehicleCrashes = parser.parse(FatalVehicleCrash.class);
	}
	
}
