package classes;

import commons.*;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.PressesKey;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

import static io.appium.java_client.touch.offset.PointOption.point;

public class Commonlib {
    public MobileDriver<MobileElement> mobildriver;
    public WebDriverWait wait;
    Parser parser = new Parser();
    public Page mypage;
    int defaultTimeout = 15;
    DataBaseConnection db = new DataBaseConnection(DataBaseConnection.DBType.ORACLE);

    public Commonlib(MobileDriver<MobileElement> mobildriver, WebDriverWait wait) {
        this.mobildriver = mobildriver;
        this.wait = wait;
    }

    /**
     * <p>Definition:Read page info from json file. If waitElement of page is exist , wait for this element that is displayed.</p>
     *
     * @param page defines the name of the page
     * @author Gry
     */
    public void seePage(String page) throws IOException, ParseException {
        if (parser.isPageExist(page)) {
            mypage = parser.getPageAttributes(page);
            allureReport(StepResultType.PASS, page + "page exist in json file", Boolean.TRUE);

            //Allure.addAttachment("page exist in json file.", new ByteArrayInputStream(((TakesScreenshot) mobildriver).getScreenshotAs(OutputType.BYTES)));
            if (mypage.getWaitElement().length() > 0) {
                waitElement(mypage.getWaitElement());
            }
        } else {
            allureReport(StepResultType.FAIL, page + "page not exist in json file", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: Wait until element located </p>
     *
     * @param element defines the name(elementName) of the element
     * @author Gry
     */
    public void waitElement(String element) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(element)));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " is not displayed(Function: waitElement).", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: Wait until element located </p>
     *
     * @param element defines the name(elementName) of the element
     * @author Gry
     */
    public void waitElement(String element, int timeout) {
        try {
            this.wait = new WebDriverWait(mobildriver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(element)));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " is not displayed(Function: waitElement).", Boolean.TRUE);
        }
        this.wait = new WebDriverWait(mobildriver, defaultTimeout);
    }

    /**
     * <p>Definition: Wait until element located </p>
     *
     * @param element defines the name(elementName) of the element
     * @author Gry
     */
    public void waitElement(By element) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " is not displayed(Function: waitElement).", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: Wait until element at index located. </p>
     *
     * @param element defines the name(elementName) of the element.
     * @param index   defines the index where the element is located
     * @author Gry
     */
    public void waitElementWithIndex(String element, int index) {
        WebElement myElement = null;
        try {
            myElement = findLocator(getLocator(element), index);
            wait.until(ExpectedConditions.visibilityOf(myElement));
            allureReport(StepResultType.PASS, element + " at " + index + ".index is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " is not displayed(Function: waitElement).", Boolean.TRUE);
        }

        if (myElement == null) {
            allureReport(StepResultType.FAIL, element + " is not displayed(Function: waitElement).", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: Wait until element located then click to element </p>
     *
     * @param element defines the name(elementName) of the element.
     * @author Gry
     */
    public void waitAndClick(String element) {
        try {
            waitElementAndReturnIfLocated(element).click();
            allureReport(StepResultType.PASS, element + " is clicked.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + "Error while clicking " + element + " element.", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: Wait until element located then click to element </p>
     *
     * @param element defines the name(elementName) of the element.
     * @author Gry
     */
    public void waitAndClick(By element) {
        try {
            waitElementAndReturnIfLocated(element).click();
            allureReport(StepResultType.PASS, element + " is clicked.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + "Error while clicking " + element + " element.", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: Find and click to element </p>
     *
     * @param element defines the name(elementName) of the element.
     * @author Gry
     */
    public void clickToElement(String element) throws UnsupportedEncodingException {
        if (mobildriver instanceof AndroidDriver) {
            try {
                mobildriver.findElement(getLocator(element)).click();
                allureReport(StepResultType.PASS, element + " is clicked.", Boolean.TRUE);
            } catch (Exception e) {
                allureReport(StepResultType.FAIL, "Error while clicking " + element + " element.", Boolean.TRUE);
            }
        } else if (mobildriver instanceof IOSDriver) {
            MobileElement mobileElement = null;
            int timeout = 15;
            for (int i = 0; i < timeout; i++) {
                try {
                    mobileElement = mobildriver.findElement(getLocator(element));
                    sleep(1);
                } catch (Exception e) {

                }

                if (mobileElement != null) {
                    allureReport(StepResultType.PASS, element + " displayed", true);
                    break;
                }
            }
            if (mobileElement == null) {
                allureReport(StepResultType.FAIL, element + " not displayed", true);
            } else {
                clickToCoordinate(mobileElement.getRect().x + (mobileElement.getRect().width / 2), mobileElement.getRect().y + (mobileElement.getRect().height / 2));
            }

        }
    }

    /**
     * <p>Definition: Find and click to element </p>
     *
     * @param element defines the name(elementName) of the element.
     * @author Gry
     */
    public void clickToElement(By element) {
        try {
            mobildriver.findElement(element).click();
            allureReport(StepResultType.PASS, element + " is clicked.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, "Error while clicking " + element + " element.", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: If element exist click to element </p>
     *
     * @param element  defines the name(elementName) of the element.
     * @param element2 defines the name(elementName) of the element2.
     * @param timeout  defines the max seconds that element is waits
     * @author Gry
     */
    public void clickIfExist(String element, String element2, int timeout) {
        try {
            if (timeout >= 5) {
                wait = new WebDriverWait(mobildriver, timeout);
            }
            if (checkElementDisplayed(element2)) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(element))).click();
            }
            allureReport(StepResultType.PASS, element + " is clicked.", Boolean.FALSE);
        } catch (Exception e) {
            allureReport(StepResultType.INFO, element + "Error while clicking " + element + " element.", Boolean.FALSE);
        }
        //restore to default
        wait = new WebDriverWait(mobildriver, 15);
    }

    public void clickIfExist(String element, int timeout) {
        try {
            if (timeout >= 5) {
                wait = new WebDriverWait(mobildriver, timeout);
            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(element))).click();
            allureReport(StepResultType.PASS, element + " is clicked.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.INFO, element + "Error while clicking " + element + " element.", Boolean.FALSE);
        }
        //restore to default
        wait = new WebDriverWait(mobildriver, 15);
    }

    /**
     * <p>Definition:Waits for the specified time. </p>
     *
     * @param seconds defines the specified time.
     * @author Gry
     */
    public void sleep(int seconds) {
        try {
            Thread.sleep((long) seconds * 1000);
        } catch (Exception e) {
            //
        }
    }

    /**
     * <p>Definition: Wait until element located then send keys to element</p>
     *
     * @param element defines the name(elementName) of the element.
     * @param text    defines the text to be entered.
     * @author Gry
     */
    public void sendKey(String text, String element) {
        try {
            waitElementAndReturnIfLocated(element).sendKeys(text);
            allureReport(StepResultType.PASS, element + " ", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " is not displayed(Function: waitElement).", Boolean.TRUE);
        }
    }

    /**
     * <p>Definition: Find element at index and wait until element clickable then sendKeys to element.</p>
     *
     * @param element defines the name(elementName) of the element.
     * @param text    defines the text to be entered.
     * @param index   defines the index where the element is located
     * @author Gry
     */
    public void sendKeyWithIndex(String text, String element, int index) {
        //MobileElement element=waitElementAndReturnIfClickable(elem);
        try {
            waitElementWithIndexAndReturnIfClickable(element, index).sendKeys(text);
            allureReport(StepResultType.PASS, "Text entered to " + element, Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, "An error was encountered while entering text to " + element, Boolean.TRUE);
        }

    }

    /**
     * <p>Definition: Hide Keyboard.</p>
     *
     * @author Gry
     */
    public void hideKeyboard() {
        mobildriver.navigate().back();
        //allureReport(StepResultType.FAIL, "element" + " is not displayed(Function: waitElement).", Boolean.TRUE);
    }

    /**
     * <p>Definition: Click to specified point.</p>
     *
     * @param x defines the x axis of the point.
     * @param y defines the y axis of the point.
     * @author Gry
     */
    public void clickToCoordinate(int x, int y) {
        try {
            TouchAction touchAction = new TouchAction(mobildriver);
            touchAction.tap(point(x, y)).perform();
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, "element" + " is not displayed(Function: waitElement).", Boolean.TRUE);
            Assert.fail("kordinata tıklarken hata ile karşılaşıldı. Error:" + e.toString());
        }

    }

    public void checkTextOfElementEqualToText(String element, String text) {
        WebElement myelement = waitElementAndReturnIfLocated(element);
        if (myelement.getText().equalsIgnoreCase(text)) {
            allureReport(StepResultType.PASS, "Text of " + element + " is equal to '" + text + "'", true);
        } else {
            allureReport(StepResultType.FAIL, "Text of " + element + " is not equal to '" + text + "'", true);
        }
    }

    public void scrollDown(int startPointPercentage, int endPointPercentage) {
        Dimension dimensions = mobildriver.manage().window().getSize();
        int Startpoint = (int) (dimensions.getHeight() * startPointPercentage / 100);
        int scrollEnd = (int) (dimensions.getHeight() * endPointPercentage / 100);
        int center = (int) (dimensions.getWidth() * 0.5);

        TouchAction action = new TouchAction(mobildriver);
        action.press(PointOption.point(center, Startpoint)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(center, scrollEnd)).release().perform();
    }

    public void scrollUntilFindElement(String element, String way, int maxScrollCount) {

        for (int i = 0; i < maxScrollCount; i++) {
            if (checkElementDisplayed(element)) {
                break;
            } else {
                if (way.equalsIgnoreCase("down")) {
                    scrollDown(95, 85);
                } else if (way.equalsIgnoreCase("up")) {
                    scrollUp(85, 95);
                }
            }
        }
    }

    public void scrollUntilFindElement(String element, String way, int maxScrollCount, int start, int stop) {

        boolean flag = false;
        for (int i = 0; i < maxScrollCount; i++) {
            if (checkElementDisplayed(element)) {
                flag = true;
                break;
            } else {
                if (way.equalsIgnoreCase("down")) {
                    scrollDown(start, stop);
                } else if (way.equalsIgnoreCase("up")) {
                    scrollUp(start, stop);
                } else if (way.equalsIgnoreCase("right")) {
                    scrollRight(start, stop);
                } else if (way.equalsIgnoreCase("rightfromcenter")) {
                    scrollRightFromCenter(start, stop);
                } else if (way.equalsIgnoreCase("rightfrombottom")) {
                    scrollRightFromBottom(start, stop);
                }
            }
        }
    }


    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //This method will scroll down until the specified element is visible
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean scrollDownTillElementVisible(String element) {
        TouchAction t = new TouchAction((PerformsTouchActions) mobildriver);

        int heightOfScreen = mobildriver.manage().window().getSize().getHeight();
        int widthOfScreen = mobildriver.manage().window().getSize().getWidth();
        boolean flag = false;

        // To get 50% of width
        int x = (int) (widthOfScreen * 0.6);
        // To get 50% of height
        int y1 = (int) (heightOfScreen * 0.75);
        int y2 = (int) (heightOfScreen * 0.45);

        for (int i = 0; i < 8; i++) {
            if (checkElementDisplayed(element)) {
                flag = true;
                break;
            }
            t.press(point(x, y1)).waitAction().moveTo(point(x, y2)).release().perform();

        }
        return flag;
    }

    public void scrollUntilFindElement(By element, String way, int maxScrollCount) {

        for (int i = 0; i < maxScrollCount; i++) {
            wait = new WebDriverWait(mobildriver, 5);
            if (checkElementDisplayed(element)) {
                break;
            } else {
                if (way.equalsIgnoreCase("down")) {
                    scrollDown(95, 85);
                } else if (way.equalsIgnoreCase("up")) {
                    scrollUp(85, 95);
                }
            }
        }
        wait = new WebDriverWait(mobildriver, 15);
        allureReport(StepResultType.INFO, "scroll " + way, true);
    }

    public void scrollUp(int startPointPercentage, int endPointPercentage) {
        Dimension dimensions = mobildriver.manage().window().getSize();
        int Startpoint = (int) (dimensions.getHeight() * startPointPercentage / 100);
        int scrollEnd = (int) (dimensions.getHeight() * endPointPercentage / 100);
        int center = (int) (dimensions.getWidth() * 0.5);

        TouchAction action = new TouchAction(mobildriver);
        action.press(PointOption.point(center, Startpoint)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(center, scrollEnd)).release().perform();
    }

    public void scrollRight(int startPointPercentage, int endPointPercentage) {
        Dimension dimensions = mobildriver.manage().window().getSize();
        int Startpoint = (int) (dimensions.getWidth() * startPointPercentage / 100);
        int scrollEnd = (int) (dimensions.getWidth() * endPointPercentage / 100);
        int center = (int) (dimensions.getHeight() * 0.15);

        TouchAction action = new TouchAction(mobildriver);
        action.press(PointOption.point(Startpoint, center)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(scrollEnd, center)).release().perform();
    }

    public void scrollRightFromCenter(int startPointPercentage, int endPointPercentage) {
        Dimension dimensions = mobildriver.manage().window().getSize();
        int Startpoint = (int) (dimensions.getWidth() * startPointPercentage / 100);
        int scrollEnd = (int) (dimensions.getWidth() * endPointPercentage / 100);
        int center = (int) (dimensions.getHeight() * 0.50);

        TouchAction action = new TouchAction(mobildriver);
        action.press(PointOption.point(Startpoint, center)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(scrollEnd, center)).release().perform();
    }

    public void scrollRightFromBottom(int startPointPercentage, int endPointPercentage) {
        Dimension dimensions = mobildriver.manage().window().getSize();
        int Startpoint = (int) (dimensions.getWidth() * startPointPercentage / 100);
        int scrollEnd = (int) (dimensions.getWidth() * endPointPercentage / 100);
        int center = (int) (dimensions.getHeight() * 0.36);

        TouchAction action = new TouchAction(mobildriver);
        action.press(PointOption.point(Startpoint, center)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(scrollEnd, center)).release().perform();
    }

    public Element getElementFromJson(String elemName) throws UnsupportedEncodingException {
        // test
        return parser.getElement(mypage.getPageName(), elemName);
    }

    public By getLocator(String elemName) throws UnsupportedEncodingException {
        By returnLocator = null;
        ElementType elemType;
        Element elem = getElementFromJson(elemName);
        String osType = mobildriver.getPlatformName();
        //getCapabilities().getCapability("platformName").toString();

        String elementValue;
        if (osType.equalsIgnoreCase("Android")) {
            elementValue = elem.getAndroidValue();
            elemType = elem.getAndroidType();
        } else {
            elementValue = elem.getIOSValue();
            elemType = elem.getIOSType();
        }
        if (elemType != null) {
            System.out.println("Element Type: " + elemType + "- Element Value: " + elementValue);
            switch (elemType.toString()) {
                case "id":
                    returnLocator = By.id(elementValue);
                    break;
                case "xpath":
                    returnLocator = By.xpath(elementValue);
                    break;
                case "className":
                    returnLocator = By.className(elementValue);
                    break;
                case "name":
                    returnLocator = By.name(elementValue);
                    break;
                case "partialLinkText":
                    returnLocator = By.partialLinkText(elementValue);
                    break;
                case "cssSelector":
                    returnLocator = By.cssSelector(elementValue);
                    break;
                case "tagName":
                    returnLocator = By.tagName(elementValue);
                    break;
            }
        }
        return returnLocator;
    }

    public WebElement findLocator(By by, int index) {
        WebElement elem = null;
        for (int i = 0; i < 30; i++) {
            try {
                elem = (WebElement) mobildriver.findElements(by).get(index);
            } catch (Exception e) {

            }

            if (elem != null) {
                break;
            }
        }
        return elem;
    }

    /**
     * <p>Definition: Wait element and check visibility of element. Then return element.</p>
     *
     * @param element defines the name(elementName) of the element.
     * @return element is visible(true) | not visible(false)
     * @author Gry
     */
    public WebElement waitElementAndReturnIfLocated(String element) {
        WebElement myelement = null;
        try {
            myelement = wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(element)));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " is not displayed.", Boolean.TRUE);
        }
        return myelement;
    }

    /**
     * <p>Definition: Wait element and check visibility of element. Then return element.</p>
     *
     * @param element defines the name(elementName) of the element.
     * @return element is visible(true) | not visible(false)
     * @author Gry
     */
    public WebElement waitElementAndReturnIfLocated(By element) {
        WebElement myelement = null;
        try {
            myelement = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " is not displayed.", Boolean.TRUE);
        }
        return myelement;
    }

    /**
     * <p>Definition: Check visibility of element.Then return that is it visible or not.</p>
     *
     * @param element defines the name(elementName) of the element.
     * @return element is visible(true) | not visible(false)
     * @author Gry
     */
    public Boolean checkElementDisplayed(String element) {
        WebElement myelement = null;
        try {
            myelement = wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(element)));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.INFO, element + " is not displayed", Boolean.TRUE);
        }
        if (myelement != null) {
            return true;
        } else {
            allureReport(StepResultType.INFO, element + " is not displayed.", Boolean.TRUE);
            return false;
        }

    }

    /**
     * <p>Definition: Check visibility of element.Then return that is it visible or not.</p>
     *
     * @param element defines the name(elementName) of the element.
     * @return element is visible(true) | not visible(false)
     * @author Gry
     */
    public Boolean checkElementDisplayed(By element) {
        WebElement myelement = null;
        try {
            myelement = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.INFO, element + " is not displayed", Boolean.TRUE);
        }
        if (myelement != null) {
            return true;
        } else {
            allureReport(StepResultType.INFO, element + " is not displayed.", Boolean.TRUE);
            return false;
        }

    }

    /**
     * <p>Definition: Find element and wait until element clickable.Then if it is clickable return it.</p>
     *
     * @param element defines the name(elementName) of the element.
     * @return mobile element
     * @author Gry
     */
    public WebElement waitElementAndReturnIfClickable(String element) {
        WebElement myelement = null;
        try {
            myelement = findLocator(getLocator(element), 0);
            allureReport(StepResultType.PASS, element + " element is found.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " element is not found.", Boolean.TRUE);
        }

        if (myelement != null) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(myelement));
                allureReport(StepResultType.PASS, element + " element is clickable.", Boolean.TRUE);
            } catch (Exception e) {
                allureReport(StepResultType.FAIL, element + " element is not clickable.", Boolean.TRUE);
            }
        } else {
            allureReport(StepResultType.FAIL, element + " element is found.", Boolean.TRUE);
        }
        return myelement;
    }

    public WebElement waitElementAndReturnIfClickable(String element, int timeout) {
        WebElement myelement = null;
        this.wait = new WebDriverWait(mobildriver, timeout);
        try {
            myelement = wait.until(ExpectedConditions.elementToBeClickable(getLocator(element)));
            allureReport(StepResultType.PASS, element + " element is clickable.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.INFO, element + " element is not clickable.", Boolean.TRUE);
        }

        return myelement;
    }

    public MobileElement waitMobileElementAndReturnIfClickable(String element, int timeout) {
        MobileElement myelement = null;
        for (int i = 0; i < timeout; i++) {
            try {
                myelement = mobildriver.findElement(getLocator(element));
                allureReport(StepResultType.PASS, element + " element is enabled.", Boolean.TRUE);
            } catch (Exception e) {
            }
            if (myelement != null && myelement.isEnabled()) {
                break;
            }
            sleep(1);
        }
        return myelement;
    }

    /**
     * <p>Definition: Find element at index and wait until element clickable.Then if it is clickable return it.</p>
     *
     * @param element defines the name(elementName) of the element.
     * @param index   defines the index where the element is located
     * @return mobile element
     * @author Gry
     */
    public WebElement waitElementWithIndexAndReturnIfClickable(String element, int index) {
        WebElement myelement = null;
        try {
            myelement = findLocator(getLocator(element), index);
            allureReport(StepResultType.PASS, element + " element is found.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.FAIL, element + " element at " + index + ".index could not found.", Boolean.TRUE);
        }

        if (myelement != null) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(myelement));
                allureReport(StepResultType.PASS, element + " element is clickable.", Boolean.TRUE);
            } catch (Exception e) {
                allureReport(StepResultType.FAIL, element + " is not clickable.", Boolean.TRUE);
            }
        } else {
            allureReport(StepResultType.FAIL, element + " is not found.", Boolean.TRUE);
        }
        return myelement;
    }


    public void startToApplication(String appName) {
        if (mobildriver instanceof AndroidDriver) {
            ArrayList<String> temp = parser.getAndroidPackageAndActivity("AndroidApps", appName);
            String appPackage = temp.get(0);
            String appActivity = temp.get(1);
            ((AndroidDriver<MobileElement>) mobildriver).startActivity(new Activity(appPackage, appActivity));
        } else if (mobildriver instanceof IOSDriver) {
            String bundleID = parser.getIOSBundle("IOSApps", appName);
            mobildriver.activateApp(bundleID);
        }
    }

    public void terminateToApplication(String appName) {
        if (mobildriver instanceof AndroidDriver) {
            ArrayList<String> temp = parser.getAndroidPackageAndActivity("AndroidApps", appName);
            String appPackage = temp.get(0);
            String appActivity = temp.get(1);
            try {
                mobildriver.terminateApp(appPackage);
            } catch (Exception e) {
            }
            ;
        } else if (mobildriver instanceof IOSDriver) {
            String bundleID = parser.getIOSBundle("IOSApps", appName);
            mobildriver.terminateApp(bundleID);
        }
    }

    //login function for yanimda beta
    public void loginBeta(String username, String password) throws IOException, ParseException {

        this.wait = new WebDriverWait(mobildriver, 10);
        seePage("LoginToYanimdaPage");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        //check for the giris yap button on the app launch page
//        if (checkElementDisplayed("KlavuzPopupClose")) {
//            clickToElement("KlavuzPopupClose");
//        }
        date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        if (checkElementDisplayed("GirisYap")) {
            clickToElement("GirisYap");

            if (checkElementDisplayed("login with different account button")) {
                clickToElement("login with different account button");
            }

            clickToElement("MobilHesabim");
        } else {
            allureReport(StepResultType.FAIL, "GirisYap is not displayed", true);
        }

        //check the username option on the login page
        if (this.checkElementDisplayed("Username")) {
            //   RememberMe.click();
            this.sendKey(username, "Username");
            //ScreenShot.takeSnapShot(oDriver, "Login");
            this.sendKey(password, "Password");

        } else {
            allureReport(StepResultType.FAIL, "Username is not displayed", true);
        }

        //click on the giris yap button to move to the otp page
        if (mobildriver instanceof AndroidDriver) {
            waitAndClick("GirisYap");
            clickIfExist("keyboard close button", 5);
            waitAndClick("HayirBtn");
            sleep(2);
        } else if (mobildriver instanceof IOSDriver) {
            waitAndClick("Login");
            waitAndClick("HayirBtn");
            sleep(2);
        }

        seePage("LoginOTPPage");

        if (checkElementDisplayed("otp")) {
            clickToElement("otp");
            sendKey("1111", "otp");
            System.out.println("Entered OTP");
        } else {
            allureReport(StepResultType.FAIL, "otp is not displayed", true);
        }

        seePage("YanimdaHomePage");
        if (mobildriver instanceof AndroidDriver) {
            if (checkElementDisplayed("KilavuzdanCikAndroid")) {
                clickToElement("KilavuzdanCikAndroid");
            }

            if (checkElementDisplayed("NewCloseIcon")) {
                clickToElement("NewCloseIcon");
            }

            if (checkElementDisplayed("one cikanlar close icon")) {
                clickToElement("one cikanlar close icon");
            }


        }
        //check for the hesabim button on the homepage
        if (mobildriver instanceof AndroidDriver) {
            waitElement("MainAccount");
        } else {
            findElementWithTimeout("MainAccount", 30);
        }
    }

    //login function for yanimda beta
    public void loginBetav2(String username, String password) throws IOException, ParseException {

        wait = new WebDriverWait(mobildriver, 10);
        seePage("koctasTask");
        int timeout = 0;
        boolean flag = false;

        if (mobildriver instanceof AndroidDriver) {
            //check for the giris yap button on the app launch page
            while (!flag && timeout < 20) {

                if (waitElementAndReturnIfClickable("my account button", 1) != null) {
                    clickToElement("my account button");
                    flag = true;
                }
                timeout += 2;
            }

            timeout = 0;
            flag = false;


            if (checkElementDisplayed("username text area")) {
                sendKey(username, "username text area");

                sendKey(password, "password text area");
                clickToElement("login button");
            } else {
                System.out.println("There is something went wrong");
            }

        } else if (mobildriver instanceof IOSDriver) {
            while (!flag && timeout < 20) {

                if (waitMobileElementAndReturnIfClickable("GirisYap", 1) != null) {
                    clickToElement("GirisYap");
                    flag = true;
                } else if (waitMobileElementAndReturnIfClickable("KlavuzPopupClose", 1) != null) {
                    clickToElement("KlavuzPopupClose");
                }
                timeout += 2;
            }

            timeout = 0;
            flag = false;

            while (!flag && timeout < 20) {

                if (waitMobileElementAndReturnIfClickable("MobilHesabim", 1) != null) {
                    clickToElement("MobilHesabim");
                    flag = true;
                } else if (waitMobileElementAndReturnIfClickable("login with different account button", 1) != null) {
                    clickToElement("login with different account button");
                }
                timeout += 2;
            }

            timeout = 0;
            flag = false;

            if (checkElementDisplayed("Username")) {
                sendKey(username, "Username");

                sendKey(password, "Password");
            } else {
                allureReport(StepResultType.FAIL, "Username is not displayed", true);
            }
            sleep(1);
            clickToElement("Username");


        }

        if (mobildriver instanceof AndroidDriver)
        //click on the giris yap button to move to the otp page
        {
            if (checkElementDisplayed("GirisYap")) {
                clickToElement("GirisYap");
                waitAndClick("HayirBtn");
                sleep(2);
            }
        } else {
            if (checkElementDisplayed("Login")) {
                clickToElement("Login");
                waitAndClick("HayirBtn");
                sleep(2);
            }
        }

        seePage("LoginOTPPage");
        if (checkElementDisplayed("otp")) {
            sendKey("1111", "otp");
            System.out.println("Entered OTP");
        } else {
            allureReport(StepResultType.FAIL, "otp is not displayed", true);
        }

        seePage("YanimdaHomePage");
        sleep(5);
        if (mobildriver instanceof AndroidDriver) {
            while (!flag && timeout <= 36) {

                if (waitElementAndReturnIfClickable("KilavuzdanCikAndroid", 1) != null) {
                    clickToElement("KilavuzdanCikAndroid");
                } else if (waitElementAndReturnIfClickable("NewCloseIcon", 1) != null) {
                    clickToElement("NewCloseIcon");
                } else if (waitElementAndReturnIfClickable("one cikanlar close icon", 1) != null) {
                    clickToElement("one cikanlar close icon");
                }
                if (waitElementAndReturnIfClickable("MainAccount", 1) != null) {
                    flag = true;
                }
                timeout += 3;
            }
        } else {
            while (!flag && timeout <= 36) {

                if (waitMobileElementAndReturnIfClickable("close black", 2) != null) {
                    clickToElement("close black");
                }
                if (waitMobileElementAndReturnIfClickable("one cikanlar close icon", 2) != null) {
                    clickToElement("one cikanlar close icon");
                }
                if (waitMobileElementAndReturnIfClickable("bu haftaki hediyen close", 2) != null) {
                    clickToElement("bu haftaki hediyen close");
                }
                if (waitMobileElementAndReturnIfClickable("MainAccount", 2) != null) {
                    flag = true;
                }
                timeout += 8;
            }
        }
    }

    public MobileElement findElementWithTimeout(String element, int timeout) throws UnsupportedEncodingException {
        MobileElement mobileElement = null;
        if (timeout > 600) {
            timeout = 600;
        }
        for (int i = 0; i < timeout; i++) {
            mobileElement = mobildriver.findElement(getLocator(element));
            sleep(1);
            if (mobileElement != null) {
                allureReport(StepResultType.PASS, element + " displayed", true);
                break;
            }
        }
        if (mobileElement == null) {
            allureReport(StepResultType.FAIL, element + " not displayed", true);
        }
        return mobileElement;
    }

    public MobileElement findElementWithTimeout(By element, int timeout) throws UnsupportedEncodingException {
        MobileElement mobileElement = null;
        if (timeout > 600) {
            timeout = 600;
        }
        for (int i = 0; i < timeout; i++) {
            mobileElement = mobildriver.findElement(element);
            sleep(1);
            if (mobileElement != null) {
                allureReport(StepResultType.PASS, element.toString() + " displayed", true);
                break;
            }
        }
        if (mobileElement == null) {
            allureReport(StepResultType.FAIL, element.toString() + " not displayed", true);
        }
        return mobileElement;
    }

    public void loginTest(String username, String password) throws Throwable {


        //Login
        this.wait = new WebDriverWait(mobildriver, 10);
        flogin(username, password);
        enterOTP();
        homepageYanimda();
        this.wait = new WebDriverWait(mobildriver, 30);

    }

    public boolean loginTest(String username, String password, String autoLoginFlag) throws Throwable {
        boolean status = false;

        //AutoLogin
        this.wait = new WebDriverWait(mobildriver, 10);
        flogin(username, password, autoLoginFlag);
        enterOTP();
        homepageYanimda();
        this.wait = new WebDriverWait(mobildriver, 30);
        return status;
    }

    public boolean loginErrorTest(String username, String password) throws Throwable {
        boolean status = false;

        //Login
        this.wait = new WebDriverWait(mobildriver, 10);
        floginerror(username, password);
        this.wait = new WebDriverWait(mobildriver, 30);
        return status;
    }

    public void flogin(String user, String pwd) throws InterruptedException, IOException, ParseException {
        sleep(10);
        seePage("LoginToYanimdaPage");

        //check for the giris yap button on the app launch page
        wait = new WebDriverWait(mobildriver, 10);
        if (checkElementDisplayed("KlavuzPopupClose")) {
            clickToElement("KlavuzPopupClose");
        }

        wait = new WebDriverWait(mobildriver, 10);
        if (checkElementDisplayed("GirisYap")) {
            clickToElement("GirisYap");
            sleep(2);
            if (checkElementDisplayed("login with different account button")) {
                clickToElement("login with different account button");
            }
            clickToElement("MobilHesabim");

        } else if (checkElementDisplayed("Login")) {
            clickToElement("Login");

        } else {

        }

        //check the username option on the login page
        if (this.checkElementDisplayed("Username")) {
            //   RememberMe.click();
            this.sendKey(user, "Username");
            //ScreenShot.takeSnapShot(oDriver, "Login");
            this.sendKey(pwd, "Password");

        } else {
            allureReport(StepResultType.FAIL, "Username is not displayed", true);
        }

        //click on the giris yap button to move to the otp page
        if (mobildriver instanceof AndroidDriver) {
            if (checkElementDisplayed("GirisYap")) {
                clickToElement("GirisYap");
                sleep(2);
                clickToElement("HayirBtn");
            } else if (checkElementDisplayed("Login")) {
                clickToElement("Login");
            }
        } else {
            if (checkElementDisplayed("GirisYapIos")) {
                clickToElement("GirisYapIos");
                sleep(2);
                waitAndClick("HayirBtn");
            } else if (checkElementDisplayed("Login")) {
                clickToElement("Login");
                waitAndClick("HayirBtn");
            }
        }


    }

    public void flogin(String user, String pwd, String autoLoginFlag) throws InterruptedException, IOException, ParseException {
        boolean status = false;
        this.sleep(10);
        seePage("LoginToYanimdaPage");

        //check for the giris yap button on the app launch page
        this.wait = new WebDriverWait(mobildriver, 10);
        if (this.checkElementDisplayed("KlavuzPopupClose")) {
            this.clickToElement("KlavuzPopupClose");
        }

        try {
            this.wait = new WebDriverWait(mobildriver, 10);
            if (this.checkElementDisplayed("GirisYap")) {

                waitElement("vodafone login welcome message");
                this.clickToElement("GirisYap");

                this.sleep(2);
                if (checkElementDisplayed("login with different account button")) {
                    clickToElement("login with different account button");
                }
                this.clickToElement("MobilHesabim");
                status = true;


            } else if (this.checkElementDisplayed("Login")) {
                this.clickToElement("Login");

                status = true;
            } else {
                //ScreenShot.takeSnapShot(oDriver, "Login");
                status = false;
                //return status;
            }
        } catch (Exception e) {
        }

        //check the username option on the login page
        if (this.checkElementDisplayed("Username")) {
            //   RememberMe.click();
            this.sendKey(user, "Username");
            //ScreenShot.takeSnapShot(oDriver, "Login");
            this.sendKey(pwd, "Password");
            status = true;
        } else if (this.checkElementDisplayed("UsernameEnglishVersion")) {
            //  	RememberMe.click();
            this.sendKey(user, "UsernameEnglishVersion");
            //UsernameEnglishVersion.sendKeys(user);
            //ScreenShot.takeSnapShot(oDriver, "Login");
            this.sendKey(pwd, "PasswordEnglishVersion");

            status = true;
        } else {
            //ScreenShot.takeSnapShot(oDriver, "Login");
            status = false;
            // return status;
        }

        //click on the giris yap button to move to the otp page
        if (this.checkElementDisplayed("GirisYap")) {
            this.clickToElement("GirisYap");
            if (autoLoginFlag == "true") {
                this.clickToElement("EvetBtn");
            } else {
                this.clickToElement("HayirBtn");
            }
            this.sleep(2);
        } else if (this.checkElementDisplayed("Login")) {
            this.clickToElement("Login");
        }

        this.sleep(10);
        //CommonLib.waitFor(10);
    }

    public void enterOTP() throws Exception {
        System.out.println("OTP Page");
        Thread.sleep(1000);
        seePage("LoginOTPPage");
        if (checkElementDisplayed("otp")) {
            clickToElement("otp");
            sendKey("1111", "otp");
            System.out.println("Entered OTP");
        } else {
            allureReport(StepResultType.FAIL, "otp is not displayed", true);
        }

        Thread.sleep(3000);
        //check for the HemenET popup
        if (checkElementDisplayed("Kapat")) {
            //close the popup if found
            clickToElement("Kapat");
            //((PressesKey) oDriver).pressKey(new KeyEvent(AndroidKey.BACK));
        }
    }

    public void homepageYanimda() throws Exception {
        Thread.sleep(2000);
        seePage("YanimdaHomePage");

        if (checkElementDisplayed("KilavuzdanCikAndroid")) {
            clickToElement("KilavuzdanCikAndroid");
        }

        if (checkElementDisplayed("NewCloseIcon")) {
            clickToElement("NewCloseIcon");
        }

        if (checkElementDisplayed("cancelNewPopUpIcon")) {
            clickToElement("cancelNewPopUpIcon");
        }
        //check for the hesabim button on the homepage
        if (mobildriver instanceof AndroidDriver) {
            waitElement("MainAccount");
        } else {
            MobileElement mobileElement = null;
            for (int i = 0; i < 20; i++) {
                mobileElement = mobildriver.findElement(getLocator("MainAccount"));
                sleep(1);
                if (mobileElement != null) {
                    allureReport(StepResultType.PASS, "Element displayed", true);
                    break;
                }
            }
            if (mobileElement == null) {
                allureReport(StepResultType.FAIL, "Element not displayed", true);
            }
        }
        // clickToElement("MainAccount");
    }

    public void logOut() throws IOException, ParseException, InterruptedException {
        Thread.sleep(5);
        seePage("YanimdaHomePage");
        if (mobildriver instanceof AndroidDriver) {
            if (checkElementDisplayed("MainAccount")) {
                clickToElement("MainAccount");
                clickToElement("exit button");
                clickToElement("evet button");
                Thread.sleep(5);
                seePage("LoginToYanimdaPage");
                waitElement("GirisYap");
            }
        } else {
            if (checkElementDisplayedIOS("MainAccount")) {
                clickToElement("MainAccount");
                clickToElement("exit button");
                clickToElement("evet button");
                Thread.sleep(5);
                seePage("LoginToYanimdaPage");
                waitElement("GirisYap");
            }

        }


    }

    public void loginProd(String username, String password) throws Exception {

        // LoginToYanimdaPage ob1= new LoginToYanimdaPage(oDriver,oExtentReport,oExtentTest,dataMap);
        //  LoginOTPPage ob2= new LoginOTPPage(oDriver,oExtentReport,oExtentTest,dataMap);
        // YanimdaHomePage ob3= new YanimdaHomePage(oDriver,oExtentReport,oExtentTest,dataMap);

        //logOutProd();
        fLoginProd(username, password);
        enterOTPprod();
        homepageYanimdaProd();

    }

    public boolean logOutProd() throws IOException, InterruptedException, ParseException {

        boolean status = false;
        seePage("LoginToYanimdaPage");
        sleep(10);

        if (checkElementDisplayed("ClosePopup")) {
            clickToElement("ClosePopup");
        }
        if (checkElementDisplayed("IslemSonucuProd")) {
            clickToElement("IslemSonucuProd");
            waitAndClick("CikisProd");
            waitAndClick("EvetBtnProd");
            status = true;
        } else if (checkElementDisplayed("HesabimAndroid")) {
            clickToElement("HesabimAndroid");
            waitAndClick("CikisProd");
            waitAndClick("EvetBtnProd");
            status = true;
        } else {
            //ScreenShot.takeSnapShot(oDriver,"Test is failed");
        }
        return status;
    }

    public boolean fLoginProd(String user, String pwd) throws Exception {
        boolean status = false;

        seePage("LoginToYanimdaPage");
        sleep(2);

        //check for the giris yap button on the app launch page

        try {
            if (checkElementDisplayed("GirisYap")) {
                clickToElement("GirisYap");
                sleep(2);
                waitAndClick("MobilHesabim");
                status = true;
            } else {
                return false;
                //MyTestNGBaseClass.allureReport("pass","Failed Login",true);
            }
        } catch (Exception e) {
        }

        //check the username option on the login page
        waitElement("Username");
        //RememberMeProd.click();
        sendKey(user, "Username");
        //MyTestNGBaseClass.allureReport("pass",user+" credentials login",true);
        sendKey(pwd, "Password");

        //click on the giris yap button to move to the otp page
        if (checkElementDisplayed("GirisYap")) {
            clickToElement("GirisYap");
        }

        if (checkElementDisplayed("BeniHatirla")) {
            waitAndClick("HayirBtn");
        }
        return status;

    }

    public boolean enterOTPprod() throws Exception {
        boolean status = false;
        System.out.println("OTP Page");
        seePage("LoginOTPPage");
        Thread.sleep(1000);


        //checking for the otp button on the page
        if (checkElementDisplayed("otpProd")) {
            //click the otp box and enter the otp
            clickToElement("otpProd");

            sendKey("1111", "otpProd");
            // for (int i = 0; i < 4; i++) {
            //    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.NUMPAD_1));
            // }
            System.out.println("Entered OTP");
            status = true;
        } else {
            //ScreenShot.takeSnapShot(mobildriver,"Tet is failed");
            status = false;
            return status;
        }

        //click on the giris yap button after passing the otp
        if (checkElementDisplayed("girisYap")) {
            clickToElement("girisYap");
            //girisYap.click();
        }
        Thread.sleep(3000);


        return status;

    }

    public boolean homepageYanimdaProd() throws Exception {
        boolean status = false;

        seePage("YanimdaHomePage");
        if (checkElementDisplayed("KilavuzdanCikAndroid")) {
            clickToElement("KilavuzdanCikAndroid");
        }
        Thread.sleep(3000);
        //check for the HemenET popup
        if (checkElementDisplayed("CloseIconProd")) {
            //close the popup if found
            clickToElement("CloseIconProd");
            System.out.println("Homepage appeared");
            //((PressesKey) oDriver).pressKey(new KeyEvent(AndroidKey.BACK));
        }
        if (checkElementDisplayed("NewCloseIcon")) {
            clickToElement("NewCloseIcon");
        }
        //check for the vodafone logo on the homepage
		/*if(CommonLib.checkElementDisplayed(oDriver, VodafoneLogo, "Vodafone Logo"))
		{
			status = true;
		}else
			{
				status = false;
				return status;
			}*/
        //check for the hesabim button on the homepage
        if (checkElementDisplayed("MainAccount")) {
            status = true;
            // ScreenShot.takeSnapShot(oDriver,"Home Page Yanımda is displayed");
        } else {
            //ScreenShot.takeSnapShot(oDriver,"Test is failed");
        }

        return status;
    }

    public String reatDataFromExcel(int rowIndex, int columnIndex, int sheetIndex, Boolean numericOrNot) {
        ExcelUtils excelUtils = new ExcelUtils();
        return excelUtils.ReadCellDataAtSheet(rowIndex, columnIndex, sheetIndex, numericOrNot);
    }

    public void allureReport(StepResultType result, String message, boolean ssFlag) {
        try {
            if (ssFlag) {
                Allure.addAttachment("Screenshot : " + message, new ByteArrayInputStream(((TakesScreenshot) mobildriver).getScreenshotAs(OutputType.BYTES)));
            }
            if (result.toString().equalsIgnoreCase("PASS")) {
                Allure.step(message, Status.PASSED);
            } else if (result.toString().equalsIgnoreCase("INFO")) {
                Allure.step(message, Status.SKIPPED);
            } else if (result.toString().equalsIgnoreCase("FAIL")) {
                Allure.step(message, Status.FAILED);
                Assert.fail(message);
            } else {
                Allure.step(message);
            }

        } catch (Exception e) {
        }

    }

    public boolean deleteofferdb(String username) throws Throwable {
        boolean status = false;
        String usernameNew = "90" + username;
        String query = "delete from pr_data_ih_fact where pysubjectıd ='" + usernameNew + "'";

        Statement stmt = db.DataBaseConn(AutomationConstants.MCCMBUST, AutomationConstants.MCCMUsername, AutomationConstants.MCCMPassword);
        int recordCount = stmt.executeUpdate(query);
        if (recordCount >= 0) {
            System.out.println(recordCount + " records deleted successfully!");
            status = true;
        }
        return status;
    }

    public void clickElementCoordinate(String element) {
        MobileElement mobileElement = null;
        int timeout = 15;
        for (int i = 0; i < timeout; i++) {
            try {
                mobileElement = mobildriver.findElement(getLocator(element));
                sleep(1);
            } catch (Exception e) {

            }

            if (mobileElement != null) {
                allureReport(StepResultType.PASS, element + " displayed", true);
                break;
            }
        }
        if (mobileElement == null) {
            allureReport(StepResultType.FAIL, element + " not displayed", true);
        } else {
            clickToCoordinate(mobileElement.getRect().x + (mobileElement.getRect().width / 2), mobileElement.getRect().y + (mobileElement.getRect().height / 2));
        }

    }


    public void floginerror(String user, String pwd) throws InterruptedException, IOException, ParseException {
        boolean status = false;
        this.sleep(10);
        seePage("LoginToYanimdaPage");

        //check for the giris yap button on the app launch page
        this.wait = new WebDriverWait(mobildriver, 30);
        if (this.checkElementDisplayed("KlavuzPopupClose")) {
            this.clickToElement("KlavuzPopupClose");
        }

        try {
            this.wait = new WebDriverWait(mobildriver, 10);
            if (this.checkElementDisplayed("GirisYap")) {
                this.clickToElement("GirisYap");
                this.sleep(2);
                this.clickToElement("MobilHesabim");
                status = true;


            } else if (this.checkElementDisplayed("Login")) {
                this.clickToElement("Login");

                status = true;
            } else {
                //ScreenShot.takeSnapShot(oDriver, "Login");
                status = false;
                //return status;
            }
        } catch (Exception e) {
        }

        //check the username option on the login page
        if (this.checkElementDisplayed("Username")) {
            //   RememberMe.click();
            this.sendKey(user, "Username");
            //ScreenShot.takeSnapShot(oDriver, "Login");
            this.sendKey(pwd, "Password");
            status = true;
        } else if (this.checkElementDisplayed("UsernameEnglishVersion")) {
            //  	RememberMe.click();
            this.sendKey(user, "UsernameEnglishVersion");
            //UsernameEnglishVersion.sendKeys(user);
            //ScreenShot.takeSnapShot(oDriver, "Login");
            this.sendKey(pwd, "PasswordEnglishVersion");

            status = true;
        } else {
            //ScreenShot.takeSnapShot(oDriver, "Login");
            status = false;
            // return status;
        }

        //click on the giris yap button to move to the otp page
        if (this.checkElementDisplayed("GirisYap")) {
            this.clickToElement("GirisYap");
            this.sleep(2);
            this.clickToElement("HayirBtn");
        } else if (this.checkElementDisplayed("Login")) {
            this.clickToElement("Login");
        }

        this.sleep(10);
        //CommonLib.waitFor(10);
    }

    //PaymentSquad
    public void checkTLIslemleri(String menu) throws IOException, InterruptedException, ParseException {
        //MyTestNGBaseClass.reportResult("INFO", "Check TLIslemleri Icon", false);
        seePage("YanimdaHomePage");

        //check for the Islemleri icon
        if (checkElementDisplayed("TLIslemleri")) {
            waitAndClick("TLIslemleri");

            if (menu.equalsIgnoreCase("one")) {
                if (checkElementDisplayed("TLYukle")) {
                    waitAndClick("TLYukle");
                    waitElement("TLYuklePage");
                }
            } else if (menu.equalsIgnoreCase("two")) {
                if (checkElementDisplayed("KolayPaket")) {
                    waitAndClick("KolayPaket");

                    if (checkElementDisplayed("KolayPaketPage")) {
                        waitAndClick("KolayOption");
                        waitElement("KolayPaymentPage");
                    }
                }
            }
        }
    }

    //PaymentSquad
    public void topupCheck(String cardCC, String mon, String year, String cvv, String pwd, String flag) throws Exception {
        if (checkElementDisplayed("TLPrice")) {
            clickToElement("TLPrice");

            if (checkElementDisplayed("TLCardTitle")) {
                clickToElement("TLCardTitle");
                //enter card details
//				CardNum.click();
                if (checkElementDisplayed("CardNum")) {
                    clickToElement("CardNum");
                    Thread.sleep(3000);

                    //enter card number
                    for (char c : cardCC.toCharArray()) {
                        ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                    }

                    //enter month
                    Thread.sleep(2000);
                    for (char c : mon.toCharArray()) {
                        ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                    }

                    //enter year
                    Thread.sleep(2000);
                    for (char c : year.toCharArray()) {
                        ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                    }

                    //enter cvv
                    Thread.sleep(2000);
                    for (char c : cvv.toCharArray()) {
                        ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                    }

                    hideKeyboard();

                    //masterpass
                    if (flag.equalsIgnoreCase("no")) {
                        waitAndClick("masterpassSwitch");
                    } else {
                        if (checkElementDisplayed("TLYukle")) {
                            System.out.println("TL Yukle Visible");
                        } else {
                            scrollUntilFindElement("TLYukle", "down", 20);
                            // CommonLib.scrollDownTillElementVisible(oDriver, TLYukle, "TLYukle");
                        }
                    }

                    if (checkElementDisplayed("TLYukle")) {
                        clickToElement("TLYukle");
                        if (checkElementDisplayed("PasscodeTitle")) {
                            sendKey(pwd, "Passcode");
                            //Passcode.sendKeys(pwd);
                            Thread.sleep(2000);
                            //System.out.println(Gonder.isDisplayed());
                            waitAndClick("Gonder");
                            //Gonder.click();

                            if (checkElementDisplayed("ImageStatus")) {
                                waitElement("SuccessMssg");
                                // MyTestNGBaseClass.reportResult("INFO", "Payment Successful", true);
                                // status = true;
                            }
                        }
                    }
                }
            }
        }

        if (checkElementDisplayed("home page button")) {
            clickToElement("home page button");
        }

        for (int i = 0; i < 5; i++) {
            if (checkElementDisplayed("my account button")) {
                break;
            } else {
                hideKeyboard();
                //CommonLib.pressBack(oDriver);
                Thread.sleep(2000);
            }
        }
    }

    //PaymentSquad
    public void topupCheckKolay(String cardCC, String mon, String year, String cvv, String pwd, String flag) throws Exception {

        if (checkElementDisplayed("TLCardTitle")) {
            clickToElement("TLCardTitle");
            //enter card details
            waitAndClick("CardNum");
            if (checkElementDisplayed("CardNum")) {
                clickToElement("CardNum");
                Thread.sleep(3000);

                //enter card number
                for (char c : cardCC.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                //enter month
                Thread.sleep(2000);
                for (char c : mon.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                //enter year
                Thread.sleep(2000);
                for (char c : year.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                //enter cvv
                Thread.sleep(2000);
                for (char c : cvv.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                hideKeyboard();

                //masterpass
                if (flag.equalsIgnoreCase("no")) {
                    //System.out.println(kolayMasterpassSwitch.isDisplayed());
                    waitAndClick("masterpassSwitch");

                    if (checkElementDisplayed("YuklemeYap")) {
                        System.out.println("Yukleme Yap Visible");
                        waitAndClick("kolayApproval");
                    } else {
                        scrollUntilFindElement("YuklemeYap", "down", 10);
                        waitAndClick("kolayApproval");
                    }
                } else {
                    if (checkElementDisplayed("YuklemeYap")) {
                        System.out.println("Yukleme Yap Visible");
                        waitAndClick("kolayApproval");
                    } else {
                        scrollUntilFindElement("YuklemeYap", "down", 10);
                        waitAndClick("kolayApproval");
                    }
                }

                if (checkElementDisplayed("YuklemeYap")) {
                    clickToElement("YuklemeYap");
                    if (checkElementDisplayed("PasscodeTitle")) {
                        sendKeyWithIndex(pwd, "Passcode", 0);
                        //Passcode.sendKeys(pwd);
                        Thread.sleep(2000);
                        //System.out.println(Gonder.isDisplayed());
                        waitAndClick("Gonder");

                        if (checkElementDisplayed("ImageStatus")) {
                            waitElement("SuccessMssg");
                        }
                    }
                }
            }
        }
        if (checkElementDisplayed("home page button")) {
            clickToElement("home page button");
        }
        if (checkElementDisplayed("my account button")) {

        } else {
            hideKeyboard();
            Thread.sleep(2000);
        }

    }

    //PaymentSquad
    public void topupCheckInvoice(String cardCC, String mon, String year, String cvv, String pwd, String flag) throws Exception {

        if (checkElementDisplayed("TLCardTitle")) {
            clickToElement("TLCardTitle");
            //enter card details
            waitAndClick("CardNum");
            if (checkElementDisplayed("CardNum")) {
                clickToElement("CardNum");
                Thread.sleep(3000);

                //enter card number
                for (char c : cardCC.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                //enter month
                Thread.sleep(2000);
                for (char c : mon.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                //enter year
                Thread.sleep(2000);
                for (char c : year.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                //enter cvv
                Thread.sleep(2000);
                for (char c : cvv.toCharArray()) {
                    ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
                }

                hideKeyboard();

                //masterpass
                if (flag.equalsIgnoreCase("no")) {
                    waitAndClick("masterpassSwitch");

                    if (checkElementDisplayed("FaturaOde")) {
                        System.out.println("Fatura Ode Visible");
                    } else {
                        scrollUntilFindElement("FaturaOde", "down", 10);
                    }
                } else {
                    if (checkElementDisplayed("FaturaOde")) {
                        System.out.println("Fatura Ode Visible");
                    } else {
                        scrollUntilFindElement("FaturaOde", "down", 10);
                    }
                }

                if (checkElementDisplayed("FaturaOde")) {
                    clickToElement("FaturaOde");
                    if (checkElementDisplayed("PasscodeTitle")) {
                        sendKey(pwd, "Passcode");
                        Thread.sleep(2000);
                        waitAndClick("Gonder");

                        if (checkElementDisplayed("ImageStatus")) {
                            waitElement("SuccessMssg");
                        }
                    }
                }
            }
        }

        if (checkElementDisplayed("home page button")) {
            clickToElement("home page button");
        }

        for (int i = 0; i < 5; i++) {
            if (checkElementDisplayed("my account button")) {
                break;
            } else {
                hideKeyboard();
                Thread.sleep(2000);
            }
        }
    }


    public void loginPageControls() throws IOException, ParseException {
        seePage("LoginToYanimdaPage");

        //check for the giris yap button on the app launch page
        wait = new WebDriverWait(mobildriver, 10);
        if (checkElementDisplayed("KlavuzPopupClose")) {
            this.clickToElement("KlavuzPopupClose");
        }
        wait = new WebDriverWait(mobildriver, 10);
        if (checkElementDisplayed("GirisYap")) {

            waitElement("vodafone login welcome message");
            clickToElement("GirisYap");

            sleep(2);
            if (checkElementDisplayed("login with different account button")) {
                clickToElement("login with different account button");
            }
            clickToElement("MobilHesabim");

            //Cannot start with a number different from 5 in Vodafone Mobil Hesabım
            waitElement("Username");
            sendKey("6433011162", "Username");
            waitAndClick("Password");
            waitElement("invalid phone number error text");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginEmailTxt"))).clear();

            //Cannot give GSM number longer than 10 digits (Ex:11 digits)
            waitAndClick("Username");
            String wrongPhone = "54330111629";
            String firstTenDigitOfWrongPhone = wrongPhone.substring(0, 10);

            for (char c : wrongPhone.toCharArray()) {
                ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
            }

            String textOnUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginEmailTxt"))).getText();
            if (textOnUsername.equalsIgnoreCase(firstTenDigitOfWrongPhone)) {
                allureReport(StepResultType.PASS, "11 digit control is successfully", true);
            } else {
                allureReport(StepResultType.FAIL, "11 digit control is unsuccessfully", true);
            }

            // password field control
            String wrongPassword = "123456789";
            waitAndClick(By.id("com.vodafone.selfservis.beta:id/loginPassTxt"));

            for (char c : wrongPassword.toCharArray()) {
                ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
            }

            String textOnPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginPassTxt"))).getText();
            if (textOnPassword.equalsIgnoreCase("12345678")) {
                allureReport(StepResultType.PASS, "8 digit control is successfully for password field", true);
            } else {
                allureReport(StepResultType.FAIL, "8 digit control is unsuccessfull for password field", true);
            }

            hideKeyboard();

            if (checkElementDisplayed("home/work internet button")) {
                waitAndClick("home/work internet button");
            } else {
                hideKeyboard();
                if (checkElementDisplayed("home/work internet button")) {
                    waitAndClick("home/work internet button");
                } else {
                    waitAndClick("home/work internet button");
                }
            }

            waitAndClick("home internet tab");
            waitElement("tc identification number text");
            waitElement("Password");

            waitAndClick("work internet tab");
            waitElement("user code/tax number text");
            waitElement("Password");

            hideKeyboard();

            if (checkElementDisplayed("MobilHesabim")) {
                waitAndClick(By.id("com.vodafone.selfservis.beta:id/ivClose"));
                waitAndClick("GirisYap");
            } else {
                hideKeyboard();
                if (checkElementDisplayed("MobilHesabim")) {
                    waitAndClick(By.id("com.vodafone.selfservis.beta:id/ivClose"));
                    waitAndClick("GirisYap");
                } else {
                    waitAndClick(By.id("com.vodafone.selfservis.beta:id/ivClose"));
                    waitAndClick("GirisYap");
                }
            }

            if (checkElementDisplayed("login with different account button")) {
                clickToElement("login with different account button");
            }
            clickToElement("MobilHesabim");
            waitElement("Username");
            sendKey("5433011162", "Username");
            waitAndClick("Password");
            for (char c : "12345678".toCharArray()) {
                ((PressesKey) mobildriver).pressKey(new KeyEvent(AndroidKey.valueOf("DIGIT_" + c)));
            }

            waitElement(By.xpath("//*[@resource-id='com.vodafone.selfservis.beta:id/loginEmailTxt' or @password='true']"));

            waitAndClick(By.id("com.vodafone.selfservis.beta:id/ivBack"));
            waitElement("MobilHesabim");
            waitAndClick(By.id("com.vodafone.selfservis.beta:id/ivClose"));
            waitAndClick("GirisYap");
            if (checkElementDisplayed("login with different account button")) {
                clickToElement("login with different account button");
            }
            clickToElement("MobilHesabim");
            waitElement("Username");
            sendKey("a", "Username");
            textOnUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginEmailTxt"))).getText();
            if (textOnUsername.length() > 0) {
                allureReport(StepResultType.PASS, "entering char to username field is not successfull", true);
            } else {
                allureReport(StepResultType.FAIL, "entering char to username field is  successfull", true);
            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginEmailTxt"))).clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginPassTxt"))).clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginEmailTxt"))).sendKeys("5463011162");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.vodafone.selfservis.beta:id/loginPassTxt"))).sendKeys("11223344");

            if (checkElementDisplayed("GirisYap")) {
                clickToElement("GirisYap");
                waitAndClick("HayirBtn");
            }
            seePage("LoginOTPPage");
            waitElement("otp");
        } else {
            allureReport(StepResultType.FAIL, "GirisYap is not displayed", true);
        }

    }


    public void swipe(int startPointPercentage, int endPointPercentage, int locationY) {
        Dimension dimensions = mobildriver.manage().window().getSize();
        int Startpoint = (dimensions.getWidth() * startPointPercentage / 100);
        int scrollEnd = (dimensions.getWidth() * endPointPercentage / 100);
        int center = locationY;

        TouchAction action = new TouchAction(mobildriver);
        action.press(PointOption.point(Startpoint, center)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(scrollEnd, center)).release().perform();
    }

    public void swipeOneTime(String elementLocated, int startPointPercentage, int endPointPercentage) {
        WebElement elem = waitElementAndReturnIfLocated(elementLocated);
        int center = elem.getLocation().getY();
        swipe(startPointPercentage, endPointPercentage, center);
    }

    public void swipeUntilFindElement(String elementLocated, By element, int startPointPercentage, int endPointPercentage, int maxScrollCount) {
        WebElement elem = waitElementAndReturnIfLocated(elementLocated);
        int center = elem.getLocation().getY();

        for (int i = 0; i < maxScrollCount; i++) {
            if (checkElementIfClickable(element, 0)) {
                break;
            } else {
                swipe(startPointPercentage, endPointPercentage, center);
            }
        }
    }

    public void swipeUntilFindElement(String element, String elementLocated, int startPointPercentage, int endPointPercentage, int maxScrollCount) throws UnsupportedEncodingException {
        WebElement elem = waitElementAndReturnIfLocated(elementLocated);
        int center = elem.getLocation().getY();
        System.out.println((elem.getRect().getX() + (elem.getSize().getWidth() / 2)) + "-" + (elem.getRect().getY() + (elem.getSize().getHeight() / 2)));

        if (mobildriver instanceof AndroidDriver) {
            center = elem.getRect().getY() + (elem.getSize().getHeight() / 2);
            TouchAction action = new TouchAction(mobildriver);
            action.press(PointOption.point(elem.getRect().getX() + (elem.getSize().getWidth() / 2), elem.getRect().getY() + (elem.getSize().getHeight() / 2))).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                    .moveTo(PointOption.point(elem.getRect().getX() + (elem.getSize().getWidth() / 2) - 100, elem.getRect().getY() + (elem.getSize().getHeight() / 2))).release().perform();

            //swipe(startPointPercentage, endPointPercentage, center);


//            for (int i = 0; i < maxScrollCount; i++) {
//                if (checkElementIfClickable(getLocator(element), 0)) {
//                    break;
//                } else {
//                    swipe(startPointPercentage, endPointPercentage, center);
//                }
//            }
        } else if (mobildriver instanceof IOSDriver) {
            for (int i = 0; i < maxScrollCount; i++) {
                if (checkIOSElementIsVisible(getLocator(element), 0)) {
                    break;
                } else {
                    swipe(startPointPercentage, endPointPercentage, center);
                }
            }
        }

    }

    public void swipeFromElemLocationUntilFindElement(String element, String elementLocated, int endPointPercentage, int maxScrollCount) throws UnsupportedEncodingException {
        WebElement elem = waitElementAndReturnIfLocated(elementLocated);
        int center = elem.getLocation().getY();
        System.out.println((elem.getRect().getX() + (elem.getSize().getWidth() / 2)) + "-" + (elem.getRect().getY() + (elem.getSize().getHeight() / 2)));
        Dimension dimensions = mobildriver.manage().window().getSize();
        int scrollEnd = (dimensions.getWidth() * endPointPercentage / 100);
        if (mobildriver instanceof AndroidDriver) {
            for (int i = 0; i < maxScrollCount; i++) {
                if (checkElementIfClickable(getLocator(element), 0)) {
                    break;
                } else {
                    TouchAction action = new TouchAction(mobildriver);
                    action.press(PointOption.point(elem.getRect().getX() + (elem.getSize().getWidth() / 2), elem.getRect().getY() + (elem.getSize().getHeight() / 2))).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                            .moveTo(PointOption.point(scrollEnd, elem.getRect().getY() + (elem.getSize().getHeight() / 2))).release().perform();
                }
            }
        } else if (mobildriver instanceof IOSDriver) {
            for (int i = 0; i < maxScrollCount; i++) {
                if (checkIOSElementIsVisible(getLocator(element), 0)) {
                    break;
                } else {
                    TouchAction action = new TouchAction(mobildriver);
                    action.press(PointOption.point(elem.getRect().getX() + (elem.getSize().getWidth() / 2), elem.getRect().getY() + (elem.getSize().getHeight() / 2))).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                            .moveTo(PointOption.point(scrollEnd, elem.getRect().getY() + (elem.getSize().getHeight() / 2))).release().perform();
                }
            }
        }
    }

    public Boolean checkElementIfClickable(By element, int index) {
        WebElement myelement = null;
        try {
            myelement = wait.until(ExpectedConditions.elementToBeClickable(element));
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.INFO, element + " is not displayed", Boolean.TRUE);
        }
        if (myelement != null) {
            return true;
        } else {
            allureReport(StepResultType.INFO, element + " is not displayed.", Boolean.FALSE);
            return false;
        }
    }

    public Boolean checkIOSElementIsVisible(By element, int index) {
        MobileElement myelement = null;
        try {
            myelement = findElementWithTimeout(element, 5);
            allureReport(StepResultType.PASS, element + " is displayed.", Boolean.TRUE);
        } catch (Exception e) {
            allureReport(StepResultType.INFO, element + " is not displayed", Boolean.TRUE);
        }
        if (myelement != null && myelement.getAttribute("visible").equalsIgnoreCase("true")) {
            return true;
        } else {
            allureReport(StepResultType.INFO, element + " is not displayed.", Boolean.FALSE);
            return false;
        }
    }

    public MobileElement iWaitElementVersion2(String element) throws UnsupportedEncodingException {
        MobileElement mobileElement = null;
        int timeout = 15;
        if (timeout > 15) {
            timeout = 15;
        }
        for (int i = 0; i < timeout; i++) {
            mobileElement = mobildriver.findElement(getLocator(element));
            sleep(1);
            if (mobileElement != null) {
                allureReport(StepResultType.PASS, element + " displayed", true);
                break;
            }
        }
        if (mobileElement == null) {
            allureReport(StepResultType.FAIL, element + " not displayed", true);
        }
        return mobileElement;
    }

    public boolean checkElementDisplayedIOS(String element) throws UnsupportedEncodingException {
        MobileElement mobileElement = null;
        int timeout = 15;
        if (timeout > 15) {
            timeout = 15;
        }
        for (int i = 0; i < timeout; i++) {
            try {
                mobileElement = mobildriver.findElement(getLocator(element));
                sleep(1);
            } catch (Exception e) {

            }
            if (mobileElement != null) {
                allureReport(StepResultType.PASS, element + " displayed", true);
                return true;
            }
        }
        if (mobileElement == null) {
            allureReport(StepResultType.INFO, element + " not displayed", true);
        }
        return false;
    }

    public void clickTobiIconIOS() throws UnsupportedEncodingException {


        MobileElement element = findElementWithTimeout(By.xpath("//XCUIElementTypeStaticText[@name=\"Tarife\"]/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/preceding-sibling::XCUIElementTypeOther/XCUIElementTypeImage"), 30);
        MobileElement element2 = findElementWithTimeout(By.xpath("//XCUIElementTypeStaticText[@name=\"Fatura\" or @name=\"TL İşlemleri\"]/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther"), 30);

        int x = element.getLocation().getX() + ((element2.getLocation().getX() - element.getLocation().getX()) / 2);
        int y = element2.getLocation().getY();

        clickToCoordinate(x, y);
//XCUIElementTypeStaticText[@name="Tarife"]/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/preceding-sibling::XCUIElementTypeOther/XCUIElementTypeImage
    }





    public void loginKoctasMobile(String username, String password) throws IOException, ParseException {

        wait = new WebDriverWait(mobildriver, 10);
        seePage("koctasTask");
        int timeout = 0;
        boolean flag = false;

        while (!flag && timeout < 20) {

            if (waitElementAndReturnIfClickable("my account button", 1) != null) {
                clickToElement("my account button");
                flag = true;
            }
            timeout += 2;
        }

        timeout = 0;
        flag = false;


        if (checkElementDisplayed("username text area")) {
            sendKey(username, "username text area");

            sendKey(password, "password text area");
            clickToElement("login button");
        } else {
            System.out.println("There is something went wrong");
        }

    }
}
