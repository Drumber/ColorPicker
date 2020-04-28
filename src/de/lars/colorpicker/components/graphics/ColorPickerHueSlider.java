package de.lars.colorpicker.components.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Color hue slider background of the ColorPicker
 * @author Lars O.
 *
 */
public class ColorPickerHueSlider {
	
	private int width, height;
	private BufferedImage bufferedImage;
	
	/**
	 * Creates a new hue slider with the specified width and height
	 * @param width the width of the slider
	 * @param height the height of the slider
	 */
	public ColorPickerHueSlider(int width, int height) {
		this.width = width;
		this.height = height;
		paintBufferedImage();
	}
	
	/**
	 * Paint the hue slider field with the size {@link #width} x {@link #height}
	 */
	private void paintBufferedImage() {
		if(width <= 0 || height <= 0)
			return;
		
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		float hStep = 1f / (width - 1);	// hue steps
		float h = 0f;	// hue
		float b = 1f;	// brightness
		float s = 1f;	// saturation
		
		for(int x = 0; x < width; x++) {
			Color color = Color.getHSBColor(h, s, b);
			
			for(int y = 0; y < height; y++) {
				bufferedImage.setRGB(x, y, color.getRGB());
			}
			
			// increase hue
			if((h += hStep) > 1f)
				h = 1f;
		}
	}
	
	/**
	 * Repaint hue slider background
	 */
	public void update() {
		paintBufferedImage();
	}
	
	/**
	 * Set new size and repaint the hue slider
	 * @param width new width
	 * @param height new height
	 */
	public void updateSize(int width, int height) {
		setWidth(width);
		setHeight(height);
		update();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

}
