package de.lars.colorpicker.utils;

import java.awt.Color;

/**
 * Static colors used by all ColorPicker panels
 * @author Lars O.
 *
 */
public class ColorPickerStyle {
	
	/**
	 * Background color of all panels
	 */
	public static Color colorBackground = new Color(238, 238, 238);
	
	/**
	 * Background color of the color palette panel
	 */
	public static Color colorBackgroundPalatte = new Color(238, 238, 238);
	
	/**
	 * Color of the selector circle
	 */
	public static Color colorForegroundSelector = Color.WHITE;
	
	
	/**
	 * Util method to set both background colors
	 * @param color new background color
	 */
	public static void setBackgrounds(Color color) {
		colorBackground = color;
		colorBackgroundPalatte = color;
	}

}
