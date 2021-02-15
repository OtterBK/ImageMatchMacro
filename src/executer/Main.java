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
	
	public static ExecutorService threadPool; //여러개의 스레드를 효율적으로 관리
	
	public static final File resourcesFolder = new File("resources");
	public static final File macroImgFolder = new File(resourcesFolder+"/macroImg");
	public static final File compareFolder = new File(resourcesFolder+"/compareImg");
	public static final File iconFolder = new File(resourcesFolder+"/icon");
	public static final File dllFolder = new File("dll");
	public static Script script = null;
	public static final String title = "마인팜용 매크로 0.63v";
	public static Rectangle minecraftRect = null;
	
	public static void main(String[] args) {

		mainFrame = new MainGUI();
		
		if(!Main.compareFolder.exists()) Main.compareFolder.mkdir();
		
//		boolean is32 = false; //빌드할때만 바꾸면댐
//		if(is32) { //32비트
//			try {
//				System.load(dllFolder.getAbsolutePath() + "\\opencv_java451_32.dll");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else { //64비트
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
		
		threadPool = Executors.newCachedThreadPool(); //쓰레드풀 초기화
		
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
				new CheckGUI(Main.mainFrame, "마인크래프트가 감지되지 않았습니다.", false, false);
				return false;
			} else {
				if(rect.x < 0 || rect.y < 0) {
					new CheckGUI(Main.mainFrame, "마인크래프트 창을 활성화 시켜주세요.", false, false);	
					return false;
				}
				Main.minecraftRect = MyUtility.getMineCraft();
				MyUtility.printLog("마인크래프트 감지됨. 위치 "+rect.toString());
				MyUtility.printLog("절대 마인크래프트 창을 옮기지 마세요.");
			}
			int similar = Integer.parseInt(mainFrame.tf_rgbSimilar.getText());	
			if(Main.mainFrame.autoAfk.isSelected()) { //잠광 매크로면
				MyUtility.printLog("잠광 매크로를 실행합니다.");
				script = new AfkScriptIgnoreShielder(similar);
			} else if(Main.mainFrame.autoFishing.isSelected()) { //낚시 매크로면
				MyUtility.printLog("낚시 매크로를 실행합니다.");
				AutoFishingScript tmpScript = new AutoFishingScript(similar);
				if(Main.mainFrame.ts_isStore.isActivated()) {
					tmpScript.isStore = true;
					try {
						int storeInterval = Integer.parseInt(mainFrame.tf_storeInterval.getText());
						tmpScript.storeInterval = storeInterval;
					}catch (NumberFormatException e) {
						new CheckGUI(Main.mainFrame, "창고 저장 간격은 숫자만 입력하세요.", false, false);
					}
				}
				script = tmpScript;
			} else if(Main.mainFrame.itemTransfer.isSelected()) { //운송 매크로면
				MyUtility.printLog("운송 매크로를 실행합니다.");				
				MyUtility.printLog("컨트롤 + F3 -> 2줄만 옮기기");
				MyUtility.printLog("컨트롤 + F4 -> 인벤에서 상자로 아이템 옮기기");
				MyUtility.printLog("컨트롤 + F5 -> 상자에서 인벤으로 아이템 옮기기");		
				MyUtility.printLog("컨트롤 + F6 -> 자동 판매");
				ItemTranferScript tmpScript = new ItemTranferScript(similar);
				script = tmpScript;
			}
			return true;
		}catch(NumberFormatException e) {
			e.printStackTrace();
			new CheckGUI(Main.mainFrame, "RGB 오차값은 숫자만 입력하세요.", false, false);
		}
		return false;

	}
}

