package com.kareo.ocr.scanner.training.minimizer;

public class TestFunction {

    int dimensions = 0;

    public double valueAt(double[] x) {
        if (x[0] > 1)
            x[0]--;
        else if (x[0] < 1)
            x[0]++;
        if (x[1] > 1)
            x[1]--;
        else if (x[1] < 1)
            x[1]++;
        else
            x[1] = 1;
        return x[1] + x[0];
    }

    ;

}
