package script;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.jnativehook.GlobalScreen;

import executer.Main;
import listener.KeyListener;
import listener.MouseListener;
import util.ImagePos;
import util.ImageScanner;
import util.MyPos;
import util.MyUtility;

public class Script {
	
	public boolean stopFlag = false;
	public int similarGap = 7;
	public KeyListener keyListener;
	public MouseListener mouseListener;
	public MyPos center = new MyPos(Main.minecraftRect.x + (Main.minecraftRect.width / 2), Main.minecraftRect.y + (Main.minecraftRect.height / 2));
	
	Robot robot;
	int findIndex = 0;
	
	private static class Type{
		public static int maxWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		public static int maxHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		public static int halfWidth = maxWidth / 2;
		public static int halfHeight = maxHeight / 2;
		
		public static final int width_0 = 0;
		public static final int width_h = halfWidth/2;
		public static final int width_hh = halfWidth;
		public static final int width_hhh = halfWidth + halfWidth/2;
		public static final int width_e = maxWidth;
		
		public static final int height_0 = 0;
		public static final int height_h = halfHeight/2;
		public static final int height_hh = halfWidth;
		public static final int height_hhh = halfWidth + halfWidth/2;
		public static final int height_e = maxWidth;
		
	}
	
	public static ImagePos searchImgUsingThread(String image, int similar) {
		long start = System.currentTimeMillis();
		//MyUtility.printLog("Start : " + start);
		//MyUtility.printLog(image);
		int maxWidth = (int) Main.minecraftRect.width;
		int maxHeight = (int) Main.minecraftRect.height;
		int halfWidth = maxWidth / 2;
		int halfHeight = maxHeight / 2; 
		
		try {
			Callable<ImagePos> task1 = new Callable<ImagePos>() {
	        	
	        	public ImagePos call() throws Exception {
	        		
	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, 0,0, halfWidth, halfHeight ,similar);
	        		return tmpPos;
	        	}
	        	
			};
			Future<ImagePos> future1 = Main.threadPool.submit(task1);
			
			Callable<ImagePos> task2 = new Callable<ImagePos>() {
	        	
	        	public ImagePos call() throws Exception {
	        		
	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, halfWidth, 0, maxWidth, halfHeight ,similar);
	        		return tmpPos;
	        	}
	        	
			};
			Future<ImagePos> future2 = Main.threadPool.submit(task2);

			
			Callable<ImagePos> task3 = new Callable<ImagePos>() {
	        	
	        	public ImagePos call() throws Exception {
	        		
	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, 0, halfHeight, halfWidth, maxHeight ,similar);
	        		return tmpPos;
	        	}
	        	
			};
			Future<ImagePos> future3 = Main.threadPool.submit(task3);
			
			Callable<ImagePos> task4 = new Callable<ImagePos>() {
	        	
	        	public ImagePos call() throws Exception {
	        		
	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, halfWidth, halfHeight, maxWidth, maxHeight ,similar);
	        		return tmpPos;
	        	}
	        	
			};
			Future<ImagePos> future4 = Main.threadPool.submit(task4);
			
	        
			ImagePos imagePos1 = future1.get();
			if(imagePos1 == null) System.out.println("false");
	        else return imagePos1;
			ImagePos imagePos2 = future2.get();
			if(imagePos2 == null) System.out.println("false");
	        else return imagePos2;
			ImagePos imagePos3 = future3.get();
			if(imagePos3 == null) System.out.println("false");
	        else return imagePos3;
			ImagePos imagePos4 = future4.get();
			if(imagePos4 == null) System.out.println("false");
	        else return imagePos4;
			
			long end = System.currentTimeMillis();
