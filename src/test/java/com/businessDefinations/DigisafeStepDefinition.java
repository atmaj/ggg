package com.businessDefinations;

import com.accenture.axe.core.AXE;
import com.common.UIOperator;
import com.framework.Framework;
import com.report.Logger;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static com.framework.Framework.framework;

import java.net.URL;

public class DigisafeStepDefinition {

    UIOperator oUI = new UIOperator();
    GenericStepDefinitions GenObj = new GenericStepDefinitions();

    @Given("^User launches bank site URL$")
    public void userLaunchesTheBankApplication() {
        String applicationURL = "";
        if (framework.GetProperty("ENV_NAME").equalsIgnoreCase("SIT")) {
            applicationURL = framework.GetProperty("SIT_APP_URL");
        }
        try {
            if (GenObj.launchApplication(applicationURL)) {
                oUI.fluentWait("BankHomePage");
                Logger.WriteLog("Successfully launched the application: '" + applicationURL + "'", true, true);
            
               
///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////
                
                URL scriptUrl = new URL("file:/C:/Users/s.m.modabbir/Documents/Project/AXE-Accessibility-Demo/bin/axe.min.js");
                AXE.inject(framework.driver, scriptUrl);
        		JSONObject responseJSON = new AXE.Builder(framework.driver,    scriptUrl).analyze();
        		JSONArray violations = responseJSON.getJSONArray("violations");
        		if (violations.length() == 0)
        		{ 
        			System.out.println("No violations found"); 
        		}
        		else {
        			AXE.writeResults("C:\\Users\\s.m.modabbir\\Desktop\\AxeReport\\", responseJSON);
        			System.out.println(AXE.report(violations));
        			System.out.println("\n\n\n>>>>>>>>>>Report saved in this path : C:\\Users\\s.m.modabbir\\Desktop\\AxeReport\\\n\n\n");
        		}
        		
///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////// 
                
                
            } else
                Logger.WriteLog("Could not launch the application: '" + applicationURL + "'", false, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not launch the application: '" + applicationURL + "'", false, true);
        }
    }

    @And("^Logged User is on Bank Home page$")
    public void verifyUserOnBankHomepage() {
        try {
            oUI.fluentWait("BankHomePage");
            if (oUI.isElementPresent("BankHomePage")) {
                Logger.WriteLog("User is landed on homepage successfully", true, true);
            
            
                // AXE.run(Framework.framework.driver, inputPath);
                URL scriptUrl = new URL("file:/C:/Users/s.m.modabbir/Documents/Project/AXE-Accessibility-Demo/bin/axe.min.js");
                AXE.inject(framework.driver, scriptUrl);
        		JSONObject responseJSON = new AXE.Builder(framework.driver,    scriptUrl).analyze();
        		JSONArray violations = responseJSON.getJSONArray("violations");
        		if (violations.length() == 0)
        		{ 
        			System.out.println("No violations found"); 
        		}
        		else {
        			AXE.writeResults("C:\\Users\\s.m.modabbir\\Desktop\\AxeReport\\", responseJSON);
        			System.out.println(AXE.report(violations));
        			System.out.println("\n\n\n>>>>>>>>>>Report saved in this path : C:\\Users\\s.m.modabbir\\Desktop\\AxeReport\\\n\n\n");
        		}
                
            
            }
            else {
                Logger.WriteLog("Could not land on homepage!", false, true);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Exception occured ! Could not land on homepage!", false, true);
        }
    }

   @When("^User clicks on 'Tab'$")
    public void user_clicks_on_tab(DataTable dataTable) {
        String strTab = oUI.getJSONObjectvalue("LoginUser1", "Tab");
        WebElement ele = oUI.getObjectFromRepository(strTab);
        try {

            ((JavascriptExecutor) framework.webDriver).executeScript("arguments[0].scrollIntoView(true);",ele );
            oUI.fluentWait(strTab);
            oUI.click(strTab);
            Logger.WriteLog("Successfully clicked on:'" + strTab + "'", true, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not click on:'" + strTab + "'", false, true);
        }

    }

    @And("^Validates Contents of Business Banking page$")
    public void validateBusinessBankpagecontents() {


        //String strsubTab1 = oUI.getJSONObjectvalue("LoginUser1", "Contents");
        // String strsubTab2 = oUI.getJSONObjectvalue("LoginUser1", "Sectors_Tab");
        String strcontent = oUI.getJSONObjectvalue("LoginUser1", "Option");

        try {
            oUI.fluentWait("BusinessBankingHomePage");
            if (oUI.isElementPresent("BusinessBankingHomePage")) {
                Logger.WriteLog("User is landed on Business Bank page successfully", true, true);
                // Logger.WriteLog("StartUp Business Content is displayed on Business Bank page successfully", true, true);
              /*  if(dataTable.toString().equalsIgnoreCase("StartUp Business")) {
                    oUI.fluentWait(strcontent);
                    if (oUI.isElementPresent(strcontent))
                        Logger.WriteLog("StartUp Business Content is displayed on Business Bank page successfully", true, true);
                    else
                        Logger.WriteLog("Could not find StartUp Business Content on Business Bank page!", false, true);
                }
                else if (dataTable.toString().equalsIgnoreCase("Accounts")) {
                    oUI.fluentWait(strcontent);
                    if (oUI.isElementPresent(strcontent))
                        Logger.WriteLog("Accounts Tab is displayed on Business Bank page successfully", true, true);
                    else
                        Logger.WriteLog("Could not find Accounts Tab on Business Bank page!", false, true);
                } else if  (dataTable.toString().equalsIgnoreCase("Banking_From_Home")) {*/
                oUI.fluentWait(strcontent);
                if (oUI.isElementPresent(strcontent))
                    Logger.WriteLog("Contents on Business Bank page are validated successfully", true, true);
                else
                    Logger.WriteLog("Could not validate Contents on Business Bank page!", false, true);

                // }
               /* oUI.fluentWait(strsubTab2);
                if (oUI.isElementPresent(strsubTab2))
                    Logger.WriteLog("Sectors Tab is displayed on Business Bank page successfully", true, true);
                else
                    Logger.WriteLog("Could not find Sectors Tab on Business Bank page!", false, true);
*/
            }
            else
                Logger.WriteLog("Could not land on Business Bank page!", false, true);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not land on Business Bank page!", false, true);
        }
    }

    @And("^Validates Contents of Corporate Banking page$")
    public void validateCorporateBankpagecontents() {

        String strcontent = oUI.getJSONObjectvalue("LoginUser1", "Option");

        try {
            oUI.fluentWait("CorporateBankingHomePage");
            if (oUI.isElementPresent("CorporateBankingHomePage")) {
                Logger.WriteLog("User is landed on Corporate Bank page successfully", true, true);

                oUI.fluentWait(strcontent);
                if (oUI.isElementPresent(strcontent))
                    Logger.WriteLog("Contents on Corporate Bank page are validated successfully", true, true);
                else
                    Logger.WriteLog("Could not validate Contents on Corporate Bank page!", false, true);


            }
            else
                Logger.WriteLog("Could not land on Corporate Bank page!", false, true);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not land on Corporate Bank page!", false, true);
        }
    }

    @And("^User clicks on Find a Branch option$")
    public void clickFindBranch() {
        String strBtn = oUI.getJSONObjectvalue("LoginUser1", "Button");
        try {
            oUI.fluentWait(strBtn);
            oUI.click(strBtn);
            Logger.WriteLog("Successfully clicked on:'" + strBtn + "'", true, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not click on:'" + strBtn + "'", false, true);
        }


    }


    @And("^Searches for branch$")
    public void searchBranch() {
        String strTextbox = oUI.getJSONObjectvalue("LoginUser1", "SearchBox");
        String strButton1 = oUI.getJSONObjectvalue("LoginUser1", "SearchButton");
        String strButton2 = oUI.getJSONObjectvalue("LoginUser1", "BranchButton");
        try {
            oUI.fluentWait(strTextbox);
            oUI.EnterText(strTextbox,"London");
            oUI.click(strButton1);
            oUI.fluentWait(strButton2);
           if( oUI.isElementPresent(strButton2)) {
               Logger.WriteLog("Successfully Searched Branch !!", true, true);

           }
           else
               Logger.WriteLog("Could not search Branch !!", false, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not click on:'" + strButton1 + "'", false, true);
        }


    }



}
