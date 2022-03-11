package com.businessDefinations;

import com.common.UIOperator;
import com.framework.Framework;
import com.framework.WebDriverWrapper;
import com.report.ExtentReportsHandler;
import com.report.Log4jReport;
import com.report.Logger;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.json.JSONObject;

import java.util.ArrayList;


public class GenericStepDefinitions {

    public static Framework framework;
    public static UIOperator oUI;
    public static boolean securityFlag = false;
    public static boolean IDVFlag = false;
    public static String cartTestID = null;
    public static boolean specialzedlinkExist = true;
    public JSONObject testData;
    public ArrayList<String> newTab;
    String parentHandle = null;

    @Before
    public void Initialize(Scenario scenario) throws Throwable {
        /*Result list to store steps for consolidated report.
         * Initializing in before to avoid duplication of steps in DB*/
        //if(System.getProperty("BuildName")!=null && !System.getProperty("BuildName").equalsIgnoreCase("null")) {
        //CART.startNewBuild("Corporate","IPORTAL","Continuous_SIT_Regression",System.getProperty("BuildName"),true);
        //cartTestID= CART.addNewTestCase(scenario.getName());
        //}
        Logger.resultList = new ArrayList<>();
        /* Initializing framework class instance */
        framework = Framework.createInstance(scenario.getName());

        /*Initializing pass fail status*/
        Framework.isTestPassOrFail = true;
        oUI = Framework.framework.UI;
        testData = Framework.framework.testData;
        Logger.Initialize("AUTOMATION RESULT", scenario.getName());
        Log4jReport.initialize();
        if (Framework.framework.webDriver == null) {
            WebDriverWrapper.LoadWebDriver();
        }
    }

    @After
    public void LogOff(Scenario scenario) {
        try {
            // Code for Logout
            Logger.WriteScenarioFooter(Framework.createInstance(scenario.getName()).strCurrentScenarioName);
            if (Framework.framework.webDriver != null) {
                try {
                    if (scenario.isFailed()) {
                        Logger.WriteLog("TestCase has failed , please refer Screenshot", false, true);
                    }
                    Framework.framework.webDriver.quit();
                } catch (Exception e) {
                    Framework.framework.webDriver.quit();
                    System.out.println(e.getMessage());
                }
                Framework.framework.webDriver = null;
                Logger.WriteScenarioFooter(scenario.getName());
            }
        } catch (Exception e) {
            Log4jReport.exception(Log4jReport.getCallerInfo(Exception.class) + e.getMessage(), e);
            System.out.println(e.getMessage());
        } finally {
            ExtentReportsHandler.inserValuesIntoDB(Logger.resultList);
            Logger.resultList.removeAll(Logger.resultList);
        }
    }


    //Common reusable methods below for application

    public boolean launchApplication(String appURL) {
        boolean flag = false;
        try {
            framework.webDriver.get(appURL);
            Framework.framework.webDriver.manage().window().maximize();
            System.out.println("Launched the application with url " + appURL);
            flag = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
        }
        return flag;
    }


}
