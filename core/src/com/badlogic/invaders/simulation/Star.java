package com.badlogic.invaders.simulation;


public class Star {

				private float lumosity;
				private float bvColor;

        private float xPos;
        private float yPos;
        private float zPos;

				public Star (float xPos, float yPos, float zPos, float lumosity, float bvColor) {
								this.xPos = xPos;
								this.yPos = yPos;
								this.zPos = zPos;

								this.lumosity = lumosity;
								this.bvColor = bvColor;
				}

				public float getXPos() {
								return xPos;
				}

        public float getYPos() {
								return yPos;
				}

				public float getZPos() {
								return zPos;
				}

				public float getLumosity() {
								return lumosity;
				}

				public float getBvColor() {
								return bvColor;
				}

}
