package com.project.stepdefs;

import com.saf.framework.*;
import io.appium.java_client.MobileElement;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;

public class StepDefs extends MyTestNGBaseClass {

    CommonLib commonLib = new CommonLib();
    int timeout = 30;
    public String uuid = UUID.randomUUID().toString();
    public boolean checkLoginControl = false;
    public static HashMap<String, String> strings = new HashMap<String, String>();
    InputStream stringsis;
    TestUtils utils;
    Parser parser = new Parser();


    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("After hook");

            try {

                    oDriver.quit();

            }
             catch (Exception e) {

                oDriver.quit();
            }
            System.out.println("action for failed scenario.");

    }


    @Given("Open the {string}")
    public void openUrl(String URL) throws Exception {
        commonLib.navigateToURL(oDriver, URL);
    }

    @When("LoginToSOT {string} and {string}")
    public void loginToApp(String username, String password) throws Throwable {
        commonLib.loginToApp(username, password);
    }

    @When("User logs in")
    public void clickLoginToApp() throws Throwable {
        clickElement("login button", 1);
    }

    @Then("OTP step passed")
    public void otpStep() throws Throwable {
        enterText("222222", "OTP text area", 1);
        Thread.sleep(2550);
        scrollDownToElement("devam button",1);
        WebElement devamo = commonLib.findElement("devam button",1);
        devamo.click();
    }


    @When("I see {string} element at index {int}")
    public void whenElementVerify(String element, int index) throws Throwable {
        verifyText(element, index);
    }

    @Then("{string} element is visible at index {int}")
    public void thenElementVerify(String element, int index) throws Throwable {
        verifyText(element, index);
    }

    @When("{string} element is not visible at index {int}")
    public void whenAbsentElementVerify(String element, int index) throws Throwable {
        commonLib.checkElementVisibilityByFalseTest(element, index);
    }


    @When("I click {string} element at index {int}")
    public void ElementClick(String element, int index) throws Throwable {
        clickElement(element, index);
    }

    @And("I must able to see {string} element at index {int}")
    public void seeControl(String element, int index) throws Throwable {
        commonLib.checkElementVisibility(element, index);
    }
    @And ("I must be able to see {string} element at index {int}")
    public void see2Control(String element, int index) throws Throwable {



        for(int i=1;i<50;i++)
        {

            commonLib.checkElementVisibility(element, i);
        }
    }

    @Then("^I see (.*) page$")
    public void seePage(String page) {
        commonLib.seePage(page);
    }

    @When("^(?:I )?click element: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean clickElement(String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                System.out.println("Clicked on object-->" + element);
                Allure.addAttachment("Element is clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                // reportResult("PASS", "I clicked the element: " + element, true);
                return true;
            }
        } catch (Exception e) {
            //reportResult("FAIL", "I cannot clicked the element: " + element, true);
            Allure.addAttachment("Element is not clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Assert.fail("Could not clicked the element:" + element);
            flag = false;
        }
        return flag;
    }

    @When("^(?:I )?have to verify the text for: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean verifyText(String element, int index) throws Exception {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                String xmlFileName = "strings.xml";
                stringsis = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
                utils = new TestUtils();
                strings = utils.parseStringXML(stringsis);

                object.click();
                String actualErrTxt = object.getText();
                if (element.contains("musteri portali")) {
                    String expectedErrText = strings.get("musteri portali");
                    System.out.println("actual text - " + actualErrTxt + "\n" + "expected text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    // reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("simkart degisikligi gecmisi linki")) {
                    String expectedErrText = strings.get("simkart degisikligi gecmisi linki");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("musteri dogrulama islemi texti")) {
                    String expectedErrText = strings.get("musteri dogrulama islemi texti");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //  reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("cancel popup")) {
                    String expectedErrText = strings.get("cancel popup");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("islem tarihi texti")) {
                    String expectedErrText = strings.get("islem tarihi texti");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("satışçı text")) {
                    String expectedErrText = strings.get("satışçı text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("bayi kodu text")) {
                    String expectedErrText = strings.get("bayi kodu text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("gerekce text")) {
                    String expectedErrText = strings.get("gerekce text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //  reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("eski simkart seri no texti")) {
                    String expectedErrText = strings.get("eski simkart seri no texti");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //  reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("yeni simkart seri no texti")) {
                    String expectedErrText = strings.get("yeni simkart seri no texti");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("ok butonu")) {
                    String expectedErrText = strings.get("ok butonu");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("kullanici girisi text")) {
                    String expectedErrText = strings.get("kullanici girisi text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("kullaadres girisi text")) {
                    String expectedErrText = strings.get("kullaadres girisi text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("kullanici kendisi text")) {
                    String expectedErrText = strings.get("kullanici kendisi text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("chipped identity card text")) {
                    String expectedErrText = strings.get("chipped identity card text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("old identity card text")) {
                    String expectedErrText = strings.get("old identity card text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("foreign identity card text")) {
                    String expectedErrText = strings.get("foreign identity card text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("tc passport new type identity card text")) {
                    String expectedErrText = strings.get("tc passport new type identity card text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("tc passport old type navy blue identity card text")) {
                    String expectedErrText = strings.get("tc passport old type navy blue identity card text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("foreigner passport")) {
                    String expectedErrText = strings.get("foreigner passport");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("fly attendant passport")) {
                    String expectedErrText = strings.get("fly attendant passport");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("marine passport")) {
                    String expectedErrText = strings.get("marine passport");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("nato command passport")) {
                    String expectedErrText = strings.get("nato command passport");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("traveling passport")) {
                    String expectedErrText = strings.get("traveling passport");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("enter tc number error text")) {
                    String expectedErrText = strings.get("enter tc number error text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("tc number missing character error text")) {
                    String expectedErrText = strings.get("tc number missing character error text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("kimlik bilgileri text")) {
                    String expectedErrText = strings.get("kimlik bilgileri text");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                }
            }
        } catch (Exception e) {
            Allure.addAttachment("Verification does not completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //reportResult("FAIL", "An error during assertion. " + element, true);
            Assert.fail("Verification is not completed:" + element);
            oDriver.quit();
            flag = false;
        } finally {
            if (stringsis != null) {
                stringsis.close();
            }
        }
        return flag;
    }

    @Then("^I enter \"([^\"]*)\" text to (.*) at index (\\d+)")
    public boolean enterText(String text, String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
//                WebDriver driver = null;
//                WebElement webElement = null;
//                String value = null;

//                JavascriptExecutor jse = (JavascriptExecutor)oDriver;
//                jse.executeScript("arguments[0].value='"+ text +"';", object);
                // object.sendKeys(Keys.NUMPAD1);
                object.sendKeys(text);
                System.out.println("The text has been entered:" + text);
                Allure.addAttachment("The text has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                //reportResult("PASS", "I entered the text: " + text, true);

                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The text has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //  reportResult("FAIL", "I cannot entered the element: " + text, true);
            Assert.fail("Could not entered the text:" + text);
            flag = false;
        }
        return flag;
    }

    @Then("^I enter a email to (.*) at index (\\d+)")
    public boolean enterEmail(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        String email = commonLib.getRandomString() + "@example.com";
        try {
            if (object != null) {
                object.sendKeys(email);
                System.out.println("The email has been entered:" + email);
                Allure.addAttachment("The email has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                // reportResult("PASS", "I entered the email: " + email, true);
                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The email has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //reportResult("FAIL", "I cannot entered the element: " + email, true);
            Assert.fail("Could not entered the email:" + email);
            flag = false;
        }
        return flag;
    }

    @Then("I go to top of the site")
    public void topOfWebsite() {
        ((JavascriptExecutor) oDriver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
    }

    @Then("^I enter unique text to (.*) at index (\\d+)")
    public boolean uniqueText(String element, int index) throws InterruptedException {
        //mouseHover(element);
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        String text = "automation" + uuid;
        boolean flag = false;
        try {
            if (object != null) {
                object.sendKeys(text);
                System.out.println("The text has been entered.");
                Allure.addAttachment("The text has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                //  reportResult("PASS", "I entered the text: " + text, true);
                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The text has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            // reportResult("FAIL", "I cannot entered the element: " + text, true);
            Assert.fail("Could not entered the text:" + text);
            flag = false;
        }
        return flag;
    }

    @Then("^I verify the area (.*) by read only at index (\\d+)")
    public boolean readOnlyAreaCheck(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;

        try {
            if (object != null) {
                if (!object.isEnabled()) {
                    System.out.println("The area is a read only area. Cannot be editable.");
                    Allure.addAttachment("The area is a read only area. Cannot be editable.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    // reportResult("PASS", "The area is a read only area. Cannot be editable.", true);
                    return true;
                }
            }
        } catch (Exception e) {
            Allure.addAttachment("The area is not a read only area. Can be editable.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //reportResult("FAIL", "The area is not a read only area. Can be editable.", true);
            Assert.fail("The area is not a read only area. Can be editable.");
            flag = false;
        }
        return flag;
    }

    @Then("^I clear text to (.*) at index (\\d+)")
    public boolean clearText(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                Thread.sleep(1000);
                object.sendKeys(Keys.CONTROL, "a");
                object.sendKeys(Keys.DELETE);
                Thread.sleep(1000);
                System.out.println("The text has been deleted.");
                Allure.addAttachment("The text has been deleted.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                //  reportResult("PASS", "The text has been deleted.", true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("The text has not been deleted.");
            Allure.addAttachment("The text has not been deleted.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //reportResult("FAIL", "The text has not been deleted", true);
            Assert.fail("Waiting element is not found!");
            flag = false;
        }
        return flag;
    }

    @And("^I wait (.*) element (\\d+) seconds at index (\\d+)")
    public void waitElement(String element, int timeout, int index) throws InterruptedException {
        commonLib.waitElement(element, timeout, index);
    }

    @When("User select element: {string} under {string} at index {int}")
    public boolean selectElement(String text, String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                System.out.println("Select the object type-->" + text + element);
                Select select = new Select(object);
                select.selectByVisibleText(text);
                Allure.addAttachment("The selection is done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                return true;
            }
        } catch (Exception e) {
            System.out.println("The selection cannot be done.");
            Allure.addAttachment("The selection cannot be done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //reportResult("FAIL", "The selection cannot be done.", true);
            Assert.fail("The selection cannot be done!");
            flag = false;
        }
        return flag;
    }

    @And("^I need to just wait")
    public void justWait() throws InterruptedException {
        Thread.sleep(25000);
    }

    @Given("I wait for {int} seconds")
    public void sleep(int sec) throws InterruptedException {
         commonLib.sleep(sec);
    }

    @Then("^(?:I )?get the information by copying the value from: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean copyElement(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                Thread.sleep(1000);
                object.sendKeys(Keys.CONTROL, "c");
                Thread.sleep(1000);
                System.out.println("The text has been copied.");
                Allure.addAttachment("The text has been copied.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                //reportResult("PASS", "The text has been copied.", true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("The copy action cannot be done.");
            Allure.addAttachment("The copy action cannot be done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //reportResult("FAIL", "The copy action cannot be done.", true);
            Assert.fail("The copy action cannot be done!");
            flag = false;

        }
        return flag;
    }

    @Then("^(?:I )?copy the information by copying the value to: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean pasteElement(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                Thread.sleep(1000);
                object.sendKeys(Keys.CONTROL, "v");
                Thread.sleep(1000);
                System.out.println("The text has been pasted.");
                Allure.addAttachment("The text has been pasted.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                // reportResult("PASS", "The text has been pasted.", true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("The paste action cannot be done.");
            Allure.addAttachment("The paste action cannot be done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            //reportResult("FAIL", "The paste action cannot be done.", true);
            Assert.fail("The paste action cannot be done!");
            flag = false;

        }
        return flag;
    }

    @When("^(?:I )?double click element: (\\w+(?: \\w+)*) at index (\\d+)")
    public void doubleClickElement(String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        commonLib.doubleClickElement(object);
    }

    @Given("I go to \"([^\"]*)\" with this username: \"([^\"]*)\" and this password:\"([^\"]*)\"")
    public void loginSystem(String URL, String username, String password) throws Exception {
        openUrl(URL);
        seePage("login");
        enterText(username, "username text area", 1);
        enterText(password, "password text area", 1);
        waitElement("login button", timeout, 1);
        clickElement("login button", 1);
        seePage("home");
    }

    @When("My website is close")
    public void checkURLControl(String URL) throws Exception {
        //eğer URL kapalı ise git URL'i ayağa kaldır.
        if (checkLoginControl = false) {
            openUrl(URL);
            checkLoginControl = true;
        }
        //eğer URL açık ise (checkLoginControl = true)  hata bas.
        else {
            throw new InterruptedException("Your page is already opened. You cannot open the URL one more time.");
        }
    }

    @When("I confirm login page by {string} index {int}")
    public boolean confirmLoginPage(String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null && object.isDisplayed()) {
                object.click();
                System.out.println(element + " opened");
                Allure.addAttachment("Element is clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                oDriver.findElement(By.xpath("//*[@id=\"headerLogout\"]")).click();
                oDriver.findElement(By.xpath("//*[contains(text(),'Evet')]")).click();
                oDriver.findElement(By.xpath("//*[@id=\"login\"]"));
                System.out.println("Session ended. I successfully logged out");
                //reportResult("PASS", "I successfully logged out: " + element, true);
                return true;
            }
        } catch (Exception e) {
            //reportResult("FAIL", "I cannot find the element: " + element, true);
            Allure.addAttachment("Element is not found.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Assert.fail("Could not find the element:" + element);
            flag = false;
        }
        return flag;
    }

    @When("I click element: {string} if it exists at index {int}")
    public void clickIfExists(String element, int index) {
        boolean flag = false;
        try {
            WebElement object = commonLib.findElement(element, index, false);
            if (object != null && object.isDisplayed()) {
                System.out.println(element + " exists.");
                object.click();
                Allure.addAttachment("Element is clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                //reportResult("PASS", "I clicked the element: " + element, true);
                flag = true;
            } else {
                System.out.println(element + " does not exist.");
                Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                Allure.step("The element does not exist", Status.SKIPPED);
                flag = false;
            }
        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        }
    }

    @Then("I check if confirmation step for the gsm or not")
    public void checkGSMConfirmation() {
        try {
            WebElement confirmationGSM = commonLib.findElement("musteri dogrulama islemi texti", 1, false);
            if (confirmationGSM != null && confirmationGSM.isDisplayed()) {
                System.out.println("Need to confirmation step. Inititate.");
                Allure.addAttachment("Element is seen.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                clickElement("tek kullanimlik sms sifresiyle radio butonu", 1);
                clickElement("sms gonder butonuna", 1);
                enterText("222222", "OTP text area", 1);
                clickElement("devam button", 1);
            } else {
                seePage("musteriPortali");
                Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                Allure.step("The element does not exist", Status.SKIPPED);
            }
        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    @When("I check if there is a {string} or not")
    public void checkAddressInfo(String element) throws NullPointerException{
        try {

         //   WebElement existingAddress = commonLib.findElement(element, 1, false);
            //existingAddress != null && existingAddress.isDisplayed()
            if (commonLib.checkDisplayed("adresi kaydet button")) {

                //      commonLib.findElement("yeni adres ekle button", 1).click();
                clickIfExists("adres tipi",1);
                // commonLib.clickElement(commonLib.findElement("adres tipi",1));
                // commonLib.findElement("adres tipi", 1).click();
                clickIfExists("adres tipi ev", 1);
                // commonLib.findElement("adres tipi ev", 1).click();
                scrollDownToElement("sehir",1);
                commonLib.findElement("sehir", 1).click();
                clickElement("istanbul selection",1);
//                scrollDownToElement("istanbul selection", 1);
//                commonLib.findElement("istanbul selection", 1).click();
                clickElement("ilce",1);
                //   commonLib.findElement("ilce",1).click();
                Thread.sleep(2000);

                commonLib.findElement("ilce selection", 1).click();
                commonLib.findElement("mahalle", 1).click();
                commonLib.clickElement(commonLib.findElement("mahalle selection",1));
                commonLib.findElement("cadde adi text box", 1).sendKeys("cadde");
                commonLib.findElement("sokak adi text box", 1).sendKeys("sokak");
                commonLib.findElement("site no text box", 1).sendKeys("3");
                commonLib.findElement("apartman no text box", 1).sendKeys("12");
                commonLib.findElement("daire no text box", 1).sendKeys("1");
                clickIfExists("adresi kaydet button", 1);
                Thread.sleep(3000);
                // commonLib.findElement("adres girisi sayfasi ileri button",1).click();
                Thread.sleep(2000);
                System.out.println(element + " does not exist.");
                Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                Allure.step("The element does not exist", Status.SKIPPED);


            } else {

                System.out.println("Existing adress.");
                //      commonLib.findElement("adres girisi sayfasi ileri button",1).click();

            }
        } catch (Exception e) {
         System.out.println("cache");
        }
    }

    public void scrollDownToElement(String searchedElement, int index) {
        WebElement object = commonLib.findElement(searchedElement, index);
        boolean flag = false;
        try {
            if (object != null) {
                String xmlFileName = "strings.xml";
                stringsis = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
                utils = new TestUtils();
                strings = utils.parseStringXML(stringsis);

                //object.click();
                String actualErrTxt = object.getText();
                if (searchedElement.contains("musteri portali")) {
                    String expectedErrText = strings.get("musteri portali");
                    System.out.println("actual text - " + actualErrTxt + "\n" + "expected text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    //return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @And("wait until element {string} is {string}")
    public void waitUntilElementIs(String element, String waitFor) {
        if (waitFor.equals("clickable")) {
            CommonLib.waitElementClickable(oDriver, commonLib.findElement(element, 1));
        }
        if (waitFor.equals("visible")) {
            CommonLib.waitElementVisible(oDriver, commonLib.findElement(element, 1));
        }
    }



    @Then("I force click element: {string} at index {int}")
    public void iForceClickElementAtIndex(String element, int index) {
        Actions actions = new Actions(oDriver);
        //actions.click(commonLib.findElement(element,index)).build().perform();
        actions.moveToElement(commonLib.findElement(element, index)).click().build().perform();
        System.out.println("click element " + element);
    }


//    @When("imageprocess")
//    public void image() throws Exception {
//
//        Screen screen = new Screen();
//
//        Pattern pattern = new Pattern("C:\\Users\\amdocsberkaplan\\ideaProjects\\image_process\\dee-sot-sotproject-web\\.idea\\images\\login_button.png");
//        pattern.similar(0.7f);
//
//        screen.wait(pattern);
//        screen.click(pattern);
//    }


    @When("I fill id information {string} element")
    public void idInfo(String element) throws NullPointerException{
        try {
            WebElement idInformationPage = commonLib.findElement("kimlik tipi text", 1);
            if (idInformationPage.isDisplayed()) {
                commonLib.findElement("customer id box",1).sendKeys(element);
                commonLib.findElement("father name box",1).sendKeys("SOTO");
                commonLib.findElement("date of birth box",1).sendKeys("12.12.1992");
                commonLib.findElement("card serial no box",1).sendKeys("918700553");
                commonLib.findElement("name box",1).sendKeys("SOTO");
                commonLib.findElement("surname box",1).sendKeys("SOTO");
                commonLib.findElement("gender type box",1).click();
                commonLib.findElement("Erkek",1).click();
                commonLib.findElement("birth place box",1).sendKeys("ADANA");
                commonLib.findElement("mother name box",1).sendKeys("SOTO");
                commonLib.findElement("mother maiden name box",1).sendKeys("SOTO");
                commonLib.findElement("msisdn no text box",1).sendKeys("5445678798");
                commonLib.findElement("email text box",1).sendKeys("soto@gmail.com");
                commonLib.findElement("kimlik bilgileri sayfasi ileri button",1).click();

            } else {

                System.out.println("Page is not found");

            }
        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        }
    }

    @When("I fill id information {string} element with click inquire")
    public void inquireIdInfo(String element) throws NullPointerException, InterruptedException {

//ok button ekle
            seePage("musteriPortali");
            WebElement idInformationPage = commonLib.findElement("kimlik tipi text", 1);

            if (idInformationPage.isDisplayed()){

                System.out.println("func running");
                commonLib.findElement("customer id box",1).sendKeys(element);
                commonLib.findElement("father name box",1).sendKeys("SOTO");
                commonLib.findElement("date of birth box",1).sendKeys("12.12.1999");
                clickIfExists("sorgula button",1);
                clickIfExists("ok button",1);
               //commonLib.findElement("sorgula button",1).click();
                //commonLib.findElement("sorgula button",1).click();
               // clickIfExists("ok button",1);
//                if (commonLib.checkDisplayed("sorgula button")){
//                    clickElement("sorgula button",1);
//                }

               // clickIfExists("ok button",1);

                Thread.sleep(2000);

            //    boolean email_text = commonLib.findElement("10-10 text",1).isDisplayed();

                if (commonLib.checkDisplayed("10-10 text")){

                    System.out.println("func running");
                    // commonLib.findElement("mother maiden name box",1).click();
                    // commonLib.findElement("mother maiden name box",1).sendKeys("SOTO");
                    clickIfExists("kimlik bilgileri sayfasi ileri button",1);
                  //  commonLib.findElement("kimlik bilgileri sayfasi ileri button",1).click();
                    Thread.sleep(2000);

                } else {

                   // waitElement("sorgula button click pop-up",timeout,1);
                   // clickIfExists("ok button",1);
                    Thread.sleep(2000);
                    commonLib.findElement("card serial no box",1).sendKeys(element);
                    commonLib.findElement("card given no box",1).sendKeys("19.05.2022");
                    commonLib.findElement("name box",1).sendKeys("SOTO");
                    commonLib.findElement("surname box",1).sendKeys("SOTO");
                    clickElement("gender type box",1);
                    commonLib.findElement("Erkek",1).click();
                    commonLib.findElement("birth place box",1).sendKeys("ADANA");
                    commonLib.findElement("mother name box",1).sendKeys("SOTO");
//                    commonLib.findElement("mother maiden name box",1).sendKeys("SOTO");
                    commonLib.findElement("msisdn no text box",1).sendKeys("5445678798");
                    commonLib.findElement("email text box",1).sendKeys("soto@gmail.com");
                    clickIfExists("kimlik bilgileri sayfasi ileri button",1);

                    //     commonLib.findElement("kimlik bilgileri sayfasi ileri button",1).click();

                    Thread.sleep(2000);

                }

            } else {

                System.out.println("Page not found");

            }
    }

    @When("I check user info")
    public void checkUserInfo() throws NullPointerException{
        try {
            Thread.sleep(2000);
            WebElement userInfoPage = commonLib.findElement("kullanici kendisi text", 1);
            if (userInfoPage.isDisplayed()) {
                System.out.println("kull kendisi text passed");
                Thread.sleep(5000);
                commonLib.findElement("kullanici bilgileri confirmed mernis statu icon",1).wait();
                Thread.sleep(2000);

               // commonLib.findElement("kullanici girisi sayfasi ileri button",1).click();

                System.out.println("func done");
            } else {

                System.out.println("Page is not found");

            }
        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        }
    }

    @When("I choose number")
    public void chooseNumberInfoPage() throws NullPointerException{
        try {
            WebElement prefixText = commonLib.findElement("prefix section", 1);
            if (prefixText.isDisplayed()) {

                commonLib.findElement("prefix section",1).click();
                commonLib.findElement("543 text",1).click();
                commonLib.findElement("gsm no text box",1).sendKeys("107");
                commonLib.findElement("gsm control button",1).click();

                commonLib.findElement("click gsm number from list",1).click();
                commonLib.findElement("click gsm no sec button",1).click();
                commonLib.findElement("gsm list sec button",1).click();
                waitElement("sim kart seri no text",timeout,1);
                commonLib.findElement("numara secim sayfasi ileri button",1).click();
                commonLib.findElement("PrePaidLockGsmPopUpYes",1).click();
                Thread.sleep(2000);

            } else {

                System.out.println("Page is not found");

            }
        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        }
    }

    @When("I check tariff screen")
    public void checkTariffPage() throws NullPointerException{
        try {
            Thread.sleep(2000);
              //WebElement tariffList = commonLib.findElement("PrePaidBtnTariffList", 1);
       //     WebElement tariffList2 = commonLib.waitElementAndReturnIfLocated("PrePaidBtnTariffList");
            if (commonLib.checkDisplayed("temel servisler text")) {

             //   waitElement("temel servisler text",timeout,1);
                waitElement("yan servisler text",timeout,1);


                commonLib.findElement("tarif servis sayfasi ileri button",1).click();
                Thread.sleep(2000);
            } else {
                commonLib.findElement("kolay tarife text",1).wait();
                commonLib.findElement("tarif servis sayfasi ileri button",1).click();

                System.out.println("Page is not found");

            }
        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        }
    }

    @When("I check order screen")
    public void checkOrderPage() throws NullPointerException{
        try {
            WebElement complete_text = commonLib.findElement("complete text", 1);
            if (complete_text.isDisplayed()) {

                commonLib.findElement("complete text",1).wait();
                commonLib.clickElement(commonLib.findElement("PrePaidSummarySend",1));
            } else {

                System.out.println("Page is not found");

            }
        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        }
    }

    @When("I check filled up user info")
    public void checkFilledUpUserInfo() throws NullPointerException, InterruptedException {

                commonLib.waitElement("identity type box",timeout,1);
                commonLib.waitElement("citizen type box",timeout,1);
                commonLib.waitElement("customer id box",timeout,1);
                commonLib.waitElement("father name box",timeout,1);
                commonLib.waitElement("date of birth box",timeout,1);
                commonLib.waitElement("card serial no box",timeout,1);
                commonLib.waitElement("card given no bo",timeout,1);
                commonLib.waitElement("name box",timeout,1);
                commonLib.waitElement("surname box",timeout,1);
                commonLib.waitElement("birth place box",timeout,1);
                commonLib.waitElement("gender type box",timeout,1);
                commonLib.waitElement("mother name box",timeout,1);
                commonLib.waitElement("mother maiden name box",timeout,1);
                commonLib.waitElement("msisdn no text box",timeout,1);
                commonLib.waitElement("email text box",timeout,1);

    }

    //-mobilden gelenler

    @Given("I wait {string} element")
    public void waitElement(String element) {
        commonLib.waitElement(element);
    }



    @Given("I wait {string} element and click")
    public void waitAndClick(String elem) throws UnsupportedEncodingException, InterruptedException {

        WebElement element = commonLib.findElement(elem);

        if (elem != null) {

            MobileElement mobileElement = null;
            int timeout=0;
            while (mobileElement==null&& timeout<15) {
                try{
                   commonLib.findElement(elem);
                    sleep(1);
                }catch (Exception e){

                }

                timeout++;
            }
            if (mobileElement != null) {
                commonLib.allureReport(StepResultType.PASS, "Element  displayed", true);
                mobileElement.click();
                commonLib.allureReport(StepResultType.PASS, "Element  clicked", true);
            } else {
                commonLib.allureReport(StepResultType.FAIL, "Element not displayed", true);
            }
        }
    }




    @When("I fill id postpaid information {string} element with click inquire")
    public void postpaidIdInfo(String element) throws NullPointerException{
        try {

            seePage("musteriPortali");
            WebElement idInformationPage = commonLib.findElement("kimlik tipi text", 1);

            if (idInformationPage.isDisplayed()){

                System.out.println("func running");

                commonLib.findElement("faturali customer id box",1).sendKeys(element);
                commonLib.findElement("faturali father name box",1).sendKeys("SOTO");
                commonLib.findElement("postpaid date of birth box",1).sendKeys("12.12.1992");
                commonLib.findElement("pospaid sorgula button",1).click();
                clickIfExists("ok button",1);

                //commonLib.findElement("sorgula button",1).click();
                Thread.sleep(2000);

                WebElement email_text = commonLib.findElement("10-10 text", 1);

                if (email_text.isDisplayed()){

                    commonLib.findElement("postpaid mother maiden name box",1).sendKeys("SOTO");
                    //    commonLib.findElement("kimlik bilgileri sayfasi ileri button",1).click();
                    clickIfExists("postpaid kimlik bilgileri sayfasi ileri button",1);
                    Thread.sleep(2000);

                } else {

                   // waitElement("sorgula button click pop-up",timeout,1);
                    clickIfExists("ok button",1);
                   // commonLib.findElement("ok button",1).click();
                    Thread.sleep(2000);
                    commonLib.findElement("postpaid card serial no box",1).sendKeys("416686584");
                    commonLib.findElement("postpaid card given no box",1).sendKeys("19.05.2022");
                    commonLib.findElement("postpaidname box",1).sendKeys("SOTO");
                    commonLib.findElement("postpaid surname box",1).sendKeys("SOTO");
                    commonLib.findElement("postpaid gender type box",1).click();
                    commonLib.findElement("Erkek",1).click();
                    commonLib.findElement("postpaid birth place box",1).sendKeys("ADANA");
                    commonLib.findElement("postpaid mother name box",1).sendKeys("SOTO");
                    commonLib.findElement("postpaid mother maiden name box",1).sendKeys("SOTO");
                    commonLib.findElement("postpaid msisdn no text box",1).sendKeys("5445678798");
                    commonLib.findElement("postpaid email text box",1).sendKeys("mail.com");
                    clickIfExists("postpaid kimlik bilgileri sayfasi ileri button",1);

                    //     commonLib.findElement("kimlik bilgileri sayfasi ileri button",1).click();

                    Thread.sleep(2000);

                }

            } else {

                System.out.println("Page not found");

            }

        } catch (Exception e) {
            Allure.addAttachment("Element does not exist.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Allure.step("The element does not exist", Status.SKIPPED);
        }
    }

    @When("I send {string} element to gsm number")
    public void enterGsmNo(String element) throws NullPointerException{
            WebElement individualText = commonLib.findElement("bireysel text", 1);
//            WebElement object;
//            object = commonLib.waitElement(element,timeout,1);
            if (individualText.isDisplayed()){

                System.out.println("gsm number func running");
//                object.click();
//                Thread.sleep(1000);
//                object.sendKeys(Keys.CONTROL,"a");
//                object.sendKeys(Keys.DELETE);
//                commonLib.findElement("gsm text area",1).sendKeys("5");
//                commonLib.findElement("gms text area", 1).sendKeys(Keys.DELETE);
                commonLib.findElement("gsm text area",1).sendKeys(element);
                commonLib.findElement("sorgula button",1).click();



//                WebElement errorPopUp = commonLib.findElement("error pop-up", 1);
//                  String errorPopUp = parser.getElement("gsmSearch","error pop-up");

//                if(errorPopUp.isDisplayed()) {
//
//                    System.out.println("error pop up if cond");
//                    for (int i = 0; i < 5; i++) {
//                        commonLib.findElement("ok button",1).click();
//                        commonLib.findElement("sorgula button",1).click();
//                       // commonLib.sleep(10);
////                        WebElement confirmationGSM = commonLib.findElement("musteri dogrulama islemi texti", 1, false);
////
////                        if (confirmationGSM.isDisplayed()) {
////                            break;
////                        }
//                    }
//                }
            } else {
                System.out.println("Page not found");
            }
    }

    @When("I choose to p status gsm number")
    public void choosePStatus() throws NullPointerException{

        for (int i=0; i<10; i++) {

            if(commonLib.checkDisplayed("//*[@class='align-middle statu e']")){
                commonLib.findElement("choose gsm number from list",1).click();
                commonLib.findElement("gsm change sec button",1).click();
                break;
            } else {
                commonLib.findElement("p status gsm change next button",1).click();
            }
        }
    }

}


