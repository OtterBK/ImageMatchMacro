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
		
		
		//클라이언트 접속 대기
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
							Main.minecraftRect.height/2 + Main.minecraftRect.y); //중심 좌표 추측
					if(Math.abs(guessCenter.x - center.x) > 100 || Math.abs(guessCenter.y - center.y) > 100) {
						new CheckGUI(null, "잘못된 중심좌표 감지 , 매크로를 재실행하세요.", false, false);
						stopScript();
						return;
					}
					
					MyUtility.printLog("초기설정완료");
					
					boolean isMacro = false;
					MyUtility.printLog("반복 시작");
					
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
						
						if(fishingStep == -1) { //낚시 단계가 없다면
							ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder+"\\fc2.png", similarGap); //물고기 잡혔는지 확인
							if(fishingState != null) { //잡혔다면
								fishingStep = 0; //단계 이행
								catchCnt += 1;
								MyUtility.printLog(catchCnt+"번째 물고기");
								if(stored) stored = false; //저장 변수 초기화
							} else { //안잡혔다면
								if(waitCnt % 60 == 0) checkMacro(); //60회에 1번씩 매크로 떳는지 확인
								
							}
						} 
						

						switch(fishingStep) {
							case 0:
								MyUtility.printLog("0단계");
								while(limitChecker++ < limitLoop) { //3번스탭 찾을때까지 반복
									bothlick(); //2번클릭 
									ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f3.png", similarGap); // 먼저																								// 확인
									if (fishingState != null) { // 3번 스탭 이미지 찾았다면
										fishingStep = 3;
										break;
									} else {
										if(limitChecker % 10 == 0) { //10번에 1번만
											fishingState = searchImgUsingThread(Main.macroImgFolder+"\\fc2.png", similarGap); //물고기 잡혔는지 확인
											if(fishingState == null) { //잡힌 상태가 아닌지 확인
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
								MyUtility.printLog("3단계"); //3단계는 아무것도 하지마
								while(limitChecker++ < limitLoop) { 
									Thread.sleep(50); //조금 기다렸다가
									ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f1.png", similarGap); // 1번있는지 확인
									if (fishingState != null) { // 1번 스탭 이미지 찾았다면
										fishingStep = 0; //0단계로 이행
										break;
									} else { //못찾았다면
										fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f0.png", similarGap); // 0번있는지 확인
										if (fishingState != null) { // 0번 스탭 이미지 찾았다면
											fishingState = searchImgUsingThread(Main.macroImgFolder + "\\f3.png", similarGap); // 3번있는지 확인
											if(fishingState == null) { //3단계 이미지가 없다면, 찐 0단계인거임
												fishingStep = 0; //0단계로 이행
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
								if(!isFishing) { //낚시 중이 아니면
									isFishing = true;
									rightClick(); //낚시 해
									waitCnt = 0;
									MyUtility.printLog("낚시 시작");
								} else { //낚시중이면
									if(!stored && isStore && catchCnt > 0) { //상자에 아직 저장안했고 저장모드 켜있다면									
										//저장스크립트
										if(catchCnt % storeInterval == 0) { //27마리 잡았다면, 정리 시도	
											ImagePos fishingState = searchImgUsingThread(Main.macroImgFolder + "\\fc2.png", similarGap); //
											if(fishingState != null) {
												fishingStep = 0;
												break; //정리 시도하려는데 낚시중이면  break
											}
											stored = true;
											
											int downCnt = 0;
											
											robot.keyPress(KeyEvent.VK_9); //우선 손 들어
											Thread.sleep(10);
											robot.keyRelease(KeyEvent.VK_9);
											
											ImagePos chestPos = null; 
											
											while(downCnt++ < 100) {
												dragDown(); //아래로 내리고
												Thread.sleep(20);
												rightClick(); //상자 열어보고
												chestPos = searchImgUsingThread(Main.macroImgFolder + "\\chestIcon.png", similarGap);
												if(chestPos != null) break; //상자 열었으면 break
											}
											
											if(chestPos != null) { //성공적으로 상자 열였다면
												//MyUtility.printLog("find");
												robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
												robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
												Thread.sleep(100);
												robot.keyPress(KeyEvent.VK_SHIFT); //쉬프트 눌러
												int pixelGap = 36; //각 인벤칸 간격
												for(int i = 0; i < 9; i++) {
													MyPos tmpPos = new MyPos(chestPos.x+Main.minecraftRect.x, chestPos.y+Main.minecraftRect.y);
													for(int j = 0; j < 3; j++) {
														if(i == 0 && j == 0) { //0,0은 무시
															continue;
														} else {
															robot.mouseMove(tmpPos.x + (pixelGap * i), tmpPos.y + (pixelGap * j));
															Thread.sleep(100);
															click();
														}
													}
												}
												Thread.sleep(100);
												robot.keyRelease(KeyEvent.VK_SHIFT); //다하면 쉬프트 때
												robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
												robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
												Thread.sleep(100);
												robot.keyPress(KeyEvent.VK_E); //인벤토리도 닫아
												Thread.sleep(10);
												robot.keyRelease(KeyEvent.VK_E); //인벤토리도 닫아
											}
											
											//못찾든 찾든 화면 원위치
											Thread.sleep(100);
											//MyUtility.printLog("해야할 드래그 횟수"+downCnt);
											int tmpInt = 0;
											while(downCnt-- > 0) {
												tmpInt++;
												dragUp(); //다시 정면바라봐
												Thread.sleep(50);
											}
											//MyUtility.printLog("실제 드래그 횟수"+tmpInt);
											
											robot.keyPress(KeyEvent.VK_2); //낚시대 들어
											Thread.sleep(10);
											robot.keyRelease(KeyEvent.VK_2);
											Thread.sleep(1000);
											rightClick(); //낚시 시작										
											
										}
									}
									waitCnt += 1;
									if(waitCnt % 100 == 0) MyUtility.printLog("낚시 중..."+waitCnt);
									if(waitCnt >= 500) { //40초 기다렸는데도 낚이기 대기중이면
										if(isStore) { //잠낚용 모드면
											MyUtility.printLog("낚시 중 아니라판단, 아이템 재정렬");
											reSort();
										}									
										
										Thread.sleep(500);
										MyUtility.printLog("재 낚시");
										robot.keyPress(KeyEvent.VK_9); //우선 손 들어
										Thread.sleep(10);
										robot.keyRelease(KeyEvent.VK_9);
										Thread.sleep(500);
										robot.keyPress(KeyEvent.VK_2); //낚시대 들어
										Thread.sleep(10);
										robot.keyRelease(KeyEvent.VK_2);
										Thread.sleep(500);
										isFishing = false; //낚시중이 아닌것같아...
									}
									
								}
						}
						
						
						Thread.sleep(50); // 0.05초마다 매크로 종료 확인함
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
		robot.keyPress(KeyEvent.VK_E); //인벤토리 열어
		Thread.sleep(10);
		robot.keyRelease(KeyEvent.VK_E); //인벤토리 열어
		Thread.sleep(500);
		ImagePos signPos = searchImgUsingThread(Main.macroImgFolder + "\\fre.png", similarGap); //표지판 찾아
		if(signPos != null) { //표지판 있으면
			MyPos startPos = new MyPos(signPos.x + 36, signPos.y ); //시작 위치
			//다음 칸이 비었는지 확인
			for (int i = 0; i < 6; i++) {
				//MyUtility.printLog("start : "+startPos.x + ", "+ startPos.y);
				ImagePos blankCheck = ImageScanner.getOnScreen(Main.macroImgFolder + "\\blank.png",
						startPos.x - 22, startPos.y - 22, 40, 40, similarGap);
				MyUtility.printLog(blankCheck + "");
				if (blankCheck != null) { // 해당 칸이 비어있다면
					//MyUtility.printLog(blankCheck.x + ", "+ blankCheck.y);
					robot.mouseMove(blankCheck.x  + Main.minecraftRect.x + 36, blankCheck.y  + Main.minecraftRect.y); // 다음칸으로 마우스 이동
					Thread.sleep(100);
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // 클릭
					Thread.sleep(30);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					Thread.sleep(20);
					robot.mouseMove(blankCheck.x  + Main.minecraftRect.x, blankCheck.y  + Main.minecraftRect.y); // 원래칸으로 마우스 이동
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // 클릭
					Thread.sleep(30);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					Thread.sleep(20);
					robot.mouseMove(Main.minecraftRect.x+50, Main.minecraftRect.y+50); //이미지 감지해야되니깐 마우스좀 저리 치워바
					Thread.sleep(100);
				}
				startPos.x += 36;
			} 
		}
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_E); //인벤토리 닫아
		Thread.sleep(10);
		robot.keyRelease(KeyEvent.VK_E); //인벤토리 닫아
		
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
			Main.mainFrame.btn_excute.setText("매크로 실행");
			MyUtility.printLog("매크로 종료");
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

	
}
