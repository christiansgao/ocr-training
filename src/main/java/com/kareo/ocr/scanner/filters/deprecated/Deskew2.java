package com.kareo.ocr.scanner.filters.deprecated;

import com.kareo.ocr.scanner.helpers.OCRHelper;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class Deskew2 {

    public static Mat deskew(Mat src, double angle) {
        Point2f center = new Point2f(src.arrayWidth() / 2, src.arrayHeight() / 2);
        Mat rotImage = getRotationMatrix2D(center, angle, 1.0);
        //1.0 means 100 % scale
        Size size = new Size(src.arrayWidth(), src.arrayHeight());
        warpAffine(src, src, rotImage, size); //INTER_LINEAR + CV_WARP_FILL_OUTLIERS
        return src;
    }

    public static Mat computeSkew(String inFile, String outputFile) {
        //Load this image in grayscale
        Mat img = imread(inFile, IMREAD_GRAYSCALE);

        //Binarize it
        //Use adaptive threshold if necessary
        //Imgproc.adaptiveThreshold(img, img, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, 40);
        threshold(img, img, 200, 255, THRESH_BINARY);

        //Invert the colors (because objects are represented as white pixels, and the background is represented by black pixels)
        bitwise_not(img, img);
        Mat element = getStructuringElement(MORPH_RECT, new Size(3, 3));

        //We can now perform our erosion, we must declare our rectangle-shaped structuring element and call the erode function
        erode(img, img, element);

        //Find all white pixels
        Mat wLocMat = new Mat(img.size(), img.type());
        findNonZero(img, wLocMat);

        //Create an empty Mat and pass it to the function
        Mat matOfPoint = new Mat(wLocMat);

        //Translate MatOfPoint to MatOfPoint2f in order to user at a next step
        Mat mat2f = new Mat();
        matOfPoint.convertTo(mat2f, CV_32FC2);

        //Get rotated rect of white pixels
        RotatedRect rotatedRect = minAreaRect(mat2f);

        Point2f vertices = new Point2f(4);
        rotatedRect.points(vertices);

        MatVector boxContours = new MatVector();
        boxContours.put(new Mat(vertices));
        drawContours(img, boxContours, 0, new Scalar(128.0, 128.0, 128.0, 128.0));

        double resultAngle = rotatedRect.angle();
        if (rotatedRect.size().width() > rotatedRect.size().height()) {
            rotatedRect.angle(rotatedRect.angle() + 90.f);
        }

        //Or
        //rotatedRect.angle = rotatedRect.angle < -45 ? rotatedRect.angle + 90.f : rotatedRect.angle;

        Mat result = deskew(imread(inFile), rotatedRect.angle());

        return result;
    }

    public static void main(String[] args) {

        // Read an image.
        Mat mat = imread("data/ReallyHardInsurance.jpeg");

        //display(mat, "Input");

        //filter mat
        Mat filtered_mat = deskew(mat, 20);
        OCRHelper.display(filtered_mat, "After");

    }
}
