package script;


import java.awt.Robot;
import java.awt.event.InputEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import executer.Main;
import listener.KeyListener;
import listener.MouseListener;

public class AfkScript{

	Robot robot;
	
	public AfkScript() {
		robot = Main.robot;
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			//aSystem.err.println(ex.getMessage());

			System.exit(1);
		}

		// Construct the example object.
		MouseListener mouseListener = new MouseListener();

		// Add the appropriate listeners.
		GlobalScreen.addNativeMouseListener(mouseListener);
		GlobalScreen.addNativeMouseMotionListener(mouseListener);
		
		KeyListener keyListener = new KeyListener();
		GlobalScreen.addNativeKeyListener(keyListener);
		
		try {
				
			
			Thread.sleep(3000);
			

			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);


			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void move(MyPos pos) {
		robot.mouseMove(pos.x, pos.y); 
		//MyUtility.printLog("move: "+pos.x + ", " + pos.y);
	}
	
	private void click() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(400);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		//MyUtility.printLog("click");
		Thread.sleep(100);	
	}
	
	public static int getRandom(int min, int max) {
		return (int)(Math.random() * (max - min + 1) + min);
	}
	
	
	private class MyPos{
		int x;
		int y;
		int id;
		
		public MyPos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public MyPos(int x, int y, int id) {
			this.x = x;
			this.y = y;
			
			this.id = id;
		}
	}
	
}
