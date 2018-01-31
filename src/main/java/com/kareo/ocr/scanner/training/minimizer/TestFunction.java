package com.kareo.ocr.scanner.training.minimizer;

import com.kareo.ocr.scanner.training.minimizer.types.Function;
import org.bytedeco.javacpp.opencv_core;

public class TestFunction implements Function{

    int dimensions = 0;

    public double valueAt(double[] x) {
        if (x[0] > 2.0)
            x[0]--;
        else if (x[0] < 1.0)
            x[0] = 2.0;
        else
            x[0] = 1.0;
        if (x[1] > 2.0)
            x[1]--;
        else if (x[1] < 1.0)
            x[1] = 2.0;
        else
            x[1] = 1.0;
        return x[1] + x[0];
    }

    public opencv_core.Mat[] getMats() {
        return null;
    }

}
