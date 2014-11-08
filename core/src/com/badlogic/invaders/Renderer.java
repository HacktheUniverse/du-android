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

package com.badlogic.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.invaders.simulation.Ship;
import com.badlogic.invaders.simulation.Simulation;

/** The renderer receives a simulation and renders it.
 * @author mzechner */
public class Renderer {
	/** sprite batch to draw text **/
	private SpriteBatch spriteBatch;
	/** the background texture **/
	private Texture backgroundTexture;
	/** the font **/
	private BitmapFont font;
	/** the rotation angle of all invaders around y **/
	private float invaderAngle = 0;
	/** status string **/
	private String status = "";
	/** keeping track of the last score so we don't constantly construct a new string **/
	private int lastScore = 0;
	private int lastLives = 0;
	private int lastWave = 0;

	/** view and transform matrix for text rendering and transforming 3D objects **/
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 transform = new Matrix4();
	private final Matrix4 normal = new Matrix4();
	private final Matrix3 normal3 = new Matrix3();

	/** perspective camera **/
	private PerspectiveCamera camera;

	/** the directional light **/
	Environment lights;

	ModelBatch modelBatch;

	final Vector3 tmpV = new Vector3();

	public Renderer () {
		try {
			lights = new Environment();
			lights.add(new DirectionalLight().set(Color.WHITE, new Vector3(-1, -0.5f, 0).nor()));

			spriteBatch = new SpriteBatch();
			modelBatch = new ModelBatch();

			backgroundTexture = new Texture(Gdx.files.internal("data/planet.jpg"), Format.RGB565, true);
			backgroundTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);

			font = new BitmapFont(Gdx.files.internal("data/font10.fnt"), Gdx.files.internal("data/font10.png"), false);

			camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void render (Simulation simulation, float delta) {
		// We explicitly require GL10, otherwise we could've used the GLCommon
		// interface via Gdx.gl
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		// renderBackground();
		gl.glEnable(GL20.GL_DEPTH_TEST);
		gl.glEnable(GL20.GL_CULL_FACE);

		// setProjectionAndCamera(simulation.ship);
    setProjectionAndCameraAugmentedReality(simulation);

		modelBatch.begin(camera);
		modelBatch.render(simulation.explosions);
		// if (!simulation.ship.isExploding) modelBatch.render(simulation.ship, lights);
		modelBatch.render(simulation.invaders, lights);
		modelBatch.render(simulation.blocks);
		modelBatch.render(simulation.shots);
		modelBatch.render(simulation.starModels);
		modelBatch.end();

		gl.glDisable(GL20.GL_CULL_FACE);
		gl.glDisable(GL20.GL_DEPTH_TEST);

		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.begin();
		if (simulation.ship.lives != lastLives || simulation.score != lastScore || simulation.wave != lastWave) {
			status = "lives: " + simulation.ship.lives + " wave: " + simulation.wave + " score: " + simulation.score;
			lastLives = simulation.ship.lives;
			lastScore = simulation.score;
			lastWave = simulation.wave;
		}
		spriteBatch.enableBlending();
		font.draw(spriteBatch, status, 0, 320);
		spriteBatch.end();

		invaderAngle += delta * 90;
		if (invaderAngle > 360) invaderAngle -= 360;
	}

	private void renderBackground () {
		viewMatrix.setToOrtho2D(0, 0, 400, 320);
		spriteBatch.setProjectionMatrix(viewMatrix);
		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(backgroundTexture, 0, 0, 480, 320, 0, 0, 512, 512, false, false);
		spriteBatch.end();
	}

	final Vector3 dir = new Vector3();

	////////////////////////////////////////
	// Camera control
	////////////////////////////////////////

	private void setProjectionAndCameraAugmentedReality(Simulation simulation) {

    //camera.position.set(0, 6, 2);
    //camera.direction.set(0, 0, -4).sub(camera.position).nor();

    //camera.rotate(simulation.getPitch()   , 1 , 0 , 0);
    //camera.rotate(simulation.getRoll()    , 0 , 1 , 0);
    //camera.rotate(simulation.getAzimuth() , 0 , 0 , 1);

    // Note that these are taken from StackOverflow:
    // http://stackoverflow.com/questions/5274514/how-do-i-use-the-android-compass-orientation-to-aim-an-opengl-camera
    camera.position.set(0, 1, 2);
    camera.direction.set(0, 0, 1);
    camera.up.set(0, 1, 0);

    camera.rotate(simulation.getAzimuth() , 0 , 1 , 0);
    Vector3 pivot = camera.direction.cpy().crs(camera.up);

    camera.rotate(simulation.getPitch(), pivot.x, pivot.y, pivot.z);
    camera.rotate(simulation.getRoll(), camera.direction.x, camera.direction.y, camera.direction.z);

    camera.update();
    //camera.apply(Gdx.gl10);
  }

	private void setProjectionAndCamera (Ship ship) {
		ship.transform.getTranslation(tmpV);
		camera.position.set(tmpV.x, 6, 2);
		camera.direction.set(tmpV.x, 0, -4).sub(camera.position).nor();
		camera.update();
	}

	////////////////////////////////////////
	// Cleanup
	////////////////////////////////////////

	public void dispose () {
		spriteBatch.dispose();
		backgroundTexture.dispose();
		font.dispose();
	}
}
