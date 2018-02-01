package com.kareo.ocr.scanner.training.minimizer;

import com.kareo.ocr.scanner.helpers.Dictionary;
import com.kareo.ocr.scanner.helpers.OCRHelper;
import com.kareo.ocr.scanner.training.minimizer.types.Function;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.util.Arrays;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class OCRMultiMatFunction implements Function {

    Dictionary dict = new Dictionary();
    Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
    Mat[] mats;
    Mat[] defaultMats;

    public OCRMultiMatFunction(Mat[] mats) {
        this.mats = OCRHelper.cloneMats(mats);
        this.defaultMats = OCRHelper.cloneMats(mats);
    }

    public static void main(String[] args) {

        // Read an image.
        Mat[] mats = new Mat[3];
        mats[0] = imread("data/unitedhealthcare-1.jpeg");
        mats[1] = imread("data/Sunshine.jpeg");
        mats[2] = imread("data/bcbs.jpeg");
        //display(mat, "Input");

        //filter mat

        OCRMultiMatFunction function = new OCRMultiMatFunction(mats);
        FilterModel model = new FilterModel();
        model.setThresh_higher(250.0);
        model.setThresh_lower(180.0);

        Mat[] filtered_mats = function.runFilters(model);

        //print string results
        double loss = function.valueAt(new double[]{190,250});
        System.out.println("OCR Loss: " + loss);

    }

    public double valueAt(double[] params) {

        FilterModel filterModel = new FilterModel(params);
        runFilters(filterModel);
        double loss = getLoss();

        return loss;

    }

    public Mat[] runFilters(FilterModel model) {

        this.mats = OCRHelper.cloneMats(this.defaultMats);

        for (Mat mat : mats) {
            //normalize(mat, mat, 0, 1., NORM_MINMAX, -1, null);
            cvtColor(mat, mat, CV_BGR2GRAY);
            //inRange(mat, new Mat(new Scalar(0.0)), new Mat(new Scalar(50.0)), mat);
            threshold(mat, mat, model.thresh_lower, model.thresh_higher, ADAPTIVE_THRESH_GAUSSIAN_C);
            //mat = grayScaleBlurrImage(mat);
            //erode(mat, mat, new Mat());
            //dilate(mat, mat, new Mat());
            //GaussianBlur(mat, mat, mat.size(), 1, 1, 1);
            //Laplacian(mat, mat, mat.depth(), 1, 3, 0, BORDER_DEFAULT);
        }
        return mats;
    }

    public double getLoss() {

        double[] losses = new double[mats.length];
        for (int i = 0; i < mats.length; i++)
            try {
                double loss = 0;
                String result = instance.doOCR(OCRHelper.matToBuf(mats[i]));
                //System.out.println("Read Document: " + result);
                Dictionary.WordResults wordResults = dict.validWords(result);
                loss -= wordResults.valid_words.size();
                loss -= (double) wordResults.invalid_words.size() / 1000.0;

                loss = -Math.pow(loss, 2.0);
                losses[i] = loss;
            } catch (TesseractException e) {
                System.err.println(e.getMessage());
            }

        double average_sq_loss = Arrays.stream(losses).sum();

        return average_sq_loss;
    }

    public Mat[] getMats() {
        return mats;
    }

    public Mat[] getDefaultMat() {
        return defaultMats;
    }


}
