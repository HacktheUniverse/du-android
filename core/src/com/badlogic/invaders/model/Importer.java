package com.badlogic.invaders.model;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.invaders.simulation.Star;

public class Importer {

        Random mRandom = new Random();

        private float randFloat() {
                return mRandom.nextFloat();
        }

        public String[] splitLines(String string) {
                String[] lines = string.split("\n");
                return lines;
        }

        public List<Star> importSimple(String filename) {
                FileHandle handle = Gdx.files.internal(filename);
                if (handle.exists()) {
                        String text = handle.readString();

                        return importSimpleString(text);
                } else {
                        throw new RuntimeException("Invalid filename! " + filename);
                }
        }

        public List<Star> importSimpleString(String dataString) {
                String[] lines = splitLines(dataString);

                ArrayList<Star> stars = new ArrayList<Star>();

                for (String lineString : lines) {
                        DataLine dataLine = parseDataLine(lineString);
                        stars.add(dataLineToSimpleStar(dataLine));

                        // Optional<SpeckLine> speckLine = importLine(lineString);
                        // if (speckLine.isPresent()
                        //                 //&& speckLine.get() instanceof DataLine
                        //                 ) {
                        //         DataLine dataLine = (DataLine) speckLine.get();
                        //         stars.add(dataLineToSimpleStar(dataLine));
                        //         // TODO: restore me
                        //         // skipped for now
                        // } else {
                        //         // we don't care about config right now.
                        //         //Gdx.app.debug("MyTag", "line was config, not data.");
                        //         //System.out.println("MyTag", "line was config, not data.");
                        //         //System.out.println("line was config, not data.");
                        //         //throw new RuntimeException("didn't recognize config line");
                        // }
                }

                return stars;
        }

        public Star dataLineToSimpleStar(DataLine dataLine) {
                float xPos = dataLine.getFloat(0);
                float yPos = dataLine.getFloat(1);
                float zPos = dataLine.getFloat(2);

                return new Star(
                                xPos,
                                yPos,
                                zPos,
                                // TODO: actually parse these
                                randFloat(),
                                randFloat()
                        );
        }

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

                if (lineString.length() == 0) {
                        return Optional.absent();
                } else {
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
        }

        private ConfigLine parseConfigLine(String line) {
                String[] parts = splitLine(line);
                return new ConfigLine(parts);
        }

        public DataLine parseDataLine(String line) {
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

                public float getFloat(int index) {
                        return mNumbers.get(index);
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
