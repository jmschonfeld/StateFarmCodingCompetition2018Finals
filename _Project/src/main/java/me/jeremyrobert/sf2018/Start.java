package me.jeremyrobert.sf2018;

import java.io.FileNotFoundException;

public class Start {

	public static void main(String[] args) {
		System.out.print("Loading all data...");
		try {
			DataManager.loadAllData();
			System.out.println(" done");
		} catch (FileNotFoundException e) {
			System.out.println();
			e.printStackTrace();
		}
	}
	
	
}
