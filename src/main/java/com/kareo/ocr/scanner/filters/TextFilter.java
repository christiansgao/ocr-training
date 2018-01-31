package com.kareo.ocr.scanner.filters;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

public class TextFilter {

    public static void main(String args[]){
        try {
            ConvertCmd cmd = new ConvertCmd();

            // create the operation, add images and operators/options
            IMOperation op = new IMOperation();
            op.addImage("/Users/christian.gao/Documents/Work/hack/ocr-tess4j-example/data/chrisChallenge.jpg");
            op.resize(800, 600);
            //op.addImage("data/chrisChallenge_2.jpg");

            // execute the operation
            cmd.run(op);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
