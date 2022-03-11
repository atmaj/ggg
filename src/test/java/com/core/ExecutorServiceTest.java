package com.core;

import com.framework.Framework;
import com.framework.MyThread;
import com.report.ExtentReportsHandler;
import com.report.Logger;
import com.util.ExcelHandler;
import com.util.HelperFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.*;


public class ExecutorServiceTest {
    public static Logger log = new Logger();
    public static HashSet<String> PurnsInUse = new HashSet<>();
    public static String MultiIteration;
    public static String colCriteria = "executionTag";
    public static String DateStamp;
    public static String ds;
    static int ConcurrentThreadCunt;
    static long StartTime = System.currentTimeMillis();
    static String Datasheet, Tags, DatasheetPath;
    static String TestDataPath = System.getProperty("user.dir") + System.getProperty("TestDataPath");
    static int startRange = 0;
    static int endRange;
    static String range;
    static String TagList;
    static String TestName;
    static String executionflag = "Yes";
    static String PreviousTestCase = "";
    static int IterNum = 0;
    static ArrayList<String> MyData = null;

    /**
     * // @param args
     *
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     **/

    @BeforeClass
    public static void callDynamicFileBuilder() {
        try {
            String runOn = System.getProperty("runOn");

            if (ExecutorServiceTest.DateStamp == null) {
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
                } else {
                    ExecutorServiceTest.DateStamp = System.getProperty("runOn") + "_" + System.getProperty("BuildNumber");
                }
            }
            MyData = ExcelHandler.GetExcelRowsAndDataCount(runOn);
            HashSet<String> regressionTags = HelperFile.createScenario(runOn);
        } catch (Exception e) {
            e.getMessage();
        }

        try {
            Properties prop = System.getProperties();
            if (prop.containsKey("DataSetRange")) {
                range = System.getProperty("DataSetRange");
            }
        } catch (Exception e) {
            startRange = 0;
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            Framework.framework.webDriver.quit();
            System.out.println("Driver killed successfully");
        } catch (Exception e) {
            System.out.println("Failed to kill the driver or driver already killed");
        }
        ExtentReportsHandler.buildConsolidatedExtentReport(ExecutorServiceTest.DateStamp);
        System.out.println("######ExecutionResultStart###Total Test Cases Executed:" + ExtentReportsHandler.totalExecutedTestCases + ":Total Test Cases Passed:" + ExtentReportsHandler.passedTestCases + ":Total Test Cases Failed:" + ExtentReportsHandler.failedTestCases + "###ExecutionTestResult###");
        System.out.println("#########################################################################");
        System.out.println("All tasks are finished!");
        System.out.println("#########################################################################");
        Long EndTime = System.currentTimeMillis();
        System.out.println("Total Execution Time :" + (EndTime - StartTime) / 1000 + "Seconds.");
        System.out.println("#########################################################################");

    }

    
    @Test
    public void executetests() throws IOException {
        FileInputStream in;
        in = new FileInputStream(System.getProperty("user.dir") + "\\config\\Config.properties");
        Properties prop = new Properties();
        prop.load(in);
        MultiIteration = prop.getProperty("DATASOURCE");
        TagList = prop.getProperty("SCENARIOTAG");
        System.out.println("Taglist: " + TagList);
        ConcurrentThreadCunt = Integer.parseInt(prop.getProperty("THREADCOUNT"));
        System.out.println("ConcurrentThreadCount: " + ConcurrentThreadCunt);
        Tags = TagList;
        Datasheet = System.getProperty("user.dir") + "\\" + prop.getProperty("DATASHEET");
        if (MultiIteration.equalsIgnoreCase("Excel")) {
            System.out.println("#############################################################");
            System.out.println("Execution started with Test Data Source as MS Excel");
            System.out.println("#############################################################");

            endRange = MyData.size();

            ArrayList<String> IterInfo = new ArrayList<String>();
            ArrayList<Future> ExecServList = new ArrayList<Future>();

            if (range != null) {
                String[] rangearray = range.split("_");
                startRange = (Integer.valueOf(rangearray[0]) - 1);
                endRange = Integer.valueOf(rangearray[1]);

                if (endRange > MyData.size()) {
                    System.out.println("End Range should be less than OR equal to" + MyData.size() +
                            "Hence running it till dataset : " + MyData.size());
                    endRange = MyData.size();

                }
            }

            for (int i = startRange; i < endRange; i++) {
                DatasheetPath = Datasheet;
                Properties config = new Properties();
                String[] MyArr = MyData.get(i).split(":");

                if (i == 0) {
                    PreviousTestCase = MyArr[0];
                    IterNum = 0;
                } else if (i > 0) {
                    if (PreviousTestCase.equalsIgnoreCase(MyArr[0])) {
                        IterNum++;
                    } else {
                        PreviousTestCase = MyArr[0];
                        IterNum = 0;
                    }
                }

                TestName = MyArr[0];

                ExecutorService executor = Executors.newFixedThreadPool(ConcurrentThreadCunt);
                Future taskOne = executor.submit(
                        new MyThread("TaskOne", TagList, TestName, Integer.toString(IterNum), Datasheet, DateStamp, System.getProperty("BuildName")));

                ExecServList.add(taskOne);
                IterInfo.add(MyArr[0]);

                /**
                 *
                 * Initiates a shut down. Threads will be shutdown dynamically once their run is completed
                 *
                 * **/
                executor.shutdown();

                try {
                    executor.awaitTermination(Long.parseLong(prop.getProperty("TerminationWaitingTime")), TimeUnit.SECONDS);
                    executor.shutdown();
                } catch (Exception e) {
                    executor.shutdownNow();
                }

            }

        }


    }


}
