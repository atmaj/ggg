package com.report;

import com.core.ExecutorServiceTest;
import com.core.TestRunner;
import com.framework.Framework;
import com.framework.WebDriverWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.utils.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.util.CART;
//import org.runner.TestRunner;

//import freemarker.core.ParseException;

public class Logger {
    public static String strLogPath = "";
    public static boolean isFooterLogged = false;
    public static boolean isInitialized = false;
    public static String strScenarioName = "";
    public static String summary_report_name = "", detail_report_name = "";
    public static String strLogPath1 = "";
    public static boolean isSummaryInitialized = false;
    public static String directory = "";
    public static String temp = null;
    public static Date startOfSuiteExecution;
    public static String executionDirName = null;
    public static String rootDirectory = "";
    public static int totalTCCount = 0;
    public static Date startDate;
    public static String fullname;
    public static String scenarioName;
    public static ArrayList<String> resultList;
    public static int StepNum = 0;
    public static String resStepStatus = "";
    static Logger logex = new Logger();
    private static List<String> logList = null;
    private static String logFile;

    /**
     * Initialize :Initialies the detail Report and creates table, when called for the very first time. In all subsequent calls,just a table is created. Also copies the images and javascript file into the new directory.
     *
     * @param strFeatureName:  Feature name for which logger needs to be created.
     * @param strScenarioName: Name of the scenario
     * @throws IOException Signals that an I/O excpetion has occured.
     */

