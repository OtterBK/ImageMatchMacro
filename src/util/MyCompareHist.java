package util;

import java.util.Arrays;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Range;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
public class MyCompareHist {
	
    public static double compare(String file1, String file2) {
    	//�̹��� �ε�
    	System.out.println("img1: "+file1);
    	System.out.println("img2: "+file2);
    	
        Mat srcBase = Imgcodecs.imread(file1);
        Mat srcTest1 = Imgcodecs.imread(file2);
        if (srcBase.empty()) {
            System.err.println("Cannot read the baseImage");
            System.exit(0);
        }
        if( srcTest1.empty()) {
        	System.err.println("Cannot read the compImage");
            System.exit(0);
        }
        
        
        //HSV �������� ����
        Mat hsvBase = new Mat(), hsvTest1 = new Mat(), hsvTest2 = new Mat();
        Imgproc.cvtColor( srcBase, hsvBase, Imgproc.COLOR_BGR2HSV );
        Imgproc.cvtColor( srcTest1, hsvTest1, Imgproc.COLOR_BGR2HSV );
        
        //�ʱ�ȭ
        int hBins = 50, sBins = 60;
        int[] histSize = { hBins, sBins };
        // hue varies from 0 to 179, saturation from 0 to 255
        float[] ranges = { 0, 180, 0, 256 };
        // Use the 0-th and 1-st channels
        int[] channels = { 0, 1 };
        
        //�⺻ �̹���, 2 ���� �׽�Ʈ �̹��� �� ���� �Ʒ��� �⺻ �̹����� ���� ������׷��� ����մϴ�.
        Mat histBase = new Mat(), histTest1 = new Mat();
        List<Mat> hsvBaseList = Arrays.asList(hsvBase);
        Imgproc.calcHist(hsvBaseList, new MatOfInt(channels), new Mat(), histBase, new MatOfInt(histSize), new MatOfFloat(ranges), false);
        Core.normalize(histBase, histBase, 0, 1, Core.NORM_MINMAX);
        List<Mat> hsvTest1List = Arrays.asList(hsvTest1);
        Imgproc.calcHist(hsvTest1List, new MatOfInt(channels), new Mat(), histTest1, new MatOfInt(histSize), new MatOfFloat(ranges), false);
        Core.normalize(histTest1, histTest1, 0, 1, Core.NORM_MINMAX);
        
        //�⺻ �̹����� ������׷��� �ٸ� ������׷� ���̿� 4���� �� ����� ���������� ����
        double result = Imgproc.compareHist( histBase, histTest1, 0 );
        return result;
    }
    
}
