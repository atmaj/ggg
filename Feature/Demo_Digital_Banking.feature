Feature: Digisafe_Business_Banking_This would cover all digital banking business scenarios for site



  @DigiBusBank_01
  Scenario: DigiBusBank_01_Verify_Business_Banking_page
  This scenario verifies the contents of Business Banking page

    Given User launches bank site URL
    And Logged User is on Bank Home page
    When User clicks on 'Tab'
      |Business|
    And Validates Contents of Business Banking page

  @DigiBusBank_02
  Scenario: DigiBusBank_02_Branch_Finder
  This scenario enables searching of branch for the bank

    Given User launches bank site URL
    And Logged User is on Bank Home page
    And User clicks on Find a Branch option
    Then Searches for branch


  @DigiBusBank_03
  Scenario: DigiBusBank_03_Verify_Corporate_Banking_page
  This scenario verifies the contents of Corporate Banking page

    Given User launches bank site URL
    And Logged User is on Bank Home page
    When User clicks on 'Tab'
      |Corporate|
    And Validates Contents of Corporate Banking page