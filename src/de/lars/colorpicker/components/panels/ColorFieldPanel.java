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

import de.lars.colorpicker.components.graphics.ColorPickerField;
import de.lars.colorpicker.components.graphics.SelectorCircle;
import de.lars.colorpicker.utils.ColorPickerStyle;
import de.lars.colorpicker.utils.ColorUitl;

/**
 * JPanel for the {@link ColorPickerField} and {@link SelectorCircle}
 * @author Lars O.
 *
 */
public class ColorFieldPanel extends ColorPickerPanelComponent {
	private static final long serialVersionUID = -2578116870843350479L;
	
	private long lastUpdate;
	private Color color;
	private ColorPickerField cpField;
	private SelectorCircle sc;
	
	/**
	 * Create a new color field panel with the initial color red
	 */
	public ColorFieldPanel() {
		this(Color.RED);
	}
	
	/**
	 * Create a new color field panel with the specified color
	 * @param color selected color
	 */
	public ColorFieldPanel(Color color) {
		this.color = color;
		float hue = ColorUitl.getHueFromColor(color);
		update();
		cpField = new ColorPickerField(fieldWidth, fieldHeight, hue);
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

		if (cpField.getBufferedImage() != null) {
			// draw color field background
			g2d.drawImage(cpField.getBufferedImage(), x, y, this);
			
			// draw color selector
			g2d.setClip(null);
			Color selColor = getColorFromImage(cpField.getBufferedImage(), sc);
			sc.setColor(selColor);
			sc.draw(g2d, selX, selY);
			color = selColor;
		}
		g2d.dispose();
	}
	
	
	/**
	 * Triggered when the mouse is dragged within the color field
	 */
	protected MouseMotionAdapter mouseDragAdapter = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			onMouseEvent(e);
		}
	};
	
	/**
	 * Triggered when the mouse is pressed within the color field
	 */
	protected MouseListener mouseClickAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			onMouseEvent(e);
		};
	};
	
	/**
	 * Handles a mouse event from the color field.
	 * Set the coordinates for the selector and trigger repaint.
	 * @param e the MouseEvent that was triggered
	 */
	protected void onMouseEvent(MouseEvent e) {
		// set coordinates of color selector and repaint
		selX = e.getX() + paddingLeft;
		selY = e.getY() + paddingTop;
		repaint();
		Color selColor = sc.getColor();
		if(cpField.getBufferedImage() != null)
			selColor = getColorFromImage(cpField.getBufferedImage(), sc); // try to get color from current image
		// fire change event
		onColorValueChanged(selColor);
	}

	/**
	 * Replaces the current color field
	 * @param colorPickerField new color picker field
	 */
	public void setColorPickerField(ColorPickerField colorPickerField) {
		cpField = colorPickerField;
	}
	
	/**
	 * Get the color picker field
	 * @return {@link ColorPickerField}
	 */
	public ColorPickerField getColorPickerField() {
		return cpField;
	}
	
	/**
	 * Update the size of the underlying color field
	 */
	public void updateSize() {
		update();
		sc.setMaxMin(selMaxX, selMinX, selMaxY, selMinY);
		cpField.updateSize(fieldWidth, fieldHeight);
	}
	
	/**
	 * Repaint color field
	 */
	public void updateColorField() {
		if(System.currentTimeMillis() - lastUpdate > 50) {	// update only 20 times per second
			lastUpdate = System.currentTimeMillis();
			cpField.update();
			repaint();
		}
	}
	
	/**
	 * Set the selected color
	 * @param color new selected color
	 */
	public void setSelectedColor(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		if(hsb[1] > 1f) hsb[1] = 1f;
		if(hsb[2] > 1f) hsb[2] = 1f;
		int relativeSat = (int) (fieldWidth * hsb[1]);	// saturation pos -> x
		int relativeBri = fieldHeight - (int) (fieldHeight * hsb[2]);	// brightness pos -> y
		
		selX = relativeSat + paddingLeft;
		selY = relativeBri + paddingTop;
		repaint();
	}
	
	/**
	 * Get the selected color
	 * @return Color
	 */
	public Color getColor() {
		return color;
	}

}
