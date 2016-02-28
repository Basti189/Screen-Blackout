package de.wolfsline.screen;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jnativehook.GlobalScreen;


public class ScreenBlackout {
	
	private static TrayIcon trayIcon;
	private static SystemTray systemTray;
	
	private static List<Screen> listScreens = new ArrayList<Screen>();
	
	public static void main(String args[]) throws Exception {
		GlobalScreen.registerNativeHook();
		GraphicsDevice gda[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		int number = 0;
		for (GraphicsDevice gDevice : gda) {
			if (gDevice == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()) {
				listScreens.add(new Screen(gDevice, ++number, true));
			} else {
				listScreens.add(new Screen(gDevice, ++number, false));
			}
			
		}
		setupTray();
	}
	
	private static void setupTray() throws AWTException {
		if (!SystemTray.isSupported()) {
			return;
		}
		systemTray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage(ScreenBlackout.class.getResource("/de/wolfsline/screen/icon.png"));
		if (image == null) {
			return;
		}
		
		PopupMenu trayPopupMenu = new PopupMenu();
		for (Screen screen : listScreens) {
			MenuItem menuScreen;
			if (screen.isDefaultScreen()) {
				menuScreen = new MenuItem("Monitor " + screen.getScreenNumber() + " (Hauptmonitor)");
				menuScreen.setEnabled(false);
			} else {
				menuScreen = new MenuItem("Monitor " + screen.getScreenNumber());
			}
			menuScreen.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		        	screen.toggle();
		        }
		    });
			trayPopupMenu.add(menuScreen);
		}
		MenuItem menuExit = new MenuItem("Beenden");
		menuExit.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            exit();          
	        }
	    });     
	    trayPopupMenu.add(menuExit);
	    trayIcon = new TrayIcon(image, "Screen Blackout", trayPopupMenu);
	    trayIcon.setImageAutoSize(true);
	    systemTray.add(trayIcon);
	    trayIcon.displayMessage("Screen Blackout", "Gestartet...", TrayIcon.MessageType.INFO);
	}
	
	/**
	 * 
	 */
	private static void exit() {
		for (Screen Screen : listScreens) {
			Screen.undone();
		}
		GlobalScreen.unregisterNativeHook();
		System.exit(0);
	}
}
