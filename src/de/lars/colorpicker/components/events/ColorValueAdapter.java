package de.lars.colorpicker.components.events;

import java.awt.Color;

public abstract class ColorValueAdapter implements ColorValueChangeEvent {
	
	@Override
	public void onColorValueChanged(Color color) {}
	@Override
	public void onHueValueChanged(float hue) {}

}
