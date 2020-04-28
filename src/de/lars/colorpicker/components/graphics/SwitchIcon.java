package de.lars.colorpicker.components.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 * Two triangles on top of each other. One points up, the other down.
 * 
 * @author Lars O.
 *
 */
public class SwitchIcon implements Icon {
	
	private Color color;
	private int width = 20, height = 20;
	
	public SwitchIcon() {
		this(Color.BLACK);
	}
	
	public SwitchIcon(Color color) {
		this.color = color;
	}
	
	public SwitchIcon(Color color, int size) {
		this.color = color;
		setIconSize(size);
	}
	
	public SwitchIcon(Color color, int width, int height) {
		this.color = color;
		this.width = width;
		this.height = height;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color);
		int w = getIconWidth();	// width
		int h = getIconHeight();// height
		int space = 1;
		g2d.fillPolygon(new Polygon(new int[] { x, x+w/2, x+w }, new int[] { y+h/2-space, y, y+h/2-space }, 3));
		g2d.fillPolygon(new Polygon(new int[] { x, x+w/2, x+w }, new int[] { y+h/2+space, y+h, y+h/2+space }, 3));
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setIconSize(int size) {
		this.width = size;
		this.height = size;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
