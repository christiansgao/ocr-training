package com.kareo.ocr.scanner.training.minimizer.types;

import org.bytedeco.javacpp.opencv_core;

public interface Function {

    public double valueAt(double[] params);
    public opencv_core.Mat[] getMats();

}
