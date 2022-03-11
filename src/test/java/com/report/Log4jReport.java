package com.report;

import org.apache.log4j.PropertyConfigurator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log4jReport {
    public static org.apache.log4j.Logger Log = org.apache.log4j.Logger.getLogger(Log4jReport.class.getName());
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date dt = new Date();

    public static void initialize() {
        PropertyConfigurator.configure(System.getProperty("user.dir") + "//config//log4j.properties");
    }

    public static void info(String message) {
        Log.info(message);
    }

    public static void error(String message) {
        Log.error(message);
    }

    public static void exception(String message, Exception e) {
        Log.error(message, e);
    }

    public static String getCallerInfo(final Class<?> clazz) {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final String className = clazz.getName();
        boolean classFound = false;
        for (int i = 1; i < stackTrace.length; i++) {
            final StackTraceElement element = stackTrace[i];
            final String callerClassName = element.getClassName();
            //check if class name is the requested class
            if (callerClassName.equals(className)) classFound = true;
            else if (classFound) {
                return "[Class] " + callerClassName + "" + "\n\t\t\t\t\t\t\t[Method]:" + element.getMethodName() + "(" + element.getLineNumber() + "):";
            }
        }
        return "";
    }

    public static String getCallerLineNumber(final Class<?> clazz) {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final String className = clazz.getName();
        boolean classFound = false;
        for (int i = 1; i < stackTrace.length; i++) {
            final StackTraceElement element = stackTrace[i];
            final String callerClassName = element.getClassName();
            //check if class name is the requested class
            if (callerClassName.equals(className)) classFound = true;
            else if (classFound) {
                return "[Class] " + callerClassName + "" + "\t[Method]:" + element.getMethodName() + "(" + element.getLineNumber() + "):";
            }
        }
        return "";
    }
}
