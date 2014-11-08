package com.davidykay.digitaluniverse.test;

import java.util.List;
import java.io.File;

import org.junit.Test;

import com.badlogic.invaders.model.Importer;
import com.badlogic.invaders.model.Importer.DataLine;
import com.badlogic.invaders.model.Importer.ConfigLine;
import com.badlogic.invaders.model.Importer.SpeckLine;

import com.google.common.base.Optional;
import com.google.common.base.Charsets;
import com.google.common.io.Files;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


import com.badlogic.invaders.simulation.Star;

public class ImporterTest {

  private static final int DOLLAR   = 100;
  private static final int THOUSAND = 1000 * DOLLAR;
  private static final int MILLION  = 1000 * THOUSAND;

  @Test
  public void shouldParseDataLine() throws Exception {
          Importer importer = new Importer();
          Importer.DataLine dataLine = importer.parseDataLine("-2.2931 -22.3478 108.2944");

          assertThat(dataLine.getFloat(0), equalTo(-2.2931f));
          assertThat(dataLine.getFloat(1), equalTo(-22.3478f));
          assertThat(dataLine.getFloat(2), equalTo(108.2944f));
  }

  @Test
  public void shouldSplitLines() throws Exception {
          Importer importer = new Importer();
          String[] lines = importer.splitLines("-2.2931 \n-22.3478 \n108.2944");

          assertThat(lines.length, equalTo(3));
  }

  @Test
  public void shouldTrimLine() throws Exception {
          Importer importer = new Importer();

          String emptyLine = importer.trimLine("# DIGITAL UNIVERSE ATLAS");
          assertThat(emptyLine, equalTo(""));

          String numberLine = importer.trimLine("-2.2931 -22.3478 108.2944 1 360.6 1 # 11 Com");
          assertThat(numberLine, equalTo("-2.2931 -22.3478 108.2944 1 360.6 1"));
  }

  @Test
  public void shouldImportLines() throws Exception {
          Importer importer = new Importer();

          Optional<SpeckLine> emptyLine = importer.importLine("# DIGITAL UNIVERSE ATLAS");
          assertThat(emptyLine.isPresent(), equalTo(false));

          Optional<SpeckLine> configLine = importer.importLine("datavar 0 numplanets");
          assertThat(configLine.isPresent(), equalTo(true));
          //assertThat(configLine.get().getClass(), equalTo(ConfigLine.class));

          Optional<SpeckLine> dataLine = importer.importLine("-2.2931 -22.3478 108.2944 1 360.6 1 # 11 Com");
          assertThat(dataLine.isPresent(), equalTo(true));
          DataLine dataLineObj = (DataLine) dataLine.get();
          assertThat(dataLineObj.getFloat(0), equalTo(-2.2931f));
          //assertThat(dataLine.get().getClass(), equalTo(DataLine.class));
  }

  @Test
  public void shouldImportStars() throws Exception {
          Importer importer = new Importer();

          String path = "/home/dk/workspace/hacktheuniverse/example/libgdx-demo-invaders/android/assets/stars/simple.speck";
          String dataString = Files.toString(new File(path), Charsets.UTF_8);

          List<Star> stars = importer.importSimpleString(dataString);

          assertThat(stars.size(), equalTo(3));

          Star star1 = stars.get(0);

          assertThat(star1.getXPos(), equalTo(-2.2931f));
          assertThat(star1.getYPos(), equalTo(-22.3478f));
          assertThat(star1.getZPos(), equalTo(108.2944f));
  }

}
