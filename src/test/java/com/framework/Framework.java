package com.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import com.common.UIOperator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
import org.openqa.selenium.WebDriver;

public class Framework {
	public WebDriver webDriver;
	public WebDriver driver;
	public static Framework framework;
	private Properties envProperties;
	private Properties configProperties;
	public UIOperator UI;
	public static boolean isTestPassOrFail=false;
	String strCurrentWindowHandle="";
	public Map<String, String> tagHashTestData;
	public JSONObject testData;
	public Queue<String> loginSequence;
	public static String strCurrentScenarioName;
	public String strSheetName;
	public Map<String, String> createdTestData;
	public String currentUser;

	/**
	 * Framework :Private Constructor for Framework
	 * @throws Exception
	 */

	private Framework() throws Exception {
		if(!LoadConfig())
		{
			throw new Exception("Unable to Load Configuration froom Config File");
		}
	}

	public static Framework createInstance(String strCurrentScenarioName) throws Exception {
		if(Framework.framework==null)
		{
			Framework.framework=new Framework();
			if(!WebDriverWrapper.LoadWebDriver())
			{
				throw new Exception("Unable to load Webdriver");
			}
			framework.UI=new UIOperator();
			framework.tagHashTestData=UIOperator.initializeExcel(System.getProperty("tagTestData"));
			framework.testData=new JSONObject(framework.tagHashTestData.get("DATA_MAP").toString());
			framework.loginSequence=new LinkedList<String>();
			framework.createdTestData=new HashMap<String,String>();
			JSONArray seq=framework.testData.getJSONArray("Login_Sequence");
			for(int i=0;i<seq.length();i++)
			{
				System.out.println("login Sequence");
				framework.loginSequence.add(seq.getString(i));
			}
			System.out.println("framework.testData"+framework.testData);
			framework.strCurrentScenarioName=strCurrentScenarioName;
		}
		return Framework.framework;
	}
	/*
	LoadConfig: Function is to load configuration file in properties
	 */
	public boolean LoadConfig() {
		envProperties = new Properties();
		String strConfigPath = System.getProperty("user.dir") + "\\Config\\Environment.properties";
		System.out.println("Picked up configuration path as " + strConfigPath);
		try {
			FileInputStream fis = new FileInputStream(strConfigPath);
			envProperties.load(fis);
			configProperties=new Properties();
			if(envProperties.getProperty("ENV_NAME").equalsIgnoreCase("SIT")){
				strConfigPath=System.getProperty("user.dir")+"\\Config\\Config.properties";
				fis = new FileInputStream(strConfigPath);
				configProperties.load(fis);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public String GetProperty(String strPropertyName) {
		return envProperties.getProperty(strPropertyName);
	}

	public String GetConfigProperty(String strPropertyName) {
		return configProperties.getProperty(strPropertyName);
	}
}
