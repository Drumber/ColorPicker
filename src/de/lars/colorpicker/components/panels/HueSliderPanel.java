package de.lars.colorpicker.components.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

import de.lars.colorpicker.components.graphics.ColorPickerHueSlider;
import de.lars.colorpicker.components.graphics.SelectorCircle;
import de.lars.colorpicker.utils.ColorPickerStyle;
import de.lars.colorpicker.utils.ColorUitl;

/**
 * Panel for {@link ColorPickerHueSlider}
 * @author Lars O.
 *
 */
public class HueSliderPanel extends ColorPickerPanelComponent {
	private static final long serialVersionUID = -8437177661568453866L;
	
	private ColorPickerHueSlider cpHueSlider;
	private SelectorCircle sc;
	private float hue;
	
	/**
	 * Create a new hue slider panel with initial color red
	 */
	public HueSliderPanel() {
		this(Color.RED);
	}
	
	/**
	 * Create a new hue slider panel with specified initial color
	 * @param color selected color
	 */
	public HueSliderPanel(Color color) {
		this.hue = ColorUitl.getHueFromColor(color);
		paddingTop = 4;
		paddingBottom = 4;
		update();
		cpHueSlider = new ColorPickerHueSlider(fieldWidth, fieldHeight);
		sc = new SelectorCircle(color, selMinX, selMinY, selMaxX, selMaxY);
		addMouseMotionListener(mouseDragAdapter);
		addMouseListener(mouseClickAdapter);
		setBackground(ColorPickerStyle.colorBackground);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int x = paddingLeft, y = paddingTop;
		
		g2d.setClip(new RoundRectangle2D.Float(x, y, fieldWidth, fieldHeight, cornerRadius, cornerRadius));
		
		if(cpHueSlider.getBufferedImage() != null) {
			// draw hue slider background
			g2d.drawImage(cpHueSlider.getBufferedImage(), x, y, this);
			
			// draw color selector
			g2d.setClip(null);
			Color selColor = getColorFromImage(cpHueSlider.getBufferedImage(), sc);
			sc.setColor(selColor);
			sc.draw(g2d, selX, selY);
			
			hue = ColorUitl.getHueFromColor(selColor);
		}
		g2d.dispose();
	}
	
	
	/**
	 * Triggered when the mouse is dragged within the hue slider field
	 */
	protected MouseMotionAdapter mouseDragAdapter = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			onMouseEvent(e);
		}
	};
	
	/**
	 * Triggered when the mouse is pressed within the hue slider field
	 */
	protected MouseListener mouseClickAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			onMouseEvent(e);
		};
	};
	
	/**
	 * Handles a mouse event from the hue slider field.
	 * Set the coordinates for the selector and trigger repaint.
	 * @param e the MouseEvent that was triggered
	 */
	protected void onMouseEvent(MouseEvent e) {
		// set coordinates of color selector and repaint
		selX = e.getX();
		repaint();
		// fire change events
		Color selColor = sc.getColor();
		if(cpHueSlider.getBufferedImage() != null)
			selColor = getColorFromImage(cpHueSlider.getBufferedImage(), sc); // try to get color from current image
		float hue = Color.RGBtoHSB(selColor.getRed(), selColor.getGreen(), selColor.getBlue(), null)[0];
		onHueValueChanged(hue);
		onColorValueChanged(selColor);
	}
	
	/**
	 * Replace current hue slider field
	 * @param hueSlider new {@link ColorPickerHueSlider}
	 */
	public void setColorPickerHueSlider(ColorPickerHueSlider hueSlider) {
		cpHueSlider = hueSlider;
	}
	
	/**
	 * Update the size and repaint
	 */
	public void updateSize() {
		update();
		selY = fieldHeight / 2 + paddingTop;
		sc.setMaxMin(selMaxX, selMinX, selMaxY, selMinY);
		cpHueSlider.updateSize(fieldWidth, fieldHeight);
	}
	
	/**
	 * Set selected hue value
	 * @param hue float 0..1
	 */
	public void setSelectedHue(float hue) {
		if(hue > 1f) hue = 1f;
		int relativePos = (int) (fieldWidth * hue);
		selX = relativePos + paddingLeft;
		repaint();
	}
	
	/**
	 * Get selected hue value
	 * @return float 0..1
	 */
	public float getHue() {
		return hue;
	}

}
