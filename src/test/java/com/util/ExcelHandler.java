package com.util;

import com.report.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class ExcelHandler {
    static String CellValString = "", rowSTRING = "";
    static String CellsString = "", CellVal = "";
    static ArrayList<String> ColumnNames = new ArrayList<String>();
    static ArrayList<String> DataValues = new ArrayList<String>();
    static ArrayList<String> RowValues = new ArrayList<String>();
    static ArrayList<String> retval = new ArrayList<String>();
    static int FirstRowLastColumnNum;
    static Logger log = new Logger();
    static int TotalRows;

    public static HashMap<Object, Object> getValueHashMap(String s) {
        HashMap<Object, Object> l1 = new HashMap<Object, Object>();
        int i = 0;
        Object[] str = s.split(",");
        while (i < str.length) {
            Object[] obj = str[i].toString().split(":");
            l1.put(obj[0], obj[i]);
            i++;
        }
        return l1;
    }

    public static ArrayList<String> GetExcelRowsAndDataCount(String runOn) {
        ArrayList<String> TCtoRowMapping = new ArrayList<String>();
        try {
            Properties tempProp = new Properties();
            String strConfigPath = System.getProperty("user.dir") + "\\config\\Environment.properties";
            System.out.println("Picked up configuration path as" + strConfigPath);
            String dataSheet = null;
            try {
                FileInputStream oFis = new FileInputStream(strConfigPath);
                tempProp.load(oFis);
                Properties configProperties = new Properties();
                if (tempProp.getProperty("ENV_NAME").equalsIgnoreCase("SIT")) {
                    strConfigPath = System.getProperty("user.dir") + "\\config\\Config.properties";
                    oFis = new FileInputStream(strConfigPath);
                    configProperties.load(oFis);
                    dataSheet = configProperties.getProperty("DATASHEET");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            FileInputStream excelFile = new FileInputStream(new File(dataSheet));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            workbook.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            DataFormatter fmt = new DataFormatter();
            for (int sn = 0; sn < workbook.getNumberOfSheets(); sn++) {
                Sheet sheet = workbook.getSheetAt(sn);
                for (int rn = sheet.getFirstRowNum() + 1; rn <= sheet.getLastRowNum(); rn++) {
                    Row row = sheet.getRow(rn);
                    if (row == null) {
                        //There is no data in this row
                    } else {
                        String strRunOn = row.getCell(1).getStringCellValue();
                        String isRun = row.getCell(0).getStringCellValue();
                        if (runOn.equalsIgnoreCase(strRunOn) && isRun.equalsIgnoreCase("Yes")) {
                            String cellStr = row.getCell(2).getStringCellValue();
                            TCtoRowMapping.add(cellStr + ":" + rn);
                        }
                    }
                }
            }
            workbook.close();
            excelFile.close();
            return TCtoRowMapping;
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
        return TCtoRowMapping;
    }


}
