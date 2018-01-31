package com.kareo.ocr.scanner.training.minimizer;

public class TestFunction {

    int dimensions = 0;

    public double valueAt(double[] x){
        if(x[0]>1)
            x[0]--;
        else
            x[0] = 1;
        if(x[1]>1)
            x[1]--;
        else
            x[1] = 1;
        if(x[2]>1)
            x[2]--;
        else
            x[2] = 1;
        return x[1] + x[0] + x[2];
    };

}
