package com.util;

import com.framework.Framework;
import com.report.Log4jReport;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {


    Connection connection = null;

    public Connection getDBConnection() {
        try {
            String databaseURL = null, user = null, password = null;
            try {
                databaseURL = Framework.framework.GetConfigProperty("Automation_DB_URL");
                user = Framework.framework.GetConfigProperty("Automation_DB_User");
                password = Framework.framework.GetConfigProperty("Automation_DB_Pwd");
                Class.forName(Framework.framework.GetConfigProperty("Automation_DB_Driver")).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Properties tempProp = new Properties();
                String strConfigPath = System.getProperty("user.dir") + "\\config\\Environment.properties";
                System.out.println("Picked up configuration path as" + strConfigPath);

                try {
                    FileInputStream oFis = new FileInputStream(strConfigPath);
                    tempProp.load(oFis);
                    Properties configProperties = new Properties();
                    if (tempProp.getProperty("ENV_NAME").equalsIgnoreCase("SIT")) {
                        strConfigPath = System.getProperty("user.dir") + "\\config\\Config.properties";
                        oFis = new FileInputStream(strConfigPath);
                        configProperties.load(oFis);
                    } else if (tempProp.getProperty("ENV_NAME").equalsIgnoreCase("CIT")) {
                        strConfigPath = System.getProperty("user.dir") + "\\config\\config_CIT.properties";
                        oFis = new FileInputStream(strConfigPath);
                        configProperties.load(oFis);
                    }
                    databaseURL = configProperties.getProperty("Automation_DB_URL");
                    user = configProperties.getProperty("Automation_DB_User");
                    password = configProperties.getProperty("Automation_DB_Pwd");
                    Class.forName(configProperties.getProperty("Automation_DB_Driver")).getDeclaredConstructor().newInstance();

                } catch (IOException e1) {
                    System.out.println(e1.getLocalizedMessage());
                }
            }
            connection = DriverManager.getConnection(databaseURL, user, password);
            if (connection != null) {
                System.out.println("Connected to  the Automation Test Data Database...");
            }
        } catch (Exception e) {
            Log4jReport.exception("Failed at DB connection", e);
        }
        return connection;
    }
}
