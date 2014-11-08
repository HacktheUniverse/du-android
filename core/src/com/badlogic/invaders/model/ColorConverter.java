package com.badlogic.invaders.model;

import com.badlogic.gdx.graphics.Color;
import java.util.Random;

public class ColorConverter {

				private static final float STAR_ALPHA = 1.0f;

				private Random mRandom = new Random();

         public Color bMinusVToColor(float bMinusV) {

								 if (bMinusV >= +1.61) {
												 return Color.valueOf("ffbd6f");
								 } else if (bMinusV >= +1.41) {
												 return Color.valueOf("ffcf95");
								 } else if (bMinusV >= +1.15) {
												 return Color.valueOf("ffddb4");
								 } else if (bMinusV >= +0.82) {
												 return Color.valueOf("ffead5");
								 } else if (bMinusV >= +0.66) {
												 return Color.valueOf("fff0e3");
								 } else if (bMinusV >= +0.59) {
												 return Color.valueOf("fff3ea");
								 } else if (bMinusV >= +0.43) {
												 return Color.valueOf("fff8f8");
								 } else if (bMinusV >= +0.31) {
												 return Color.valueOf("f7f5ff");
								 } else if (bMinusV >= +0.14) {
												 return Color.valueOf("e2e7ff");
								 } else if (bMinusV >= 0.00) {
												 return Color.valueOf("d1dbff");
								 } else if (bMinusV >= -0.16) {
												 return Color.valueOf("bbccff");
								 //} else if (bMinusV >= -0.30) {
								 } else {
												 return Color.valueOf("a7bcff");
								 }
				 }

				public int bMinusVToKelvin(float bMinusV) {
								if (bMinusV > 1.40) {
												return 3840;
								} else if (bMinusV > 0.81) {
												return 5150;
								} else if (bMinusV > 0.58) {
												return 5940;
								} else if (bMinusV > 0.30) {
												return 7300;
								} else if (bMinusV > -0.02) {
												return 9790;
								} else if (bMinusV > -0.30) {
												return 30000;
								} else {
												return 42000;
								}
								// O5V 	–0.33 	–1.19 	–0.15 	–0.32 	42,000
								// B0V 	–0.30 	–1.08 	–0.13 	–0.29 	30,000
								// A0V 	–0.02 	–0.02 	0.02 	–0.02 	9,790
								// F0V 	0.30 	0.03 	0.30 	0.17 	7,300
								// G0V 	0.58 	0.06 	0.50 	0.31 	5,940
								// K0V 	0.81 	0.45 	0.64 	0.42 	5,150
								// M0V 	1.40 	1.22 	1.28 	0.91 	3,840
				}

				// public Color kelvinToRgb(int kelvin) {
				// }

				/**
				 * Returns the mapped range from lower bound to higher bound.
				 * Calculation result 60 = map(6, 0, 10, 0, 100);
				 * @param curValue The known value between minCurValue and maxCurvalue
				 * @param minCurValue The minimum value of the lower bound range range
				 * @param maxCurValue The maximum value of the lower bound range
				 * @param minValue The minimum value of the higher bound range
				 * @param maxValue The maximum value of the higher bound range
				 * @return The calculated value between minValue and maxValue
				 */
				public double map(double curValue, double minCurValue, double maxCurValue, double minValue, double maxValue){
								return (curValue-minCurValue)/(maxCurValue-minCurValue) * (maxValue-minValue) + minValue;
				}

				/**
				 * Return the kelvins from a given temperature
				 * @param temp
				 * @return The mapped kelvin value (may not be as exact as the real kelvins scale)
				 */
				// public long getKelvinFromTemp(String temp){
				// 				double iTemp = Double.parseDouble(temp);
				// 				long kelvin = 0;
				// 				if(iTemp<=15){
				// 								kelvin = 2700;
				// 				} else if(iTemp>=15 && iTemp <= 27){
				// 								kelvin = (int)MathImpl.map(iTemp, 15, 27, 2700, 27000);
				// 				} else {
				// 								kelvin = 27000;
				// 				}
				// 				return kelvin;
				// }

				/**
				 * Calculate th HSB based on Kelvin degrees (reasonable OK between the 1000 and 40000).
				 * @param kelvin Kelvin value
				 * @param colorType
				 * @return Array[0]=H, Array[1]=S and Array[2]=B when colorType hsb otherwise rgb
				 */
				public Color kelvinToColor(int kelvin){
								double tmpCalc;
								double r;
								double g;
								double b;
								float[] hsbvals = null;

								if(kelvin>40000) kelvin = 40000;
								if(kelvin<1000) kelvin = 1000;

								kelvin = kelvin / 100;

								/// red
								if(kelvin<= 66){
												r = 255;
								} else {
												tmpCalc = kelvin - 60;
												tmpCalc = 329.698727446 * Math.pow(tmpCalc,-0.1332047592);
												r = tmpCalc;
												if(r<0) r = 0;
												if(r>255) r = 255;
								}
								/// green
								if(kelvin <= 66){
												tmpCalc = kelvin;
												tmpCalc = 99.4708025861 * Math.log(tmpCalc) - 161.1195681661;
												g = tmpCalc;
												if(g<0) g = 0;
												if(g>255) g = 255;
								} else {
												tmpCalc = kelvin - 60;
												tmpCalc = 288.1221695283 * Math.pow(tmpCalc,-0.0755148492);
												g = tmpCalc;
												if(g<0) g = 0;
												if(g>255) g = 255;
								}
								/// blue
								if(kelvin >= 66){
												b = 255;
								} else if(kelvin <= 19){
												b = 0;
								} else {
												tmpCalc = kelvin - 10;
												tmpCalc = 138.5177312231 * Math.log(tmpCalc) - 305.0447927307;
												b = tmpCalc;
												if(b < 0) b = 0;
												if(b > 255) b = 255;
								}

								//return new Color(
																//(float) r,
																//(float) g,
																//(float) b,
																//STAR_ALPHA);
								return new Color(
																(float) mRandom.nextFloat(),
																(float) mRandom.nextFloat(),
																(float) mRandom.nextFloat(),
																STAR_ALPHA);
								//Color(float r, float g, float b, float a)
				}
}
