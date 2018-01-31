package com.kareo.ocr.scanner.training;
import com.kareo.ocr.scanner.helpers.OCRHelper;
import com.kareo.ocr.scanner.training.minimizer.FilterParams;
import com.kareo.ocr.scanner.training.minimizer.OCRFunction;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.util.Arrays;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

public class GradientDescent {

    int BATCHSIZE = 50;
    int MAX_EPOCH = 3;
    int MAX_SAME_LOSS = 15;

    double TOLERANCE = .1;
    double[][] ranges;
    public GradientDescent(double[][] ranges){
        this.ranges = ranges;
    }
    //public double minimize(TestFunction function, double[] initial, double learningRate){
    public double minimize(OCRFunction function, double[] initial, double learningRate){

        int params_length = initial.length;
        double step = 0;
        double loss_2 = 0;
        double loss_1 = 0;
        double min_loss = 10000;
        int epochs = 0;

        int test = initial.length;

        while(true){

            if(min_loss == loss_2 || epochs == MAX_EPOCH )
                break;
            else
                min_loss = loss_2;

            epochs += 1;

            for (int i = 0; i < initial.length; i++) {
                double l = learningRate;
                int sameLoss = 0;
                for (int b = 0; b < BATCHSIZE; b++) {
                    loss_1 = function.valueAt(initial);
                    step = learningRate * initial[i];
                    addStep(initial, i, step);
                    loss_2 = function.valueAt(initial);
                    OCRHelper.saveImage(function.getMat(),"Variable: " + i + "Batch: " + b);
                    if(loss_2>loss_1) {
                        addStep(initial, i, -step);
                        learningRate = learningRate * -.5;
                    } else if(loss_2 == loss_1){
                        if(sameLoss == MAX_SAME_LOSS)
                            break;
                        sameLoss += 1;
                        if(initial[i] == ranges[i][1] || initial[i] == ranges[i][0])
                            learningRate = learningRate * -1;
                        else
                            learningRate = learningRate * 1.5;
                    }
                    System.out.println("LOSS: " + loss_1 + " BATCH: " + b + " Interation: " + i + " LearningRate: " + learningRate);
                    System.out.println(Arrays.toString(initial));
                }
            }

        }

        return loss_2;
    }

    public void addStep(double[] initial, int i, double step){
        double steppedValue = initial[i] + step;
        if(steppedValue> ranges[i][0] && steppedValue <ranges[i][1])
            initial[i] = steppedValue;
        else if(steppedValue< ranges[i][0])
            initial[i] = ranges[i][0];
        else
            initial[i] = ranges[i][1];
    }
    public void minimizeTest(){
        double[] testSeed = {1000};

    }

    public static void main (String args[]){

        Mat mat = imread("data/unitedhealthcare-1.jpeg");
        OCRFunction function = new OCRFunction(mat);
        //TestFunction function = new TestFunction();
        double[] init = {0,255};
        GradientDescent gradientDescent = new GradientDescent(FilterParams.ranges);
        double loss =gradientDescent.minimize(function, init, 0.1);
        System.out.println("Final Loss" + loss);

    }
}
