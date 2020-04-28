package de.lars.colorpicker.components.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class PlusIcon implements Icon {

	private Color color;
	private int width = 20, height = 20;
	private int thickness = 4;
	
	public PlusIcon() {
		this(Color.BLACK);
	}
	
	public PlusIcon(Color color) {
		this.color = color;
	}
	
	public PlusIcon(Color color, int size) {
		this.color = color;
		setIconSize(size);
	}
	
	public PlusIcon(Color color, int width, int height) {
		this.color = color;
		this.width = width;
		this.height = height;
	}
	
	public PlusIcon(Color color, int width, int height, int thickness) {
		this.color = color;
		this.width = width;
		this.height = height;
		this.thickness = thickness;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color);
		int w = getIconWidth();	// width
		int h = getIconHeight();// height
		int thick = thickness;	// thickness
		
		int space = (w - thick) / 2;
		
		g2d.fillRect(x, y+space, w, thick);	// -
		g2d.fillRect(x+space, y, thick, h);	// |  -> +
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
