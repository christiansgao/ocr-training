package com.kareo.ocr.scanner.helpers;

import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureGenerator {

    public static final int ROW_LENGTH = 12;
    public static final String[] FEATURES_HEADER = {
            "ImageName", "Word", "Category", "WordBefore", "WordAfter", "WordPosition", "WordLength", "CharCount", "NumCount", "PunctCount", "LineNumber", "ContainsRealWord", "IsInsuranceWord"
    };
    public Dictionary dictionary = new Dictionary();

    /*Features:
        -0 Image Name
        -1 Word
        -2 Category (null, classification word, important class)
        -3 Word Before
        -4 Word After
        -5 Word Position in Line
        -6 # Total Length
        -7 # Characters
        -8 # Of Numericals
        -9 # Of Punct
        -10 # Line Number
        -11 # In Dict after removing punct
        -12 # In Medical List
    */
    public List<String[]> generateFeatures(String ocrResults, String filename) {

        List<String[]> test = new ArrayList<String[]>();
        String[] lines = ocrResults.split("\n");
        for (String line : lines) {

            String[] lineArray = line.split("\\s+");
            for(int w = 0; w < lineArray.length; w++) {
                String[] row = new String[12];
                row[0] = filename == null ? " " : filename;
                row[1] = lineArray[w];
                row[2] = " ";
                row[3] = w ==0 ? " " : lineArray[w-1] ;
                row[4] = w == lineArray.length-1 ? " " : lineArray[w+1];
                row[5] = String.valueOf(w);
                row[6] = String.valueOf(lineArray[w].length());
                row[7] = String.valueOf(lineArray[w].replaceAll("[^a-z]", "").length());
                row[8] = ;
                row[9] = ;
                row[10] = ;
                row[11] = ;
                row[12] = ;
                row[13] = ;


            }

        }
        lines[0] = "string";
        test.add(lines);
        return test;

    }

    public static void main(String[] args) {

        try {
            // Read an image.
            Tesseract tesseract = new Tesseract();
            FeatureGenerator featureGenerator = new FeatureGenerator();
            Mat img = OCRHelper.readImage("data/unitedhealthcare-1.jpeg");
            //Mat resizeimage = new Mat();
            //Size sz = new opencv_core.Size(500,500);
            //resize(img, resizeimage, sz );
            String words = tesseract.doOCR(OCRHelper.matToBuf(img));
            List<String[]> strList = featureGenerator.generateFeatures(words);
            //OCRHelper.display(resizeimage, "string");
            System.out.println(Arrays.toString(strList.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
