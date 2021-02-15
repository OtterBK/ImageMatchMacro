package listener;


import java.awt.Button;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import executer.Main;
import script.ItemTranferScript;
import script.Script;
import util.ImagePos;
import util.MyUtility;

public class KeyListener_ItemTransfer extends KeyListener implements NativeKeyListener {
	int sellCnt = 0;
	Script caller;
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		// System.out.println("Key Pressed: " +
		// NativeKeyEvent.getKeyText(e.getKeyCode()));

		// MyUtility.printLog(NativeKeyEvent.getKeyText(e.getKeyCode()));
		if (e.getKeyCode() == NativeKeyEvent.VC_CAPS_LOCK) {

			Main.script.stopFlag = true;
			// if(isCtrl) {
			// System.exit(1);

		} else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
			isCtrl = true;
		} else if (e.getKeyCode() == NativeKeyEvent.VC_F6) {

			if (isCtrl) {
				if (Main.script != null && Main.script instanceof ItemTranferScript && !isMacroing) {

					try {
						isMacroing = true;
						autoFishSell();
						Thread.sleep(2000);
						isMacroing = false;
						isCtrl = false;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		} else if (e.getKeyCode() == NativeKeyEvent.VC_F3) {

			if (isCtrl) {
				if (Main.script != null && Main.script instanceof ItemTranferScript && !isMacroing) {

					try {
						isMacroing = true;
						MyUtility.lineMacro(2);
						Thread.sleep(2000);
						isMacroing = false;
						isCtrl = false;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		} else if (e.getKeyCode() == NativeKeyEvent.VC_F4) {

			if (isCtrl) {
				if (Main.script != null && Main.script instanceof ItemTranferScript && !isMacroing) {

					try {
						isMacroing = true;
						MyUtility.invToChest();
						Thread.sleep(2000);
						isMacroing = false;
						isCtrl = false;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		} else if (e.getKeyCode() == NativeKeyEvent.VC_F5) {
			if (isCtrl) {
				if (Main.script != null && Main.script instanceof ItemTranferScript && !isMacroing) {
					// MyUtility.printLog("DID");
					try {
						isMacroing = true;
						MyUtility.chestToInv();

						Thread.sleep(2000);
						isMacroing = false;
						isCtrl = false;
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		} else if (e.getKeyCode() == NativeKeyEvent.VC_F7) {
			if (isCtrl) {
				if (Main.script != null && Main.script instanceof ItemTranferScript && !isMacroing) {
					// MyUtility.printLog("DID");
					try {
						Main.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						Main.robot.keyPress(KeyEvent.VK_E);
						Thread.sleep(10);
						Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						Main.robot.keyRelease(KeyEvent.VK_E);
					}catch(Exception ex) {
						
					}
					
				}
			}
		}
		
	
	}
	
	public boolean checkStop() {
		if(caller != Main.script) return false; //��ũ�� ������ ��ũ��Ʈ�� ���� ��ũ��Ʈ�� �ٸ��� false
		return Main.script.stopFlag;
	}
	
	   
    public void autoFishSell() throws InterruptedException {
    	caller = Main.script;
    	
    	MyUtility.printLog("����� �ڵ� �Ǹ� ����");
    	sellCnt = 0;
    	
    	while(true) {
    		int moveCnt = sellCnt/2; //�̵��� Ƚ��, 2�� �ȶ����� ���� ���ڷ� �̵��Ұ���
    		for(int i = 0; i < moveCnt; i++) { //moveCnt ��ŭ �̵�
    			Main.robot.keyPress(KeyEvent.VK_W); // ������ 0.37��
        		Thread.sleep(370);
        		Main.robot.keyRelease(KeyEvent.VK_W);
        		Thread.sleep(100);
    		}
    		
    		if (checkStop())return;
    		
        	Main.robot.keyPress(KeyEvent.VK_1); //1�� ����
        	Thread.sleep(10);
        	Main.robot.keyRelease(KeyEvent.VK_1); 
        	Thread.sleep(100);	
        	if (checkStop())return;
    		
    		Main.robot.keyRelease(KeyEvent.VK_SHIFT);
    		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    		Thread.sleep(100);
    		Main.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); // ���� �����
    		Thread.sleep(20);
    		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    		Thread.sleep(1000);
    		if (checkStop())return;
    		
    		ImagePos bigChestPos = Script.searchImgUsingThread(Main.macroImgFolder + "\\bigChest.png", Main.script.similarGap);
    		if(bigChestPos == null) {
    			MyUtility.printLog("���� ���� ����");
    			return;
    		}
    		if (checkStop())return;
    		if(sellCnt % 2 == 0) { //1��°�ٷ�
    			Main.robot.mouseMove(bigChestPos.x + Main.minecraftRect.x - 12, bigChestPos.y + 30 + Main.minecraftRect.y); //����0��ĭ���� ���콺 �̵�	
    		} else { //4��° �ٷ�
    			Main.robot.mouseMove(bigChestPos.x + Main.minecraftRect.x - 12, bigChestPos.y + 30 + 108 + Main.minecraftRect.y); //����0��ĭ���� ���콺 �̵�	
    		}
    		
    		Thread.sleep(150);
        	
        	MyUtility.lineMacro(3); //���ڿ��� �κ����� 3�� �Űܴ��
        	Thread.sleep(500);
        	if(checkStop()) return;
        	
        	Main.robot.keyPress(KeyEvent.VK_E); //�κ��ݱ�
        	Thread.sleep(10);
        	Main.robot.keyRelease(KeyEvent.VK_E); //�κ��ݱ�
        	Thread.sleep(100);	
        	
        	if(checkStop()) return;
        	Main.robot.keyPress(KeyEvent.VK_SHIFT); //�޴�����
        	Thread.sleep(150);
        	Main.robot.keyPress(KeyEvent.VK_F); //
        	Thread.sleep(10);
        	Main.robot.keyRelease(KeyEvent.VK_SHIFT); 
        	Main.robot.keyRelease(KeyEvent.VK_F); //
        	if(checkStop()) return;
        	
        	Thread.sleep(1000);
        	ImagePos fishRod = Script.searchImgUsingThread(Main.macroImgFolder+"\\fishRod.png", Main.script.similarGap);
        	if(checkStop()) return;
        	if(fishRod == null) {
        		MyUtility.printLog("�޴� ã�� ����,"); 
        		return;
        	}
    		Main.robot.mouseMove(fishRod.x + Main.minecraftRect.x, fishRod.y + Main.minecraftRect.y); // ���ô� Ŭ��
    		Thread.sleep(100);
    		Main.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    		Thread.sleep(20);
    		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    		if (checkStop())return;

    		Thread.sleep(3000); // �����ͷ� �̵��ϴµ� �ɸ��½ð�
    		if (checkStop())return;

    		Main.robot.keyPress(KeyEvent.VK_W); // ������ 0.25��
    		Thread.sleep(250);
    		Main.robot.keyRelease(KeyEvent.VK_W);
    		Thread.sleep(100);
    		if (checkStop())return;

    		Main.robot.keyPress(KeyEvent.VK_A); // �������� 3.410��
    		Thread.sleep(3410);
    		Main.robot.keyRelease(KeyEvent.VK_A);
    		Thread.sleep(100);
    		if (checkStop())return;

    		Main.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); // ���콺 ��Ŭ��
    		Thread.sleep(20);
    		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    		Thread.sleep(1000);
    		if (checkStop())return;

    		ImagePos shopPos = Script.searchImgUsingThread(Main.macroImgFolder + "\\fishShopTop.png",
    				Main.script.similarGap);
    		if (checkStop())return;
    		if (shopPos == null) {
    			MyUtility.printLog("���� ���� ����");return;
    		}
    		Main.robot.mouseMove(Main.script.center.x - 144, Main.script.center.y + 80); // �κ� 1��° ĭ���� ���콺
    		Thread.sleep(100);
    		if (checkStop())return;

    		MyUtility.lineMacro(3); //���ڿ��� �κ����� 3�� �Űܴ��
    		Thread.sleep(200);
    		if (checkStop())return;

    		Main.robot.mouseMove(Main.script.center.x, Main.script.center.y + 18); // �ȱ�� �̵�
    		Thread.sleep(100);
    		if (checkStop()) return;

    		Main.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // �ȱ�
    		Thread.sleep(20);
    		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    		Thread.sleep(100);
    		sellCnt++;
    		MyUtility.printLog(sellCnt+" �� ����");
    		if (checkStop()) return;

    		Main.robot.keyPress(KeyEvent.VK_E); // �����ݱ�
    		Thread.sleep(10);
    		Main.robot.keyRelease(KeyEvent.VK_E); // �����ݱ�
    		Thread.sleep(100);
    		if (checkStop()) return;

    		Main.robot.keyPress(KeyEvent.VK_SHIFT); // �޴�����
    		Thread.sleep(150);
    		Main.robot.keyPress(KeyEvent.VK_F); //
    		Thread.sleep(10);
    		Main.robot.keyRelease(KeyEvent.VK_SHIFT);
    		Main.robot.keyRelease(KeyEvent.VK_F);
    		if (checkStop()) return;

    		Thread.sleep(1000);
    		ImagePos farmPos = Script.searchImgUsingThread(Main.macroImgFolder + "\\farm.png", Main.script.similarGap);
    		if (checkStop()) return;
    		if(farmPos == null) {
    			MyUtility.printLog("�� ã�� ����");return;
    		}
    		Main.robot.mouseMove(farmPos.x + Main.minecraftRect.x, farmPos.y + Main.minecraftRect.y); // �� ���� Ŭ��
    		Thread.sleep(100);
    		Main.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    		Thread.sleep(20);
    		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    		Thread.sleep(3000);
    		if (checkStop())return;
    		
    	}
    	
		
    
    }
    


}