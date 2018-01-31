package com.kareo.ocr.scanner.training.minimizer;
import com.kareo.ocr.scanner.helpers.Dictionary;
import com.kareo.ocr.scanner.helpers.OCRHelper;
import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.javacpp.opencv_core.*;

import java.awt.image.BufferedImage;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import com.kareo.ocr.scanner.helpers.OCRHelper;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.opencv_core.Mat;



public class OCRFunction {

    Dictionary dict = new Dictionary();
    Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
    Mat mat;
    Mat defaultMat;

    public OCRFunction(Mat mat){
        this.mat = mat;
        this.defaultMat = mat;
    }

    public double valueAt(double[] params) {

        this.mat = defaultMat.clone();
        FilterParams filterParams = new FilterParams();
        filterParams.thresh_lower = (int) params[0];
        filterParams.thresh_higher = (int) params[1];
        runFilters(filterParams);
        double loss = getLoss();

        return loss;

    }

    public Mat runFilters(FilterParams params) {

        //normalize(mat, mat, 0, 1., NORM_MINMAX, -1, null);
        cvtColor(mat, mat, CV_BGR2GRAY);
        //inRange(mat, new Mat(new Scalar(0.0)), new Mat(new Scalar(50.0)), mat);
        threshold(mat, mat, params.thresh_lower, params.thresh_higher, ADAPTIVE_THRESH_GAUSSIAN_C);
        //mat = grayScaleBlurrImage(mat);
        //erode(mat, mat, new Mat());
        //dilate(mat, mat, new Mat());
        //GaussianBlur(mat, mat, mat.size(), 1, 1, 1);
        //Laplacian(mat, mat, mat.depth(), 1, 3, 0, BORDER_DEFAULT);
        return mat;
    }

    public double getLoss() {

        try {
            double loss = 0;
            String result = instance.doOCR(OCRHelper.matToBuf(this.mat));
            //System.out.println("Read Document: " + result);
            Dictionary.WordResults wordResults = dict.validWords(result);
            loss -= wordResults.valid_words.size();
            loss -= (double) wordResults.invalid_words.size()/ 1000.0;


            return loss;

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        return 0.0;
    }
    public Mat getMat() {
        return mat;
    }

    public Mat getDefaultMat() {
        return defaultMat;
    }

    public static void main(String[] args) {

        // Read an image.
        Mat mat = imread("data/unitedhealthcare-1.jpeg");
        //display(mat, "Input");

        //filter mat
        OCRFunction function = new OCRFunction(mat);
        Mat filtered_mat = function.runFilters(new FilterParams());
        OCRHelper.display(filtered_mat, "After");



        //print string results
        double loss = function.getLoss();
        System.out.println("OCR Loss: " + loss);


        //String fileName = "/Users/chris.kim/sandbox/ocr-app/images/card-grayscale.jpg";
        //File f = new File(fileName);
        //imwrite(f.getAbsolutePath(), filtered_mat);
    }



}
