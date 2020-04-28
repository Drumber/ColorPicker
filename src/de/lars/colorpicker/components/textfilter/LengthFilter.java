package de.lars.colorpicker.components.textfilter;

import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Limits the text length of a JTextField
 * @author Lars O.
 *
 */
public class LengthFilter extends PlainDocument {
	private static final long serialVersionUID = -6882250450106680661L;
	
	protected ActionListener listener;
	protected int maxLength;
	protected boolean pauseListener = false;
	
	public LengthFilter(int maxLength) {
		this.maxLength = maxLength;
	}
	
	public LengthFilter(int maxLength, ActionListener listener) {
		this.maxLength = maxLength;
		this.listener = listener;
	}
	
	public void setActionListener(ActionListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if(str == null || isMaxLength(getLength() + str.length())) {
			super.insertString(offs, str, a);
			fireEvent();
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		fireEvent();
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
