package com.kareo.ocr.scanner.filters;

import com.kareo.ocr.scanner.helpers.OCRHelper;
import org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_core.*;

import org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class SpecFilter {

    private static Mat specFilter(Mat mat){

        normalize(mat, mat, 0, 255, NORM_MINMAX, -1, null);
        //threshold(mat, mat, 0, 255, THRESH_OTSU);
        erode(mat, mat, new Mat());
        dilate(mat, mat, new Mat(), new Point(0, 0), 9, BORDER_CONSTANT, new Scalar(1));
        return mat;
    };

    public static void main(String[] args) {

        try {
            // Read an image.
            Mat img =  OCRHelper.readImage("data/unitedhealthcare-1.jpeg");
            Mat deSpeckled = specFilter(img);
            OCRHelper.display(img, "Before");

            OCRHelper.display(deSpeckled, "After");
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
