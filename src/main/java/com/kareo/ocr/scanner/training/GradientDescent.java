package com.kareo.ocr.scanner.training;
import com.github.lbfgs4j.LbfgsMinimizer;
import com.kareo.ocr.scanner.helpers.OCRHelper;
import com.kareo.ocr.scanner.training.minimizer.OCRFunction;
import edu.stanford.nlp.optimization.*;
import edu.stanford.nlp.util.logging.Redwood;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

public class GradientDescent {

    int MAX_ITERATIONS = 5000;
    double TOLERANCE = .1;
    int TERMINATE_ITERATIONS = 10;
    double INIT_GAIN = .01;

    public double minimize(OCRFunction function, double[] initial, double learningRate){

        int params_length = initial.length;
        double step = 0;
        double loss_2 = 0;
        double loss_1 = 0;
        int terminate = 0;


        for (int i = 0; i < initial.length; i++) {
            double l = learningRate;
            for (int b = 0; b < MAX_ITERATIONS; b++) {
                loss_1 = function.valueAt(initial);
                step = learningRate * initial[i];
                initial[i] += step;
                loss_2 = function.valueAt(initial);
                OCRHelper.display(function.getMat(),"Iteration: " + b + "Variable: " + i);
                if(loss_2>loss_1) {
                    initial[i] -= step;
                    learningRate = learningRate * -.5;
                } else if(loss_2 == loss_1){
                    if(terminate == TERMINATE_ITERATIONS)
                        break;
                    terminate += 1;
                    learningRate = learningRate * 1.5;
                }
                System.out.println("Loss" + loss_1);
            }
        }

        return loss_2;
    }

    public void minimizeTest(){
        double[] testSeed = {1000};

    }

    public static void main (String args[]){

        Mat mat = imread("data/unitedhealthcare-1.jpeg");
        OCRFunction function = new OCRFunction(mat);
        double[] init = {0,255};
        GradientDescent gradientDescent = new GradientDescent();
        double loss =gradientDescent.minimize(function, init, 0.1);
        System.out.println("Final Loss" + loss);

    }
}
