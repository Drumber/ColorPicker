package de.lars.colorpicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import de.lars.colorpicker.components.events.ColorValueAdapter;
import de.lars.colorpicker.components.events.ColorValueChangeEvent;
import de.lars.colorpicker.components.events.PaletteItemListener;
import de.lars.colorpicker.components.graphics.ColorPaletteItem;
import de.lars.colorpicker.components.panels.*;
import de.lars.colorpicker.listener.ColorListener;
import de.lars.colorpicker.utils.ColorPickerStyle;
import de.lars.colorpicker.utils.ColorUitl;

/**
 * Simple and modern ColorPicker
 * 
 * @author Lars O.
 *
 */
public class ColorPicker extends JComponent {
	private static final long serialVersionUID = 5683870507919526815L;
	
	/**
	 * <p>Static color array for {@link ColorPalettePanel}</p>
	 * <p>Used when {@link #sharedPaletteColors} <code>true</code></p>
	 */
	public static Color[] paletteColors;
	
	/**
	 * {@link HueSliderPanel} height
	 */
	private static final int HUE_SLIDER_HEIGHT = 25;
	
	private ColorFieldPanel panelField;
	private HueSliderPanel panelHueSlider;
	private ColorPreviewPanel panelPreview;
	private ColorValuesPanel panelValues;
	private ColorPalettePanel panelColorPalette;
	
	private Color selColor;
	private List<ColorListener> listColorListener;
	
	private int prevHeight, prevWidth;
	private boolean previewPanelEnabled, colorValuesEnabled, colorPaletteEnabled;
	private boolean sharedPaletteColors;
	
	/**
	 * Creates a ColorPicker pane with an initial color of red
	 */
	public ColorPicker() {
		this(Color.RED);
	}
	
	/**
	 * Creates a ColorPicker pane with the specified initial color
	 * @param initialColor selected color
	 */
	public ColorPicker(Color initialColor) {
		this(initialColor, 30, true, true, true, true);
	}
	
