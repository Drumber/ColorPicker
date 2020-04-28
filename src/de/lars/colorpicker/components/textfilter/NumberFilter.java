package de.lars.colorpicker.components.textfilter;

import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Limits input of a JTextField to only numbers
 * @author Lars O.
 *
 */
public class NumberFilter extends DocumentFilter {
	
	protected int maxLength;
	protected ActionListener listener;
	protected boolean pauseListener;
	
	public NumberFilter() {
		maxLength = -1;
	}
	
	public NumberFilter(int maxLength) {
		this.maxLength = maxLength;
	}
	
	public NumberFilter(int maxLength, ActionListener listener) {
		this.maxLength = maxLength;
		this.listener = listener;
	}
	
	public void setActionListener(ActionListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
			throws BadLocationException {
		
		if(isNumber(string) && isMaxLength(fb.getDocument().getLength() + string.length())) {
			super.insertString(fb, offset, string, attr);
			fireEvent();
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {
		
		if((isNumber(text) && isMaxLength(fb.getDocument().getLength() + text.length())) || length != 0) {
			super.replace(fb, offset, length, text, attrs);
			if(length == 0) {
				fireEvent();
			}
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		super.remove(fb, offset, length);
		fireEvent();
	}
	
	
	private boolean isNumber(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isMaxLength(int length) {
		if(length <= maxLength || maxLength == -1) {
			return true;
		}
		return false;
	}
	
	public void setPauseListener(boolean pause) {
		pauseListener = pause;
	}
	
	protected void fireEvent() {
		if(listener != null && !pauseListener) {
			listener.actionPerformed(null);
		}
	}

}
