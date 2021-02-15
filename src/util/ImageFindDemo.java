package util;
 
import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 13  * ȭ�� ���� ������ �׸� ã��
 14  * @author Jeby Sun
 15  * @date 2014-09-13
 16  * @website http://www.jebysun.com
  */
 public class ImageFindDemo {
     
     BufferedImage screenShotImage;    //ȭ�� ĸó
     BufferedImage keyImage;           //�˻� ��� �׸�
     Image imagekey;
 
     int scrShotImgWidth;              //��ũ���� �ʺ�
     int scrShotImgHeight;             //��ũ���� ����
   
      int keyImgWidth;                  //�˻� ��� �̹��� �ʺ�
      int keyImgHeight;                 //��ǥ �̹��� ���� ã��
      
      int[][] screenShotImageRGBData;   //ȭ�� ĸó RGB ������
      int[][] keyImageRGBData;          //�˻� ��� �׸� RGB ������
      
      int[][][] findImgData;            //�˻� ��� ��ǥ ������ ȭ�� ĸó ���� ��ǥ ������ 
      
      
      public ImageFindDemo()  {
          screenShotImage = this.getFullScreenShot();
          //imagekey = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("DPIcon.jpg"));
         
          
          ImageObserver obserb = new ImageObserver() {
            
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                // TODO Auto-generated method stub
                return false;
            }
        };
          try {
            keyImage = ImageIO.read(new File("macro.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          screenShotImageRGBData = this.getImageGRB(screenShotImage);
          keyImageRGBData = this.getImageGRB(keyImage);
          scrShotImgWidth = screenShotImage.getWidth();
          scrShotImgHeight = screenShotImage.getHeight();
          keyImgWidth = keyImage.getWidth();
          keyImgHeight = keyImage.getHeight();
          
          //ã�� �����ߴ�
          this.findImage();
          
      }
      
      /**
       * ��ü ȭ�� ĸó
       * @return ���� BufferedImage
       */
      public BufferedImage getFullScreenShot() {
          BufferedImage bfImage = null;
          int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
          int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
          try {
              Robot robot = new Robot();
              bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
          } catch (AWTException e) {
              e.printStackTrace();
          }
          return bfImage;
     }
      
      /**
       * ���� ���� �б� ��ǥ �̹���
       * @param keyImagePath - �׸� ���� ���
       * @return ���� �׸� BufferedImage ���
       */
      public BufferedImage getBfImageFromPath(String keyImagePath) {
          BufferedImage bfImage = null;
          try {
              bfImage = ImageIO.read(new File(keyImagePath));
          } catch (IOException e) {
              e.printStackTrace();
          }
          return bfImage;
      }
      
      /**
       * �ٰ� BufferedImage �׸� RGB �迭 ��������
       * @param bfImage
       * @return
       */
      public int[][] getImageGRB(BufferedImage bfImage) {
          int width = bfImage.getWidth();
          int height = bfImage.getHeight();
          int[][] result = new int[height][width];
          for (int h = 0; h <height; h++) {
              for (int w = 0; w <width; w++) {
                  //��� getRGB (w, h) �������� �� �� ���� ��, ARGB �ƴ� ���� ���α׷� �� ���, RGB. �׷��� �ٺ�ȭ�� �ʿ��ϴ� ARGB ��ȯ�� ���� RGB, �� bufImg.getRGB(w, h) & 0xFFFFFF. 
                  result[h][w] = bfImage.getRGB(w, h) & 0xFFFFFF;
              }
         }
          return result;
      }
      
     
     /**
      * �׸� ã��
      */
     public void findImage() {
         findImgData = new int[keyImgHeight][keyImgWidth][2];
         //ȭ�� ĸó �ȼ� �� ������ ���̸� �Űܴٴϱ�
         for(int y=0; y<scrShotImgHeight-keyImgHeight; y++) {
             for(int x=0; x<scrShotImgWidth-keyImgWidth; x++) {
                 //��� �׸� ũ�� �ٰ��Ͽ� ���� ��ǥ �� �簢 �� ȭ�� ���� �� �� ����, 
                 //�Ǵ� ��ũ���� ���� ���� �� �� �� �� �׸� B �� �� �ȼ� ���� ���� �ִ��� ��, 
                 //���� ���� ������ ȭ�� ���� �� ���� ���� ��� �� ��ǥ �� ��� �� ���� ����. 
                 if((keyImageRGBData[0][0]^screenShotImageRGBData[y][x])==0
                         && (keyImageRGBData[0][keyImgWidth-1]^screenShotImageRGBData[y][x+keyImgWidth-1])==0
                         && (keyImageRGBData[keyImgHeight-1][keyImgWidth-1]^screenShotImageRGBData[y+keyImgHeight-1][x+keyImgWidth-1])==0
                         && (keyImageRGBData[keyImgHeight-1][0]^screenShotImageRGBData[y+keyImgHeight-1][x])==0) {
                     
                     boolean isFinded = isMatchAll(y, x);
                     //���� ���� �ᱹ ������ ���� ������ ������ �׸� ã�� ������ ���� �� ��ġ ���� ��ǥ ������ �˻� ��� �迭. 
                     if(isFinded) {
                         for(int h=0; h<keyImgHeight; h++) {
                             for(int w=0; w<keyImgWidth; w++) {
                                 findImgData[h][w][0] = y+h; 
                                 findImgData[h][w][1] = x+w;
                             }
                         }
                         return;
                     }
                 }
             }
         }
     }
     
     /**
      * �Ǵ� ȭ�� ���� ��ǥ �� �� ���� ���� ��� ���� �� ��, ���� �׸� �� ������ ����. 
      * @param y - ��ǥ �׸� ���� �� �ȼ� �� �ϰ� ��ġ�ϴ� ȭ�� y ��ǥ
      * @param x - ��ǥ �׸� ���� �� �ȼ� �� �ϰ� ��ġ�ϴ� ȭ�� x ��ǥ
      * @return 
      */
     public boolean isMatchAll(int y, int x) {
         int biggerY = 0;
         int biggerX = 0;
         int xor = 0;
         for(int smallerY=0; smallerY<keyImgHeight; smallerY++) {
             biggerY = y+smallerY;
             for(int smallerX=0; smallerX<keyImgWidth; smallerX++) {
                 biggerX = x+smallerX;
                 if(biggerY>=scrShotImgHeight || biggerX>=scrShotImgWidth) {
                     return false;
                 }
                 xor = keyImageRGBData[smallerY][smallerX]^screenShotImageRGBData[biggerY][biggerX];
                 if(xor!=0) {
                     return false;
                 }
             }
             biggerX = x;
         }
         return true;
     }
     
     /**
      * ��� �˻� �� ��ǥ ������
      */
     private void printFindData() {
         for(int y=0; y<keyImgHeight; y++) {
             for(int x=0; x<keyImgWidth; x++) {
                 System.out.print("("+this.findImgData[y][x][0]+", "+this.findImgData[y][x][1]+")");
             }
             System.out.println();
         }
     }
 
     
     public static void main(String[] args) {
         ImageFindDemo demo = new ImageFindDemo();
         demo.printFindData();
     }
 
}
 