	/**
	 * Creates a ColorPicker pane with the specified parameters
	 * @param initialColor selected color
	 * @param maxPaletteColors maximum number of color palette items
	 * @param previewPanel enable or disable {@link ColorPreviewPanel}
	 * @param colorValues enable or disable {@linkplain ColorValuesPanel}
	 * @param colorPalette enable or disable {@link ColorPalettePanel}
	 * @param sharedPaletteColors enable or disable shared color palette ({@link #paletteColors})
	 */
	public ColorPicker(Color initialColor, int maxPaletteColors, boolean previewPanel, boolean colorValues, boolean colorPalette, boolean sharedPaletteColors) {
		this.previewPanelEnabled = previewPanel;
		this.colorValuesEnabled = colorValues;
		this.colorPaletteEnabled = colorPalette;
		this.sharedPaletteColors = sharedPaletteColors;
		this.selColor = initialColor;
		this.listColorListener = new ArrayList<>();
		setBackground(ColorPickerStyle.colorBackground);
		
		panelField = new ColorFieldPanel(initialColor);
		panelField.setCornerRadius(10);
		panelField.setPadding(8);
		
		panelHueSlider = new HueSliderPanel(initialColor);
		panelHueSlider.setPreferredSize(new Dimension(Integer.MAX_VALUE, HUE_SLIDER_HEIGHT));
		panelHueSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, HUE_SLIDER_HEIGHT));
		panelHueSlider.setCornerRadius(5);
		panelHueSlider.setPadding(8, 12, 8, 12);
		
		panelValues = new ColorValuesPanel(initialColor);
		panelValues.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panelValues.setBorder(new EmptyBorder(0, 10, 0, 10));
		
		panelPreview = new ColorPreviewPanel(initialColor);
		panelPreview.setCanAdd(true);
		
		panelColorPalette = new ColorPalettePanel();
		if(paletteColors != null && paletteColors.length > 0 && sharedPaletteColors) {
			panelColorPalette.addColor(paletteColors);
		}
		
		addPanels();
		
		// add listeners
		this.addComponentListener(onResize);
		panelField.addColorValueChangeListener(onColorChanged);
		panelHueSlider.addColorValueChangeListener(onHueChanged);
		panelValues.setValueChangeListener(onColorValueChanged);
		panelPreview.addMouseListener(onPreviewPanelClicked);
		panelColorPalette.addPaletteItemListener(onPaletteItemClicked);
	}
	
	
	/**
	 * Add all panels to the pane
	 */
	private void addPanels() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(panelField);
		
		if(previewPanelEnabled) {
			JPanel hPanel = new JPanel(new BorderLayout());
			hPanel.setBackground(ColorPickerStyle.colorBackground);
			hPanel.setBorder(new EmptyBorder(0, 8, 0, 0));
			hPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
			hPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
			hPanel.add(panelPreview, BorderLayout.WEST);
			hPanel.add(panelHueSlider, BorderLayout.CENTER);
			this.add(hPanel);
		} else {
			panelHueSlider.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
			panelHueSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
			this.add(panelHueSlider);
		}
		
		if(colorValuesEnabled)
			this.add(panelValues);
		
		if(colorPaletteEnabled) {
			JScrollPane scrollPalette = new JScrollPane(panelColorPalette);
			scrollPalette.setPreferredSize(new Dimension(Integer.MAX_VALUE, 60));
			scrollPalette.setMinimumSize(new Dimension(Integer.MAX_VALUE, 60));
			scrollPalette.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
			scrollPalette.setViewportBorder(null);
			scrollPalette.setBorder(BorderFactory.createEmptyBorder());
			scrollPalette.getVerticalScrollBar().setUnitIncrement(16);
			this.add(scrollPalette);
		}
	}
	
	
	/**
	 * Triggered when window is resized
	 */
	private ComponentAdapter onResize = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			if(prevHeight != getHeight() || prevWidth != getWidth()) {
				prevHeight = getHeight();
				prevWidth = getWidth();
				Color prevColor = panelField.getColor();
				float prevHue = panelHueSlider.getHue();
				updateSize();
				// set color & hue to previous color & hue
				panelField.setSelectedColor(prevColor);
				panelHueSlider.setSelectedHue(prevHue);
			}
		}
	};
	
	/**
	 * Triggered when selector in color field was moved
	 */
	private ColorValueAdapter onColorChanged = new ColorValueAdapter() {
		public void onColorValueChanged(Color color) {
			colorUpdated(color);
		};
	};
	
	/**
	 * Triggered when selector in hue slider was moved
	 */
	private ColorValueAdapter onHueChanged = new ColorValueAdapter() {
		public void onHueValueChanged(float hue) {
			panelField.getColorPickerField().setHue(hue);
			panelField.updateColorField();
			panelValues.setHue(hue);
			colorUpdated(panelField.getColor());
		};
	};
	
	/**
	 * Update color of other panels and fire color change event
	 * @param color Updated color
	 */
	private void colorUpdated(Color color) {
		panelValues.setColor(color);
		panelPreview.setColor(color);
		selColor = color;
		fireColorEvent(color);
		updateColorValuePanel();
	}
	
	
	/**
	 * Triggered when value in color values panel was changed/edited
	 */
	private ColorValueChangeEvent onColorValueChanged = new ColorValueChangeEvent() {
		@Override
		public void onHueValueChanged(float hue) {
			panelField.getColorPickerField().setHue(hue);
			panelField.updateColorField();
			panelHueSlider.setSelectedHue(hue);
		}
		
		@Override
		public void onColorValueChanged(Color color) {
			panelField.setSelectedColor(color);
			panelPreview.setColor(color);
			selColor = color;
			fireColorEvent(color);
		}
	};
	
	
	/**
	 * Triggered when color preview panel was clicked
	 */
	private MouseAdapter onPreviewPanelClicked = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if(panelPreview.canAdd()) {
				panelColorPalette.addColor(panelPreview.getColor());
				if(sharedPaletteColors) {
					ColorPicker.paletteColors = panelColorPalette.getPaletteColors();
				}
			}
		};
	};
	
	
	/**
	 * Triggered when color palette item was clicked
	 */
	private PaletteItemListener onPaletteItemClicked = new PaletteItemListener() {
		@Override
		public void onPaletteItemClicked(int index, ColorPaletteItem item) {
			setSelectedColor(item.getColor());
		}
	};
	
	
	/**
	 * Trigger {@link ColorListener}
	 * @param color Event color
	 */
	private void fireColorEvent(Color color) {
		for(ColorListener listener : listColorListener) {
			if(listener != null) {
				listener.onColorChanged(color);
			}
		}
	}
	
	
	/**
	 * Set the selected color and hue
	 * @param color new selected color
	 * @param hue hue value of the color (used for {@link HueSliderPanel})
	 */
	public void setSelectedColor(Color color, float hue) {
		panelField.getColorPickerField().setHue(hue);
		panelField.updateColorField();
		panelHueSlider.setSelectedHue(hue);
		panelField.setSelectedColor(color);
		panelValues.setColor(color);
		panelValues.setHue(hue);
		selColor = color;
		fireColorEvent(color);
		updateColorValuePanel();
	}
	
	/**
	 * Set the selected color
	 * @param color new selected color
	 */
	public void setSelectedColor(Color color) {
		setSelectedColor(color, ColorUitl.getHueFromColor(color));
	}
	
	/**
	 * Get the selected color
	 * @return selected color
	 */
	public Color getColor() {
		return selColor;
	}
	
	
	/**
	 * Trigger repaint of color field and hue slider
	 */
	public void updateSize() {
		panelField.updateSize();
		panelHueSlider.updateSize();
	}
	
	/**
	 * Update values of {@link ColorValuesPanel}
	 */
	public void updateColorValuePanel() {
		panelValues.updateValues();
	}
	
	
	/**
	 * Enable or disable specified panels
	 * @param previewPanel circle next to the hue slider that shows the selected color and
	 * allows to add a new color to the palette
	 * @param colorValues displays RGB, HEX or HSV of the selected color
	 * @param colorPalette color palette panel
	 */
	public void setPanelsEnabled(boolean previewPanel, boolean colorValues, boolean colorPalette) {
		this.previewPanelEnabled = previewPanel;
		this.colorValuesEnabled = colorValues;
		this.colorPaletteEnabled = colorPalette;
		this.removeAll();
		addPanels();
		revalidate();
		updateUI();
	}
	
	
	/**
	 * Add color change listener
	 * @param listener {@link ColorListener}
	 */
	public void addColorListener(ColorListener listener) {
		listColorListener.add(listener);
	}
	
	
	/**
	 * Get the {@link ColorPalettePanel} of this ColorPicker
	 * @return ColorPalettePanel
	 */
	public ColorPalettePanel getColorPalettePanel() {
		return panelColorPalette;
	}
	
	/**
	 * Set maximum amount of color palette items
	 * @param maxPaletteItems maximum platte items
	 */
	public void setMaxPaletteItems(int maxPaletteItems) {
		panelColorPalette.setMaxPaletteItems(maxPaletteItems);
	}
	
	
	/*==========
	 * DIALOG
	 *==========*/
	
	/**
	 * Shows a ColorPicker dialog. Blocks the thread that started the dialog.
	 * @param title title of the dialog
	 * @param initialColor selected color
	 * @return selected color or <code>null</code> if <code>Cancel</code> was clicked
	 */
	public static Color showDialog(String title, Color initialColor) {
		return showDialog(title, "Ok", "Cancel", new Dimension(500, 350), initialColor);
	}
	
	/**
	 * Shows a ColorPicker dialog. Blocks the thread that started the dialog.
	 * @param title title of the dialog
	 * @param textOk text shown for the OK button
	 * @param textCancel text shown for the Cancel button
	 * @param size preferred size of the dialog
	 * @param initialColor selected color
	 * @return selected color or <code>null</code> if <code>Cancel</code> was clicked
	 */
	public static Color showDialog(String title, String textOk, String textCancel, Dimension size, Color initialColor) {
		final ColorPicker cp = new ColorPicker(initialColor != null ? initialColor : Color.RED);
		
		JDialog dialog = createDialog(cp, title, textOk, textCancel, size, initialColor);
		dialog.pack();
		
		dialog.show();
		dialog.dispose();
		
		return cp.getColor();
	}
	
	/**
	 * Shows a ColorPicker dialog without color palette. Blocks the thread that started the dialog.
	 * @param title title of the dialog
	 * @param initialColor selected color
	 * @param showValuesPanel should the {@link ColorValuesPanel} be shown
	 * @return selected color or <code>null</code> if <code>Cancel</code> was clicked
	 */
	public static Color showSimpleDialog(String title, Color initialColor, boolean showValuesPanel) {
		final ColorPicker cp = new ColorPicker(initialColor != null ? initialColor : Color.RED, -1, true, showValuesPanel, false, false);
		
		JDialog dialog = createDialog(cp, title, "Ok", "Cancel", new Dimension(500, 350), initialColor);
		dialog.pack();
		
		dialog.show();
		dialog.dispose();
		
		return cp.getColor();
	}
	
	
	/**
	 * Creates a new {@link JDialog} with a ColorPicker pane
	 * @param cp {@link ColorPicker} instance
	 * @param title title of the dialog
	 * @param textOk text shown for the OK button
	 * @param textCancel text shown for the Cancel button
	 * @param size preferred size of the dialog
	 * @param initialColor selected color
	 * @return JDialog
	 */
	public static JDialog createDialog(ColorPicker cp, String title, String textOk, String textCancel, Dimension size, Color initialColor) {
		Frame window = JOptionPane.getRootFrame();
		JDialog dialog = new JDialog(window, title, true);
		dialog.setPreferredSize(size);
		dialog.getAccessibleContext().setAccessibleDescription(title);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBackground(ColorPickerStyle.colorBackground);
		
		JButton btnOk = new JButton("Ok");
		panelButtons.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.hide();
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		panelButtons.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cp.selColor = null;
				dialog.hide();
			}
		});
		
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setBackground(ColorPickerStyle.colorBackground);
		
		rootPanel.add(cp, BorderLayout.CENTER);
		rootPanel.add(panelButtons, BorderLayout.SOUTH);
		
		dialog.setContentPane(rootPanel);
		return dialog;
	}
	
}
