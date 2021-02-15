package script;


import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import executer.Main;
import gui.CheckGUI;
import listener.KeyListener;
import listener.MouseListener;
import util.ImagePos;
import util.ImageScanner;
import util.MyPos;
import util.MyUtility;

public class AutoFishingScript extends Script{
	
	boolean isFishing;
	public boolean isStore = false;
	private boolean stored = false;
	public int storeInterval = 27;
	
	public AutoFishingScript(int similar) {
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
		
		
		//Ŭ���̾�Ʈ ���� ���
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					
					
					Thread.sleep(3000);
					
					

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
					
					MyUtility.printLog("�ʱ⼳���Ϸ�");
					
					boolean isMacro = false;
					MyUtility.printLog("�ݺ� ����");
					
					if(stopFlag) {
						stopScript();
						return;
					}
					
					int waitCnt = 0;
					int limitLoop = 100;
					int limitChecker = 0;
					int fishingStep = -1;
					int catchCnt = 0;
					
					while(true) {
						limitChecker = 0;
						
						if(fishingStep == -1) { //���� �ܰ谡 ���ٸ�
							ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder+"\\fc2.png", similarGap); //����� �������� Ȯ��
							if(fishingState != null) { //�����ٸ�
								fishingStep = 0; //�ܰ� ����
								catchCnt += 1;
								MyUtility.printLog(catchCnt+"��° �����");
								if(stored) stored = false; //���� ���� �ʱ�ȭ
							} else { //�������ٸ�
								if(waitCnt % 60 == 0) checkMacro(); //60ȸ�� 1���� ��ũ�� ������ Ȯ��
								
							}
						} 
						

						switch(fishingStep) {
							case 0:
								MyUtility.printLog("0�ܰ�");
								while(limitChecker++ < limitLoop) { //3������ ã�������� �ݺ�
									bothlick(); //2��Ŭ�� 
									ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f3.png", similarGap); // ����																								// Ȯ��
									if (fishingState != null) { // 3�� ���� �̹��� ã�Ҵٸ�
										fishingStep = 3;
										break;
									} else {
										if(limitChecker % 10 == 0) { //10���� 1����
											fishingState = searchImgUsingThread(Main.macroImgFolder+"\\fc2.png", similarGap); //����� �������� Ȯ��
											if(fishingState == null) { //���� ���°� �ƴ��� Ȯ��
												waitCnt = 0;
												fishingStep = -1;
												break;
											}
										}		
									}
									if(checkStop()) return;
								}								
								break;
							case 3:
								MyUtility.printLog("3�ܰ�"); //3�ܰ�� �ƹ��͵� ������
								while(limitChecker++ < limitLoop) { 
									Thread.sleep(50); //���� ��ٷȴٰ�
									ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f1.png", similarGap); // 1���ִ��� Ȯ��
									if (fishingState != null) { // 1�� ���� �̹��� ã�Ҵٸ�
										fishingStep = 0; //0�ܰ�� ����
										break;
									} else { //��ã�Ҵٸ�
										fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f0.png", similarGap); // 0���ִ��� Ȯ��
										if (fishingState != null) { // 0�� ���� �̹��� ã�Ҵٸ�
											fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f3.png", similarGap); // 3���ִ��� Ȯ��
											if(fishingState == null) { //3�ܰ� �̹����� ���ٸ�, �� 0�ܰ��ΰ���
												fishingStep = 0; //0�ܰ�� ����
												break;
											}			
										} else {
											fishingState = searchImgUsingThread(Main.macroImgFolder + "\\fc2.png", similarGap); //
											if(fishingState == null) {
												waitCnt = 0;
												fishingStep = -1;
												break;
											}
										}
									}
									if(checkStop()) return;
								}	
								break;
							default:
								if(!isFishing) { //���� ���� �ƴϸ�
									isFishing = true;
									rightClick(); //���� ��
									waitCnt = 0;
									MyUtility.printLog("���� ����");
								} else { //�������̸�
									if(!stored && isStore && catchCnt > 0) { //���ڿ� ���� ������߰� ������ ���ִٸ�									
										//���彺ũ��Ʈ
										if(catchCnt % storeInterval == 0) { //27���� ��Ҵٸ�, ���� �õ�	
											ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder + "\\fc2.png", similarGap); //
											if(fishingState != null) {
												fishingStep = 0;
												break; //���� �õ��Ϸ��µ� �������̸�  break
											}
											stored = true;
											
											int downCnt = 0;
											
											robot.keyPress(KeyEvent.VK_9); //�켱 �� ���
											Thread.sleep(10);
											robot.keyRelease(KeyEvent.VK_9);
											
											ImagePos chestPos = null; 
											
											while(downCnt++ < 100) {
												dragDown(); //�Ʒ��� ������
												Thread.sleep(20);
												rightClick(); //���� �����
												chestPos = searchImgUsingThread(Main.macroImgFolder + "\\chestIcon.png", similarGap);
												if(chestPos != null) break; //���� �������� break
											}
											
											if(chestPos != null) { //���������� ���� �����ٸ�
												//MyUtility.printLog("find");
												robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
												robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
												Thread.sleep(100);
												robot.keyPress(KeyEvent.VK_SHIFT); //����Ʈ ����
												int pixelGap = 36; //�� �κ�ĭ ����
												for(int i = 0; i < 9; i++) {
													MyPos tmpPos = new MyPos(chestPos.x+Main.minecraftRect.x, chestPos.y+Main.minecraftRect.y);
													for(int j = 0; j < 3; j++) {
														if(i == 0 && j == 0) { //0,0�� ����
															continue;
														} else {
															robot.mouseMove(tmpPos.x + (pixelGap * i), tmpPos.y + (pixelGap * j));
															Thread.sleep(100);
															click();
														}
													}
												}
												Thread.sleep(100);
												robot.keyRelease(KeyEvent.VK_SHIFT); //���ϸ� ����Ʈ ��
												robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
												robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
												Thread.sleep(100);
												robot.keyPress(KeyEvent.VK_E); //�κ��丮�� �ݾ�
												Thread.sleep(10);
												robot.keyRelease(KeyEvent.VK_E); //�κ��丮�� �ݾ�
											}
											
											//��ã�� ã�� ȭ�� ����ġ
											Thread.sleep(100);
											//MyUtility.printLog("�ؾ��� �巡�� Ƚ��"+downCnt);
											int tmpInt = 0;
											while(downCnt-- > 0) {
												tmpInt++;
												dragUp(); //�ٽ� ����ٶ��
												Thread.sleep(50);
											}
											//MyUtility.printLog("���� �巡�� Ƚ��"+tmpInt);
											
											robot.keyPress(KeyEvent.VK_2); //���ô� ���
											Thread.sleep(10);
											robot.keyRelease(KeyEvent.VK_2);
											Thread.sleep(1000);
											rightClick(); //���� ����										
											
										}
									}
									waitCnt += 1;
									if(waitCnt % 100 == 0) MyUtility.printLog("���� ��..."+waitCnt);
									if(waitCnt >= 500) { //40�� ��ٷȴµ��� ���̱� ������̸�
										if(isStore) { //�ᳬ�� ����
											MyUtility.printLog("���� �� �ƴ϶��Ǵ�, ������ ������");
											reSort();
										}									
										
										Thread.sleep(500);
										MyUtility.printLog("�� ����");
										robot.keyPress(KeyEvent.VK_9); //�켱 �� ���
										Thread.sleep(10);
										robot.keyRelease(KeyEvent.VK_9);
										Thread.sleep(500);
										robot.keyPress(KeyEvent.VK_2); //���ô� ���
										Thread.sleep(10);
										robot.keyRelease(KeyEvent.VK_2);
										Thread.sleep(500);
										isFishing = false; //�������� �ƴѰͰ���...
									}
									
								}
						}
						
						
						Thread.sleep(50); // 0.05�ʸ��� ��ũ�� ���� Ȯ����
						if(checkStop()) return;
							
					}
									
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		Main.threadPool.submit(thread);
		
		
	}
	
	private void reSort() throws InterruptedException {
		robot.keyPress(KeyEvent.VK_E); //�κ��丮 ����
		Thread.sleep(10);
		robot.keyRelease(KeyEvent.VK_E); //�κ��丮 ����
		Thread.sleep(500);
		ImagePos signPos = searchImgUsingThread(Main.macroImgFolder + "\\fre.png", similarGap); //ǥ���� ã��
		if(signPos != null) { //ǥ���� ������
			MyPos startPos = new MyPos(signPos.x + 36, signPos.y ); //���� ��ġ
			//���� ĭ�� ������� Ȯ��
			for (int i = 0; i < 6; i++) {
				//MyUtility.printLog("start : "+startPos.x + ", "+ startPos.y);
				ImagePos blankCheck = ImageScanner.getOnScreen(Main.macroImgFolder + "\\blank.png",
						startPos.x - 22, startPos.y - 22, 40, 40, similarGap);
				MyUtility.printLog(blankCheck + "");
				if (blankCheck != null) { // �ش� ĭ�� ����ִٸ�
					//MyUtility.printLog(blankCheck.x + ", "+ blankCheck.y);
					robot.mouseMove(blankCheck.x  + Main.minecraftRect.x + 36, blankCheck.y  + Main.minecraftRect.y); // ����ĭ���� ���콺 �̵�
					Thread.sleep(100);
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Ŭ��
					Thread.sleep(30);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					Thread.sleep(20);
					robot.mouseMove(blankCheck.x  + Main.minecraftRect.x, blankCheck.y  + Main.minecraftRect.y); // ����ĭ���� ���콺 �̵�
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // Ŭ��
					Thread.sleep(30);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					Thread.sleep(20);
					robot.mouseMove(Main.minecraftRect.x+50, Main.minecraftRect.y+50); //�̹��� �����ؾߵǴϱ� ���콺�� ���� ġ����
					Thread.sleep(100);
				}
				startPos.x += 36;
			} 
		}
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_E); //�κ��丮 �ݾ�
		Thread.sleep(10);
		robot.keyRelease(KeyEvent.VK_E); //�κ��丮 �ݾ�
		
	}
	
	private void dragDown() {
		robot.mouseMove(center.x, center.y + 200);
		//MyUtility.printLog("drag Down : " + center.x + ", " + (center.y + 100));
	}
	
	private void dragUp() {
		robot.mouseMove(center.x, center.y - 198);
		//MyUtility.printLog("drag Up : " + center.x + ", " + (center.y - 100));
	}
	
	private boolean checkStop() {
		if (stopFlag) {
			Main.mainFrame.btn_excute.setEnabled(true);
			Main.mainFrame.btn_excute.setText("��ũ�� ����");
			MyUtility.printLog("��ũ�� ����");
			return true;
		} else return false;
	}
	
	
	private void move(MyPos pos) {
		robot.mouseMove(pos.x, pos.y); 
		//MyUtility.printLog("move: "+pos.x + ", " + pos.y);
	}
	
	private void click() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(1);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		//MyUtility.printLog("click");	
	}	
	
	private void bothlick() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(1);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		//MyUtility.printLog("click");	
	}
	
	private void rightClick() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		Thread.sleep(1);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);

	}
	
	private void quardClick() throws InterruptedException{
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		Thread.sleep(1);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
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

	
}
