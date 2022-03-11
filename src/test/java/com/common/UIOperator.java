package com.common;

import com.framework.Framework;
import com.report.Log4jReport;
import com.report.Logger;
import cucumber.api.Scenario;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.framework.Framework.framework;

//import com.sun.media.sound.InvalidFormatException;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

public class UIOperator {
    public static ArrayList<String> regList = new ArrayList<String>();
    public static Map<String, String> map = new HashMap<String, String>();
    public static Framework framework;
    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    static Map<String, Map<String, String>> finalRepo;
    static WebDriver driver;
    public Queue<String> ctaSequence, linkSequence, subLinkSequence, LinkToExistOnPage, messageXpathSequence, tourPageSequence, screenSrquence, actionOnSettings;
    public JSONObject testData;
    Scenario scenario;

    public static ArrayList<String> readRegressionTags(String runOn) throws Exception {
        int i;
        String strCelldata = null;
        Properties tempProp = new Properties();
        String strConfigPath = System.getProperty("user.dir") + "\\config\\Environment.properties";
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
        FileInputStream fis = new FileInputStream(dataSheet);
        XSSFWorkbook xssf = new XSSFWorkbook(fis);
        Sheet sh = xssf.getSheetAt(0);
        int rownTotal = sh.getLastRowNum();
        System.out.println("Total rows in data sheet:" + rownTotal);
        for (i = 0; i < rownTotal + 1; i++) {
            Cell xssCell = sh.getRow(i).getCell(1);
            String strCellRunOn = xssCell.getStringCellValue();
            String isRun = sh.getRow(i).getCell(0).getStringCellValue();
            if (strCellRunOn.equalsIgnoreCase(runOn) && isRun.equalsIgnoreCase("Yes")) {
                xssCell = sh.getRow(i).getCell(2);
                strCelldata = xssCell.getStringCellValue();
                UIOperator.regList.add(strCelldata);
            }
        }
        return UIOperator.regList;
    }

    public static Map<String, String> initializeExcel(String regressionTag) throws Exception {
        Map<String, String> mapTestData;
        mapTestData = readfunctionalTags(regressionTag);
        return mapTestData;
    }

    public static Map<String, String> readfunctionalTags(String regressionTag) throws Exception {
        int intColcnt, i, j;
        String strCelldata = null;
        String strDataindex = null;
        String dataSheet = null;
        try {

            dataSheet = Framework.framework.GetConfigProperty("DATASHEET");
            System.out.println("Hello:" + dataSheet);
        } catch (Exception e) {
            Properties tempProp = new Properties();
            String strConfigPath = System.getProperty("user.dir") + "\\config\\Environment.properties";
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
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
        FileInputStream fis = new FileInputStream(dataSheet);
        XSSFWorkbook xssf = new XSSFWorkbook(fis);
        Sheet sh = xssf.getSheetAt(0);
        int rownTotal = sh.getLastRowNum();

        for (i = 0; i < rownTotal + 1; i++) {
            intColcnt = sh.getRow(i).getPhysicalNumberOfCells();
            Cell xssCell = sh.getRow(i).getCell(2);
            strCelldata = xssCell.getStringCellValue();
            if (strCelldata.equalsIgnoreCase(regressionTag)) {
                for (j = 0; j < intColcnt; j++) {
                    xssCell = sh.getRow(0).getCell(j);
                    strDataindex = xssCell.getStringCellValue();
                    xssCell = sh.getRow(i).getCell(j);
                    xssCell.setCellType(CellType.STRING);
                    strCelldata = xssCell.getStringCellValue();
                    map.put(strDataindex, strCelldata);
                }
            }
        }
        return UIOperator.map;
    }

    private static String retrieveLatestFile(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile.getPath();
    }

    public void FluentWait(int timeOut, int pulse, final String elementName, String... dynamicValue) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(Framework.framework.webDriver)
                .withTimeout(timeOut, TimeUnit.SECONDS).pollingEvery(pulse, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

    }

    public void fluentWait(final String elementName, String... dynamicValue) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(Framework.framework.webDriver)
                    .withTimeout(180, TimeUnit.SECONDS).pollingEvery(4, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            WebElement foo = wait.until(ExpectedConditions.elementToBeClickable(getObjectFromRepository(elementName, dynamicValue)));
        } catch (Exception e) {
            Logger.WriteLog("Element" + elementName + " not found after waiting", false, true);
        }

    }

