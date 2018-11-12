package me.jeremyrobert.sf2018.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;
import java.lang.reflect.ParameterizedType;

public class CSVParser<T > {
	
	private String filename;
	
	public CSVParser(String filename) {
		this.filename = filename;
	}

	@SuppressWarnings("unchecked")
	public T[] parse() throws FileNotFoundException {
		Class<T> objClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

		Map<String, Field> colFields = new HashMap<>(); // Maps column name to the class's field

		Arrays.stream(objClass.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(DataField.class))
				.forEach(field -> {
					DataField annotation = field.getAnnotation(DataField.class);
					colFields.put(annotation.colName(), field);
				});

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

					Field field = colFields.get(columnName);

					if (field == null) continue;

					Object parsed;
					if (field.getType() == Integer.TYPE) {
						parsed = Integer.parseInt(value);
					} else if (field.getType() == Double.TYPE) {
						parsed = Double.parseDouble(value);
					} else if (field.getType() == String.class) {
						parsed = value;
					} else {
						throw new RuntimeException("Unsupported DataField type: " + field.getType().getName());
					}
					
					field.set(object, parsed);
				}
				
				list.add(object);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		scanner.close();
		T[] arr = (T[]) new Object[0];
		return list.toArray(arr);
	}
}
