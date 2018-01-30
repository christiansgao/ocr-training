package com.kareo.ocr.scanner.filters;

import com.kareo.ocr.scanner.helpers.OCRHelper;
import org.bytedeco.javacpp.Pointer;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class HorizontalFilter {

    public static void main(String[] args) {

        String fileName = args.length >= 1 ? args[0] : "data/chrisChallenge.jpg"; // if no params provided, compute the defaut image
        IplImage src = cvLoadImage(fileName, 0);
        IplImage dst;
        IplImage colorDst;
        CvMemStorage storage = cvCreateMemStorage(0);
        CvSeq lines = new CvSeq();

        if (src == null) {
            System.out.println("Couldn't load source image.");
            return;
        }

        dst = cvCreateImage(cvGetSize(src), src.depth(), 1);
        colorDst = cvCreateImage(cvGetSize(src), src.depth(), 3);

        cvCanny(src, dst, 180, 90, 3);

        cvCvtColor(dst, colorDst, CV_GRAY2BGR);

        /*
         * apply the probabilistic hough transform
         * which returns for each line deteced two points ((x1, y1); (x2,y2))
         * defining the detected segment
         */

        System.out.println("Using the Probabilistic Hough Transform");
        lines = cvHoughLines2(dst, storage, CV_HOUGH_PROBABILISTIC, 1, Math.PI / 180, 40, 50, 10, 0, CV_PI);
        // HoughLinesP( dst, lines, 1, CV_PI/180, 80, 30, 10 );

        for (int i = 0; i < lines.total(); i++) {
            // Based on JavaCPP, the equivalent of the C code:
            // CvPoint* line = (CvPoint*)cvGetSeqElem(lines,i);
            // CvPoint first=line[0], second=line[1]
            // is:

            Pointer line = cvGetSeqElem(lines, i);
            CvPoint pt1  = new CvPoint(line).position(1);
            CvPoint pt2  = new CvPoint(line).position(0);

            //cvSobel(dst, colorDst,  0, 90, 3);


            System.out.println("Line spotted: ");
            System.out.println("\t pt1: " + pt1);
            System.out.println("\t pt2: " + pt2);
            cvLine(colorDst, pt1, pt2, CV_RGB(255, 0, 0), 3, CV_AA, 0); // draw the segment on the image
        }


        OCRHelper.IplToMat(src);
        OCRHelper.display(colorDst, "colorDst");

        //cvSaveImage(SOURCE_FILE, src);
        //cvSaveImage(HOUGH_FILE, colorDst);
    }
}