package com.kareo.ocr.scanner.helpers;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;
import org.omg.SendingContext.RunTime;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

/**
 * Created by christian.gao on 1/29/18.
 */
public class OCRHelper {
    static ToIplImage toIplConverter = new OpenCVFrameConverter.ToIplImage();
    static ToMat toMatConverter = new OpenCVFrameConverter.ToMat();
    static Java2DFrameConverter paintConverter = new Java2DFrameConverter();


    public static BufferedImage matToBuf(Mat image) {

        Frame frame = toIplConverter.convert(image);
        return paintConverter.getBufferedImage(frame, 1);

    }

    public static Mat BufToMat(BufferedImage image) throws IOException {
        Frame frame = paintConverter.getFrame(image);
        return toMatConverter.convert(frame);
    }


    public static Mat IplToMat(IplImage image) {

        Frame frame = toMatConverter.convert(image);
        return toMatConverter.convertToMat(frame);

    }

    public static IplImage MatToIpl(Mat image) {

        Frame frame = toIplConverter.convert(image);
        return toIplConverter.convertToIplImage(frame);

    }

    public static void saveImage(Mat mat,String source_name){
        IplImage image = MatToIpl(mat);
        cvSaveImage("data/testImages/" + source_name + ".jpg", image);
    }

    public static void display(Object image, String caption) {

        CanvasFrame canvas = new CanvasFrame(caption, 1.0);
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Convert from OpenCV Mat to Java Buffered image for display
        OpenCVFrameConverter converter = new OpenCVFrameConverter.ToMat();
        // Show image on window.

        if(image instanceof Mat || image instanceof IplImage)
            canvas.showImage(converter.convert(image));
        else if (image instanceof BufferedImage)
            canvas.showImage((BufferedImage) image);
        else
            throw new RuntimeException("Bad Image!");

    }
}