    public static void Initialize(String strFeatureName, String strScenarioName) throws IOException {
        isFooterLogged = false;
        Logger.strScenarioName = "";
        Logger.StepNum = 0;
        if (Logger.strScenarioName.isEmpty()) {
            scenarioName = System.getProperty("tagTestData").substring(1);
            if (StringUtils.isEmpty(TestRunner.tstamp)) {
                TestRunner.tstamp = ExecutorServiceTest.DateStamp;
            }
            Log4jReport.info("TestRunner.tstamp" + TestRunner.tstamp);

            File dir = new File(System.getProperty("user.dir") + "\\report\\extent-reports\\" + TestRunner.tstamp + "\\" + scenarioName);
            System.setProperty("log4jPath", System.getProperty("user.dir") + "\\report\\extent-reports\\" + TestRunner.tstamp + "\\" + scenarioName + "\\" + scenarioName + ".log");
            Log4jReport.info("log4jPath" + System.getProperty("user.dir") + "\\report\\extent-reports\\" + TestRunner.tstamp + "\\" + scenarioName + "\\" + scenarioName + ".log");
            if (!dir.exists()) {
                dir.mkdir();
                Log4jReport.info("Extent dir created at :" + System.getProperty("user.dir") + "\\report\\extent-reports\\" + TestRunner.tstamp + "\\" + scenarioName);
            } else {
                Log4jReport.info("Extent dir created at :" + System.getProperty("user.dir") + "\\report\\extent-reports\\" + TestRunner.tstamp + "\\" + scenarioName);
            }

            DateFormat format1 = new SimpleDateFormat("dd_HH_mm_ss");
            Date dateObj1 = new Date();
            executionDirName = scenarioName + "_" + format1.format(dateObj1);
            String dirName = "Report" + "_" + format1.format(dateObj1);
            String fname = System.getProperty("user.dir");
            File logsFile = new File(System.getProperty("user.dir") + "\\Logs\\");
            if (!logsFile.exists()) {
                logsFile.mkdir();
            }

            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String sanityDir = fname + "\\" + "Logs" + "\\" + "RegressionExecution_" + f.format(date) + "\\";
            File newDire = new File(sanityDir);
            if (!newDire.exists()) {
                boolean res1 = false;
                try {
                    newDire.mkdir();
                    res1 = true;
                } catch (SecurityException se) {
                }
                if (res1) {
                    System.out.println("DIR created. Dir Name =" + sanityDir);
                    rootDirectory = executionDirName;
                }
            }
            fullname = sanityDir + executionDirName;
            File newDir1 = new File(fullname);
            if (!newDir1.exists()) {
                boolean result1 = false;
                try {
                    newDir1.mkdir();
                    result1 = true;
                } catch (SecurityException se) {
                }
                if (result1) {
                    System.out.println("DIR created. Dir Name =" + fullname);
                    rootDirectory = executionDirName;
                }
            }
        }

        if (Logger.strScenarioName.equalsIgnoreCase(Framework.framework.strCurrentScenarioName)) {
            isInitialized = true;
        } else {
            System.out.println("its not equal..!");
            Logger.strScenarioName = Framework.framework.strCurrentScenarioName;
            isInitialized = false;
        }
        System.out.println("Verify step isInitialized = " + isInitialized);

        if (!isInitialized) {
            totalTCCount = totalTCCount + 1;
            System.out.println("Write to report initially");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String dateStart = sdf.format(cal.getTime());
            System.out.println("dateStart = " + dateStart);

            try {
                startDate = sdf.parse(dateStart);
                System.out.println("startDate = " + startDate);
            } catch (ParseException e) {
            }

            DateFormat format = new SimpleDateFormat("dd_HH_mm_ss");
            Date dateobj = new Date();
            String strFileName = "DetailReport" + "_" + format.format(dateobj) + ".html";
            detail_report_name = strFileName;
            strLogPath = fullname + "\\" + strFileName;
            System.out.println("strLogPath " + strLogPath);
            File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\detail_report_new.txt");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String sTemplateText = new String(data, "UTF-8");
            FileWriter logger = new FileWriter(strLogPath, false);
            logger.write(sTemplateText);
            logger.write("<table id ='customers'><tr class='header expand'><th colspan ='4' style='background-color:#005580;'>" + "Scenario:" + strScenarioName + " " + "<span'><!--Scenario Result--></span><span class='sign' style='float:right'></span></tr>");
            logger.write("<tr><th style='width:50%'>Description</th><th style ='width:10%'>Screenshot</th><th style='width:10%'>Result</th>");
            logger.close();
            logList = new ArrayList<String>();
            isInitialized = true;

            String CAF_image = "CAF_cropped.jpg";

            Path source = Paths.get(System.getProperty("user.dir") + "\\src\\test\\resources\\CAF_cropped.jpg");
            File f1 = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\CAF_cropped.jpg");

            if (f1.exists()) {
                System.out.println("File already exists. Skip copying");
            } else {
                Path destination = Paths.get(System.getProperty("user.dir") + "\\Logs\\" + executionDirName + "\\" + directory + "\\" + CAF_image);
                Files.copy(source, destination);
            }

            String js5 = "js5.js";
            File file2 = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\js5.js");
            FileInputStream fis2 = new FileInputStream(file2);
            byte[] data2 = new byte[(int) file2.length()];
            fis2.read(data2);
            fis2.close();
            String sTemplateText2 = new String(data2, "UTF-8");
            FileWriter logger2 = new FileWriter(fullname + "\\" + directory + "\\" + js5, false);
            logger2.write(sTemplateText2);
            logger2.close();
        } else {
            System.out.println("Write to report data");
            FileWriter logger = new FileWriter(strLogPath, true);
            logger.write("<table id ='customers'><tr class='header expand'><th colspan ='4' style='background-color:#005580;'>" + "Scenario:" + strScenarioName + " " + "<span'><!--Scenario Result--></span><span class='sign' style='float:right'></span></tr>");
            logger.write("<tr><th style='width:50%'>Description</th><th style ='width:10%'>Screenshot</th><th style='width:10%'>Result</th>");
            logger.close();
        }
    }


    /**
     * Closes a table. Call when you scenario ends. (Call it from your @after method)
     *
     * @param strScenarioName
     */

