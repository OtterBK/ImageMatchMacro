package executer;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jnativehook.GlobalScreen;

import gui.CheckGUI;
import gui.MainGUI;
import script.AfkScriptIgnoreShielder;
import script.AutoFishingScript;
import script.ItemTranferScript;
import script.Script;
import util.MyUtility;

public class Main {
	
	public static Robot robot;
	public static MainGUI mainFrame;
	
	public static ExecutorService threadPool; //�������� �����带 ȿ�������� ����
	
	public static final File resourcesFolder = new File("resources");
	public static final File macroImgFolder = new File(resourcesFolder+"/macroImg");
	public static final File compareFolder = new File(resourcesFolder+"/compareImg");
	public static final File iconFolder = new File(resourcesFolder+"/icon");
	public static final File dllFolder = new File("dll");
	public static Script script = null;
	public static final String title = "�����ʿ� ��ũ�� 0.63v";
	public static Rectangle minecraftRect = null;
	
	public static void main(String[] args) {

		mainFrame = new MainGUI();
		
		if(!Main.compareFolder.exists()) Main.compareFolder.mkdir();
		
//		boolean is32 = false; //�����Ҷ��� �ٲٸ��
//		if(is32) { //32��Ʈ
//			try {
//				System.load(dllFolder.getAbsolutePath() + "\\opencv_java451_32.dll");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else { //64��Ʈ
//			try {
//				System.load(dllFolder.getAbsolutePath() + "\\opencv_java451_64.dll");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}


		
//		URL src = Main.class.getResource("/img/base.jpg");
//		String baseFile = src.getFile();
//		
//		src = Main.class.getResource("/img/comp1.jpg");
//		String compFile1 = src.getFile();
//		
//		src = Main.class.getResource("/img/comp2.jpg");
//		String compFile2 = src.getFile();

		MyUtility.printLog(title);
		MyUtility.printLog(macroImgFolder.getAbsolutePath());
		MyUtility.printLog(compareFolder.getAbsolutePath());
//		System.out.println(imgFolder.getAbsolutePath());
//		
		
		threadPool = Executors.newCachedThreadPool(); //������Ǯ �ʱ�ȭ
		
//		String files[] = {
//		                  imgFolder+"\\macro.png",
//		                  imgFolder+"\\macro2.png",
//		                  imgFolder+"\\macro3.png",
//		                 
//						};
//		
//        new CompareHist().run(files);

		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean excuteScript() {
		if(script != null) {
			script.stopFlag = true;
			if(script.keyListener != null) GlobalScreen.removeNativeKeyListener(script.keyListener);
			if(script.mouseListener != null) GlobalScreen.removeNativeMouseListener(script.mouseListener);
		}
		try {
			Rectangle rect = MyUtility.getMineCraft();
			if(rect == null) {
				new CheckGUI(Main.mainFrame, "����ũ����Ʈ�� �������� �ʾҽ��ϴ�.", false, false);
				return false;
			} else {
				if(rect.x < 0 || rect.y < 0) {
					new CheckGUI(Main.mainFrame, "����ũ����Ʈ â�� Ȱ��ȭ �����ּ���.", false, false);	
					return false;
				}
				Main.minecraftRect = MyUtility.getMineCraft();
				MyUtility.printLog("����ũ����Ʈ ������. ��ġ "+rect.toString());
				MyUtility.printLog("���� ����ũ����Ʈ â�� �ű��� ������.");
			}
			int similar = Integer.parseInt(mainFrame.tf_rgbSimilar.getText());	
			if(Main.mainFrame.autoAfk.isSelected()) { //�ᱤ ��ũ�θ�
				MyUtility.printLog("�ᱤ ��ũ�θ� �����մϴ�.");
				script = new AfkScriptIgnoreShielder(similar);
			} else if(Main.mainFrame.autoFishing.isSelected()) { //���� ��ũ�θ�
				MyUtility.printLog("���� ��ũ�θ� �����մϴ�.");
				AutoFishingScript tmpScript = new AutoFishingScript(similar);
				if(Main.mainFrame.ts_isStore.isActivated()) {
					tmpScript.isStore = true;
					try {
						int storeInterval = Integer.parseInt(mainFrame.tf_storeInterval.getText());
						tmpScript.storeInterval = storeInterval;
					}catch (NumberFormatException e) {
						new CheckGUI(Main.mainFrame, "â�� ���� ������ ���ڸ� �Է��ϼ���.", false, false);
					}
				}
				script = tmpScript;
			} else if(Main.mainFrame.itemTransfer.isSelected()) { //��� ��ũ�θ�
				MyUtility.printLog("��� ��ũ�θ� �����մϴ�.");				
				MyUtility.printLog("��Ʈ�� + F3 -> 2�ٸ� �ű��");
				MyUtility.printLog("��Ʈ�� + F4 -> �κ����� ���ڷ� ������ �ű��");
				MyUtility.printLog("��Ʈ�� + F5 -> ���ڿ��� �κ����� ������ �ű��");		
				MyUtility.printLog("��Ʈ�� + F6 -> �ڵ� �Ǹ�");
				ItemTranferScript tmpScript = new ItemTranferScript(similar);
				script = tmpScript;
			}
			return true;
		}catch(NumberFormatException e) {
			e.printStackTrace();
			new CheckGUI(Main.mainFrame, "RGB �������� ���ڸ� �Է��ϼ���.", false, false);
		}
		return false;

	}
}

