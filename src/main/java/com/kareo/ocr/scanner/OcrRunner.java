package com.kareo.ocr.scanner;

import com.kareo.ocr.scanner.helpers.OCRHelper;
import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.javacpp.opencv_core.*;

import java.awt.image.BufferedImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class OcrRunner {

    public static Mat runFilters(Mat mat) {

        System.out.println(mat.depth());

        //normalize(mat, mat, 0, 1., NORM_MINMAX, -1, null);
        //cvtColor(mat, mat, CV_BGR2GRAY);
        int H_MIN = 110;
        int H_MAX = 160;
        int S_MIN = 0;
        int S_MAX = 70;
        int V_MIN = 110;
        int V_MAX = 256;
        //inRange(mat, new Mat(new Scalar(0.0)), new Mat(new Scalar(50.0)), mat);
        //threshold(mat, mat, 190, 255, ADAPTIVE_THRESH_GAUSSIAN_C);
        mat = grayScaleBlurrImage(mat);
        //erode(mat, mat, new Mat());
        //dilate(mat, mat, new Mat());
        //GaussianBlur(mat, mat, mat.size(), 1, 1, 1);
        //Laplacian(mat, mat, mat.depth(), 1, 3, 0, BORDER_DEFAULT);

        return mat;

    }

    public static String runOCR(Mat mat) {

        Tesseract instance = Tesseract.getInstance();
        String result = "";

        BufferedImage b = OCRHelper.matToBuf(mat);

        try {
            result = instance.doOCR(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Mat rotate(Mat mat) {

        return mat;
    }

    public static Mat removeShadows(Mat mat) {

        return mat;
    }

    public static Mat grayScaleBlurrImage(Mat mat) {
        IplImage srcImage = OCRHelper.MatToIpl(mat);
        IplImage destImage = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_8U, 1);
        cvCvtColor(srcImage, destImage, CV_BGR2GRAY);
        Mat grayImageMat = OCRHelper.iplToMat(destImage);
//        display(grayImageMat, "Intermediate result");
        GaussianBlur(grayImageMat, grayImageMat, new Size(5, 5), 0.0, 0.0, BORDER_DEFAULT);

        destImage = OCRHelper.MatToIpl(grayImageMat);

        cvErode(destImage, destImage);
        cvDilate(destImage, destImage);
//        display(destImage, "Gray Scale Mat");
        grayImageMat = OCRHelper.iplToMat(destImage);
        return grayImageMat;
    }

    public static void main(String[] args) {

        // Read an image.
        Mat mat = imread("data/unitedhealthcare-1.jpeg");
        //display(mat, "Input");

        //filter mat
        Mat filtered_mat = runFilters(mat);
        OCRHelper.display(filtered_mat, "After");



        //print string results
        String ocr_results = runOCR(filtered_mat);
        System.out.println("OCR: " + ocr_results);


        //String fileName = "/Users/chris.kim/sandbox/ocr-app/images/card-grayscale.jpg";
        //File f = new File(fileName);
        //imwrite(f.getAbsolutePath(), filtered_mat);
    }

}