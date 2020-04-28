package de.lars.colorpicker.components.panels;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import de.lars.colorpicker.components.ColorValueMode;
import de.lars.colorpicker.components.events.ColorValueChangeEvent;
import de.lars.colorpicker.components.graphics.SwitchIcon;
import de.lars.colorpicker.components.textfilter.LengthFilter;
import de.lars.colorpicker.components.textfilter.NumberFilter;
import de.lars.colorpicker.utils.ColorPickerStyle;
import de.lars.colorpicker.utils.ColorUitl;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Panel that shows RGB, HEX and HSV values of the current selected color
 * @author Lars O.
 *
 */
public class ColorValuesPanel extends JPanel {
	private static final long serialVersionUID = -52773940591839018L;
	
	protected ColorValueMode mode = ColorValueMode.RGB;
	private Color color; // current color
	private float hue;
	private ColorValueChangeEvent listener;
	protected JButton btnModeSwitch;
	protected JTextField field1, field2, field3;
	protected JLabel label1, label2, label3;
	protected GridBagConstraints gbc_label1;
	
	/**
	 * Create a new color values panel with the specified initial color
	 * @param color
	 */
	public ColorValuesPanel(Color color) {
		this.color = color;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0};
		gridBagLayout.columnWidths = new int[]{100, 100, 100, 50};
		setLayout(gridBagLayout);
		setBackground(ColorPickerStyle.colorBackground);
		
		label1 = new JLabel("lbl1");
		gbc_label1 = new GridBagConstraints();
		gbc_label1.insets = new Insets(0, 0, 5, 5);
		gbc_label1.gridy = 0;
		gbc_label1.gridx = 0;
		add(label1, gbc_label1);
		
		label2 = new JLabel("lbl2");
		GridBagConstraints gbc_label2 = new GridBagConstraints();
		gbc_label2.insets = new Insets(0, 0, 5, 5);
		gbc_label2.gridx = 1;
		gbc_label2.gridy = 0;
		add(label2, gbc_label2);
		
		label3 = new JLabel("lbl3");
		GridBagConstraints gbc_label3 = new GridBagConstraints();
		gbc_label3.insets = new Insets(0, 0, 5, 5);
		gbc_label3.gridx = 2;
		gbc_label3.gridy = 0;
		add(label3, gbc_label3);
		
		field1 = new JTextField();
		field1.addFocusListener(fieldFocusLostListener);
		addField(0, 1, field1);
		
		field2 = new JTextField();
		field2.addFocusListener(fieldFocusLostListener);
		addField(1, 1, field2);
		
		field3 = new JTextField();
		field3.addFocusListener(fieldFocusLostListener);
		addField(2, 1, field3);
		
		btnModeSwitch = new JButton(new SwitchIcon(Color.BLACK, 10, 15));
		btnModeSwitch.setToolTipText("Switch Mode");
		btnModeSwitch.setBorderPainted(false);
		btnModeSwitch.setFocusPainted(false);
		btnModeSwitch.setBackground(new Color(220, 220, 220));
		btnModeSwitch.setAlignmentX(RIGHT_ALIGNMENT);
		btnModeSwitch.addActionListener(modeSwitchListener);
		GridBagConstraints gbc_btnModeSwitch = new GridBagConstraints();
		gbc_btnModeSwitch.insets = new Insets(0, 0, 5, 0);
		gbc_btnModeSwitch.gridx = 3;
		gbc_btnModeSwitch.gridy = 1;
		gbc_btnModeSwitch.anchor = GridBagConstraints.LINE_END;
		gbc_btnModeSwitch.fill = GridBagConstraints.BOTH;
		add(btnModeSwitch, gbc_btnModeSwitch);
		
