package com.businessDefinations;

import com.common.UIOperator;
import com.framework.Framework;
import com.report.Log4jReport;
import com.report.Logger;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class Module1StepDefinition {

    UIOperator oUI = new UIOperator();

    @Given("^User launches URL$")
    public void user_launches_URL() throws Throwable {
        // Write code here that turns the phrase above into concrete actions

        System.out.println(" Checked for site");
        if (user_launches_myte_url()) {
            Thread.sleep(4000);
            System.out.println("Title : " + Framework.framework.webDriver.getTitle());
            Assert.assertTrue("MYTE page loaded", Framework.framework.webDriver.getTitle().equalsIgnoreCase("Time - MyTimeAndExpenses - Version MyTE 12.2.84"));

            try {
                if (oUI.existsElement("MyteLoginPopUp")) {
                    Thread.sleep(3000);
                    System.out.println("MYTE Tutorial Pop up is displayed !!");
                    Logger.WriteLog("MYTE Tutorial Pop up is displayed !!", true, true);
                    //Framework.framework.webDriver.findElement(By.id())
                    oUI.click("TutorialCloseBtn");
                    Thread.sleep(2000);
                    Logger.WriteLog("URL Launched successfully", true, true);
                } else {
                    Logger.WriteLog("MYTE Tutorial Pop up is not displayed , Continuing with flow :) ", true, true);
                }
            } catch (Exception e) {
                e.getMessage();
                System.out.println(" MYTE Tutorial Pop up is NOT present !!");
                Logger.WriteLog("MYTE Tutorial Pop up is NOT present", true, true);
            }
        }
    }

    public boolean user_closes_myte_site() {
        try {
            Framework.framework.webDriver.quit();
            Logger.WriteLog("Browser is closed", true, true);
            return true;
        } catch (Exception e) {
            e.getMessage();
            Log4jReport.exception(Log4jReport.getCallerInfo(Exception.class) + e.getMessage(), e);
        }
        return true;
    }

    public boolean user_launches_myte_url() {
        boolean flag = false;
        try {

            System.out.println("Launching MYTE URL !!");
            Framework.framework.webDriver.get("https://myte.accenture.com");
            Framework.framework.webDriver.manage().window().maximize();
            Logger.WriteLog("MYTE URL Launched", true, true);
            flag = true;

        } catch (Exception e) {
            flag = false;
            e.getMessage();
            Logger.WriteLog("Could not launch MyTe URL!", false, true);
            Log4jReport.exception(Log4jReport.getCallerInfo(Exception.class) + e.getMessage(), e);
        }
        return flag;
    }

    @Given("^User is on myte login page$")
    public void user_is_on_myte_login_page() throws Throwable {
        // Write code here that turns the phrase above into concrete actions

    }

    @And("^Logged User is on Home page$")
    public void validate_user_home_page() {

        try {
            System.out.println("MYTE Home page" + oUI.existsElement("timesheetstatus"));
            Assert.assertTrue("MYTE HOme page is launched ! ", oUI.existsElement("timesheetstatus"));
            Logger.WriteLog("MYTE HOme page is launched", true, true);
        } catch (Exception e) {
            e.getMessage();
            Logger.WriteLog("Homepage screen not loaded !", false, true);
            Log4jReport.exception(Log4jReport.getCallerInfo(Exception.class) + e.getMessage(), e);
        }
    }

    @And("^Click on tab$")
    public void click_on_tab() {
        try {

            oUI.clickOnWebElement("//*[@id='ctl00_ctl00_MainContentPlaceHolder_Expense-IN']//span");
            System.out.println("User Clicked on Expenses tab !!");
            Logger.WriteLog("User Clicked on Expenses tab", true, true);
        } catch (Exception e) {
            e.getMessage();
            Logger.WriteLog("Could not click on expenses tab on MyTe Homepage", false, true);
            Log4jReport.exception(Log4jReport.getCallerInfo(Exception.class) + e.getMessage(), e);
        }
    }

    @When("^User enters username and password$")
    public void user_enters_username_and_password(DataTable arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
        // E,K,V must be a scalar (String, Integer, Date, enum etc)
    }

    @When("^Click Login Button$")
    public void click_Login_Button() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^User logins sucessfully$")
    public void user_logins_sucessfully() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^User logout successfully$")
    public void user_logout_successfully() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            Framework.framework.webDriver.quit();
            System.out.println("User is logged out successfully !!");
        } catch (Exception e) {

            e.getMessage();
        }
    }

}
