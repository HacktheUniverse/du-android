/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.invaders.simulation;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.common.collect.Lists;
import com.google.common.collect.Lists;
import com.google.common.base.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.invaders.model.ColorConverter;
import com.badlogic.invaders.model.Importer;

public class Simulation implements Disposable {
        private static final boolean SMOOTH_CAMERA = false;

	public final static float PLAYFIELD_MIN_X = -14;
	public final static float PLAYFIELD_MAX_X = 14;
	public final static float PLAYFIELD_MIN_Z = -15;
	public final static float PLAYFIELD_MAX_Z = 2;

	public List<StarModel> starModels = new ArrayList<StarModel>();
	public ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	public transient SimulationListener listener;
	public float multiplier = 1;
	public int score;
	public int wave = 1;

	public Model starModel;
	public Model explosionModel;

	private ArrayList<Shot> removedShots = new ArrayList<Shot>();
	private ArrayList<Explosion> removedExplosions = new ArrayList<Explosion>();

	private final Vector3 tmpV1 = new Vector3();
	private final Vector3 tmpV2 = new Vector3();

	private static final String TAG = "Simulation";
	private static final boolean DEBUG = true;

	public Simulation () {
		populate();
	}

	private void populate () {
		ObjLoader objLoader = new ObjLoader();
		starModel = objLoader.loadModel(Gdx.files.internal("data/shot.obj"));
		starModel.materials.get(0).set(ColorAttribute.createDiffuse(1, 1, 0, 1f));

		float[] vertices = new float[4 * 16 * (3 + 2)];
		short[] indices = new short[6 * 16];
		int idx = 0;
		int index = 0;
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 4; column++) {
				vertices[idx++] = 1;
				vertices[idx++] = 1;
				vertices[idx++] = 0;
				vertices[idx++] = 0.25f + column * 0.25f;
				vertices[idx++] = 0 + row * 0.25f;

				vertices[idx++] = -1;
				vertices[idx++] = 1;
				vertices[idx++] = 0;
				vertices[idx++] = 0 + column * 0.25f;
				vertices[idx++] = 0 + row * 0.25f;

				vertices[idx++] = -1;
				vertices[idx++] = -1;
				vertices[idx++] = 0;
				vertices[idx++] = 0f + column * 0.25f;
				vertices[idx++] = 0.25f + row * 0.25f;

				vertices[idx++] = 1;
				vertices[idx++] = -1;
				vertices[idx++] = 0;
				vertices[idx++] = 0.25f + column * 0.25f;
				vertices[idx++] = 0.25f + row * 0.25f;

				final int t = (4 * row + column) * 4;
				indices[index++] = (short)(t);
				indices[index++] = (short)(t + 1);
				indices[index++] = (short)(t + 2);
				indices[index++] = (short)(t);
				indices[index++] = (short)(t + 2);
				indices[index++] = (short)(t + 3);
			}
		}


    // dummyStars();
    importStars();
	}

  private void dummyStars() {
          Star sol = new Star(0, 0, 0, 1f, 360.6f);

          Star starA = new Star(-2.2931f, -22.3478f, 108.2944f, 1f, 360.6f);
          Star starB = new Star(-28.9180f, 85.3411f, 78.4522f, 1f, 389.5f);
          Star starC = new Star(-20.1427f, 68.3916f, -27.4397f, 1f, 249.1f);


          List<Star> stars = Lists.newArrayList(sol);

          Function<Star, StarModel> starToStarModel =
                  new Function<Star, StarModel>() {
                          public StarModel apply(Star star) {
                                  return StarModel.newInstance(star, starModel);
                          }
                  };

          List<StarModel> starModels = Lists.transform(stars, starToStarModel);
          this.starModels = starModels;
          System.out.println("Starmodels: " + starModels);

  }

  private void importStars() {
          Importer importer = new Importer();
          String path = "stars/expl.speck";
          List<Star> stars = importer.importComplex(path);

          final ModelBuilder modelBuilder = new ModelBuilder();
          final ColorConverter colorConverter = new ColorConverter();

          Function<Star, StarModel> starToStarModel =
                  new Function<Star, StarModel>() {
                          public StarModel apply(Star star) {
                                  float size = 0.5f;
                                  // Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
                                  //int kelvin = colorConverter.bMinusVToKelvin(star.getBvColor());
                                  //Color color = colorConverter.kelvinToColor(kelvin);

                                  Color color = colorConverter.bMinusVToColor(star.getBvColor());
                                  Material material = new Material(ColorAttribute.createDiffuse(color));

                                  final Model boxModel = modelBuilder.createBox(size, size, size,
                                                  material,
                                                  Usage.Position | Usage.Normal);

                                  return StarModel.newInstance(star, boxModel);
                          }
                  };

          List<StarModel> starModels = Lists.transform(stars, starToStarModel);
          this.starModels = starModels;
          System.out.println("Imported stars (not models): " + stars);
          System.out.println("Starmodels: " + starModels);
  }

	public void update (float delta) {

	}

	////////////////////////////////////////
	// Camera Orientation
	////////////////////////////////////////

  private LinkedList<Orientation> mOrientations = new LinkedList<Orientation>();
  private static final int MAX_ORIENTATIONS = 30;

  private float mAzimuth;
  private float mPitch  ;
  private float mRoll   ;

	public void rotateLeft(float delta, float distance) {
	  // TODO: see if we need to compute delta as well
		mPitch -= distance;
	}

	public void rotateRight(float delta, float distance) {
		mPitch += distance;
	}

  public CameraOrientation getCameraOrientation() {
          averageOrientationValues();
          return new CameraOrientation(
                          mAzimuth,
                          mPitch,
                          mRoll);
  }

	//public float getAzimuth() {
  //  return mAzimuth;
  //}
  //public float getPitch() {
  //  return mPitch;
  //}
  //public float getRoll() {
  //  return mRoll;
  //}

	private void averageOrientationValues() {
    float azimuth = 0.0f;
    float pitch   = 0.0f;
    float roll    = 0.0f;
    for (Orientation o : mOrientations) {
      azimuth += o.azimuth;
      pitch   += o.pitch;
      roll    += o.roll;
    }
    int count = mOrientations.size();
    // Average out the values.
    // We can loop around in yaw/azimuth.
    mAzimuth = (azimuth / count) % 360;
    mPitch   = pitch / count;
    mRoll    = roll / count;
  }

  /**
   * Add a given orientation to our queue.
   */
  public void addOrientation(float timeDelta, Orientation orientation) {

          if (SMOOTH_CAMERA) {
                  if (mOrientations.size() > MAX_ORIENTATIONS) {
                          mOrientations.remove();
                  }
          } else {
                  mOrientations.clear();
          }


    // Note that these are taken from StackOverflow:
    // http://stackoverflow.com/questions/5274514/how-do-i-use-the-android-compass-orientation-to-aim-an-opengl-camera

    mOrientations.offer(orientation);
    // TODO: Technically we don't care until we retrieve.
    // Average out our values

    // if (DEBUG) {
    //   Gdx.app.log(TAG, String.format("NEW Orientation: %s",
    //                                  orientation.toString()));
    //   Gdx.app.log(TAG, String.format("AVERAGE Orientation: (%f, %f, %f)",
    //                                  mRoll,
    //                                  mPitch,
    //                                  mAzimuth));
    // }
  }

	////////////////////////////////////////
	// Accelerometer
	////////////////////////////////////////

	/**
   * Take orientation data from the device.
   */
  public void updateOrientation(
                  float timeDelta,
                  float azimuth ,
                  float pitch   ,
                  float roll
      ) {
    // Adjust the raw values coming in.
    float adjustedRoll = -roll -90;
    float adjustedPitch = -pitch;

    // Adjust for the freaky coordinate system.
    float adjustedAzimuth = -azimuth - 180;
    float delta = mAzimuth - adjustedAzimuth;
    float invertedDelta = 360 - delta;

    final float INVERSE_TOLERANCE = 180;

    float massagedAzimuth;
    if (Math.abs(delta) > INVERSE_TOLERANCE) {
      // If we're way out of wack, let's just use the inversion.
      massagedAzimuth = mAzimuth + invertedDelta;
    } else {
      massagedAzimuth = adjustedAzimuth;
    }

    // Massaged values
    Orientation orientation = new Orientation(
        massagedAzimuth,
        adjustedRoll,
        adjustedPitch);
    addOrientation(timeDelta, orientation);
  }

	/*****************************************
	 * Other
	 ****************************************/

	@Override
	public void dispose () {
		explosionModel.dispose();
	}

	////////////////////////////////////////
	// Classes
	////////////////////////////////////////

  public class Orientation {
    //public final Vector3 position = new Vector3(0,1.5f,0);
    /** Angle left or right of the vertical */
    public float azimuth = 0.0f;
    /** Angle above or below the horizon */
    public float pitch = 0.0f;
    /** Angle about the direction as defined by yaw and pitch */
    public float roll = 0.0f;

    public Orientation(float azimuth, float pitch, float roll) {
      this.azimuth = azimuth;
      this.pitch   = pitch;
      this.roll    = roll;
    }
    public String toString() {
      return String.format("(%f, %f, %f)",
                    roll,
                    pitch,
                    azimuth);
    }
  }

}
