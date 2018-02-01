package com.kareo.ocr.scanner.training;

import com.kareo.ocr.scanner.helpers.DataCSVWriter;
import com.kareo.ocr.scanner.helpers.FileHelper;
import com.kareo.ocr.scanner.training.minimizer.FilterModel;
import com.kareo.ocr.scanner.training.minimizer.OCRMultiMatFunction;
import com.kareo.ocr.scanner.training.minimizer.types.Function;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.util.Arrays;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

public class GradientDescent {

    int BATCHSIZE = 100;
    int MAX_EPOCH = 4;
    int MAX_SAME_LOSS = 15;

    double TOLERANCE = .1;
    double[][] ranges;

    public GradientDescent(double[][] ranges) {
        this.ranges = ranges;
    }

    public static void main(String args[]) {

        Mat[] mats = new Mat[3];
        mats[0] = imread("data/unitedhealthcare-1.jpeg");
        mats[1] = imread("data/Sunshine.jpeg");
        mats[2] = imread("data/bcbs.jpeg");

        OCRMultiMatFunction function = new OCRMultiMatFunction(mats);
        //TestFunction function = new TestFunction();
        double[] init = {20, 255};
        GradientDescent gradientDescent = new GradientDescent(FilterModel.ranges);
        double loss = gradientDescent.minimize(function, init, 0.1);
        System.out.println("Final Loss " + loss);

    }

    //public double minimize(TestFunction function, double[] initial, double learningRate){
    public double minimize(Function function, double[] initial, double learningRate) {

        int params_length = initial.length;
        double step = 0;
        double loss_2 = 0;
        double loss_1 = 0;
        double min_loss = 10000;
        int epochs = 0;

        while (true) {

            if (epochs == MAX_EPOCH)
                break;

            epochs += 1;

            for (int i = 0; i < initial.length; i++) {
                double l = learningRate;
                int sameLoss = 0;
                for (int b = 0; b < BATCHSIZE; b++) {
                    loss_1 = function.valueAt(initial);
                    step = learningRate * initial[i];
                    addStep(initial, i, step);
                    loss_2 = function.valueAt(initial);
                    if (loss_2 > loss_1) {
                        addStep(initial, i, -step);
                        learningRate = learningRate * -.5;
                    } else if (loss_1 < loss_2) {
                        addStep(initial, i, step);
                        learningRate = learningRate * 1.5;
                    } else if (loss_2 == loss_1) {
                        if (sameLoss == MAX_SAME_LOSS)
                            break;
                        sameLoss += 1;
                        if (initial[i] == ranges[i][1] || initial[i] == ranges[i][0])
                            learningRate = learningRate * -1.2;
                        else
                            learningRate = learningRate * 1.5 * sameLoss;
                    }


                    System.out.println("LOSS: " + loss_1 + " BATCH: " + b + " Paramenter: " + i + " LearningRate: " + learningRate + " Epoch: " + epochs + " SameLoss: " + sameLoss);
                    System.out.println("Parameters: " + Arrays.toString(initial));
                    DataCSVWriter.appendToFile(initial, "data/trainingdata/parameters-1.csv");
                    FileHelper.saveImages(function.getMats(), "BATCH: " + b + " Interation: " + i);
                    if (min_loss > loss_2)
                        min_loss = loss_2;
                }
            }

        }

        return min_loss;
    }

    public void addStep(double[] initial, int i, double step) {
        double steppedValue = initial[i] + step;
        if (steppedValue > ranges[i][0] && steppedValue < ranges[i][1])
            initial[i] = steppedValue;
        else if (steppedValue < ranges[i][0])
            initial[i] = ranges[i][0];
        else
            initial[i] = ranges[i][1];
    }

    public void minimizeTest() {
        double[] testSeed = {1000};

    }
}
