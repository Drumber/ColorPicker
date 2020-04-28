package de.lars.colorpicker.utils;

import java.awt.Color;

/**
 * Some color util stuff
 * @author Lars O.
 *
 */
public class ColorUitl {
	
	public static float getHueFromColor(Color c) {
		return Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0];
	}
	
	public static Color getColorFromHue(float hue) {
		return Color.getHSBColor(hue, 1.0f, 1.0f);
	}
	
	public static int[] convertToHSV(float[] hsb) {
		int[] hsv = new int[3];
		hsv[0] = Math.round(hsb[0] * 360f);	// hue in degrees
		hsv[1] = Math.round(hsb[1] * 100f);	// saturation in percent
		hsv[2] = Math.round(hsb[2] * 100f);	// brightness in percent
		return hsv;
	}
	
	public static float[] convertToHSB(int[] hsv) {
		float[] hsb = new float[3];
		hsb[0] = hsv[0] / 360f;
		hsb[1] = hsv[1] / 100f;
		hsb[2] = hsv[2] / 100f;
		return hsb;
	}

}
