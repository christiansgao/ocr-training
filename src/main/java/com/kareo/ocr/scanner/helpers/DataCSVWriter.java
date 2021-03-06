package com.kareo.ocr.scanner.helpers;

import com.opencsv.CSVWriter;

import java.io.File;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class DataCSVWriter {

    public static String get_date_stamp(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void appendToFile(String[] fields, String path){
        try {
            File newFile = new File(path);
            if(!new File(path).exists()) newFile.createNewFile();
            Writer writer = Files.newBufferedWriter(Paths.get(path), StandardOpenOption.APPEND);
            CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER);

            csvWriter.writeNext(fields);
            csvWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writeAll(List<String[]> fieldList, String path){
        try {

            Writer writer = Files.newBufferedWriter(Paths.get(path));

            CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER);

            csvWriter.writeAll(fieldList);
            csvWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        DataCSVWriter.appendToFile(new String[]{"Sundar Pichai ♥", "sundar.pichai@gmail.com", "+1-1111111111", "India"}, "./testcsv.csv");

    }
}