    public static void WriteScenarioFooter(String strScenarioName) {

        if (isFooterLogged == true) {
            return;
        }
        isFooterLogged = true;
        try {
            File file = new File(strLogPath);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String completeLog = new String(data, "UTF-8");
            if (Framework.isTestPassOrFail) {
                completeLog = completeLog.replace("<!--Scenario Result-->", "<b><font color='green'>PASS</font></b>");
            } else {
                completeLog = completeLog.replace("<!--Scenario Result-->", "<b><font color='red'>FAIL</font></b>");

            }
            FileWriter logger = new FileWriter(strLogPath, false);
            completeLog = completeLog + "</table>";
            logger.write(completeLog);
            logger.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }


    /**
     * Call at the end of execution,it closes current table and attaches closing tags
     *
     * @throws IOException
     */

    public static void WriteFooter() throws IOException {
        File file = new File(strLogPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String completeLog = new String(data, "UTF-8");
        if (Framework.isTestPassOrFail) {
            completeLog = completeLog.replace("<!--Scenario Result-->", "<b><font color='green'>PASS</font></b>");
        } else {
            completeLog = completeLog.replace("<!--Scenario Result-->", "<b><font color='red'>FAIL</font></b>");

        }

        FileWriter logger = new FileWriter(strLogPath, false);
        completeLog = completeLog + "</table></body></html>";
        logger.write(completeLog);
        logger.close();
    }


    /**
     * Used to write a row into the table.
     *
     * @param strLogContent
     * @param isPassorFail
     * @param isSnapshot
     * @throws Throwable
     */


    public static void WriteLog(String strLogContent, boolean isPassorFail, boolean isSnapshot) {
        FileWriter logger = null;
        try {
            logger = new FileWriter(strLogPath, true);
        } catch (IOException e) {

        }
        if (isPassorFail) {
            resStepStatus = "PASSED";
            Log4jReport.info(Log4jReport.getCallerLineNumber(Logger.class) + " " + strLogContent);
            if (System.getProperty("BuildName") != null && !System.getProperty("BuildName").equalsIgnoreCase("null")) {
                //CART.addTestStep(cartTestID,"",strLogContent,"","","PASS");
            }
        } else {
            resStepStatus = "FAILED";
            Log4jReport.error(Log4jReport.getCallerInfo(Logger.class) + " " + strLogContent);
            if (System.getProperty("BuildName") != null && !System.getProperty("BuildName").equalsIgnoreCase("null")) {
                //CART.addTestStep(cartTestID,"",strLogContent,"","","FAIL");
            }
        }

        Logger.addExtentStep(strLogContent, resStepStatus, Logger.getCurrentDateStamp());
        if (isSnapshot) {
            try {
                strLogContent = "<td style='text-align:left;'>" + strLogContent + "</td>" + "<td>" + WebDriverWrapper.CaptureSCreenshot() + "</td>";
            } catch (IOException e1) {
                Log4jReport.error(e1.getMessage());
            }
            try {
                takeSnapShot(Framework.framework.webDriver, System.getProperty("user.dir") + "\\report\\extent-reports\\" + TestRunner.tstamp + "\\" + scenarioName + "\\Images\\" + Logger.scenarioName + "_Iter-Num_" + TestRunner.diternum + "_Step-" + (StepNum - 1) + ".png");
            } catch (Exception e) {
                Log4jReport.error(e.getMessage());
            }
            System.out.println("Snapshot Logger.java======" + System.getProperty("user.dir") + "\\report\\extent-reports\\" + TestRunner.tstamp + "\\" + scenarioName + "\\Images\\" + Logger.scenarioName + "_Iter-Num_" + TestRunner.diternum + "_Step-" + (StepNum - 1) + ".png");
        }

        if (!isSnapshot) {
            strLogContent = "<td style='text-align:left;'>" + strLogContent + "</td>" + "<td>" + "N/A" + "</td>";
        }

        if (isPassorFail) {
            String strTempLog = ("<tr> STEP_RESULT <td><font color='green'>Pass </font></td></tr>").replace("STEP_RESULT", strLogContent);
            try {
                logger.write(strTempLog);
            } catch (IOException e) {

            }
            System.out.println("[PASS]: " + strLogContent);
        } else {
            String strTempLog = ("<tr>  STEP_RESULT <td><font color='red'>Fail </font></td></tr>").replace("STEP_RESULT", strLogContent);
            try {
                logger.write(strTempLog);
            } catch (IOException e) {

            }
            System.out.println("[FAIL]: " + strLogContent);
        }
        try {
            logger.close();
        } catch (IOException e) {

        }
        if (!isPassorFail) {
            Framework.isTestPassOrFail = false;
            Assert.fail("Scenario is failed. Please check the logs");
            try {
                throw new Exception("Scenario is failed.Please check the logs");
            } catch (Exception e) {
                Log4jReport.error(Log4jReport.getCallerInfo(Logger.class) + e.getMessage());
            }
        }
    }


    public static void WriteLogWithPDF(String strLogContent, boolean isPassorFail, String fileName) throws Exception {
        FileWriter logger = null;
        try {
            logger = new FileWriter(strLogPath, true);
        } catch (IOException e) {

        }

        if (isPassorFail) {
            strLogContent = "<td style= 'text-align:left;'>" + strLogContent + "</td>" + "<td><a href=" + fileName + " target=_blank>Report</a></td>";
            String strTempLog = ("<tr> STEP_RESULT <td><font color='green'> Pass </font> </td></tr>").replace("STEP_RESULT", strLogContent);
            try {
                logger.write(strTempLog);
            } catch (Exception e) {
            }
            System.out.println(" [PASS]: " + strLogContent);
        } else {
            strLogContent = "<td style='text-align:left;'>" + strLogContent + "</td>" + "<td>N/A</td>";
            String strTempLog = ("<tr> STEP_RESULT <td><font color='red'> Fail </font></td></tr>").replace("STEP_RESULT", strLogContent);
            try {
                logger.write(strTempLog);
            } catch (Exception e) {
            }
            System.out.println(" [FAIL]: " + strLogContent);
        }
        try {
            logger.close();
        } catch (Exception e) {
        }

        if (isPassorFail) {
            resStepStatus = "PASSED";
        } else {
            resStepStatus = "FAILED";
        }
        Logger.addExtentStep(strLogContent, resStepStatus, Logger.getCurrentDateStamp());
        if (!isPassorFail) {
            Framework.isTestPassOrFail = false;
            throw new Exception("Scenario is failed. Please check the logs");
        }
    }

    public static void WriteLogWithRobot(String strLogContent, boolean isPassorFail, String fileName) throws Exception {

        FileWriter logger = null;
        try {
            logger = new FileWriter(strLogPath, true);
        } catch (Exception e) {
        }

        if (isPassorFail) {
            strLogContent = "<td style= 'text-align:left;'>" + strLogContent + "</td>" + "<td><a href=" + fileName + " target=_blank>Screenshot</a></td>";
            String strTempLog = ("<tr> STEP_RESULT <td><font color='green'> Pass </font> </td></tr>").replace("STEP_RESULT", strLogContent);
            try {
                logger.write(strTempLog);
            } catch (Exception e) {
            }
            System.out.println(" [PASS]: " + strLogContent);
        } else {
            strLogContent = "<td style='text-align:left;'>" + strLogContent + "</td>" + "<td>N/A</td>";
            String strTempLog = ("<tr> STEP_RESULT <td><font color='red'> Fail </font></td></tr>").replace("STEP_RESULT", strLogContent);
            try {
                logger.write(strTempLog);
            } catch (Exception e) {
            }
            System.out.println(" [FAIL]: " + strLogContent);
        }
        try {
            logger.close();
        } catch (Exception e) {
        }

        if (!isPassorFail) {
            Framework.isTestPassOrFail = false;
            throw new Exception("Scenario is failed. Please check the logs");
        }
    }

    /**
     * Initializes the Summary Report
     *
     * @param title
     * @throws IOException
     */

    public static void Initialize_Summary(String title) throws IOException {
        if (!Logger.isSummaryInitialized) {
            DateFormat format = new SimpleDateFormat("dd_HH_mm_ss");
            Date dateobj = new Date();
            String strFileName = "SummaryResult" + "_" + format.format(dateobj) + ".html";
            summary_report_name = strFileName;
            strLogPath1 = System.getProperty("user.dir") + "\\Logs\\" + strFileName;
            File file = new File(System.getProperty("user.dir") + "\\resources\\summary_report_template.txt");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String sTemplateText = new String(data, "UTF-8");
            FileWriter logger = new FileWriter(strLogPath1, false);
            sTemplateText = sTemplateText.replace("<!--Inser title here-->", title);
            logger.write(sTemplateText);
            logger.close();
            Logger.isSummaryInitialized = true;

        } else {
            return;
        }
    }


    /**
     * Used to inser images into the summary report
     *
     * @param name1
     * @param i
     * @param scenarios_passed
     * @param scenarios_failed
     * @param j
     * @param feature_steps_passed
     * @param feature_steps_failed
     * @param feature_steps_skipped
     * @param k
     * @param duration_in_string
     * @param feature_status
     */

    public static void insert_row(String name1, int i, int scenarios_passed,
                                  int scenarios_failed, int j, int feature_steps_passed,
                                  int feature_steps_failed, int feature_steps_skipped, int k,
                                  String duration_in_string, String feature_status) {

        try {
            File file = new File(strLogPath1);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String completeLog = new String(data, "UTF-8");

            FileWriter logger = new FileWriter(strLogPath1, false);
            long duration = Long.parseLong(duration_in_string);
            double duration_in_seconds = duration / 1000000000.0;
            double duration_in_minutes = duration_in_seconds / 60;
            double duration_in_sec = duration_in_seconds % 60;
            System.out.println("In seconds: " + duration_in_sec);
            duration_in_string = Math.round(duration_in_minutes) + " Min " + Math.round(duration_in_sec * 100.0) / 100.0 + " Sec";
            if (feature_status.equals("Passed")) {
                completeLog = completeLog.replace("<!--Insert Rows here-->", "<tr><td>" + name1 + "/td><td>" + i + "</td><td>" + scenarios_passed + "</td><td>" + scenarios_failed + "</td><td>" +
                        j + "</td><td>" + feature_steps_passed + "</td><td>" + feature_steps_failed + "</td><td>" + feature_steps_skipped + "</td><td>" + k + "</td><td>" + duration_in_string + "</td><td><font color='green'>" + feature_status + "</font>\"</td></tr>" + "<!--Insert Rows here-->");
            } else if (feature_status.equals("Failed")) {
                completeLog = completeLog.replace("<!--Insert Rows here-->", "<tr><td>" + name1 + "/td><td>" + i + "</td><td>" + scenarios_passed + "</td><td>" + scenarios_failed + "</td><td>" +
                        j + "</td><td>" + feature_steps_passed + "</td><td>" + feature_steps_failed + "</td><td>" + feature_steps_skipped + "</td><td>" + k + "</td><td>" + duration_in_string + "</td><td><font color='red'>" + feature_status + "</font>\"</td></tr>" + "<!--Insert Rows here-->");
            }
            logger.write(completeLog);
            logger.close();
        } catch (Exception e) {
        }
    }


    /**
     * used to add links to the summary report ( The summary report and detail report need to be linked together.
     */

    public synchronized static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {

        // Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

        // Call getScreenshotAs method to create image file

        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);

        // Move image file to new destination

        File destFile = new File(fileWithPath);

        // copy file at destination

        FileUtils.copyFile(srcFile, destFile);
    }


    public static String getCurrentDateStamp() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND); // Note: no direct getter available
        System.out.println("Current Stamp : " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        return +year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }


    public synchronized static void addExtentStep(String resultDescription, String resStepStatus, String stepTimeStamp) {
        int spnum = StepNum++;
        System.out.println(Logger.scenarioName + "_" + TestRunner.diternum + " : " + TestRunner.tstamp + "###"
                + Logger.scenarioName + "_" + TestRunner.diternum + "###" + (spnum) + "###" + resultDescription + "###"
                + resStepStatus + "###" + stepTimeStamp);
        resultList.add(TestRunner.tstamp + "###" + Logger.scenarioName + "_" + TestRunner.diternum + "###" + (spnum)
                + "###" + resultDescription + "###" + resStepStatus + "###" + stepTimeStamp);

    }


}
