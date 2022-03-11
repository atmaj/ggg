package com.core;

import com.framework.Framework;
import com.report.ExtentReportsHandler;
import com.report.Log4jReport;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Properties;

@RunWith(Cucumber.class)

@CucumberOptions(

        features = {"src/test/resources/features/"},
        tags = {"@DigiBusBank_01"}, 
        glue = "com.businessDefinations",
        monochrome = true,
        plugin = {"pretty", "html:target/cucumber", "json:target/cucumber.json",
                "html:target/cucumber-report-html"})


public class TestRunner {
    public static String iternum;
    public static int diternum;
    public static String DataSheet;

    public static String tstamp = "";
    public static String DynamicTimeStamp = "";

    @BeforeClass
    public static void extent() throws Exception {

        FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "\\config\\Config.properties");
        Properties prop = new Properties();
        prop.load(in);

        iternum = prop.getProperty("iternum");
        diternum = Integer.parseInt(iternum);
        DataSheet = prop.getProperty("user.dir") + System.getProperty("DataSheet");
        System.out.println("System.getProperty(\"Tsp" + System.getProperty("Ts"));
        if (System.getProperty("Ts") == null) {
            if (System.getProperty("BuildNumber") == null) {
                LocalDateTime now = LocalDateTime.now();
                int year = now.getYear();
                int month = now.getMonthValue();
                int day = now.getDayOfMonth();
                int hour = now.getHour();
                int minute = now.getMinute();
                int second = now.getSecond();
                int millis = now.get(ChronoField.MILLI_OF_SECOND);
                ExecutorServiceTest.DateStamp = day + "_" + month + "_" + year + "_" + hour + "h_" + minute + "m_" + second + "s";
                System.out.println(ExecutorServiceTest.DateStamp);
            } else {
                ExecutorServiceTest.DateStamp = System.getProperty("runOn") + "_" + System.getProperty("BuildNumber");
            }
        } else {
            ExecutorServiceTest.DateStamp = System.getProperty("Ts");
        }
        Log4jReport.info("DateStamp=" + ExecutorServiceTest.DateStamp);
        File dir = new File(System.getProperty("user.dir") + "\\report\\extent-reports\\" + ExecutorServiceTest.DateStamp + "\\");
        System.out.println("Build Consolidated Extent dir: " + dir);
        if (!dir.exists()) {
            dir.mkdir();
            System.out.println("Build consolidated Extent dir created ");
        }
        System.out.println("==============================Batch Execution started=================================");
    }


    @AfterClass
    public static void CreateExtentReport() throws Exception {
        try {
            Framework.framework.webDriver.quit();
            System.out.println("Driver killed successfully");
        } catch (Exception e) {
            System.out.println("Failed to kill the driver or driver already killed");
        }
        ExtentReportsHandler.buildConsolidatedExtentReport(tstamp);
    }
}

