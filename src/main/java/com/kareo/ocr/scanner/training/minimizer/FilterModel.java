package com.kareo.ocr.scanner.training.minimizer;

/**
 * Created by christian.gao on 1/30/18.
 */
public class FilterModel {
    public static double[][] ranges = {{0, 255}, {0, 255}};
    Integer thresh_lower = 0;
    Integer thresh_higher = 255;

    public FilterModel() {

    }

    public FilterModel(double[] params) {

        thresh_lower = (int) params[0];
        thresh_higher = (int) params[1];
    }

    public static double[][] getRanges() {
        return ranges;
    }

    public static void setRanges(double[][] ranges) {
        FilterModel.ranges = ranges;
    }

    public Integer getThresh_lower() {
        return thresh_lower;
    }

    public void setThresh_lower(Integer thresh_lower) {
        this.thresh_lower = thresh_lower;
    }

    public Integer getThresh_higher() {
        return thresh_higher;
    }

    public void setThresh_higher(Integer thresh_higher) {
        this.thresh_higher = thresh_higher;
    }

}
