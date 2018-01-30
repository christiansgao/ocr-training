package com.kareo.ocr.scanner.training.minimizer;

public class OCRFunction {

    int dimensions = 0;

    public OCRFunction(double[] params){
        dimensions = params.length;
    }

    double valueAt(){

    };

    int domainDimension(){
        return dimensions;
    };


}
