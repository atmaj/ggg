Feature: Myte_Homepage_Features

  @Myte_Homepage_1
  Scenario: Myte_Homepage_1_To view assignments in MyTe

    Given User launches MyTe URL
    And Logged user navigates to homepage
    When User clicks on 'Link'
      | Assignments        |

  @Myte_Homepage_2
  Scenario: Myte_Homepage_2_To view homepage in MyTe

    Given User launches MyTe URL
    And Logged user navigates to homepage
    And Logout from application

  @Myte_Homepage_3
  Scenario: Myte_Homepage_3_To view homepage in MyTe

    Given User launches MyTe URLw
    And Logged user navigates to homepagef
    And Logout from application

