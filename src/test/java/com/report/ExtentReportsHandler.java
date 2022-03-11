package com.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.util.DBConnection;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class ExtentReportsHandler {
    final static String filePath = "Extent.html";
    public static int passedTestCases = 0;
    public static int failedTestCases = 0;
    public static int totalExecutedTestCases = 0;
    static ExtentReports extent;
    static ExtentHtmlReporter htmlReporter;
    static Date TotalStartTime, TotalEndTime;
    static DBConnection db = new DBConnection();

    public static void buildConsolidatedExtentReport(String reportid) {
        passedTestCases = 0;
        failedTestCases = 0;
        totalExecutedTestCases = 0;

        try {
            System.out.println(
                    "############################################################################");
            System.out.println(
                    "##############Started Building Consolidated Extent Report###################");
            System.out.println(
                    "############################################################################");
            long repStartTime = System.currentTimeMillis();
            extent = new ExtentReports();
            System.out.println("Report can be found"
                    + " at path : "
                    + System.getProperty("user.dir") + "\\report\\extent-reports\\" + reportid + "\\extent.html");
            htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "\\report\\extent-reports\\"
                    + reportid + "\\extent.html");
            htmlReporter.config().setDocumentTitle("Automation Execution Summery");
            htmlReporter.config().setReportName("CAF-Demo");
            //Name of the Report

            htmlReporter.config().setTheme(Theme.DARK);
            htmlReporter.config().setJS("for (i=0;i<document.getElementsBytagName(\"img\").length;i++){\r\n"
                    + "document.getElementsBytagName(\"img\").item(i).align=\"right\";;\r\n" + "}");
            htmlReporter.setAppendExisting(false);
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            Connection con = db.getDBConnection();
            Statement stmt1 = con.createStatement();
            //Need to check the DB name
            ResultSet rs1 = stmt1.executeQuery("Select distinct (testcaseid) from caf_extentreport where reportid='"
                    + reportid + "'order by testcaseid");
            Statement stmt = con.createStatement();
            //Need to check the DB name
            ResultSet rs = stmt.executeQuery("Select * from caf_extentreport where reportid='" + reportid + "'order by testcaseid,resstepnum");
            ArrayList<ExtentData> MyExtentData = new ArrayList();
            int tbldextentreports = 0, tblreststepnum;
            String tblreportid, tbltestcaseid = null, tblresultdescription, tblresstepstatus;
            Date tbltimestamp;
            String prvTestCaseID = "";
            HashMap<String, ArrayList<ExtentData>> myData = new HashMap<String, ArrayList<ExtentData>>();
            Date overallStartTime = new Date();
            Date overallEndTime = new Date();

            int i = 0;
            rs.last();
            int rsTotalSize = rs.getRow();
            rs.beforeFirst();
            while (rs.next()) {
                tbldextentreports = rs.getInt(1);
                tblreportid = rs.getString(2);
                tbltestcaseid = rs.getString(3);
                tblreststepnum = rs.getInt(4);
                tblresultdescription = rs.getString(5);
                tblresstepstatus = rs.getString(6);
                tbltimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString(7));

                ExtentData MyExtData = new ExtentData();
                MyExtData.setTblidextentreports(tbldextentreports);
                MyExtData.setTblreportid(tblreportid);
                MyExtData.setTbltestcaseid(tbltestcaseid);
                MyExtData.setTblresstepstatus(tblresstepstatus);
                MyExtData.setTblreststepnum(tblreststepnum);
                MyExtData.setTblresultdescription(tblresultdescription);
                MyExtData.setTbltimestamp(tbltimestamp);

                if (i == 0) {
                    prvTestCaseID = tbltestcaseid;
                }
                if (tbltestcaseid.equalsIgnoreCase(prvTestCaseID)) {
                    MyExtentData.add(MyExtData);
                    prvTestCaseID = tbltestcaseid;
                    i++;
                } else {
                    myData.put(prvTestCaseID, MyExtentData);
                    prvTestCaseID = tbltestcaseid;
                    MyExtentData = new ArrayList();
                    MyExtentData.add(MyExtData);
                    i++;
                }
                if (i == rsTotalSize) {
                    myData.put(prvTestCaseID, MyExtentData);
                    prvTestCaseID = tbltestcaseid;
                }
            }
            while (rs1.next()) {
                String TestCaseName = rs1.getString(1);
                System.out.println("Test Case : " + TestCaseName);
                ExtentTest test = extent.createTest(TestCaseName);
                boolean isPassed = false;
                for (int j = 0; j < (myData.get(TestCaseName).size()); j++) {
                    try {
                        int len = TestCaseName.length();
                        String TCname = TestCaseName.substring(0, len - 2);
                        String itrnum = TestCaseName.substring(len - 1);
                        String ImageName = TCname + "_Iter-Num_" + itrnum + "_Step-"
                                + (myData.get(TestCaseName)).get(j).getTblreststepnum();
                        System.out.println("ImageName: " + ImageName);
                        String imageFile = ImageName + ".png";
                        File imgFile = new File("\\report\\extent-reports\\" + reportid + "\\" + TCname + "\\Images\\" + imageFile);
                        String relativepath = "..\\..\\..\\report\\extent-reports\\" + reportid + "\\" + TCname + "\\Images\\" + imageFile;
                        String imgFilePath = System.getProperty("user.dir") + imgFile;
                        File NewImgPath = new File(imgFilePath);

                        isPassed = myData.get(TestCaseName).get(j).getTblresstepstatus().equalsIgnoreCase("PASSED");

                        if (NewImgPath.exists()) {
                            if (myData.get(TestCaseName).get(j).getTblresstepstatus().equalsIgnoreCase("PASSED")) {
                                test.log(Status.PASS, myData.get(TestCaseName).get(j).getTblresultdescription(), MediaEntityBuilder.createScreenCaptureFromPath(relativepath).build());

                            } else if (myData.get(TestCaseName).get(j).getTblresstepstatus().equalsIgnoreCase("FAILED")) {
                                test.log(Status.FAIL, myData.get(TestCaseName).get(j).getTblresultdescription(), MediaEntityBuilder.createScreenCaptureFromPath(relativepath).build());

                            }
                        } else {
                            if (myData.get(TestCaseName).get(j).getTblresstepstatus().equalsIgnoreCase("PASSED")) {
                                test.log(Status.PASS, myData.get(TestCaseName).get(j).getTblresultdescription());

                            } else if (myData.get(TestCaseName).get(j).getTblresstepstatus().equalsIgnoreCase("FAILED")) {
                                test.log(Status.FAIL, myData.get(TestCaseName).get(j).getTblresultdescription());

                            }
                        }

                        Date date = myData.get(TestCaseName).get(j).getTbltimestamp();

                        test.getModel().getLogContext().get(j).setTimestamp(date);
                        Date endTime = new Date();
                        Date startTime = new Date();
                        for (int k = 0; k < myData.get(TestCaseName).size() - 1; k++) {
                            Date date1 = myData.get(TestCaseName).get(k).getTbltimestamp();
                            if (k == 0) {
                                endTime = date1;
                            }
                            if (k < myData.get(TestCaseName).size() - 1) {
                                Date date2 = myData.get(TestCaseName).get(k + 1).getTbltimestamp();
                                if (date1.before(date2)) {
                                    if (endTime.before(date2)) {
                                        endTime = date2;
                                        overallEndTime = endTime;
                                    }
                                    if (date1.after(overallEndTime)) {
                                        overallEndTime = date1;
                                    } else if (date2.after(overallEndTime)) {
                                        overallStartTime = date2;
                                    }
                                }

                            }

                        }
                        for (int l = 0; l < myData.get(TestCaseName).size() - 1; l++) {
                            Date date1 = myData.get(TestCaseName).get(1).getTbltimestamp();


                            if (l == 0) {
                                startTime = date1;
                            }
                            if (l < myData.get(TestCaseName).size() - 1) {
                                Date date2 = myData.get(TestCaseName).get(l + 1).getTbltimestamp();
                                if (date1.before(date2)) {
                                    if (date1.before(startTime)) {
                                        startTime = date1;
                                        overallStartTime = startTime;
                                    }
                                    if (date1.before(overallStartTime)) {
                                        overallStartTime = date1;
                                    } else if (date2.before(overallStartTime)) {
                                        overallStartTime = date2;

                                    }
                                }
                            }
                        }
                        test.getModel().setEndTime(endTime);
                        test.getModel().setStartTime(startTime);
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
                extent.flush();
                String status = "0";
                if (isPassed) {
                    status = "1";
                    passedTestCases++;
                } else {
                    failedTestCases++;
                }
                if (System.getProperty("BuildNumber") != null) {

                    String query = "INSERT INTO 'heatmap' ('no','name','date','status','buildId') VALUES('" + TestCaseName + "_" + System.getProperty("BuildNumber") + "', '" + TestCaseName + "', now(),'" + status + "','" + System.getProperty("BuildNumber") + ")')" +
                            "ON DUPLICATE KEY UPDATE 'status' = '" + status + "'";
                    PreparedStatement preparedstmt = con.prepareStatement(query);
                    preparedstmt.execute();

                }
                totalExecutedTestCases++;

            }
            System.out.println("Overall Start Time : " + overallStartTime);
            htmlReporter.setStartTime(overallStartTime);
            System.out.println("Overall End time :" + overallEndTime);
            htmlReporter.setEndTime(overallEndTime);

            con.close();

            long repEndTime = System.currentTimeMillis();
            System.out.println("Total Consolidated Extent Report build Time: " + ((repEndTime - repStartTime) / 1000) + "Seconds");
            System.out.println(""
                    + "##############################################################################################");
            System.out.println(""
                    + "##################################Ended Building Consolidated Extent Report###################");
            System.out.println(""
                    + "##############################################################################################");

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static synchronized void inserValuesIntoDB(ArrayList<String> insertValues) {
        try {
            Connection con = db.getDBConnection();
            Statement stmt = con.createStatement();
            String columnvalues = "";
            HashSet<String> myHashSet = new HashSet<String>();
            myHashSet.addAll(insertValues);
            insertValues.clear();
            insertValues.addAll(myHashSet);
            String query = "insert into caf_extentreport (reportid,testcaseid,resstepnum,resultdescription,resstepstatus,timestamp)"
                    + "values (?,?,?,?,?,?)";
            for (int i = 0; i < insertValues.size(); i++) {

                String[] insertValue = insertValues.get(i).split("###");
                System.out.println(insertValue);
                PreparedStatement preparedstmt = con.prepareStatement(query);
                preparedstmt.setString(1, insertValue[0]);
                preparedstmt.setString(2, insertValue[1]);
                preparedstmt.setString(3, insertValue[2]);
                preparedstmt.setString(4, insertValue[3]);
                preparedstmt.setString(5, insertValue[4]);
                preparedstmt.setString(6, insertValue[5]);
                preparedstmt.execute();
            }

            Thread.sleep(5000);

            String[] Values = insertValues.get(0).split("###");

            Statement stmt1 = con.createStatement();

            ResultSet rs = stmt1.executeQuery("select count(*) from caf_extentreport where reportid='" + Values[0] + "'"

                    + "and testcaseid='" + Values[1] + "'");


            int tblrowcountreturned = 0;

            while (rs.next()) {

                tblrowcountreturned = rs.getInt(1);

                System.out.println("Total rows returned : " + tblrowcountreturned);

            }


            if (tblrowcountreturned == 0) {
                for (int i = 0; i < insertValues.size(); i++) {

                    String[] insertValue = insertValues.get(i).split("###");

                    System.out.println(insertValue);
                    PreparedStatement preparedStmt = con.prepareStatement(query);

                    preparedStmt.setString(1, insertValue[0]);

                    preparedStmt.setString(2, insertValue[0]);

                    preparedStmt.setString(3, insertValue[0]);

                    preparedStmt.setString(4, insertValue[0]);

                    preparedStmt.setString(5, insertValue[0]);

                    preparedStmt.execute();
                }
            }
            con.close();

        } catch (Exception e) {
            e.getMessage();
        }

    }

    public static String[] returnResultList(ArrayList<String> insertValues) {
        String[] insertValue = null;
        try {
            insertValues = Logger.resultList;
            HashSet<String> myHashSet = new HashSet<String>();
            myHashSet.addAll(insertValues);
            insertValues.clear();
            insertValues.addAll(myHashSet);
            for (int i = 0; i < insertValues.size(); i++) {
                insertValue = insertValues.get(i).split("###");
                System.out.println(insertValue);
            }
        } catch (Exception e) {
            System.out.println("Could not retrieve the result list values");
            System.out.println(e.getMessage());
        }
        return insertValue;
    }

}
