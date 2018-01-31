package com.kareo.ocr.scanner.filters.deprecated;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

public class TextFilter {

    public static void main(String args[]){
        try {
            ConvertCmd cmd = new ConvertCmd();

            // create the operation, add images and operators/options
            IMOperation op = new IMOperation();
            op.addImage("ficx7.jpg");
            op.resize(800, 600);
            op.addImage("data/chrisChallenge_2.jpg");

            // execute the operation
            System.out.println(cmd);
            cmd.run(op);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
