package com.kareo.ocr.scanner.training.minimizer.types;
import com.kareo.ocr.scanner.helpers.Dictionary;
import com.kareo.ocr.scanner.helpers.OCRHelper;
import com.kareo.ocr.scanner.training.minimizer.FilterParams;
import net.sourceforge.tess4j.Tesseract;

import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.opencv_core.Mat;


public class Function {

    Dictionary dict = new Dictionary();
    Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
    Mat mat;
    Mat defaultMat;

    public Function(Mat mat){
        this.mat = mat;
        this.defaultMat = mat;
    }

    public double valueAt(double[] params) {

        Mat mat = defaultMat.clone();
        FilterParams filterParams = new FilterParams();
        filterParams.thresh_lower = (int) params[0];
        filterParams.thresh_higher = (int) params[1];
        runFilters(mat, filterParams);
        double loss = getLoss();

        return loss;

    }

    public void runFilters(Mat mat, FilterParams params) {

        //normalize(mat, mat, 0, 1., NORM_MINMAX, -1, null);
        cvtColor(mat, mat, CV_BGR2GRAY);
        //inRange(mat, new Mat(new Scalar(0.0)), new Mat(new Scalar(50.0)), mat);
        threshold(mat, mat, params.thresh_lower, params.thresh_higher, ADAPTIVE_THRESH_GAUSSIAN_C);
        //mat = grayScaleBlurrImage(mat);
        //erode(mat, mat, new Mat());
        //dilate(mat, mat, new Mat());
        //GaussianBlur(mat, mat, mat.size(), 1, 1, 1);
        //Laplacian(mat, mat, mat.depth(), 1, 3, 0, BORDER_DEFAULT);

        this.mat =  mat;
    }

    public double getLoss() {

        try {
            double loss;
            String result = instance.doOCR(OCRHelper.matToBuf(this.mat));
            System.out.println("Found words: " + result);
            List<String> valid_word = dict.validWords(result);
            return -valid_word.size();

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



}
