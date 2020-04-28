package de.lars.colorpicker.components.panels;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.lars.colorpicker.components.events.PaletteItemListener;
import de.lars.colorpicker.components.graphics.ColorPaletteItem;
import de.lars.colorpicker.utils.ColorPickerStyle;
import de.lars.colorpicker.utils.WrapLayout;

/**
 * Panel that can show multiple color rectangles
 * @author Lars O.
 *
 */
public class ColorPalettePanel extends JPanel {
	private static final long serialVersionUID = 4384165202058823533L;
	
	/** Behavior when adding a new color palette item if the maximum number of items has been reached */
	public enum ItemAddBehavior {
		/** No new item can be added */
		NONE,
		/** Replace first item when full  */
		REPLACE_FIRST,
		/** Replace last item when full */
		REPLACE_LAST
	}
	
	protected List<PaletteItemListener> listItemListener;
	protected List<ColorPaletteItem> listPaletteItems;
	protected int maxPaletteItems;
	protected ItemAddBehavior addBehavior = ItemAddBehavior.REPLACE_FIRST;
	protected int itemSize;
	
	/**
	 * Create a new color palette with unlimited amount of palette items
	 */
	public ColorPalettePanel() {
		this(-1, ColorPaletteItem.DEFAULT_SIZE);
	}
	
	/**
	 * Create a new color palette with specified maximum amount of palette items and item size
	 * @param maxItems Maximum number of palette items
	 * @param itemSize Size of the palette items
	 */
	public ColorPalettePanel(int maxItems, int itemSize) {
		this(maxItems, itemSize, null);
	}
	
	/**
	 * Create a new color palette
	 * @param maxItems Maximum number of palette items
	 * @param itemSize Size of the palette items
	 * @param listColors Predefined palette items
	 */
	public ColorPalettePanel(int maxItems, int itemSize, List<Color> listColors) {
		listItemListener = new ArrayList<>();
		listPaletteItems = new ArrayList<>();
		this.maxPaletteItems = maxItems;
		this.itemSize = itemSize;
		
		setLayout(new WrapLayout(FlowLayout.LEFT));
		setBackground(ColorPickerStyle.colorBackgroundPalatte);
		
		if(listColors != null) {
			addColor(listColors.toArray(new Color[listColors.size()]));
		}
		addPaletteItemsToPanel();
	}
	
	
	/**
	 * Add palette items to the panel
	 */
	protected void addPaletteItemsToPanel() {
		this.removeAll();
		for(ColorPaletteItem item : listPaletteItems) {
			add(item);
		}
	}
	
	
	/**
	 * Add new color(s) to the color palette
	 * @param color
	 */
	public void addColor(Color... color) {
		for(Color c : color) {
			ColorPaletteItem item = new ColorPaletteItem(c, itemSize);
			addColorPaletteItem(item);
		}
	}
	
	
	/**
	 * Add a new palette item instance to the color palette
	 * @param item palette item
	 */
	public void addColorPaletteItem(ColorPaletteItem item) {
		// click listener
		item.addMouseListener(paletteItemClickListener);
		
		if(listPaletteItems.size() < maxPaletteItems || maxPaletteItems <= -1) {
			listPaletteItems.add(item);
		} else {
			switch(addBehavior) {
				case REPLACE_FIRST:
					rotatePaletteItemList(true);
					listPaletteItems.set(0, item);
					break;
				case REPLACE_LAST:
					rotatePaletteItemList(false);
					listPaletteItems.set(listPaletteItems.size()-1, item);
					break;
				case NONE:
					return;
			}
		}
		addPaletteItemsToPanel();
		this.revalidate();
		this.updateUI();
	}
	
	/**
	 * Shift items in the {@link #listPaletteItems} in the specified direction
	 * @param rightDirection <code>true</code> = right; <code>false</code> = left direction
	 */
	protected void rotatePaletteItemList(boolean rightDirection) {
		if(rightDirection) {
			listPaletteItems.add(0, null);
			listPaletteItems.remove(listPaletteItems.size()-1);
		} else {
			for(int i = 0; i < listPaletteItems.size()-1; i++) {
				listPaletteItems.set(i, listPaletteItems.get(i+1));
			}
		}
	}
	
	
	/**
	 * Add a palette item click listener
	 * @param listener {@link PaletteItemListener}
	 */
	public void addPaletteItemListener(PaletteItemListener listener) {
		listItemListener.add(listener);
	}
	
	
	/**
	 * Triggered when a palette item was clicked
	 */
	protected MouseAdapter paletteItemClickListener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if(e.getComponent() instanceof ColorPaletteItem) {
				ColorPaletteItem item = (ColorPaletteItem) e.getComponent();
				fireItemClickEvent(item);
			}
		};
	};
	
	
	/**
	 * Trigger all listeners
	 * @param item event palette item
	 */
	protected void fireItemClickEvent(ColorPaletteItem item) {
		// get index of item
		int index = listPaletteItems.indexOf(item);
		for(PaletteItemListener l : listItemListener) {
			if(l != null) {
				l.onPaletteItemClicked(index, item);
			}
		}
	}

	
	/**
	 * Get all colors of this color palette
	 * @return Color array
	 */
	public Color[] getPaletteColors() {
		Color[] colors = new Color[listPaletteItems.size()];
		for(int i = 0; i < colors.length; i++) {
			colors[i] = listPaletteItems.get(i).getColor();
		}
		return colors;
	}
	
	/**
	 * Get all color palette items
	 * @return List
	 */
	public List<ColorPaletteItem> getListPaletteItems() {
		return listPaletteItems;
	}

	/**
	 * Replace current palette item list
	 * @param listPaletteItems new color palette item list
	 */
	public void setListPaletteItems(List<ColorPaletteItem> listPaletteItems) {
		this.listPaletteItems = listPaletteItems;
	}

	/**
	 * Get the specified maximum amount of palette items
	 * @return maximum amount
	 */
	public int getMaxPaletteItems() {
		return maxPaletteItems;
	}

	/**
	 * Set the maximum amount of palette items
	 * @param maxPaletteItems maximum amount
	 */
	public void setMaxPaletteItems(int maxPaletteItems) {
		this.maxPaletteItems = maxPaletteItems;
	}

	public ItemAddBehavior getAddBehavior() {
		return addBehavior;
	}

	public void setAddBehavior(ItemAddBehavior addBehavior) {
		this.addBehavior = addBehavior;
	}

	public int getItemSize() {
		return itemSize;
	}

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}

}
