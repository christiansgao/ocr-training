package com.kareo.ocr.scanner.helpers;

import com.kareo.ocr.scanner.training.minimizer.obj.NamedMat;
import org.bytedeco.javacpp.opencv_core;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import  org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

public class FileHelper {

    public static NamedMat[] getMats(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        NamedMat[] namedMats = new NamedMat[listOfFiles.length];

        for(int i = 0; i < listOfFiles.length; i++)
            namedMats[i] = new NamedMat(imread(listOfFiles[i].getAbsolutePath()),listOfFiles[i].getName());

        return namedMats;
    }

    public static opencv_core.Mat readImage(String source_name){
        opencv_core.Mat mat = imread(source_name);
        return mat;
    }

    public static BufferedImage readToBuffer(String source_name){
        opencv_core.Mat mat = imread(source_name);
        return OCRHelper.matToBuf(mat);
    }

    public static void saveImage(opencv_core.Mat mat, String source_name){
        //opencv_core.IplImage image = OCRHelper.MatToIpl(mat);
        String filename = "data/testImages/" + source_name + ".jpg", image;
        File f = new File(filename);
        imwrite(f.getAbsolutePath(), mat);
    }

    public static void saveImages(opencv_core.Mat[] mats, String source_name){

        for(int i = 0; i < mats.length; i ++) {
            File f = new File("data/testImages/" + source_name +"-" + i + ".jpg");
            imwrite(f.getAbsolutePath(), mats[i]);
        }

    }

}
