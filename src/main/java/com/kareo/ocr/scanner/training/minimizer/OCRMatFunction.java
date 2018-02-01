package com.kareo.ocr.scanner.training.minimizer;

import com.kareo.ocr.scanner.helpers.Dictionary;
import com.kareo.ocr.scanner.helpers.OCRHelper;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class OCRMatFunction {

    Dictionary dict = new Dictionary();
    Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
    FilterModel model;

    public OCRMatFunction(FilterModel model){
        this.model = model;
    }

    public double valueAt(Mat mat, double[] params) {

        FilterModel filterModel = new FilterModel(params);
        runFilters(mat);
        return getLoss(mat);

    }

    public void runFilters(Mat mat) {

        //normalize(mat, mat, 0, 1., NORM_MINMAX, -1, null);
        cvtColor(mat, mat, CV_BGR2GRAY);
        //inRange(mat, new Mat(new Scalar(0.0)), new Mat(new Scalar(50.0)), mat);
        threshold(mat, mat, model.thresh_lower, model.thresh_higher, ADAPTIVE_THRESH_GAUSSIAN_C);
        //mat = grayScaleBlurrImage(mat);
        //erode(mat, mat, new Mat());
        //dilate(mat, mat, new Mat());
        //GaussianBlur(mat, mat, mat.size(), 1, 1, 1);
        //Laplacian(mat, mat, mat.depth(), 1, 3, 0, BORDER_DEFAULT);
        //return mat;
    }

    public String getOCR(Mat mat){
        try {
            runFilters(mat);
            String result = instance.doOCR(OCRHelper.matToBuf(mat));

            return result;
        } catch(Exception e){
            e.printStackTrace();
        }
        return "Error in Ocr";
    }

    public double getLoss(Mat mat) {
        try {
            double loss = 0;

            String result = instance.doOCR(OCRHelper.matToBuf(mat));
            //System.out.println("Read Document: " + result);
            Dictionary.WordResults wordResults = dict.validWords(result);
            loss -= wordResults.valid_words.size();
            loss -= (double) wordResults.invalid_words.size() / 1000.0;

            loss = -Math.pow(loss, 2.0);
            return loss;
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return 0.0;
    }

    public static void main(String[] args) {

        // Read an image.
        Mat mat = imread("data/unitedhealthcare-1.jpeg");

        //filter mat
        FilterModel model = new FilterModel();
        OCRMatFunction function = new OCRMatFunction(model);
        model.setThresh_higher(250.0);
        model.setThresh_lower(180.0);
        function.runFilters(mat);

        //print string results
        String loss = function.getOCR(mat);
        System.out.println("String: " + loss);

    }

}
