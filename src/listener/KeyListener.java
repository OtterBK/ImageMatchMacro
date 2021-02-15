package listener;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import executer.Main;
import script.ItemTranferScript;
import util.MyUtility;

public class KeyListener implements NativeKeyListener {
	boolean isCtrl = false;
	boolean isDoing = false;
	boolean isMacroing = false;
	
	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		// System.out.println("Key Pressed: " +
		// NativeKeyEvent.getKeyText(e.getKeyCode()));

		// MyUtility.printLog(NativeKeyEvent.getKeyText(e.getKeyCode()));
		if (e.getKeyCode() == NativeKeyEvent.VC_CAPS_LOCK) {

			Main.script.stopFlag = true;
			// if(isCtrl) {
			// System.exit(1);

		} 
		
	
	}
	
	public void nativeKeyReleased(NativeKeyEvent e) {
		System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
			isCtrl = false;
		}
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
	}

}