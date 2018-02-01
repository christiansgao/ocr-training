package com.kareo.ocr.scanner.training.minimizer;

/**
 * Created by christian.gao on 1/30/18.
 */
public class FilterModel {
    public static double[][] ranges = {{0, 255}, {0, 255}};
    Double thresh_lower = 0.0;
    Double thresh_higher = 255.0;

    public FilterModel() {

    }

    public FilterModel(double[] params) {

        thresh_lower = (double) params[0];
        thresh_higher = (double) params[1];
    }

    public FilterModel(double thresh_lower, double thresh_higher) {

        this.thresh_lower = thresh_lower;
        this.thresh_higher = thresh_higher;
    }

    public static double[][] getRanges() {
        return ranges;
    }

    public static void setRanges(double[][] ranges) {
        FilterModel.ranges = ranges;
    }

    public Double getThresh_lower() {
        return thresh_lower;
    }

    public void setThresh_lower(Double thresh_lower) {
        this.thresh_lower = thresh_lower;
    }

    public Double getThresh_higher() {
        return thresh_higher;
    }

    public void setThresh_higher(Double thresh_higher) {
        this.thresh_higher = thresh_higher;
    }

}
