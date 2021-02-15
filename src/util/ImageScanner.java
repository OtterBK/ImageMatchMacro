package util;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import executer.Main;
 
public class ImageScanner {
 
	private static int alphaGap = 255; 
    
    public static ImagePos getOnScreen(String filePath, int similarGap){
    	int _alphaGap = alphaGap;
    	
    	//MyUtility.printLog("START : "+System.currentTimeMillis());
    	long start = System.currentTimeMillis();
    	BufferedImage bi = null;
        BufferedImage image = null;
        try {
        	bi = ImageIO.read(new File(filePath));
        	image = new Robot().createScreenCapture(Main.minecraftRect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //MyUtility.printLog(image.getHeight() * image.getWidth()+"픽셀");
        for(int x = 0; x< image.getWidth();x++){
            for(int y = 0; y< image.getHeight();y++){
                
                boolean invalid = false;
                ImagePos tmpPos = new ImagePos(); //좌표 저장용
                tmpPos.x = x;
                tmpPos.y = y;
                int k = x,l = y;
                for(int a = 0;a<bi.getWidth();a++){
                    l = y;
                    if(image.getWidth() - x < bi.getWidth() ||
                    		image.getHeight() - y < bi.getHeight()) {
                    	invalid = true;
                    	break;
                    }
                    	
                    for(int b = 0;b<bi.getHeight();b++){
                    	int biRGB = bi.getRGB(a, b);
                    	int igRGB = image.getRGB(k, l);
                    	
                    	int bi_A = (biRGB & 0xff000000) >>> 24; //A 가져오기(투명도)
                    	if(bi_A <= 0) {
                    		l++;
                    		continue;
                    	}
                    	else if(bi.getRGB(a, b) != image.getRGB(k, l)){ 
                        	 //오차값 적용
                        	
                        	int bi_R = (biRGB & 0x00ff0000) >>> 16; //R
                        	int bi_G = (biRGB & 0x0000ff00) >>> 8; //G
                        	int bi_B = biRGB & 0x000000ff; //B 가져오기
                        	
                        	
                        	int ig_A = (igRGB & 0xff000000) >>> 24;
                        	int ig_R = (igRGB & 0x00ff0000) >>> 16;
                        	int ig_G = (igRGB & 0x0000ff00) >>> 8;
                        	int ig_B = biRGB & 0x000000ff;
                        	if(Math.abs(bi_A - ig_A) > _alphaGap
                        			|| Math.abs(bi_R - ig_R) > similarGap
                        			|| Math.abs(bi_G - ig_G) > similarGap
                        			|| Math.abs(bi_B - ig_B) > similarGap) {
                                invalid = true;
                                break;
                        	} else {
                        		 l++;
                        	}
                        }
                        else{
                            l++;
                        }
                    }
                    if(invalid){
                        break;
                    }else{
                        k++;
                    }
                        
                }
                
                if(!invalid){
                	tmpPos.x += (int)bi.getWidth()/2;
                	tmpPos.y += (int)bi.getHeight()/2;
                	//MyUtility.printLog("END : "+System.currentTimeMillis());
                	long end = System.currentTimeMillis();
                	//MyUtility.printLog("TIME : "+(end - start));
                    return tmpPos;
                }
            }
			
			
        }
        //MyUtility.printLog("END : "+System.currentTimeMillis());
    	long end = System.currentTimeMillis();
    	//MyUtility.printLog("TIME : "+(end - start));
		
        return null; //If no image is found
        
    }
    
    public static ImagePos getOnScreen(String filePath, int startW, int startY, int searchW, int searchH, int similarGap){
    	
    	long start = System.currentTimeMillis();
    	long loopCnt = 0;
    	
    	if(startW < 0) startW = 0;
    	if(startY < 0) startY = 0;
    	if(startW + searchW >= Main.minecraftRect.width) searchW = (int) (Main.minecraftRect.width - startW -1);
    	if(startY + searchH >= Main.minecraftRect.height) searchH = (int) (Main.minecraftRect.height - startY -1);
    	
    	
    	int _alphaGap = alphaGap;
    	//MyUtility.printLog(startW + ", " + startY + ", " + (startW + searchW) + "," + (startY + searchH));
    	
    	BufferedImage bi = null;
        BufferedImage image = null;
        try {
        	bi = ImageIO.read(new File(filePath));
            image = new Robot().createScreenCapture(Main.minecraftRect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //MyUtility.printLog(searchW * searchH+"픽셀");
        for(int x = startW; x< startW+searchW ;x++){
            for(int y = startY; y< startY+searchH ;y++){
                
                boolean invalid = false;
                ImagePos tmpPos = new ImagePos(); //좌표 저장용
                tmpPos.x = x;
                tmpPos.y = y;
                int k = x,l = y;
                for(int a = 0;a<bi.getWidth();a++){
                    l = y;
                    if(image.getWidth() - x < bi.getWidth() ||
                    		image.getHeight() - y < bi.getHeight()) {
                    	invalid = true;
                    	break;
                    }
                    	
                    for(int b = 0;b<bi.getHeight();b++){
                    	loopCnt++;
                    	int biRGB = bi.getRGB(a, b);
                    	int igRGB = image.getRGB(k, l);
                    	
                    	int bi_A = (biRGB & 0xff000000) >>> 24; //A 가져오기(투명도)
                    	if(bi_A <= 0) {
                    		l++;
                    		continue;
                    	}
                    	else if(bi.getRGB(a, b) != image.getRGB(k, l)){ 
                        	 //오차값 적용
                        	
                        	int bi_R = (biRGB & 0x00ff0000) >>> 16; //R
                        	int bi_G = (biRGB & 0x0000ff00) >>> 8; //G
                        	int bi_B = biRGB & 0x000000ff; //B 가져오기
                        	
                        	
                        	int ig_A = (igRGB & 0xff000000) >>> 24;
                        	int ig_R = (igRGB & 0x00ff0000) >>> 16;
                        	int ig_G = (igRGB & 0x0000ff00) >>> 8;
                        	int ig_B = biRGB & 0x000000ff;
                        	if(Math.abs(bi_A - ig_A) > _alphaGap
                        			|| Math.abs(bi_R - ig_R) > similarGap
                        			|| Math.abs(bi_G - ig_G) > similarGap
                        			|| Math.abs(bi_B - ig_B) > similarGap) {
                                invalid = true;
                                break;
                        	} else {
                        		 l++;
                        	}
                        }
                        else{
                            l++;
                        }
                    }
                    if(invalid){
                        break;
                    }else{
                        k++;
                    }
                        
                }
                
                if(!invalid){
                	tmpPos.x += (int)bi.getWidth()/2;
                	tmpPos.y += (int)bi.getHeight()/2;
                    return tmpPos;
                }
            }
        }
        long end = System.currentTimeMillis();
        //MyUtility.printLog("time: " + (end - start));
       // MyUtility.printLog("loop: " + (loopCnt));
        return null; //If no image is found
    }
 
}