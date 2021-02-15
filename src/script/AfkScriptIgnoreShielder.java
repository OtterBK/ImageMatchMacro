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
							Main.minecraftRect.height/2 + Main.minecraftRect.y); //중심 좌표 추측
					if(Math.abs(guessCenter.x - center.x) > 100 || Math.abs(guessCenter.y - center.y) > 100) {
						new CheckGUI(null, "잘못된 중심좌표 감지 , 매크로를 재실행하세요.", false, false);
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
					MyUtility.printLog("초기설정완료");
					
					boolean isMacro = false;
					MyUtility.printLog("반복 시작");
					
					if(stopFlag) {
						stopScript();
						return;
					}
					
					while(true) {
						
						//미리 찍어둔 매크로화면과 현재 화면 이미지 유사도 비교
						isMacro = checkMacro();

						//if(!isMacro) {
							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						//}
						

							
						for(int i = 0; i < 10; i++) {
							Thread.sleep(500); //5초마다 매크로 확인함
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
//		MyUtility.capture(); //화면캡쳐
//        double result = MyCompareHist.compare(Main.macroImgFolder+"\\macro.png", Main.compareFolder+"\\screenshot.png");
//        MyUtility.printLog("매크로 이미지와의 유사값 : "+result);
//        if(result > 0.5) { //0.5 이상이면
//        	MyUtility.printLog("매크로 방지창 감지됨!");	
//        	return true;
//        } else {
//        	MyUtility.printLog("매크로 방지창 감지되지 않음");
//        	return false;
//        }
//	}
	
//	private boolean checkMacro() throws InterruptedException {
//		
//		ImagePos checkPos = searchImgUsingThread(Main.macroImgFolder+"\\macroBar.png", similarGap); //매크로 바 이미지 찾기
//		if(checkPos == null) {
//			MyUtility.printLog("매크로 방지창 감지안됨");
//			return false; //없으면 return false;	
//		}
//		MyUtility.printLog("매크로 방지창 감지됨");
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
//				MyUtility.printLog("매크로 방지창 감지 안됨");
//			} else if(pos_btn1 != null 
//					&& pos_btn2 != null
//					&& pos_btn3 != null
//					&& pos_btn4 != null
//					&& pos_btn5 != null){
//				MyUtility.printLog("매크로 방지창 감지됨, 해제 시도");
//				MyUtility.printLog("버튼1 좌표 : "+pos_btn1.x +", "+ pos_btn1.y);
//				MyUtility.printLog("버튼2 좌표 : "+pos_btn2.x +", "+ pos_btn2.y);
//				MyUtility.printLog("버튼3 좌표 : "+pos_btn3.x +", "+ pos_btn3.y);
//				MyUtility.printLog("버튼4 좌표 : "+pos_btn4.x +", "+ pos_btn4.y);
//				MyUtility.printLog("버튼5 좌표 : "+pos_btn5.x +", "+ pos_btn5.y);
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
//				MyUtility.printLog("매크로창 감지, 부족한 버튼 찾기, 마우스 이동");
//				robot.mouseMove(Main.minecraftRect.x+50, Main.minecraftRect.y+50);
//				MyUtility.printLog("이동좌표 : "+(Main.minecraftRect.x+50)+" , "+(Main.minecraftRect.y+50));
//				
//				if(pos_btn5 == null) { //버튼 5를 못찾은거면 어쩔수가없음
//					if(pos_btn5 == null)MyUtility.printLog("버튼 5 감지안됨");
//				} else if(pos_btn4 == null) { //버튼 4를 못찾았을때
//					ImagePos pos_tmp_btn4 = searchImgUsingThread(Main.macroImgFolder+"\\d4.png", similarGap);
//					if(pos_tmp_btn4 != null) {
//						MyUtility.printLog("버튼 4 완료됨"); //버튼 4가 완료된 상태라면
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5)); //버튼 5클릭
//						click();
//					}
//					else MyUtility.printLog("버튼 4 감지안됨");
//				} else if(pos_btn3 == null) {
//					ImagePos pos_tmp_btn3 = searchImgUsingThread(Main.macroImgFolder+"\\d3.png", similarGap);
//					if(pos_tmp_btn3 != null) { //버튼 3이 완료된 상태면
//						MyUtility.printLog("버튼 3 완료됨");
//						btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
//						click();
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5)); //버튼 4,5클릭
//						click();
//					}
//					else MyUtility.printLog("버튼 3 감지안됨");
//				} else if(pos_btn2 == null) {
//					ImagePos pos_tmp_btn2 = searchImgUsingThread(Main.macroImgFolder+"\\d2.png", similarGap);
//					if(pos_tmp_btn2 != null) { //버튼 2가 완료된 상태면
//						MyUtility.printLog("버튼 2 완료됨");
//						btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3)); //버튼3,4,5
//						click();
//						btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
//						click();
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
//					}
//					else MyUtility.printLog("버튼 2 감지안됨");
//				} else if(pos_btn1 == null) {
//					ImagePos pos_tmp_btn1 = searchImgUsingThread(Main.macroImgFolder+"\\d1.png", similarGap);
//					if(pos_tmp_btn1 != null) { //버튼 1이 완료됐다면
//						MyUtility.printLog("버튼 1 완료됨");
//						btnMove(new MyPos(pos_btn2.x, pos_btn2.y, 2)); //버튼 2,3,4,5 클릭
//						click();
//						btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3));
//						click();
//						btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
//						click();
//						btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
//						click();
//					}
//					else MyUtility.printLog("버튼 1 감지안됨"); //미완료 버튼1을 못찾았고 완료버튼도 못찾았다면 어쩔수가없음
//				} else {
//					MyUtility.printLog("모든 버튼을 찾았습니다. 매크로 해제 재시도"); 
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

