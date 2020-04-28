package de.lars.colorpicker.components.events;

import java.awt.Color;

public interface ColorValueChangeEvent {
	
	void onColorValueChanged(Color color);
	void onHueValueChanged(float hue);

}
