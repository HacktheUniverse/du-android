package com.badlogic.invaders.simulation;


public class Star {

				private float lumosity;
				private float bvColor;

				public Star (float lumosity, float bvColor) {
								this.lumosity = lumosity;
								this.bvColor = bvColor;
				}

				public float getLumosity() {
								return lumosity;
				}

				public float getBvColor() {
								return bvColor;
				}

}
