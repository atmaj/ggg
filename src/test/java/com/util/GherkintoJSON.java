package com.util;

import gherkin.formatter.JSONFormatter;
import gherkin.formatter.JSONPrettyFormatter;
import gherkin.parser.Parser;
import gherkin.util.FixJava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class GherkintoJSON {

    private String format;
//To get the total running time (optional)

    public GherkintoJSON(String outFormat) {
        this.format = outFormat;
    }

    public String getOutFormat() {
        return format;
    }

    public String gherkinTojson(String fPath) {
        //Define Feature File and JSON file path
        String gherkin = null;
        try {
            gherkin = FixJava.readReader(new InputStreamReader(new FileInputStream(fPath), StandardCharsets.UTF_8));


        } catch (FileNotFoundException e) {

            System.out.println("Feature File not found");
        } catch (RuntimeException e) {
            e.getMessage();
        }

        StringBuilder json = new StringBuilder();

        JSONFormatter formatter;
        //pretty or ugly selection, pretty by default
        if (format.equalsIgnoreCase("ugly")) {
            formatter = new JSONFormatter(json);//not pretty
        } else {
            formatter = new JSONPrettyFormatter(json);//pretty
        }

        Parser parser = new Parser(formatter);
        parser.parse(gherkin, fPath, 0);
        formatter.done();
        formatter.close();

        //Finally flush and close
        try {

            return json.toString();
        } catch (Exception e) {
            e.getMessage();
            return "";
        }
    }

}

