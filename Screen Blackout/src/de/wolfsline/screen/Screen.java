package de.wolfsline.screen;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;

import javax.swing.JWindow;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Screen extends JWindow implements NativeKeyListener {

	private static final long serialVersionUID = -7931914819560167594L;

	private GraphicsDevice _gDevice;
	private boolean _isDefaultScreen = false;
	private int _screenNumber = -1;
	private boolean _isBlack = false;

	private boolean isPressedAlt = false;

	public Screen(GraphicsDevice gDevice, int screenNumber, boolean defaultScreen) {
		this._gDevice = gDevice;
		this._screenNumber = screenNumber;
		this._isDefaultScreen = defaultScreen;
		
		GlobalScreen.getInstance().addNativeKeyListener(this);

		getContentPane().setBackground(Color.black);

		Rectangle bounds = _gDevice.getDefaultConfiguration().getBounds();
		setLocation(bounds.getLocation());
		setSize(bounds.getSize());

		validate();

		setAlwaysOnTop(true);
	}

	/**
	 * 
	 */
	public void blackout() {
		setVisible(true);
		_isBlack = true;
	}

	/**
	 * 
	 */
	public void undone() {
		setVisible(false);
		_isBlack = false;
	}
	
	/**
	 * 
	 */
	public void toggle() {
		if (_isBlack) {
			undone();
		} else {
			blackout();
		}
	}

	/**
	 * 
	 * @return isBlack
	 */
	public boolean hasBlackout() {
		return this._isBlack;
	}

	/**
	 * 
	 * @return screenNumber
	 */
	public int getScreenNumber() {
		return this._screenNumber;
	}

	/**
	 * 
	 * @return isDefaultScreen
	 */
	public boolean isDefaultScreen() {
		return this._isDefaultScreen;
	}

	// ------ NativeKeyListener ------ //

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		String keyText = NativeKeyEvent.getKeyText(arg0.getKeyCode());
		if (keyText.equals("Alt")) {
			isPressedAlt = true;
		}
		if (isPressedAlt) {
			if (keyText.equals(String.valueOf(_screenNumber))) {
				toggle();
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		String keyText = NativeKeyEvent.getKeyText(arg0.getKeyCode());
		if (keyText.equals("Alt")) {
			isPressedAlt = false;
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
	}
}