    public boolean EnterText(String strEditName, String strValue) throws Exception {
        try {
            FluentWait(60, 2, strEditName);
            WebElement element = getObjectFromRepository(strEditName);
            element.sendKeys(strValue);
            return true;
        } catch (Exception e) {
            Logger.WriteLog("Text" + strValue + "could not be entered in the field" + strEditName, false, true);
            return false;
        }
    }

    public void tabUsingSelenium(WebElement element) {
        try {
            element.sendKeys(Keys.TAB);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isElementPresent(String name, String... dynamicValue) throws Exception {
        try {
            WebElement element = getObjectFromRepository(name, dynamicValue);
            FluentWait(15, 4, name, dynamicValue);
            if (element.isDisplayed()) {
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String getJSONObjectvalue(String jsonObject, String objectKey) {
        try {
            JSONArray object = Framework.framework.testData.getJSONObject(jsonObject).optJSONArray(objectKey);
            if (object == null)
                return Framework.framework.testData.getJSONObject(jsonObject).getString(objectKey);
            else
                return getValueFromQueue(objectKey);
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray getJSONObjectArray(String jsonObject, String objectKey) {
        try {
            JSONArray object = Framework.framework.testData.getJSONObject(jsonObject).optJSONArray(objectKey);
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    public String getValueFromJSONObject(JSONObject jsonObject, String objectKey) {
        try {
            return jsonObject.getString(objectKey);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existsElement(String id) {
        try {
            Framework.framework.webDriver.findElement(By.id(id));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public boolean click(String strButtonName) throws Exception {
        try {
            WebElement element = getObjectFromRepository(strButtonName);
            JavascriptExecutor js = (JavascriptExecutor) Framework.framework.webDriver;
            js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid red;');", element);
            Thread.sleep(500);
            js.executeScript("arguments[0].setAttribute('style','border:2px solid white;');", element);
            element.click();
            System.out.println("Clicked on element : " + strButtonName);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Unable to click on : " + strButtonName, false, true);
            return false;
        }
    }

    public boolean click(String strButtonName, String... dynamicValue) throws Exception {
        try {
            WebElement element = getObjectFromRepository(strButtonName, dynamicValue);
            JavascriptExecutor js = (JavascriptExecutor) Framework.framework.webDriver;
            js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid red;');", element);
            Thread.sleep(500);
            js.executeScript("arguments[0].setAttribute('style','border:2px solid white;');", element);
            element.click();
            System.out.println("Clicked on element : " + strButtonName);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Unable to click on : " + strButtonName, false, true);
            return false;
        }
    }

    public String getTextFromPage(String strButtonName) throws Exception {
        String str = "";
        str = getObjectFromRepository(strButtonName).getText();
        return str;
    }

    public boolean enterText(String strEditName, String strValue) throws Exception {
        try {
            WebElement element = getObjectFromRepository(strEditName);
            element.clear();
            element.sendKeys(strValue);
            System.out.println("Entered value : " + strValue + " in text box : " + strEditName);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Unable to enter text in : " + strEditName, false, true);
            return false;
        }
    }

    public void clearText(String strTextField) throws Exception {
        try {
            WebElement element = getObjectFromRepository(strTextField);
            element.clear();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Unable to clear text in : " + strTextField, false, true);
        }
    }

    public WebElement getObjectFromRepository(String strControlName, String... dynamicValue) {
        WebElement oElement = null;
        try {
            String objectRepositoryPath = "";
            objectRepositoryPath = Framework.framework.GetConfigProperty("Object_Repository");
            String line = "";
            String actualLine = "";
            BufferedReader br = new BufferedReader(new FileReader(objectRepositoryPath));
            while ((line = br.readLine()) != null) {
                if (line.startsWith(strControlName + ",")) {
                    actualLine = line;
                }
            }
            br.close();
            String[] strArr = actualLine.split(",");
            String strId = "";
            String strName = "";
            String strClassName = "";
            String strXPath = "";
            String strLinkText = "";
            String strPartialLinkText = "";
            String strTagName = "";
            String strCSSSelector = "";
            try {
                strId = strArr[2];
                strName = strArr[3];
                strClassName = strArr[4];
                strXPath = strArr[9];
                strLinkText = strArr[5];
                strPartialLinkText = strArr[6];
                strTagName = strArr[7];
                strCSSSelector = strArr[8];
            } catch (ArrayIndexOutOfBoundsException ax) {

            }
            if (!strId.isEmpty()) {
                strId = String.format(strId, dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.id(strId));
            } else if (!strName.isEmpty()) {
                strName = String.format(strName, dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.name(strName));
            } else if (!strClassName.isEmpty()) {
                strClassName = String.format(strClassName, dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.className(strClassName));
            } else if (!strLinkText.isEmpty()) {
                strLinkText = String.format(strLinkText, dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.linkText(strLinkText));
            } else if (!strPartialLinkText.isEmpty()) {
                strPartialLinkText = String.format(strPartialLinkText, dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.partialLinkText(strPartialLinkText));
            } else if (!strTagName.isEmpty()) {
                strTagName = String.format(strTagName, dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.tagName(strTagName));
            } else if (!strCSSSelector.isEmpty()) {
                //strCSSSelector=String.format(strCSSSelector,dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.cssSelector(strCSSSelector));
            } else if (!strXPath.isEmpty()) {
                strXPath = String.format(strXPath, dynamicValue);
                oElement = Framework.framework.webDriver.findElement(By.xpath(strXPath));
            }


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return oElement;
    }

    public void clickOnWebElement(String xpath) throws Exception {
        WebElement we = Framework.framework.webDriver.findElement(By.xpath(xpath));
        if (we != null) {
            JavascriptExecutor js = (JavascriptExecutor) Framework.framework.webDriver;
            js.executeScript("arguments[0].click()", we);
            System.out.println("Clicked on element : " + xpath);
        } else {
            Logger.WriteLog("Unable to click at : " + xpath, false, true);
        }
    }

    public void verifyUIMessage(String webElement, String message) throws Exception {
        if (getObjectFromRepository(webElement).getText().contains(message))
            Logger.WriteLog("Expected Message : '" + message + "' displayed successfully on page", true, true);
        else
            Logger.WriteLog("Expected Message : '" + message + "'  is not displayed on page", false, true);

    }

    public boolean isElementSelected(String elementToBeVerified, String... dynamicValue) {
        WebElement element = getObjectFromRepository(elementToBeVerified, dynamicValue);
        return element.isSelected();
    }

    public boolean selectFromListAndClick(String strControlName, String text) throws Exception {
        String selectedValue = null;
        try {
            List<WebElement> elements = getObjectsFromRepository(strControlName);
            for (WebElement webElement : elements) {
                if (webElement.getText().trim().equalsIgnoreCase(text)) {
                    selectedValue = webElement.getText();
                    webElement.click();
                    System.out.println("Clicked on element : " + webElement.getText());
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<WebElement> getObjectsFromRepository(String strControlName, String... dynamicValue) {
        List<WebElement> oElement = null;
        try {
            String objectRepositoryPath = "src\\test\\resources\\objectRepository\\Object_Repository.csv";
            //objectRepositoryPath=Framework.framework.GetConfigProperty("Object_Repository");
            String line = "";
            String actualLine = "";
            BufferedReader br = new BufferedReader(new FileReader(objectRepositoryPath));
            while ((line = br.readLine()) != null) {
                if (line.startsWith(strControlName + ",")) {
                    actualLine = line;
                }
            }
            br.close();
            String[] strArr = actualLine.split(",");
            String strId = "";
            String strName = "";
            String strClassName = "";
            String strXPath = "";
            String strLinkText = "";
            String strPartialLinkText = "";
            String strTagName = "";
            String strCSSSelector = "";
            try {
                strId = strArr[2];
                strName = strArr[3];
                strClassName = strArr[4];
                strXPath = strArr[9];
                strLinkText = strArr[5];
                strPartialLinkText = strArr[6];
                strTagName = strArr[7];
                strCSSSelector = strArr[8];
            } catch (ArrayIndexOutOfBoundsException ax) {

            }
            if (!strId.isEmpty()) {
                strId = String.format(strId, dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.id(strId));
            } else if (!strName.isEmpty()) {
                strName = String.format(strName, dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.name(strName));
            } else if (!strClassName.isEmpty()) {
                strClassName = String.format(strClassName, dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.className(strClassName));
            } else if (!strLinkText.isEmpty()) {
                strLinkText = String.format(strLinkText, dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.linkText(strLinkText));
            } else if (!strPartialLinkText.isEmpty()) {
                strPartialLinkText = String.format(strPartialLinkText, dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.partialLinkText(strPartialLinkText));
            } else if (!strTagName.isEmpty()) {
                strTagName = String.format(strTagName, dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.tagName(strTagName));
            } else if (!strCSSSelector.isEmpty()) {
                //strCSSSelector=String.format(strCSSSelector,dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.cssSelector(strCSSSelector));
            } else if (!strXPath.isEmpty()) {
                strXPath = String.format(strXPath, dynamicValue);
                oElement = Framework.framework.webDriver.findElements(By.xpath(strXPath));
            }


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return oElement;
    }

    public String extractText(String elementName) throws Exception {
        String str = "";
        str = getObjectFromRepository(elementName).getText();
        return str;
    }

    public String getAttribute(String elementName, String attributeName) throws Exception {
        String str = "";
        str = getObjectFromRepository(elementName).getAttribute(attributeName);
        return str;
    }

    public boolean isElementEnabled(String element) {
        WebElement ele = getObjectFromRepository(element);
        return ele.isEnabled();
    }

    public String addRandomNum(int n) {
        String SALTCHARS = "123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < n) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public boolean actionHover(String lnkText) {
        try {
            WebElement element = getObjectFromRepository(lnkText);
            Actions action = new Actions(Framework.framework.webDriver);
            action.moveToElement(element).build().perform();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void actionPageDown() {
        try {
            Actions action = new Actions(Framework.framework.webDriver);
            action.sendKeys(Keys.PAGE_DOWN).perform();
        } catch (Exception e) {

        }
    }

    public String getValueFromQueue(String controlName) {
        String value = null;
        if (controlName.equalsIgnoreCase("cta")) {
            try {
                if (!ctaSequence.isEmpty()) {
                    value = ctaSequence.remove();
                }
            } catch (Exception e) {
                ctaSequence = new LinkedList<String>();
                JSONArray seq = Framework.framework.testData.getJSONObject(Framework.framework.currentUser).getJSONArray("CTA");
                for (int i = 0; i < seq.length(); i++) {
                    ctaSequence.add(seq.getString(i));
                }
                value = ctaSequence.remove();
            }
        } else if (controlName.equalsIgnoreCase("link")) {
            try {
                if (!linkSequence.isEmpty()) {
                    value = linkSequence.remove();
                }
            } catch (Exception e) {
                linkSequence = new LinkedList<String>();
                JSONArray seq = Framework.framework.testData.getJSONObject(Framework.framework.currentUser).getJSONArray("Link");
                for (int i = 0; i < seq.length(); i++) {
                    linkSequence.add(seq.getString(i));
                }
                value = linkSequence.remove();
            }
        } else if (controlName.equalsIgnoreCase("sublink")) {
            try {
                if (!subLinkSequence.isEmpty()) {
                    value = subLinkSequence.remove();
                }
            } catch (Exception e) {
                subLinkSequence = new LinkedList<String>();
                JSONArray seq = Framework.framework.testData.getJSONObject(Framework.framework.currentUser).getJSONArray("Sublink");
                for (int i = 0; i < seq.length(); i++) {
                    subLinkSequence.add(seq.getString(i));
                }
                value = subLinkSequence.remove();
            }
        }
        return value;
    }

    public boolean checkFileWithExt(String downloadPath, String fileExt) {
        boolean flag = false;
        File dir = new File(downloadPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] dir_contents = dir.listFiles();
        for (int i = 0; i < dir_contents.length; i++) {
            if (dir_contents[i].getName().contains(fileExt)) {
                dir_contents[i].delete();
            }
            return flag = true;
        }
        return flag;
    }

    public void deleteFileWithExt(String downloadPath, String fileExt) {
        File dir = new File(downloadPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] dir_contents = dir.listFiles();
        for (int i = 0; i < dir_contents.length; i++) {
            if (dir_contents[i].getName().contains(fileExt)) {
                dir_contents[i].delete();
            }
        }
    }

    public boolean isFileDownloaded(String downloadPath, String fileExt) {
        boolean flag = false;
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();
        for (int i = 0; i < dir_contents.length; i++) {
            if (dir_contents[i].getName().toLowerCase().contains(fileExt.toLowerCase()))
                return flag = true;
        }
        return flag;
    }

    public String retrieveValueFromJson(String jsonKeyToRetrieve, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.get(jsonKeyToRetrieve).toString();
    }

    public void readFromExcel() throws Exception {
        String strCellData = null;
        String s = retrieveLatestFile(System.getProperty("user.dir") + "\\report\\temp");
        Log4jReport.info("Latest file is: " + s);
        FileInputStream fis = new FileInputStream(s);

        XSSFWorkbook xssf = new XSSFWorkbook(fis);
        Sheet sh = xssf.getSheetAt(0);
        int rownTotal = sh.getLastRowNum() - sh.getFirstRowNum();
        Log4jReport.info("Total row count is " + rownTotal);

        for (int i = 0; i < rownTotal + 1; i++) {
            Row row = sh.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Log4jReport.info(row.getCell(j).getStringCellValue() + "||");
            }
            Logger.WriteLog("The Excel is verified along with its data", true, true);
        }
    }

    public String getDesiredDateFromCurrent(String dateFormat, int arg) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        cal.setTime(date);

        cal.add(Calendar.DATE, arg);
        Date desiredDate = cal.getTime();
        String strDate = formatter.format(desiredDate);
        return strDate;
    }

    public void alertCheck() {
        WebDriverWait wait = new WebDriverWait(Framework.framework.webDriver, 5);
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = Framework.framework.webDriver.switchTo().alert();
            String alert_error_msg = alert.getText();
            alert.accept();
            Framework.framework.webDriver.switchTo().defaultContent();
            Logger.WriteLog("Alert message is displayed as : " + alert_error_msg, true, false);
        } catch (Exception e) {
            Log4jReport.info("No alert message was found");
        }
    }

    public void selectValueInDropdown(String webElement, String value, String selectionType) {
        try {
            Select sel = new Select(getObjectFromRepository(webElement));
            if (selectionType.equalsIgnoreCase("text"))
                sel.selectByVisibleText(value);

            else if (selectionType.equalsIgnoreCase("value"))
                sel.selectByValue(value);

            else if (selectionType.equalsIgnoreCase("id"))
                sel.selectByIndex(Integer.parseInt(value));
        } catch (Exception e) {
            Logger.WriteLog("Could not select the value from dropdown : ", false, true);
        }
    }

    public void extractValuesFromListDropdown(String strControlName) throws Exception {
        try {
            List<WebElement> elements = getObjectsFromRepository(strControlName);
            for (WebElement webElement : elements) {
                System.out.println("Dropdown Values are : " + webElement.getText());
                Log4jReport.info("Dropdown Values are : " + webElement.getText());
                break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }
    //class end
}


