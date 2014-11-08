package com.badlogic.invaders.simulation;


public class CameraOrientation {

  private float mAzimuth;
  private float mPitch  ;
  private float mRoll   ;

	public CameraOrientation(float azimuth, float pitch  , float roll) {
					mAzimuth = azimuth;
					mPitch   = pitch;
					mRoll    = roll;
	}

	public float getAzimuth() {
    return mAzimuth;
  }
  public float getPitch() {
    return mPitch;
  }
  public float getRoll() {
    return mRoll;
  }

}
