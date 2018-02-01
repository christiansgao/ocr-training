package com.kareo.ocr.scanner.training.minimizer.obj;

import org.bytedeco.javacpp.opencv_core;

public class NamedMat {
    opencv_core.Mat mat;
    String name;

    public NamedMat(opencv_core.Mat mat, String name) {
        this.mat = mat;
        this.name = name;
    }

    public opencv_core.Mat getMat() {
        return mat;
    }

    public String getName() {
        return name;
    }
}