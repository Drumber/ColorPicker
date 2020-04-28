package de.lars.colorpicker.components.graphics;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import de.lars.colorpicker.components.panels.ColorPalettePanel;

/**
 * Colored rectangle for {@link ColorPalettePanel}
 * @author Lars O.
 *
 */
public class ColorPaletteItem extends JComponent {
	private static final long serialVersionUID = 3136721417951434978L;
	
	public static final int DEFAULT_SIZE = 30;
	public static final int DEFAULT_CORNER_RADIUS = 5;
	
	private Color color;
	private int width, height;
	private int cornerRadius;
	private boolean isMouseOver;
	
	/**
	 * Creates a new color palette item with the specified color
	 * @param color background color
	 */
	public ColorPaletteItem(Color color) {
		this(color, DEFAULT_SIZE);
	}
	
	/**
	 * Creates a new color palette item with the specified color and size (width & height)
	 * @param color background color
	 * @param size size of the rectangle (width & height)
	 */
	public ColorPaletteItem(Color color, int size) {
		this(color, size, size, DEFAULT_CORNER_RADIUS);
	}
	
	/**
	 * Creates a new color palette item
	 * @param color background color
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @param cornerRadius corner radius
	 */
	public ColorPaletteItem(Color color, int width, int height, int cornerRadius) {
		this.color = color;
		this.width = width;
		this.height = height;
		this.cornerRadius = cornerRadius;
		addMouseListener(mouseListener);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color);
		
		int w = width, h = height;
		int x = 0, y = 0;
		if(isMouseOver) {
			w -= 2;
			h -= 2;
			x += 1;
			y += 1;
		}
		g2d.fillRoundRect(x, y, w, h, cornerRadius, cornerRadius);
		g2d.dispose();
	}
	
	
	protected MouseAdapter mouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			isMouseOver = true;
			repaint();
		};
		
		public void mouseExited(MouseEvent e) {
			isMouseOver = false;
			repaint();
		};
	};
	
	
	/**
	 * Get the color of this palette item
	 * @return Color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the color for this palette item
	 * <p>Must be repainted manually</p>
	 * @param color new color
	 */
	public void setColor(Color color) {
		this.color = color;
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

	
	@Override
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(width, height);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(width, height);
	}

}
