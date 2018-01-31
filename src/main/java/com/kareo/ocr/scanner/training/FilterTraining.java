package com.kareo.ocr.scanner.training;

import com.kareo.ocr.scanner.helpers.OCRHelper;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_imgproc.*;


/**
 * Created by christian.gao on 1/30/18.
 */
public class FilterTraining {

    Mat mat;
    Mat defaultMat;
    Tesseract instance = Tesseract.getInstance();

    public Mat getMat() {
        return mat;
    }

    public Mat getDefaultMat() {
        return defaultMat;
    }

    // JNA Interface Mapping
    public FilterTraining(Mat mat){
        defaultMat = mat;
        mat = mat;

    }

    public double getLoss() {

        try {
            String result = instance.doOCR(OCRHelper.matToBuf(mat));
            System.out.println("Found words: " + result);

            String result_clean = result.replaceAll("\n"," ").trim().replaceAll(" +", " ");
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        return 0.0;
    }

    public void runFilters(Mat mat, Object[] params) {

        FilterParams filterParams = new FilterParams();
        filterParams.thresh_lower = (Integer) params[0];
        filterParams.thresh_higher = (Integer) params[1];
        runFilters(mat, filterParams);

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

        this.mat = mat;

    }

    public class FilterParams {
        Integer thresh_lower = 190;
        Integer thresh_higher = 255;
    }
}
