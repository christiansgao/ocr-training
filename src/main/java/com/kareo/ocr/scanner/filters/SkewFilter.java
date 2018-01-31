package com.kareo.ocr.scanner.filters;

import com.kareo.ocr.scanner.helpers.OCRHelper;
import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * Created by christian.gao on 1/30/18.
 */
public class SkewFilter {

    private static double findskew(Mat mat){

        return 0.0;
    };

    private static Mat skewFilter(Mat mat){

        return mat;
    };

    public static void main(String[] args) {

        try {
            // Read an image.
            Mat img =  OCRHelper.readImage("data/ReallyHardInsurance.jpeg");
            double skewness = findskew(img);
            Mat deskewed = skewFilter(img);
            System.out.println("skew: " + skewness);
            OCRHelper.display(img, "Before");

            OCRHelper.display(deskewed, "After");
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
