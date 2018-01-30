package com.kareo.ocr.scanner.training;
import com.github.lbfgs4j.LbfgsMinimizer;
import edu.stanford.nlp.optimization.*;
import edu.stanford.nlp.util.logging.Redwood;

import java.util.List;

public class GradientDescent {

    int MAX_ITERATIONS = 5000;
    double TOLERANCE = .1;
    double INIT_GAIN = .01;

    public class Funct extends AbstractStochasticCachingDiffFunction{
        public void calculate(double[] x) {
        }

        public double valueAt(double[] x) {
            if(x[0]>1)
                x[0]--;
            if(x[1]>1)
                x[1]--;
            return x[1] + x[0];
        }

        public int domainDimension(){
            return 1;
        }
        public int dataDimension(){
            return 1;
        }
        public void calculateStochastic(double[] d1, double[] d2, int[] i){

        }
    }

    public Function getTestFunction(){

        Function function = new AbstractStochasticCachingDiffFunction() {

            public void calculate(double[] x) {
                if(x[0]>1)
                    x[0]--;
                if(x[1]>1)
                    x[1]--;
            }

            public double valueAt(double[] x) {
                if(x[0]>1)
                    x[0]--;
                if(x[1]>1)
                    x[1]--;
                return x[1] + x[0];
            }

            public int domainDimension(){
                return 1;
            }
            public int dataDimension(){
                return 1;
            }
            public void calculateStochastic(double[] d1, double[] d2, int[] i){

            }

        };
        return function;
    }


    public void minimizeTest(){
        SMDMinimizer qn = new SMDMinimizer<Function>(INIT_GAIN,1,StochasticCalculateMethods.GradientOnly,MAX_ITERATIONS) ;
        double[] testSeed = {1000};
        Function testFunction = getTestFunction();
        //qn.setM(10);
        double[] min = qn.minimize(testFunction,TOLERANCE,testSeed);
        for(double value : min) {
            System.out.println(value);
        }
    }

    public static void main (String args[]){
        GradientDescent gradientDescent = new GradientDescent();
        gradientDescent.minimizeTest();

    }
}