		switchMode(mode);
	}
	
	
	/**
	 * Triggered when the mode button was clicked
	 */
	private ActionListener modeSwitchListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			circleMode();
		}
	};
	
	
	/**
	 * Set the value change listener
	 * @param listener {@link ColorValueChangeEvent}
	 */
	public void setValueChangeListener(ColorValueChangeEvent listener) {
		this.listener = listener;
	}
	
	/**
	 * Set current color
	 * @param color new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Get current color
	 * @return Color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Get current hue value
	 * @return float 0..1
	 */
	public float getHue() {
		return hue;
	}

	/**
	 * Set current hue value
	 * @param hue float 0..1
	 */
	public void setHue(float hue) {
		this.hue = hue;
	}
	
	/**
	 * Update current displayed value fields
	 */
	public void updateValues() {
		pauseFieldListener(true); // pause listener of TextField to prevent triggering
		if(mode == ColorValueMode.RGB)
			modeRGBUpdateValues();
		else if(mode == ColorValueMode.HEX)
			modeHexUpdateValues();
		else
			modeHSVUpdateValues();
		pauseFieldListener(false);
	}
	
	/**
	 * Pause all field input change listener. Used when field input is set programmatically
	 * @param pause pause?
	 */
	private void pauseFieldListener(boolean pause) {
		JTextField[] fields = {field1, field2, field3};
		for(JTextField field : fields) {
			if(((PlainDocument) field.getDocument()).getDocumentFilter() instanceof NumberFilter) {
				((NumberFilter) ((PlainDocument) field.getDocument()).getDocumentFilter()).setPauseListener(pause);
			}
			if(field.getDocument() instanceof LengthFilter) {
				((LengthFilter) field.getDocument()).setPauseListener(pause);
			}
		}
	}
	
	/**
	 * Set color and hue value and update fields
	 * @param color new color
	 * @param hue new hue value 0..1
	 */
	public void updateValues(Color color, float hue) {
		setColor(color);
		setHue(hue);
		updateValues();
	}

	/**
	 * Switch to the specified mode
	 * @param mode value mode (RGB, HEX, HSV)
	 */
	public void switchMode(ColorValueMode mode) {
		this.mode = mode;
		if(mode == ColorValueMode.RGB)
			modeRGB();
		else if(mode == ColorValueMode.HEX)
			modeHex();
		else
			modeHSV();
		// update values
		updateValues();
		revalidate();
		updateUI();
	}
	
	/**
	 * Circle between the value modes
	 */
	public void circleMode() {
		if(mode == ColorValueMode.RGB)
			mode = ColorValueMode.HEX;
		else if(mode == ColorValueMode.HEX)
			mode = ColorValueMode.HSV;
		else
			mode = ColorValueMode.RGB;
		switchMode(mode);
	}
	
	
	protected void modeRGB() {
		removeFields();
		addField(0, 1, field1);
		addField(1, 1, field2);
		addField(2, 1, field3);
		label1.setText("R");
		label2.setText("G");
		label3.setText("B");
	}
	
	protected void modeRGBUpdateValues() {
		field1.setText(String.valueOf(color.getRed()));
		field2.setText(String.valueOf(color.getGreen()));
		field3.setText(String.valueOf(color.getBlue()));
	}
	
	protected void modeHSV() {
		removeFields();
		addField(0, 1, field1);
		addField(1, 1, field2);
		addField(2, 1, field3);
		label1.setText("H");
		label2.setText("S");
		label3.setText("V");
	}
	
	protected void modeHSVUpdateValues() {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		hsb[0] = hue;
		int[] hsv = ColorUitl.convertToHSV(hsb);
		
		field1.setText(String.valueOf(hsv[0]));
		field2.setText(String.valueOf(hsv[1]));
		field3.setText(String.valueOf(hsv[2]));
	}
	
	protected void modeHex() {
		removeFields();
		addField(0, 3, field1);
		label1.setText("");
		label2.setText("HEX");
		label3.setText("");
	}
	
	protected void modeHexUpdateValues() {
		field1.setText(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
	}
	
	
	/**
	 * Add field to the {@link GridBagLayout}
	 * @param index grid x (column)
	 * @param width grid width ({@link GridBagConstraints#gridx})
	 * @param field field to add
	 */
	protected void addField(int index, int width, JTextField field) {
		field.setHorizontalAlignment(JTextField.CENTER);
		
		((PlainDocument) field.getDocument()).setDocumentFilter(null);
		field.setDocument(new PlainDocument());
		if(mode == ColorValueMode.RGB || mode == ColorValueMode.HSV) {
			((PlainDocument) field.getDocument()).setDocumentFilter(new NumberFilter(3, fieldInputListener));
		} else if(mode == ColorValueMode.HEX) {
			field.setDocument(new LengthFilter(7, fieldInputListener));
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 10);
		gbc.gridy = 1;
		gbc.gridx = index;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = width;
		add(field, gbc);
	}
	
	/**
	 * Remove all fields from the panel
	 */
	private void removeFields() {
		remove(field1);
		remove(field2);
		remove(field3);
		for(JTextField field : new JTextField[] {field1, field2, field3}) {
			((PlainDocument) field.getDocument()).setDocumentFilter(null);
			field.setDocument(new PlainDocument());
		}
	}
	
	
	/**
	 * Triggered on text input
	 */
	protected ActionListener fieldInputListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Color c = color;
			try {
				
				if(mode == ColorValueMode.RGB) {
					int r = Integer.parseInt(field1.getText());
					int g = Integer.parseInt(field2.getText());
					int b = Integer.parseInt(field3.getText());
					c = new Color(r, g, b);
					hue = ColorUitl.getHueFromColor(c);
				} else if(mode == ColorValueMode.HEX) {
					c = Color.decode(field1.getText());
					hue = ColorUitl.getHueFromColor(c);
				} else if(mode == ColorValueMode.HSV) {
					int[] hsv = new int[3];
					hsv[0] = Integer.parseInt(field1.getText());
					hsv[1] = Integer.parseInt(field2.getText());
					hsv[2] = Integer.parseInt(field3.getText());
					float[] hsb = ColorUitl.convertToHSB(hsv);
					c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
					hue = hsb[0];
				}
				
				color = c;
				fireChangeEvent();
				
			} catch(IllegalArgumentException ex) {
				//ex.printStackTrace();
			}
		}
	};
	
	/**
	 * Trigger listener
	 */
	protected void fireChangeEvent() {
		if(listener != null) {
			listener.onColorValueChanged(color);
			listener.onHueValueChanged(hue);
		}
	}
	
	
	/**
	 * Triggered when the cursor leaves the field
	 */
	protected FocusAdapter fieldFocusLostListener = new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			if(e.getComponent() instanceof JTextField) {
				JTextField field = (JTextField) e.getComponent();
				if(field.getText().isEmpty()) {
					if(mode == ColorValueMode.RGB || mode == ColorValueMode.HSV) {
						field.setText("0");
					}
				}
			}
		};
	};

}
