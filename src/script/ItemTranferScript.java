package script;


import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import executer.Main;
import gui.CheckGUI;
import listener.KeyListener;
import listener.KeyListener_ItemTransfer;
import listener.MouseListener;
import util.ImagePos;
import util.MyPos;
import util.MyUtility;

public class ItemTranferScript extends Script{
	
	public ItemTranferScript(int similar) {
		robot = Main.robot;
		this.similarGap = similar;
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			//aSystem.err.println(ex.getMessage());

			System.exit(1);
		}

		// Construct the example object.
		mouseListener = new MouseListener();

		// Add the appropriate listeners.
		GlobalScreen.addNativeMouseListener(mouseListener);
		GlobalScreen.addNativeMouseMotionListener(mouseListener);
		keyListener = new KeyListener_ItemTransfer();
		GlobalScreen.addNativeKeyListener(keyListener);
		
		
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					int checkCnt = 0;
					Thread.sleep(3000);
					
					
					PointerInfo pt = MouseInfo.getPointerInfo();
					center.x = (int)pt.getLocation().getX();
					center.y = (int)pt.getLocation().getY();
					
					
					
					MyPos guessCenter = new MyPos(Main.minecraftRect.width / 2 + Main.minecraftRect.x, 
							Main.minecraftRect.height/2 + Main.minecraftRect.y); //중심 좌표 추측
					if(Math.abs(guessCenter.x - center.x) > 100 || Math.abs(guessCenter.y - center.y) > 100) {
						new CheckGUI(null, "잘못된 중심좌표 감지 , 매크로를 재실행하세요.", false, false);
						stopScript();
						return;
					}
					
					
					MyUtility.printLog("운송 매크로 실행중...");	
					
					while(true) {
						Thread.sleep(1000);
						if (stopFlag) {
							stopScript();
							return;
						} else {
							checkCnt++;
							if(checkCnt == 10) {
								MyUtility.printLog("운송 매크로 실행중...");	
							}
						}
					}
					
											
				}catch(Exception e) {
					MyUtility.printLog(e.getMessage());
				}
			}
		};
		
		Main.threadPool.submit(thread);
		
		
	}
}
