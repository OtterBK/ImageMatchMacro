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
			// ��ü ȭ�� Capture
			Robot robot = new Robot();
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

			// PNG����.
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
			Robot robot = new Robot(); //���� â ĸ��
			robot.keyPress(KeyEvent.VK_PRINTSCREEN);
			robot.keyRelease(KeyEvent.VK_PRINTSCREEN);

			// PNG����.

			
			File file = new File(Main.compareFolder + "\\screenshot.png");
			String outputfile = file.getAbsolutePath();

			Thread.sleep(500);
			copyTo(outputfile); //�̹��� ����
			//MyUtility.printLog("saved");
		}catch (Exception e) {
			MyUtility.printLog(e.toString());
		}
		


	}
	
	public static void captureActiveWindow() {
		
		try {
			Robot robot = new Robot(); //���� â ĸ��
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_PRINTSCREEN);
			robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
			robot.keyRelease(KeyEvent.VK_ALT);

			// PNG����.

			
			File file = new File(Main.compareFolder + "\\screenshot.png");
			String outputfile = file.getAbsolutePath();

			Thread.sleep(200);
			copyTo(outputfile); //�̹��� ����
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
    	MyUtility.printLog("ã�½ð� : " + (end - start));
    	return rect;
    
    }
    
    public static void chestToInv() throws InterruptedException {
    	MyUtility.printLog("ū���� -> �κ� ��ũ�� ����");
		PointerInfo mPointer = MouseInfo.getPointerInfo();			
		
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(10);
		Main.robot.keyPress(KeyEvent.VK_SHIFT); //����Ʈ ����
		int pixelGap = 36; //�� �κ�ĭ ����
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
		Main.robot.keyRelease(KeyEvent.VK_SHIFT); //���ϸ� ����Ʈ ��
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
    }
    
    public static void invToChest() throws InterruptedException {
    	MyUtility.printLog("�κ� -> ū���� ��ũ�� ����");
		PointerInfo mPointer = MouseInfo.getPointerInfo();			
		
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(10);
		Main.robot.keyPress(KeyEvent.VK_SHIFT); //����Ʈ ����
		int pixelGap = 36; //�� �κ�ĭ ����
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
		Main.robot.keyRelease(KeyEvent.VK_SHIFT); //���ϸ� ����Ʈ ��
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
    }
    
    public static void lineMacro(int line) throws InterruptedException {
    	MyUtility.printLog(line+"�� ��ũ�� ����");
		PointerInfo mPointer = MouseInfo.getPointerInfo();			
		
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(10);
		Main.robot.keyPress(KeyEvent.VK_SHIFT); //����Ʈ ����
		int pixelGap = 36; //�� �κ�ĭ ����
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
		Main.robot.keyRelease(KeyEvent.VK_SHIFT); //���ϸ� ����Ʈ ��
		Main.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); 
		Main.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
    }
 
    
    public static Rectangle getFullscreen() {
    	return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }
    
    
    public static String lineSpacing(String text, int stand) { //stand �� �������� ���ڿ� ����
		String str = text; //���� �۾��� ���� �ӽ÷� str�� text ����
		if(text.length() > stand) { //���ڿ��� ���̰� 1�� ���ѹ��ڼ��� �ʰ��ߴٸ�
			List<String> tmpStr = new ArrayList<>(); //���� �۾��� ���� ���ڿ� list ����
			int i = 0; //���ڿ� ���� �۾��� ���� ����
			for(; i < text.length() - stand; i += stand) { //���� ��ȣ�� ���� ���ڿ� ������ŭ �ݺ�
				int subStart = i; //���� ��������
				int subEnd = subStart + stand; //���� ������
				tmpStr.add(text.substring(subStart, subEnd)); //������ ���ڿ��� tmpStr�� �߰�
			}
			tmpStr.add(text.substring(i, text.length())); //���� ���ڿ� ����
			str = ""; //str �ʱ�ȭ
			for(i = 0; i < tmpStr.size() - 1; i++) //tmpStr�� ����ִ� ���ڿ��� ������ ���ڿ��� ������ ���� ���� ó��
				str += tmpStr.get(i) + "<br>"; //���� ��ȣ �߰�
			str += tmpStr.get(i); //������ ���ڿ��� �߰��� �ʿ����
		}
		str = str.replaceAll("(\r|\n|\r\n|\n\r)","<br>");//�����ȣ��ȯ
		return str = "<html><body>" + str + "</body></html>"; //�� ��ȯ
	}
    
	//�̹��� ũ�� ����
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
