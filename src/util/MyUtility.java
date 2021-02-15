package util;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;

import addon.MyColor;
import executer.Main;
import script.Script;

public class MyUtility {

	public static void printLog(String log) {
		Main.mainFrame.printLog(log);
	}
	
	public static void capture() {
		try {
			// 전체 화면 Capture
			Robot robot = new Robot();
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

			// PNG저장.
			File file = new File(
					Main.compareFolder+"\\screenshot.png");
			ImageIO.write(screenFullImage, "png", file);
			MyUtility.printLog("full screen saved");
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void captureWindow2() {
		
		try {
			Robot robot = new Robot(); //현재 창 캡쳐
			robot.keyPress(KeyEvent.VK_PRINTSCREEN);
			robot.keyRelease(KeyEvent.VK_PRINTSCREEN);

			// PNG저장.

			
			File file = new File(Main.compareFolder + "\\screenshot.png");
			String outputfile = file.getAbsolutePath();

			Thread.sleep(500);
			copyTo(outputfile); //이미지 저장
			//MyUtility.printLog("saved");
		}catch (Exception e) {
			MyUtility.printLog(e.toString());
		}
		


	}
	
	public static void captureActiveWindow() {
		
		try {
			Robot robot = new Robot(); //현재 창 캡쳐
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_PRINTSCREEN);
			robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
			robot.keyRelease(KeyEvent.VK_ALT);

			// PNG저장.

			
			File file = new File(Main.compareFolder + "\\screenshot.png");
			String outputfile = file.getAbsolutePath();

			Thread.sleep(200);
			copyTo(outputfile); //이미지 저장
			MyUtility.printLog("saved");
		}catch (Exception e) {
			MyUtility.printLog(e.toString());
		}
		


	}



    static int copyTo(String filename) throws Exception {
        Transferable content = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (content == null) {
            System.err.println("error: nothing found in clipboard");
            return 1;
        }
        if (!content.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            System.err.println("error: no image found in clipbaord");
            return 2;
        }
        BufferedImage img = (BufferedImage) content.getTransferData(DataFlavor.imageFlavor);
        String ext = ext(filename);
        if (ext == null) {
            ext = "png";
            filename += "." + ext;
        }
        File outfile = new File(filename);
        ImageIO.write(img, ext, outfile);
        System.err.println("image copied to: " + outfile.getAbsolutePath());
        return 0;
    }

    static String ext(String filename) {
        int pos = filename.lastIndexOf('.') + 1;
        if (pos == 0 || pos >= filename.length()) return null;
        return filename.substring(pos);
    }
    

    public static Rectangle getMineCraft() {
    	long start = System.currentTimeMillis();
    	Rectangle rect = null;
    	for (DesktopWindow desktopWindow : WindowUtils.getAllWindows(true)) {
    	    if (desktopWindow.getTitle().contains("Minecraft")) {
    	         rect = desktopWindow.getLocAndSize();
    	    }
    	}
    	long end = System.currentTimeMillis();
    	MyUtility.printLog("찾는시간 : " + (end - start));
    	return rect;
    
    }
    
    public static void chestToInv() throws InterruptedException {
    	MyUtility.printLog("큰상자 -> 인벤 매크로 실행");
		PointerInfo mPointer = MouseInfo.getPointerInfo();			
		
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(10);
		Main.robot.keyPress(KeyEvent.VK_SHIFT); //쉬프트 눌러
		int pixelGap = 36; //각 인벤칸 간격
		for(int j = 0; j < 6; j++) {
			MyPos tmpPos = new MyPos(mPointer.getLocation().x, mPointer.getLocation().y);
			for(int i = 0; i < 9; i++) {
				Main.robot.mouseMove(tmpPos.x + (pixelGap * i), tmpPos.y + (pixelGap * j));
				Thread.sleep(50);
				Main.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				Thread.sleep(20);
				Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				Thread.sleep(20);
			}
		}
		Thread.sleep(10);
		Main.robot.keyRelease(KeyEvent.VK_SHIFT); //다하면 쉬프트 때
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
    }
    
    public static void invToChest() throws InterruptedException {
    	MyUtility.printLog("인벤 -> 큰상자 매크로 실행");
		PointerInfo mPointer = MouseInfo.getPointerInfo();			
		
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(10);
		Main.robot.keyPress(KeyEvent.VK_SHIFT); //쉬프트 눌러
		int pixelGap = 36; //각 인벤칸 간격
		for(int j = 0; j < 4; j++) {
			MyPos tmpPos = new MyPos(mPointer.getLocation().x, mPointer.getLocation().y);
			for(int i = 0; i < 9; i++) {
				Main.robot.mouseMove(tmpPos.x + (pixelGap * i), tmpPos.y + (pixelGap * j) + (j==3 ? 10 : 0));
				Thread.sleep(50);
				Main.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				Thread.sleep(20);
				Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				Thread.sleep(20);
			}
		}
		Thread.sleep(10);
		Main.robot.keyRelease(KeyEvent.VK_SHIFT); //다하면 쉬프트 때
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
    }
    
    public static void lineMacro(int line) throws InterruptedException {
    	MyUtility.printLog(line+"줄 매크로 실행");
		PointerInfo mPointer = MouseInfo.getPointerInfo();			
		
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(10);
		Main.robot.keyPress(KeyEvent.VK_SHIFT); //쉬프트 눌러
		int pixelGap = 36; //각 인벤칸 간격
		for(int j = 0; j < line; j++) {
			MyPos tmpPos = new MyPos(mPointer.getLocation().x, mPointer.getLocation().y);
			for(int i = 0; i < 9; i++) {
				Main.robot.mouseMove(tmpPos.x + (pixelGap * i), tmpPos.y + (pixelGap * j));
				Thread.sleep(50);
				Main.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				Thread.sleep(20);
				Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				Thread.sleep(20);
			}
		}
		Thread.sleep(10);
		Main.robot.keyRelease(KeyEvent.VK_SHIFT); //다하면 쉬프트 때
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
    }
 
    
    public static Rectangle getFullscreen() {
    	return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }
    
    
    public static String lineSpacing(String text, int stand) { //stand 값 기준으로 문자열 개행
		String str = text; //개행 작업을 위해 임시로 str에 text 복사
		if(text.length() > stand) { //문자열의 길이가 1줄 제한문자수를 초과했다면
			List<String> tmpStr = new ArrayList<>(); //개행 작업을 위한 문자열 list 선언
			int i = 0; //문자열 추출 작업을 위한 변수
			for(; i < text.length() - stand; i += stand) { //개행 기호를 붙일 문자열 개수만큼 반복
				int subStart = i; //추출 시작지점
				int subEnd = subStart + stand; //추출 끝지점
				tmpStr.add(text.substring(subStart, subEnd)); //추출한 문자열을 tmpStr에 추가
			}
			tmpStr.add(text.substring(i, text.length())); //남은 문자열 추출
			str = ""; //str 초기화
			for(i = 0; i < tmpStr.size() - 1; i++) //tmpStr에 들어있는 문자열중 마지막 문자열을 제외한 값에 개행 처리
				str += tmpStr.get(i) + "<br>"; //개행 기호 추가
			str += tmpStr.get(i); //마지막 문자열은 추가할 필요없음
		}
		str = str.replaceAll("(\r|\n|\r\n|\n\r)","<br>");//개행기호변환
		return str = "<html><body>" + str + "</body></html>"; //값 반환
	}
    
	//이미지 크기 변경
	public static ImageIcon resizeImage(ImageIcon baseIcon, int newWidth, int newHeight) {
		Image tmpImage = baseIcon.getImage();
		Image chgImage = tmpImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		ImageIcon chgIcon = new ImageIcon(chgImage);
		return chgIcon;
	}
	
	private static Icon emptyIcon;
	   private static Icon selectedIcon;

	   // create our Circle ImageIcons
	   public static ImageIcon getEmptyIcon(int BI_WIDTH, Color borderColor){
	      // first the empty circle
	      BufferedImage img = new BufferedImage(BI_WIDTH, BI_WIDTH, BufferedImage.TYPE_INT_ARGB);
	      Graphics2D g2 = img.createGraphics();
	      g2.setStroke(new BasicStroke(2f));
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      int x = 4;
	      int y = x;
	      int width = BI_WIDTH - 2 * x;
	      int height = width;
	      g2.setColor(borderColor);
	      g2.drawOval(x, y, width, height);
	      g2.dispose();

	      return new ImageIcon(img);
	   }
	   
	   public static ImageIcon getSelectedIcon(int BI_WIDTH, Color fillColor, Color borderColor) {
		   
	      // next the filled circle
		  BufferedImage img = new BufferedImage(BI_WIDTH, BI_WIDTH, BufferedImage.TYPE_INT_ARGB);
		  Graphics2D g2 = img.createGraphics();
	      g2.setStroke(new BasicStroke(2f));
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	      int x = 4;
	      int y = x;
	      int width = BI_WIDTH - 2 * x;
	      int height = width;
	      
	      g2.setColor(fillColor);
	      g2.fillOval(x, y, width, height);
	      g2.setColor(borderColor);
	      g2.drawOval(x, y, width, height);
	      g2.dispose();

	      return new ImageIcon(img);
	   }

}
