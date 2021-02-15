package util;

import org.opencv.core.Core;

public class OpenCVImageSearch {

	public OpenCVImageSearch() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static int compareHistogram(String fileName1, String fileName2) {
		int retVal = 0;
		
		long startTime = System.currentTimeMillis();
		
		//Mat img = Imgcodecs.imread(fileName1, Imgcodecs.CV)
		return 0;
	}
	
}
