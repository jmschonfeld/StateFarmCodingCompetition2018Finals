package me.jeremyrobert.sf2018.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.reflect.ParameterizedType;

public class CSVParser<T > {
	
	private String filename;
	
	public CSVParser(String filename) {
		this.filename = filename;
	}

	@SuppressWarnings("unchecked")
	public T[] parse() throws FileNotFoundException {
		Class<T> objClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException("Unable to find file '" + filename + "'");
		}
		
		Scanner scanner = new Scanner(f);
		String columnLine = scanner.nextLine();
		String[] headers = columnLine.split("\\,");
		ArrayList<T> list = new ArrayList<T>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(",");
			try {
				T object = objClass.newInstance();
				
				for (int i = 0; i < parts.length; i++) {
					String columnName = headers[i];
					String value = parts[i];
					
					// TODO: Robert apply the 'value' to 'object' with the correct field using 'columnName' (and the class 'objClass')
					
				}
				
				list.add(object);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		scanner.close();
		T[] arr = (T[]) new Object[0];
		return list.toArray(arr);
	}

}
