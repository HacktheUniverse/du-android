package com.badlogic.invaders.model;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.List;

public class Importer {

				public DataSet importDataSet(String filename) {
								return new DataSet();
				}

				private String[] splitLine(String line) {
								String[] splited = line.split("\\s+");
								return splited;
				}

				private Optional<SpeckLine> importLine(String lineString) {

								String[] parts = splitLine(lineString);
								String first = parts[0];

								if (Character.isLetter(first.charAt(0))){
												// if it begins with a letter
												SpeckLine configLine = parseConfigLine(lineString);
												return Optional.of(configLine);
								} else if (Character.isDigit(first.charAt(0))){
												// if it begins with a number
												SpeckLine dataLine = parseDataLine(lineString);
												return Optional.of(dataLine);
								} else {
												// not valid data. continue
												return Optional.absent();
								}

				}

				private ConfigLine parseConfigLine(String line) {
								String[] parts = splitLine(line);
								return new ConfigLine(parts);
				}

				private DataLine parseDataLine(String line) {
								String[] components = splitLine(line);

								ArrayList<Float> numbers = new ArrayList<Float>();

								for (String numString : components) {
												float f = Float.parseFloat(numString);
												numbers.add(f);
								}

								return new DataLine(numbers);
				}

				public class DataLine implements SpeckLine {
								private List<Float> mNumbers;

								public DataLine(List<Float> numbers) {
												mNumbers = numbers;
								}
				}

				public class ConfigLine implements SpeckLine {
								private String[] components;

								public ConfigLine(String[] components) {
												this.components = components;
								}

								public boolean isDataVar() {
												return components[0].equals("datavar");
								}

								public DataVar getDataVar() {
												return new DataVar(
																				components[2],
																				Integer.parseInt(components[1]));
								}
				}

				public class DataVar {
								private String mName;
								private int mIndex;

								public DataVar(String name, int index) {
												mName = name;
												mIndex = index;
								}

				}

				public interface SpeckLine {

				}
}
