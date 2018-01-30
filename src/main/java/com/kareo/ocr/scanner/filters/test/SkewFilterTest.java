package com.kareo.ocr.scanner.filters.test;

import com.kareo.ocr.scanner.filters.Deskew;
import org.apache.commons.imaging.Imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created on 03/05/2014.
 */
public class SkewFilterTest {
    public static void main(String[] args) {
        String[] args2 = {
                "data/unitedhealthcare-1.jpeg"};
        mainFct(args2);

    }

    public static void mainFct(String[] args) {
        try {
            if (args.length < 1 || args[0] == "--help" || args[0] == "-h") {
                System.out.print(
                        "Usage: java HocrToPdf INPUTURL.tiff INPUTURL.html OUTPUTURL.pdf\n" +
                                "\n" +
                                "Converts hOCR files into PDF\n" +
                                "\n" +
                                "Example: java com.acoveo.hocrtopdf.HocrToPdf file:///home/username/hocr.html ./output.pdf\n");
                if (args.length < 1)
                    System.exit(-1);
                else
                    System.exit(0);
            }
            URL inputImgFile = null;
            File imgFile = null;
            try {
                imgFile = new File(args[0]);
            } catch (Exception e) {
                System.out.println("First and second parameters have to be a valid URL");
                e.printStackTrace();
                System.exit(-1);
            }
            BufferedImage img = Imaging.getBufferedImage(imgFile);
            if (img == null) {
                System.exit(-1);
            }
            System.out.println("Image name : " + imgFile);

            System.out.println(" - page " + " width x height = " + img.getWidth() + " x " + img.getHeight());
            double res = Deskew.doIt(img);
            System.out.println(" -- skew " + " :" + res);

            //OCRHelper.display(img,"img");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

