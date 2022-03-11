package com.framework;

import java.awt.Frame;
import java.awt.image.BufferedImage;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.file.FileUtil;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.report.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class WebDriverWrapper {

	public static boolean LoadWebDriver(){
		try{ 
			if(Framework.framework.GetProperty("BROWSERTYPE").toUpperCase().equals("CHROME")){
				Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
				String strChromeDriverPath = System.getProperty("user.dir") + "\\resources\\Drivers\\chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", strChromeDriverPath);
				Framework.framework.webDriver = new ChromeDriver();
			}
			else if(Framework.framework.GetProperty("BROWSERTYPE").toUpperCase().equals("IE")){
				String strIEDriverPath = System.getProperty("user.dir") + "\\resources\\Drivers\\IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", strIEDriverPath);
				Framework.framework.webDriver = new InternetExplorerDriver();
			}
			//Set default timeout for WebDriver
			SetPageLoadTimeOut(Long.parseLong(Framework.framework.GetProperty("PAGE_LOAD_TIMEOUT")));
			SetImplicitTimeOut(Long.parseLong(Framework.framework.GetProperty("IMPLICIT_TIMEOUT")));
			SetScriptTimeOut(Long.parseLong(Framework.framework.GetProperty("SCRIPT_TIMEOUT")));
			
			return true;
		}
		
		
		catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static void SetPageLoadTimeOut(long intValue){
		Framework.framework.webDriver.manage().timeouts().pageLoadTimeout(intValue, TimeUnit.SECONDS);
	}
	public static void SetImplicitTimeOut(long intValue){
		Framework.framework.webDriver.manage().timeouts().implicitlyWait(intValue, TimeUnit.SECONDS);
	}
	public static void SetScriptTimeOut(long intValue){
		Framework.framework.webDriver.manage().timeouts().setScriptTimeout(intValue, TimeUnit.SECONDS);
	}
	public static List<String> screenshotName = new ArrayList<String>();
	
	public static String CaptureSCreenshot() throws IOException{ //Create a file in Logs folder with current date if not exists
		String screenshotOption = Framework.framework.GetProperty("SCREENSHOT_REQD");
		if(screenshotOption == null)
			screenshotOption = "yes";
		if(screenshotOption.equalsIgnoreCase("yes")){
			DateFormat format = new SimpleDateFormat("dd_MMM_YY");
			Date dateobj = new Date();
			String strDirName = format.format(dateobj);
			File file = new File (Logger.fullname + "\\" + strDirName);
			if(! file.exists())
				file.mkdir();
			format = new SimpleDateFormat("HH_mm_ss");
			dateobj = new Date();
			String strFileName = format.format(dateobj) + ".png";
			String strCompleteFileName = Logger.fullname + "\\" + strDirName ; //"\\" + strFileName;
			screenshotName.add(strCompleteFileName);
			File scrFile = ((TakesScreenshot) Framework.framework.webDriver).getScreenshotAs(OutputType.FILE);
			File screenshotFile = new File(strCompleteFileName, strFileName);
		//	BufferedImage image = Shutterbug.shootPage(Framework.framework.webDriver, ScrollStrategy.BOTH_DIRECTIONS, 500, true).getImage();
			//FileUtil.writeImage(image, "png", screenshotFile);
			String strWebLink = "<a href =\""+".\\" + strDirName + "\\" + strFileName + "\" target=\"_blank\"> Screenshot </a>";
			FileUtils.copyFile(scrFile,screenshotFile);
			return strWebLink;
			
		}
		else
			return "No screenshot";
				
		
	}
}