//			MyUtility.printLog("end : " + end);
			//MyUtility.printLog("time : " + (end - start));
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static ImagePos searchImgUsingThread(String image, int startX, int startY, int searchW, int searchH,int similar) {
		long start = System.currentTimeMillis();
		//MyUtility.printLog("Start : " + start);
		//MyUtility.printLog(image);
		int maxWidth = (int) Main.minecraftRect.width;
		int maxHeight = (int) Main.minecraftRect.height;
		int halfWidth = maxWidth / 2;
		int halfHeight = maxHeight / 2; 
		
		try {
			Callable<ImagePos> task1 = new Callable<ImagePos>() {
	        	
	        	public ImagePos call() throws Exception {
	        		
	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, startX, startY, searchW, searchH ,similar);
	        		return tmpPos;
	        	}
	        	
			};
			Future<ImagePos> future1 = Main.threadPool.submit(task1);
			
			ImagePos imagePos1 = future1.get();
			if(imagePos1 == null) System.out.println("false");
	        else return imagePos1;
			
			long end = System.currentTimeMillis();
//			MyUtility.printLog("end : " + end);
			//MyUtility.printLog("time : " + (end - start));
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public void stopScript() {
		Main.mainFrame.btn_excute.setEnabled(true);
		Main.mainFrame.btn_excute.setText("��ũ�� ����");
		MyUtility.printLog("��ũ�� ����");
		if(keyListener != null) GlobalScreen.removeNativeKeyListener(keyListener);
		if(mouseListener != null) GlobalScreen.removeNativeMouseListener(mouseListener);
		Main.script = null;
	}
	
	public boolean checkMacro() throws InterruptedException {
		
		ImagePos checkPos = searchImgUsingThread(Main.macroImgFolder+"\\macroBar.png", similarGap); //��ũ�� �� �̹��� ã��
		if(checkPos == null) {
			MyUtility.printLog("��ũ�� ����â �����ȵ�");
			return false; //������ return false;	
		}
		MyUtility.printLog("��ũ�� ����â ������");
		
		ArrayList<Future<ImagePos>> btnTh = new ArrayList<Future<ImagePos>>(5);
		
		for(findIndex = 1; findIndex < 6; findIndex++) {
			final int fileIndex = findIndex;
			Callable<ImagePos> task = new Callable<ImagePos>() {
	        	
	        	public ImagePos call() throws Exception {
	        		
	        		ImagePos tmpPos = searchImgUsingThread(Main.macroImgFolder+"\\"+fileIndex+".png", similarGap);
	        		//MyUtility.printLog(tmpPos+"");
	        		return tmpPos;
	        	}
	        	
			};
			btnTh.add(Main.threadPool.submit(task));
		}
		
		try {
			ImagePos pos_btn1 = btnTh.get(0).get();
			ImagePos pos_btn2 = btnTh.get(1).get();
			ImagePos pos_btn3 = btnTh.get(2).get();
			ImagePos pos_btn4 = btnTh.get(3).get();
			ImagePos pos_btn5 = btnTh.get(4).get();

			//MyUtility.printLog("d1");
			
			
			if(pos_btn1 == null
					&& pos_btn2 == null
					&& pos_btn3 == null
					&& pos_btn4 == null
					&& pos_btn5 == null) {
				MyUtility.printLog("��ũ�� ����â ���� �ȵ�");
			} else if(pos_btn1 != null 
					&& pos_btn2 != null
					&& pos_btn3 != null
					&& pos_btn4 != null
					&& pos_btn5 != null){
				MyUtility.printLog("��ũ�� ����â ������, ���� �õ�");
				MyUtility.printLog("��ư1 ��ǥ : "+pos_btn1.x +", "+ pos_btn1.y);
				MyUtility.printLog("��ư2 ��ǥ : "+pos_btn2.x +", "+ pos_btn2.y);
				MyUtility.printLog("��ư3 ��ǥ : "+pos_btn3.x +", "+ pos_btn3.y);
				MyUtility.printLog("��ư4 ��ǥ : "+pos_btn4.x +", "+ pos_btn4.y);
				MyUtility.printLog("��ư5 ��ǥ : "+pos_btn5.x +", "+ pos_btn5.y);
				
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				script_btnMove(new MyPos(pos_btn1.x, pos_btn1.y, 1));
				script_click();
				script_btnMove(new MyPos(pos_btn2.x, pos_btn2.y, 2));
				script_click();
				script_btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3));
				script_click();
				script_btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
				script_click();
				script_btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
				script_click();
				
				return true;
			} else {
				MyUtility.printLog("��ũ��â ����, ������ ��ư ã��, ���콺 �̵�");
				robot.mouseMove(Main.minecraftRect.x+50, Main.minecraftRect.y+50);
				MyUtility.printLog("�̵���ǥ : "+(Main.minecraftRect.x+50)+" , "+(Main.minecraftRect.y+50));
				
				if(pos_btn5 == null) { //��ư 5�� ��ã���Ÿ� ��¿��������
					if(pos_btn5 == null)MyUtility.printLog("��ư 5 �����ȵ�");
				} else if(pos_btn4 == null) { //��ư 4�� ��ã������
					ImagePos pos_tmp_btn4 = searchImgUsingThread(Main.macroImgFolder+"\\d4.png", similarGap);
					if(pos_tmp_btn4 != null) {
						MyUtility.printLog("��ư 4 �Ϸ��"); //��ư 4�� �Ϸ�� ���¶��
						script_btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5)); //��ư 5Ŭ��
						script_click();
					}
					else MyUtility.printLog("��ư 4 �����ȵ�");
				} else if(pos_btn3 == null) {
					ImagePos pos_tmp_btn3 = searchImgUsingThread(Main.macroImgFolder+"\\d3.png", similarGap);
					if(pos_tmp_btn3 != null) { //��ư 3�� �Ϸ�� ���¸�
						MyUtility.printLog("��ư 3 �Ϸ��");
						script_btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
						script_click();
						script_btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5)); //��ư 4,5Ŭ��
						script_click();
					}
					else MyUtility.printLog("��ư 3 �����ȵ�");
				} else if(pos_btn2 == null) {
					ImagePos pos_tmp_btn2 = searchImgUsingThread(Main.macroImgFolder+"\\d2.png", similarGap);
					if(pos_tmp_btn2 != null) { //��ư 2�� �Ϸ�� ���¸�
						MyUtility.printLog("��ư 2 �Ϸ��");
						script_btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3)); //��ư3,4,5
						script_click();
						script_btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
						script_click();
						script_btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
					}
					else MyUtility.printLog("��ư 2 �����ȵ�");
				} else if(pos_btn1 == null) {
					ImagePos pos_tmp_btn1 = searchImgUsingThread(Main.macroImgFolder+"\\d1.png", similarGap);
					if(pos_tmp_btn1 != null) { //��ư 1�� �Ϸ�ƴٸ�
						MyUtility.printLog("��ư 1 �Ϸ��");
						script_btnMove(new MyPos(pos_btn2.x, pos_btn2.y, 2)); //��ư 2,3,4,5 Ŭ��
						script_click();
						script_btnMove(new MyPos(pos_btn3.x, pos_btn3.y, 3));
						script_click();
						script_btnMove(new MyPos(pos_btn4.x, pos_btn4.y, 4));
						script_click();
						script_btnMove(new MyPos(pos_btn5.x, pos_btn5.y, 5));
						script_click();
					}
					else MyUtility.printLog("��ư 1 �����ȵ�"); //�̿Ϸ� ��ư1�� ��ã�Ұ� �Ϸ��ư�� ��ã�Ҵٸ� ��¿��������
				} else {
					MyUtility.printLog("��� ��ư�� ã�ҽ��ϴ�. ��ũ�� ���� ��õ�"); 
				}
				
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private void script_btnMove(MyPos pos) {
		robot.mouseMove(pos.x+Main.minecraftRect.x, pos.y+Main.minecraftRect.y+5); 
		//MyUtility.printLog("move: "+pos.x + ", " + pos.y);
	}
	
	private void script_click() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(400);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		//MyUtility.printLog("click");
		Thread.sleep(100);	
	}

	
