package com.badlogic.invaders.simulation;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;


public class StarModel extends ModelInstance {

	private final static Vector3 tmpV = new Vector3();
	private Star mStar;

  public static StarModel newInstance(Star star, Model model) {
          Vector3 position = new Vector3(star.getXPos(), star.getYPos(), star.getZPos());
          StarModel starModel = new StarModel(model, position);
          starModel.setStar(star);
          return starModel;
  }

	private StarModel (Model model, Vector3 position) {
		super(model, position);
		// this.position.set(position);
	}

  public void setStar(Star star) {
          mStar = star;
  }

	public void update (float delta) {
          // transform.trn(0, 0, -SHOT_VELOCITY * delta);
		// transform.getTranslation(tmpV);
		// if (tmpV.z > Simulation.PLAYFIELD_MAX_Z) hasLeftField = true;
		// if (tmpV.z < Simulation.PLAYFIELD_MIN_Z) hasLeftField = true;
	}


  public String toString() {
          return mStar.toString();
  }
}
