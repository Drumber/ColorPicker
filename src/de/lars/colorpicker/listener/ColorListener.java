package de.lars.colorpicker.listener;

import java.awt.Color;

/**
 * Listener for receiving color change events
 */
public interface ColorListener {

	/**
	 * Invoked when a color changes
	 * @param color Event color
	 */
	void onColorChanged(Color color);
	
}
