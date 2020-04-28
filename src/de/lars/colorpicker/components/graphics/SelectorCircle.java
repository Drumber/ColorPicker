package de.lars.colorpicker.components.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import de.lars.colorpicker.utils.ColorPickerStyle;

/**
 * Selector circle which can display the current selected color
 * @author Lars O.
 *
 */
public class SelectorCircle {
	
	public static final int WIDTH = 20, HEIGHT = 20;
	private int minX, maxX, minY, maxY;
	private Color color;
	
	/**
	 * Create a new SelectorCircle
	 * @param color shown color
	 * @param minX minimum x position
	 * @param minY minimum y position
	 * @param maxX maximum x position
	 * @param maxY maximum y position
	 */
	public SelectorCircle(Color color, int minX, int minY, int maxX, int maxY) {
		this.color = color;
		setMaxMin(maxX, minX, maxY, minY);
	}
	
	/**
	 * Draw the circle at the specified position
	 * @param g2d Graphics instance
	 * @param x x position
	 * @param y y position
	 */
	public void draw(Graphics2D g2d, int x, int y) {
		x = checkX(x);
		y = checkY(y);
		
		int cX = x - WIDTH / 2;		// center x position
		int cY = y - HEIGHT / 2;	// center y position
		
		// draw background
		g2d.setColor(ColorPickerStyle.colorForegroundSelector);
		g2d.fillOval(cX, cY, WIDTH, HEIGHT);
		// draw foreground
		g2d.setColor(color);
		int padding = 6;	// inner padding
		g2d.fillOval(cX + padding/2, cY + padding/2, WIDTH - padding, HEIGHT - padding);
	}
	
	
	/**
	 * Check if the value is within the range <code>minX -  maxX</code>
	 * @param x x value
	 * @return x if <code>minX > x < maxX</code> or {@link #minX} / {@link #maxX}
	 */
	public int checkX(int x) {
		if(x < minX) x = minX;
		if(x > maxX) x = maxX;
		return x;
	}
	
	/**
	 * Check if the value is within the range <code>minY -  maxY</code>
	 * @param y y value
	 * @return y if <code>minY > y < maxY</code> or {@link #minY} / {@link #maxY}
	 */
	public int checkY(int y) {
		if(y < minY) y = minY;
		if(y > maxY) y = maxY;
		return y;
	}
	
	public void setMaxMin(int maxX, int minX, int maxY, int minY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	/**
	 * Get set color
	 * @return Color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the color for this selector
	 * @param color new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

}
