package de.lars.colorpicker.components.panels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.lars.colorpicker.components.events.ColorValueChangeEvent;

/**
 * Super class of {@link ColorFieldPanel} and {@link HueSliderPanel}
 * @author Lars O.
 *
 */
public class ColorPickerPanelComponent extends JPanel {
	private static final long serialVersionUID = -9165281910898239760L;
	
	protected List<ColorValueChangeEvent> colorChangeListener = new ArrayList<>();
	protected int cornerRadius;
	protected int fieldWidth, fieldHeight;
	protected int paddingLeft, paddingRight, paddingTop, paddingBottom;
	protected int selMinX, selMaxX, selMinY, selMaxY;	// values used for SelectorCircle
	protected int selX, selY;

	/**
	 * Calculate new size of the color field and new position of selector
	 */
	protected void update() {
		calcFieldSize();
		calcSelectorValues();
	}
	
	protected void calcFieldSize() {
		fieldWidth = getWidth() - paddingLeft - paddingRight;
		fieldHeight = getHeight() - paddingTop - paddingBottom;
	}
	
	protected void calcSelectorValues() {
		selMinX = paddingLeft;
		selMaxX = getWidth() - paddingRight - 1;
		selMinY = paddingTop;
		selMaxY = getHeight() - paddingBottom - 1;
	}
	
	/**
	 * Add color change listener
	 * @param listener {@link ColorValueChangeEvent}
	 */
	public void addColorValueChangeListener(ColorValueChangeEvent listener) {
		colorChangeListener.add(listener);
	}
	
	/**
	 * Fires a color changed event
	 * @param color changed color
	 */
	protected void onColorValueChanged(Color color) {
		for(ColorValueChangeEvent listener : colorChangeListener) {
			if(listener != null) {
				listener.onColorValueChanged(color);
			}
		}
	}
	
	/**
	 * Fires a hue changed event
	 * @param hue changed hue
	 */
	protected void onHueValueChanged(float hue) {
		for(ColorValueChangeEvent listener : colorChangeListener) {
			if(listener != null) {
				listener.onHueValueChanged(hue);;
			}
		}
	}

	public int getCornerRadius() {
		return cornerRadius;
	}

	public void setCornerRadius(int cornerRadius) {
		this.cornerRadius = cornerRadius;
	}

	public int getFieldWidth() {
		return fieldWidth;
	}

	public void setFieldWidth(int fieldWidth) {
		this.fieldWidth = fieldWidth;
	}

	public int getFieldHeight() {
		return fieldHeight;
	}

	public void setFieldHeight(int fieldHeight) {
		this.fieldHeight = fieldHeight;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}
	
	public void setPadding(int left, int top, int right, int bottom) {
		paddingLeft = left;
		paddingRight = right;
		paddingBottom = bottom;
		paddingTop = top;
	}
	
	public void setPadding(int padding) {
		paddingLeft = padding;
		paddingRight = padding;
		paddingBottom = padding;
		paddingTop = padding;
	}
	
}