//	public ImagePos searchImgUsingThread(String image, int similar) {
//		long start = System.currentTimeMillis();
////		MyUtility.printLog("Start : " + start);
////		MyUtility.printLog(image);
//		
//		ArrayList<Future<ImagePos>> thList = new ArrayList<Future<ImagePos>>(5);
//		
//		try {
//			Callable<ImagePos> task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_0 ,Type.height_0, Type.width_h, Type.height_0 ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_h ,Type.height_0, Type.width_hh, Type.height_0 ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hh ,Type.height_0, Type.width_hhh, Type.height_0 ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hhh ,Type.height_0, Type.width_e, Type.height_0 ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//
//			//2��° ��
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_0 ,Type.height_h, Type.width_h, Type.height_hh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_h ,Type.height_h, Type.width_hh, Type.height_hh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hh ,Type.height_h, Type.width_hhh, Type.height_hh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hhh ,Type.height_h, Type.width_e, Type.height_hh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			//3���� ��
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_0 ,Type.height_hh, Type.width_h, Type.height_hhh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_h ,Type.height_hh, Type.width_hh, Type.height_hhh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hh ,Type.height_hh, Type.width_hhh, Type.height_hhh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hhh ,Type.height_hh, Type.width_e, Type.height_hhh ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//		
//			//4��°��
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_0 ,Type.height_hhh, Type.width_h, Type.height_e ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_h ,Type.height_hhh, Type.width_hh, Type.height_e ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hh ,Type.height_hhh, Type.width_hhh, Type.height_e ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//			
//			task = new Callable<ImagePos>() {
//	        	
//	        	public ImagePos call() throws Exception {
//	        		
//	        		ImagePos tmpPos = ImageScanner.getOnScreen(image, Type.width_hhh ,Type.height_hhh, Type.width_e, Type.height_e ,similar);
//	        		return tmpPos;
//	        	}
//	        	
//			};
//			thList.add(Main.threadPool.submit(task));
//
//	        
//			for(Future<ImagePos> th : thList) {
//				ImagePos imagePos = th.get();
//				if(imagePos == null) System.out.println("false");
//		        else return imagePos;
//			}
//	
//			long end = System.currentTimeMillis();
//			MyUtility.printLog("time : " + (end - start));
//			
//			return null;
//		}catch(Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}
}
