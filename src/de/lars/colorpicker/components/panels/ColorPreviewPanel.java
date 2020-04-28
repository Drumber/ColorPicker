package de.lars.colorpicker.components.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import de.lars.colorpicker.components.graphics.PlusIcon;
import de.lars.colorpicker.utils.ColorPickerStyle;

/**
 * Circle that shows the current selected color
 * @author Lars O.
 *
 */
public class ColorPreviewPanel extends JPanel {
	private static final long serialVersionUID = 5113388212746461289L;
	
	public static final int DEFAULT_SIZE = 30;
	
	private int padding;
	private Color color;
	private int width, height;
	private boolean isMouseOver, canAdd;
	
	/**
	 * Create a new preview panel with the specified initial color
	 * @param color shown color
	 */
	public ColorPreviewPanel(Color color) {
		this(color, DEFAULT_SIZE, 2);
	}
	
	/**
	 * Create a new preview panel with specified color, size and padding
	 * @param color shown color
	 * @param size circle size
	 * @param padding padding
	 */
	public ColorPreviewPanel(Color color, int size, int padding) {
		this.color = color;
		this.width = size;
		this.height = size;
		this.padding = padding;
		addMouseListener(mouseListener);
		setBackground(ColorPickerStyle.colorBackground);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color);
		
		int x = padding;
		int y = padding;
		int w = getWidth() - 2*padding;
		int h = getHeight() - 2*padding;
		
		// draw background circle
		g2d.fillOval(x, y, w, h);
		
		// draw foreground plus icon
		if(isMouseOver && canAdd) {
			// draw transparent background
			g2d.setColor(new Color(0, 0, 0, 50));
			g2d.fillOval(x, y, w, h);
			
			// draw icon
			new PlusIcon(Color.WHITE, w-14, h-14, 2).paintIcon(this, g2d, x+7, y+7);
		}
		g2d.dispose();
	}
	
	
	/**
	 * Triggered when mouse enters or exits the circle
	 */
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
	 * Get current shown color
	 * @return Color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set shown color
	 * @param color new color
	 */
	public void setColor(Color color) {
		this.color = color;
		repaint();
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
	
	/**
	 * Should show Plus-Icon
	 * @param canAdd can add new palette item
	 */
	public void setCanAdd(boolean canAdd) {
		this.canAdd = canAdd;
	}
	
	/**
	 * Is Plus-Icon shown
	 * @return boolean
	 */
	public boolean canAdd() {
		return canAdd;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
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
