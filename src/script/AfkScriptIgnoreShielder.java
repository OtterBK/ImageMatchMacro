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
import listener.MouseListener;
import util.ImagePos;
import util.MyPos;
import util.MyUtility;

public class AfkScriptIgnoreShielder extends Script{
	
	public AfkScriptIgnoreShielder(int similar) {
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
		keyListener = new KeyListener();
		GlobalScreen.addNativeKeyListener(keyListener);
		
		
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					
					
					Thread.sleep(3000);
					
					PointerInfo pt = MouseInfo.getPointerInfo();
					center.x = (int)pt.getLocation().getX();
					center.y = (int)pt.getLocation().getY();
					
					MyPos guessCenter = new MyPos(Main.minecraftRect.width / 2 + Main.minecraftRect.x, 
							Main.minecraftRect.height/2 + Main.minecraftRect.y); //�߽� ��ǥ ����
					if(Math.abs(guessCenter.x - center.x) > 100 || Math.abs(guessCenter.y - center.y) > 100) {
						new CheckGUI(null, "�߸��� �߽���ǥ ���� , ��ũ�θ� ������ϼ���.", false, false);
						stopScript();
						return;
					}

					//robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					
//					int xPos = 890;
//					int yPos = 455;
//					
//					int gapX = 35;
//					
//					ArrayList<MyPos> buttonList = new ArrayList<MyPos>(5);
//					for(int i = 0; i < 5; i++) {
//						buttonList.add(new MyPos(xPos, yPos, i));
//						xPos += gapX;
//					}
//									
					MyUtility.printLog("�ʱ⼳���Ϸ�");
					
					boolean isMacro = false;
					MyUtility.printLog("�ݺ� ����");
					
					if(stopFlag) {
						stopScript();
						return;
					}
					
					while(true) {
						
						//�̸� ���� ��ũ��ȭ��� ���� ȭ�� �̹��� ���絵 ��
						isMacro = checkMacro();

						//if(!isMacro) {
							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						//}
						

							
						for(int i = 0; i < 10; i++) {
							Thread.sleep(500); //5�ʸ��� ��ũ�� Ȯ����
							if(stopFlag) {
								stopScript();
								return;
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
	
	
	private void btnMove(MyPos pos) {
		robot.mouseMove(pos.x+Main.minecraftRect.x, pos.y+Main.minecraftRect.y+5); 
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
	
//	private boolean checkMacro() {
//		MyUtility.capture(); //ȭ��ĸ��
//        double result = MyCompareHist.compare(Main.macroImgFolder+"\\macro.png", Main.compareFolder+"\\screenshot.png");
//        MyUtility.printLog("��ũ�� �̹������� ���簪 : "+result);
//        if(result > 0.5) { //0.5 �̻��̸�
//        	MyUtility.printLog("��ũ�� ����â ������!");	
//        	return true;
//        } else {
//        	MyUtility.printLog("��ũ�� ����â �������� ����");
//        	return false;
//        }
//	}
	
//	private boolean checkMacro() throws InterruptedException {
//		
//		ImagePos checkPos = searchImgUsingThread(Main.macroImgFolder+"\\macroBar.png", similarGap); //��ũ�� �� �̹��� ã��
//		if(checkPos == null) {
//			MyUtility.printLog("��ũ�� ����â �����ȵ�");
//			return false; //������ return false;	
//		}
//		MyUtility.printLog("��ũ�� ����â ������");
//		
//		ArrayList<Future<ImagePos>> btnTh = new ArrayList<Future<ImagePos>>(5);
//		
//		for(findIndex = 1; findIndex < 6; findIndex++) {
//			final int fileIndex = findIndex;
//			Callable<ImagePos> task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = searchImgUsingThread(Main.macroImgFolder+"\\"+fileIndex+".png", similarGap);
//	        		//MyUtility.printLog(tmpPos+"");
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			btnTh.add(Main.threadPool.submit(task));
//		}
//		
//		try {
//			ImagePos pos_btn1 = btnTh.get(0).get();
//			ImagePos pos_btn2 = btnTh.get(1).get();
//			ImagePos pos_btn3 = btnTh.get(2).get();
//			ImagePos pos_btn4 = btnTh.get(3).get();
//			ImagePos pos_btn5 = btnTh.get(4).get();
//
//			//MyUtility.printLog("d1");
//			
//			
//			if(pos_btn1 == null
//					&& pos_btn2 == null
//					&& pos_btn3 == null
//					&& pos_btn4 == null
//					&& pos_btn5 == null) {
//				MyUtility.printLog("��ũ�� ����â ���� �ȵ�");
//			} else if(pos_btn1 != null 
//					&& pos_btn2 != null
//					&& pos_btn3 != null
//					&& pos_btn4 != null
//					&& pos_btn5 != null){
//				MyUtility.printLog("��ũ�� ����â ������, ���� �õ�");
//				MyUtility.printLog("��ư1 ��ǥ : "+pos_btn1.x +", "+ pos_btn1.y);
//				MyUtility.printLog("��ư2 ��ǥ : "+pos_btn2.x +", "+ pos_btn2.y);
//				MyUtility.printLog("��ư3 ��ǥ : "+pos_btn3.x +", "+ pos_btn3.y);
//				MyUtility.printLog("��ư4 ��ǥ : "+pos_btn4.x +", "+ pos_btn4.y);
//				MyUtility.printLog("��ư5 ��ǥ : "+pos_btn5.x +", "+ pos_btn5.y);
//				
//				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//				btnMove(new MyPos(pos_btn1.x, pos_btn1.y, 1));
//				click();
//				btnMove(new MyPos(pos_btn2.x, pos_btn2.y, 2));
//				click();
//				btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3));
//				click();
//				btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
//				click();
//				btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
//				click();
//				
//				return true;
//			} else {
//				MyUtility.printLog("��ũ��â ����, ������ ��ư ã��, ���콺 �̵�");
//				robot.mouseMove(Main.minecraftRect.x+50, Main.minecraftRect.y+50);
//				MyUtility.printLog("�̵���ǥ : "+(Main.minecraftRect.x+50)+" , "+(Main.minecraftRect.y+50));
//				
//				if(pos_btn5 == null) { //��ư 5�� ��ã���Ÿ� ��¿��������
//					if(pos_btn5 == null)MyUtility.printLog("��ư 5 �����ȵ�");
//				} else if(pos_btn4 == null) { //��ư 4�� ��ã������
//					ImagePos pos_tmp_btn4 = searchImgUsingThread(Main.macroImgFolder+"\\d4.png", similarGap);
//					if(pos_tmp_btn4 != null) {
//						MyUtility.printLog("��ư 4 �Ϸ��"); //��ư 4�� �Ϸ�� ���¶��
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5)); //��ư 5Ŭ��
//						click();
//					}
//					else MyUtility.printLog("��ư 4 �����ȵ�");
//				} else if(pos_btn3 == null) {
//					ImagePos pos_tmp_btn3 = searchImgUsingThread(Main.macroImgFolder+"\\d3.png", similarGap);
//					if(pos_tmp_btn3 != null) { //��ư 3�� �Ϸ�� ���¸�
//						MyUtility.printLog("��ư 3 �Ϸ��");
//						btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
//						click();
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5)); //��ư 4,5Ŭ��
//						click();
//					}
//					else MyUtility.printLog("��ư 3 �����ȵ�");
//				} else if(pos_btn2 == null) {
//					ImagePos pos_tmp_btn2 = searchImgUsingThread(Main.macroImgFolder+"\\d2.png", similarGap);
//					if(pos_tmp_btn2 != null) { //��ư 2�� �Ϸ�� ���¸�
//						MyUtility.printLog("��ư 2 �Ϸ��");
//						btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3)); //��ư3,4,5
//						click();
//						btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
//						click();
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
//					}
//					else MyUtility.printLog("��ư 2 �����ȵ�");
//				} else if(pos_btn1 == null) {
//					ImagePos pos_tmp_btn1 = searchImgUsingThread(Main.macroImgFolder+"\\d1.png", similarGap);
//					if(pos_tmp_btn1 != null) { //��ư 1�� �Ϸ�ƴٸ�
//						MyUtility.printLog("��ư 1 �Ϸ��");
//						btnMove(new MyPos(pos_btn2.x, pos_btn2.y, 2)); //��ư 2,3,4,5 Ŭ��
//						click();
//						btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3));
//						click();
//						btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
//						click();
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
//						click();
//					}
//					else MyUtility.printLog("��ư 1 �����ȵ�"); //�̿Ϸ� ��ư1�� ��ã�Ұ� �Ϸ��ư�� ��ã�Ҵٸ� ��¿��������
//				} else {
//					MyUtility.printLog("��� ��ư�� ã�ҽ��ϴ�. ��ũ�� ���� ��õ�"); 
//				}
//				
//				
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return false;
//	}
	

	
}

