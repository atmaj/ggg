Feature: MyteTest

  @Login @Myte_01
  Scenario: Myte_01_Login to MYTE
    Given User launches URL
    And Logged User is on Home page
    And Click on tab
    Then User logout successfully

  @Login @Myte_02
  Scenario: Myte_02_Test login to myte with invalid uesr credentials
    Given User launches URL
    And User is on myte login page
    When User enters username and password
      | abc@accenture.com | password123 |
    And Click Login Button
    Then User is unable to Login

@Login @Myte_03
  Scenario: Myte_03_Login to MYTE
    Given User launches URL
    And User is on login page
    When User enters username and password
    | abc@accenture.com |password123|
    And Click Login Button
    Then User logins sucessfully
    Then User logout successfully
  @Login @Myte_01.1
  Scenario: Myte_01.1_To view assignments in MyTe
    Given User launches MyTe URL
    And Logged user navigates to homepage
    When User clicks on 'Link'
#    | Link |
#    | Assignments |
#    Then user perform the 'Action'
#    |Action|
#    |Verify Assignments|