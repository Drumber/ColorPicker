package de.lars.colorpicker.components.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Color field of the ColorPicker
 * @author Lars O.
 *
 */
public class ColorPickerField {
	
	private int width, height;
	private BufferedImage bufferedImage;
	private float currentHue;
	
	/**
	 * Creates a new color field with the specified width and height and an initial hue of <code>0F</code> (red)
	 * @param width width of the field
	 * @param height height of the field
	 */
	public ColorPickerField(int width, int height) {
		this(width, height, 0f);
	}
	
	/**
	 * Creates a new color field
	 * @param width width of the field
	 * @param height height of the field
	 * @param hue initial hue value <code>(0.0F..1.0F)</code>
	 */
	public ColorPickerField(int width, int height, float hue) {
		this.width = width;		// = saturation
		this.height = height;	// = brightness
		currentHue = hue;
		paintBufferedImage();
	}
	
	/**
	 * Paint the color field with the size {@link #width} x {@link #height}
	 */
	private void paintBufferedImage() {
		if(width <= 0 || height <= 0)
			return;
		
		bufferedImage = null;
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		float bStep = 1f / (height - 1);	// brightness steps
		float sStep = 1f / (width - 1);		// saturation steps
		float b = 1f;	// brightness
		float s = 0f;	// saturation
		
		for(int y = 0; y < height; y++) {		//brightness
			
			for(int x = 0; x < width; x++) {	//saturation
				
				Color color = Color.getHSBColor(currentHue, s, b);
				bufferedImage.setRGB(x, y, color.getRGB());
				
				// increase saturation
				if((s += sStep) > 1f)
					s = 1f;
			}
			// decrease brightness
			if((b -= bStep) < 0)
				b = 0;
			// reset saturation
			s = 0f;
		}
	}
	
	/**
	 * Repaint color field
	 */
	public void update() {
		paintBufferedImage();
	}
	
	/**
	 * Set new size and repaint field
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

	/**
	 * Hue value of this field
	 * @return float 0..1
	 */
	public float getHue() {
		return currentHue;
	}

	/**
	 * Set new hue value for this field
	 * @param currentHue hue value (0..1)
	 */
	public void setHue(float currentHue) {
		this.currentHue = currentHue;
	}

}
