package com.businessDefinations;

import com.common.UIOperator;
import com.report.Logger;
import com.util.DBConnection;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.sql.Connection;

import static com.framework.Framework.framework;

public class Module2StepDefinition {

    UIOperator oUI = new UIOperator();
    GenericStepDefinitions GenObj = new GenericStepDefinitions();

    @Given("^User launches MyTe URL$")
    public void userLaunchesTheApplication() {
        String applicationURL = "";
        //String userData = new DBConnection().getDBConnection().getUserData();
        //if(userData!=null){
        if (framework.GetProperty("ENV_NAME").equalsIgnoreCase("SIT")) {
            applicationURL = framework.GetProperty("SIT_APP_URL");
        }
        try {
            if (GenObj.launchApplication(applicationURL)) {
                oUI.fluentWait("MyTETextHomepage");
                Logger.WriteLog("Successfully launched the application: '" + applicationURL + "'", true, true);
            } else
                Logger.WriteLog("Could not launch the application: '" + applicationURL + "'", false, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not launch the application: '" + applicationURL + "'", false, true);
        }
    }

    @And("^Logged user navigates to homepage$")
    public void verifyUserOnHomepage() {
        try {
            oUI.fluentWait("MyTETextHomepage");
            if (oUI.isElementPresent("MyTETextHomepage"))
                Logger.WriteLog("User is landed on homepage successfully", true, true);
            else
                Logger.WriteLog("Could not land on homepage!", false, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not land on homepage!", false, true);
        }
    }

    @When("^User clicks on 'Link'$")
    public void user_clicks_on_link(DataTable dataTable) {
        String strLink = oUI.getJSONObjectvalue("LoginUser1", "Link");
        try {
            oUI.fluentWait(strLink);
            oUI.click(strLink);
            Logger.WriteLog("Successfully clicked on:'" + strLink + "'", true, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.WriteLog("Could not click on:'" + strLink + "'", false, true);
        }

    }


}
