Feature: Favori Urun Ekleme Silme

  Background:
    Given Open the "https://www.koctas.com.tr/"
    
    Then I see koctasTask page



  @Test @KoctasWebTask
  Scenario Outline: Koctas Favori Urun Ekleme Silme test case
    Then I must able to see "koctas main page" element at index 1
    Then I click button to "login or register button"
    Then I click button to "login to app button"
    Then I wait for 3 seconds
   # Then I click element: "cookie accept button" if it exists at index 1


    Then Login To Koctas web page "<username-s>" and "<password-s>"
    Then I click button to "login button"

    Then I enter "Gardıroplar" text to search box at index 1

#    Then I click "search button" element at index 1
    Then I must able to see "gardıroplar text" element at index 1
    Then I wait for 5 seconds

    Then I scroll down to "second page button" element

#    Then I click button to "second page button"
    Then I must able to see "second page button" element at index 1

    Then I wait for 3 seconds
    Then I click button to "add to favorites button"
    Then I wait for 5 seconds
    Then I click button to "my favorites button"
    Then I click button to "save button"
    Then I click button to "my account button"

    Then I click button to "my favorite list button"
    Then I must able to see "my favorite list text" element at index 1
    Then I must able to see "product text" element at index 1
    Then I must able to see "create new list text" element at index 1
    Then I click button to "favorite product"
    Then I click button to "remove to favorites button"
    Then I click button to "my favorites button"
    Then I click button to "save button"

    Then I must able to see "check favorite product text" element at index 1




    Examples:
      | username-s   | password-s   |
      | berkerkaplan45@gmail.com      | Koctas34-- |




