@KoctasMobileTask
Feature: Koctas Favori Urun Ekleme Silme





  @KoctasMobileTask
  Scenario Outline:Koctas Favori Urun Ekleme Silme Mobile Test Automation

    Given I see "KoctasTask" page
    When I login with username:<Username> password:<Password>
    And I wait "my account button" element and click
    And I wait "my favorite list" element and click
    Then I wait for 3 seconds
    Then I wait "control to zero product" element
    Then I wait "categories button" element and click
    Then I wait "heating and cooling button" element and click
    Then I wait "klima button" element and click
    Then I wait "see all of them button" element and click
    Then I wait "choose first product" element and click
    Then I wait "favorite icon" element and click
    Then I wait "create new list" element and click
    Then I send "my list" text to "list name box" element
    Then I wait "save button" element and click
    Then I wait "info pop-up" element
    Then I wait "ok button" element and click
    Then I wait "my list checkbox" element and click

    Then I wait "save button" element and click
    Then I wait "added product pop-up" element
    Then I wait "ok button" element and click

    Then I wait "back button" element and click
    Then I wait "my account button" element and click
    And I wait "my favorite list" element and click
    And I wait "control to one product" element
    And I wait "my favorite list card" element and click
    And I wait "my favorite product card" element
    And I wait "remove to favorite list" element and click
    And I wait "warning pop-up" element
    And I wait "ok button" element and click
    And I wait "no product in your list text" element
    And I wait "back button" element and click
    Then I wait "control to zero product" element




    Then I wait "tobi welcome message" element
    Then I wait "tobiXButton" element and click
    Then I wait "tobiIcon" element

    Examples:
      | Username                 |  Password |
      | berkerkaplan45@gmail.com | Koctas34-- |
