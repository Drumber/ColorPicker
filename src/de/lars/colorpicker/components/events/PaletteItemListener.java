package de.lars.colorpicker.components.events;

import de.lars.colorpicker.components.graphics.ColorPaletteItem;

public interface PaletteItemListener {
	
	void onPaletteItemClicked(int index, ColorPaletteItem item);

}
