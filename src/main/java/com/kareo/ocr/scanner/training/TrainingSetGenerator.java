package com.kareo.ocr.scanner.training;

import com.kareo.ocr.scanner.helpers.DataCSVWriter;
import com.kareo.ocr.scanner.helpers.FeatureGenerator;
import com.kareo.ocr.scanner.helpers.FileHelper;
import com.kareo.ocr.scanner.training.minimizer.FilterModel;
import com.kareo.ocr.scanner.training.minimizer.OCRMatFunction;
import com.kareo.ocr.scanner.training.minimizer.obj.NamedMat;

import java.util.ArrayList;
import java.util.List;

public class TrainingSetGenerator {

    FeatureGenerator featureGenerator;

    public TrainingSetGenerator() {
        featureGenerator = new FeatureGenerator();
    }

    public void generateTrainingSet(String imagefolder) {

        NamedMat[] mats = FileHelper.getMats(imagefolder);
        FilterModel model = new FilterModel(31.859278245069248, 255.0);
        System.out.println("Starting Filtering and OCR Reading");
        OCRMatFunction ocrMatFunction = new OCRMatFunction(model);
        List<String[]> results = new ArrayList<String[]>();
        System.out.println("Starting Feature Generation");

        for (NamedMat namedMat : mats) {
            String result = ocrMatFunction.getOCR(namedMat.getMat());
            List<String[]> res = featureGenerator.generateFeatures(result, namedMat.getName());
            results.addAll(res);
        }

        DataCSVWriter.write(FeatureGenerator.FEATURES_HEADER, "data/trainingset/trainingset-model1-1-hist.csv");
        DataCSVWriter.writeAll(results, "data/trainingset/trainingset-model1-1-hist.csv", true);
        System.out.println("All Finished");

    }

    public static void main(String[] args) {

        TrainingSetGenerator generator = new TrainingSetGenerator();
        generator.generateTrainingSet("data/trainingImages");

    }

}
