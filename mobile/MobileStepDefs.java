package stepdef;

import classes.Commonlib;
import commons.StepResultType;
import cucumber.api.java.en.When;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.LocalDriver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MobileStepDefs {

    //private final DesiredCapabilitiesUtil desiredCapabilitiesUtil = new DesiredCapabilitiesUtil();
    public MobileDriver<MobileElement> driver;
    public WebDriverWait wait;
    Commonlib commonlib;
    public int waitSeconds = 15;
    public String activeAppBundle = null;


    @Before
    public void setup() {
        driver = LocalDriver.getTLDriver();
        wait = new WebDriverWait(driver, waitSeconds);
        commonlib = new Commonlib(driver, wait);
    }

    @After
    public void finish() {
        if (activeAppBundle != null) {
            commonlib.terminateToApplication(activeAppBundle);
        }
    }

    @Given("I see {string} page")
    public void seePage(String page) throws IOException, ParseException {
        commonlib.seePage(page);
    }

    @Given("I wait {string} element")
    public void waitElement(String element) {
        commonlib.waitElement(element);
    }

    @Given("I wait {string} elements")
    public void waitMultipleElement(String elements) {
        String[] mylements = elements.split(";");
        for (String element : mylements) {

            commonlib.waitElement(element);
        }
    }

    @Given("I wait {string} element with index:{int}")
    public void waitElementWithIndex(String elem, int index) {
        commonlib.waitElementWithIndex(elem, index);
    }

    @Given("I wait {string} element and click")
    public void waitAndClick(String elem) throws UnsupportedEncodingException, InterruptedException {

        if (driver instanceof AndroidDriver) {
            commonlib.waitAndClick(elem);
        }else if(driver instanceof IOSDriver)
        {
            commonlib.waitAndClick(elem);
        }
        else {
            MobileElement mobileElement = null;
            int timeout=0;
            while (mobileElement==null&& timeout<15) {
                try{
                    mobileElement = driver.findElement(commonlib.getLocator(elem));
                    sleep(1);
                }catch (Exception e){

                }

                timeout++;
            }
            if (mobileElement != null) {
                commonlib.allureReport(StepResultType.PASS, "Element  displayed", true);
                mobileElement.click();
                commonlib.allureReport(StepResultType.PASS, "Element  clicked", true);
            } else {
                commonlib.allureReport(StepResultType.FAIL, "Element not displayed", true);
            }
        }
    }

    @Given("I click {string} element if {string} element is exist timeout:{int}")
    public void clickIfExist(String elem, String elem2, int timeout) {
        commonlib.clickIfExist(elem, elem2, timeout);
    }

    @Given("I click coordinate of given {string} element")
    public void clickCoordinate(String elem) throws UnsupportedEncodingException {
        MobileElement mobileElement = driver.findElement(commonlib.getLocator(elem));
        clickToCoordinate(mobileElement.getLocation().getX(), mobileElement.getLocation().getY());
    }

    @Given("I click {string} element")
    public void click(String elem) throws UnsupportedEncodingException {
        commonlib.clickToElement(elem);
    }

    @Given("I wait for {int} seconds")
    public void sleep(int sec) throws InterruptedException {
        commonlib.sleep(sec);
    }

    @Given("I send {string} text to {string} element")
    public void sendKey(String text, String elem) {
        commonlib.waitElementAndReturnIfLocated(elem).sendKeys(text);
    }

    @Given("I send {string} text to {string} element with index:{int}")
    public void sendKeyWithIndex(String text, String elem, int index) {
        //MobileElement element=waitElementAndReturnIfClickable(elem);
        commonlib.waitElementWithIndexAndReturnIfClickable(elem, index).sendKeys(text);
    }

    @Given("I hide keyboard")
    public void hideKeyboard() {
        commonlib.hideKeyboard();
    }

    @Given("I click android back button")
    public void clickAndroidBackButton() {
        commonlib.hideKeyboard();
    }

    @Given("I click to coordinate x:{int} ,y:{int}")
    public void clickToCoordinate(int x, int y) {
        commonlib.clickToCoordinate(x, y);
    }

    @Given("^LoginToYanimdaApp UAT \"([^\"]*)\" sheet index:\"([^\"]*)\"$")
    public void logintoyanimdaappUAT(int rowl, int sheetIndex) throws Throwable {
        String evrironmentValue = commonlib.reatDataFromExcel(1, 9, 1, false);
        String username = "";
        String password = "";

        if (evrironmentValue.equalsIgnoreCase("SAT")) {
            username = commonlib.reatDataFromExcel(rowl, 0, sheetIndex, true);
            password = commonlib.reatDataFromExcel(2, 2, sheetIndex, true);

            commonlib.loginTest(username, password);
        } else if (evrironmentValue.equalsIgnoreCase("Live")) {

            username = commonlib.reatDataFromExcel(rowl, 1, sheetIndex, true);
            password = commonlib.reatDataFromExcel(2, 2, sheetIndex, true);

            commonlib.loginProd(username, password);
        }
    }

    //"^I login with username:([^\"]*) password:([^\"]*) package:(prod|beta)
    @Given("^I read data from (\\d+).sheet with row:(\\d+) column:(\\d+) return value numeric:(true|false)$")
    public void printDataFromExcel(int sheetIndex, int rowIndex, int columnIndex, String numericOrNot) {
        if (numericOrNot.equalsIgnoreCase("true")) {
            System.out.println(commonlib.reatDataFromExcel(rowIndex, columnIndex, sheetIndex, true));
        } else {
            System.out.println(commonlib.reatDataFromExcel(rowIndex, columnIndex, sheetIndex, false));
        }
    }

    //@Given("I login with username:{string} password:{string} package:{string}")
    @Given("^I select an add-on package type and select package that will be cancel sheet index:\"([^\"]*)\" row index:\"([^\"]*)\"$")
    public void selectPackages(int sheetIndex, int rowIndex) throws IOException, ParseException, InterruptedException {
        String packageTypeValue = commonlib.reatDataFromExcel(rowIndex, 3, sheetIndex, false);
        commonlib.seePage("AddonPage");
        commonlib.waitElement("TarifemVePaketlerim text area");
        commonlib.waitElement("EkPaketlerim");
        WebElement myelement = commonlib.waitElementAndReturnIfLocated("EkPaketlerim");
        //int y=myelement.getLocation().getY();
        Dimension dimensions = commonlib.mobildriver.manage().window().getSize();
        commonlib.scrollDown(50, 5);
        //commonlib.scrollDown(90, 80);

        try {
            commonlib.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.LinearLayout[@content-desc='" + packageTypeValue + "']"))).click();
            commonlib.allureReport(StepResultType.PASS, "", true);
        } catch (Exception e) {
            commonlib.allureReport(StepResultType.FAIL, "", true);
        }
        String packageValue = commonlib.reatDataFromExcel(rowIndex, 4, sheetIndex, false);
        commonlib.scrollUntilFindElement(By.xpath("//*[@text='" + packageValue + "']"), "down", 20);
        commonlib.clickToElement(By.xpath("//*[@text='" + packageValue + "']"));
        //commonlib.scrollUntilFindElement("package cancel button","down",5);
    }

    @Given("^I scroll until find ([^\"]*) element with direction:(down|up) and max scroll count:(\\d+)$")
    public void scrollUntilFindElement(String element, String direction, int scrollCount) {
        commonlib.scrollUntilFindElement(element, direction, scrollCount);
    }

    @Given("^I scroll until find ([^\"]*) element with direction:(down|up|right|rightfromcenter|rightfrombottom) , max scroll count:(\\d+),start:(\\d+) percentage and stop:(\\d+) percentage$")
    public void scrollUntilFindElement(String element, String direction, int scrollCount, int start, int stop) {
        commonlib.scrollUntilFindElement(element, direction, scrollCount, start, stop);
    }

    @Given("^I scroll down with start percentage:\"([^\"]*)\" and end percentage:\"([^\"]*)\"$")
    public void scrollDown(int start, int end) {
        commonlib.scrollDown(start, end);
    }

    @Given("I compare text of ([^\"]*) element with text:([^\"]*)$")
    public void compareElementTextWithAnotherText(String element, String text) {
        commonlib.checkTextOfElementEqualToText(element, text);
    }

    @Given("^I login with username:([^\"]*) password:([^\"]*) package:(prod|beta)$")
    public void yanimdalogin(String user, String pwd, String selectionPackage) throws Throwable {
        if (selectionPackage.equalsIgnoreCase("prod")) {
            commonlib.loginProd(user, pwd);
        } else if (selectionPackage.equalsIgnoreCase("beta")) {
            commonlib.loginBetav2(user, pwd);

        } else {
            commonlib.allureReport(StepResultType.FAIL, "Package name is undefined", false);
        }
    }

    @Given("Logout app if i login")
    public void logout() throws ParseException, InterruptedException, IOException {
        commonlib.logOut();
    }

    @Given("^I login with username:([^\"]*) password:([^\"]*) package:(prod|beta) autoLoginFlag:(true|false)$")
    public void yanimdaPlogin(String user, String pwd, String selectionPackage, String autoLoginFlag) throws Throwable {
        if (selectionPackage.equalsIgnoreCase("prod")) {
            commonlib.loginProd(user, pwd);
        } else if (selectionPackage.equalsIgnoreCase("beta")) {
            commonlib.loginTest(user, pwd, autoLoginFlag);
        } else {
            commonlib.allureReport(StepResultType.FAIL, "Package name is undefined", false);
        }
    }

    @Given("I click {string} element if exist timeout:{int}")
    public void clickIfExist(String elem, int timeout) {
        commonlib.clickIfExist(elem, timeout);
    }

    //@Given("I try login with username:{string} password:{string} package:{string}")
    @Given("^I try login with username:([^\"]*) password:([^\"]*) package:(prod|beta)$")
    public void yanimdaLoginError(String user, String pwd, String selectionPackage) throws Throwable {
        if (selectionPackage.equalsIgnoreCase("prod")) {
            commonlib.loginErrorTest(user, pwd);
        } else if (selectionPackage.equalsIgnoreCase("beta")) {
            commonlib.loginErrorTest(user, pwd);
        } else {
            commonlib.allureReport(StepResultType.FAIL, "Package name is undefined", false);
        }
    }

    //@Given("I login with username:{string} password:{string} package:{string}")
    @Given("^I check TL processes menu:([^\"]*)$")
    public void checkTLIslemleri(String menu) throws Throwable {
        commonlib.checkTLIslemleri(menu);
    }

    @When("^CheckTopupPage \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
    public void checkTopupPage(String cardNum, String mon, String year, String CVV, String passcode, String flag) throws Throwable {
        commonlib.topupCheck(cardNum, mon, year, CVV, passcode, flag);
    }

    @When("^CheckKolayPage \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
    public void checkKolayPage(String cardNum, String mon, String year, String CVV, String passcode, String flag) throws Throwable {
        commonlib.topupCheckKolay(cardNum, mon, year, CVV, passcode, flag);
    }

    @Then("I scroll until {string} element visible")
    public void iScrollUntilElementVisible(String element) {
        commonlib.scrollDownTillElementVisible(element);
    }

    @And("I execute delete query {string}")
    public void iExecuteDeleteQuery(String username) throws Throwable {
        commonlib.deleteofferdb(username);
    }

    @When("^CheckInvoicePage \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
    public void checkInvoicePage(String cardNum, String mon, String year, String CVV, String passcode, String flag) throws Throwable {
        commonlib.topupCheckInvoice(cardNum, mon, year, CVV, passcode, flag);
    }

    @Given("I start {string} application")
    public void iStartApplication(String appName) throws InterruptedException {
        System.out.println("uygulama başlatılıyor :)");
        commonlib.startToApplication(appName);
        activeAppBundle = appName;
    }

    @Given("I close {string} application")
    public void iCloseApplication(String appName) throws InterruptedException {
        System.out.println("uygulama kapatılıyor :)");
        commonlib.terminateToApplication(appName);
    }

    @Then("I swipe until find {string} element with {string} located element startPointPercentage:{int} endPointPercentage:{int} maxScrollCount:{int}")
    public void swipeUntilFind(String element, String locatedElement, int startPointPercentage, int endPointPercentage, int maxScrollCount) throws UnsupportedEncodingException {
        commonlib.swipeUntilFindElement(element,locatedElement,startPointPercentage,endPointPercentage,maxScrollCount);
    }

    @Then("I swipe one time with {string} located element startPointPercentage:{int} endPointPercentage:{int}")
    public void swipeOneTime( String locatedElement, int startPointPercentage, int endPointPercentage) {

        commonlib.swipeOneTime(locatedElement,startPointPercentage,endPointPercentage);

    }

    @Then("I autologin with {string}")
    public void iAutologinWithUsername(String username) throws IOException, ParseException {
        commonlib.seePage("LoginToYanimdaPage");
        commonlib.waitAndClick("GirisYap");
        commonlib.waitAndClick(By.xpath("//*[contains(@text,'" + username + "')]"));
        commonlib.seePage("CommonPage");
        waitMultipleElement("tobi icon;my account text;advantages text;recipe text");
    }

    @Given("^Logout app if i login env:(prod|beta)$")
    public void logoutAppIfILoginEnv(String env) throws IOException, ParseException, InterruptedException {
        commonlib.seePage("CommonPage");
        if (commonlib.checkElementDisplayed("NewCloseIcon")) {
            commonlib.clickToElement("NewCloseIcon");
        }

        if (env.equalsIgnoreCase("prod")) {
            if (commonlib.checkElementDisplayed("my account button")) {
                waitAndClick("my account button");
                waitAndClick("exit button");
                waitAndClick("ok button");
                waitElement("GirisYap");
            }
        } else if (env.equalsIgnoreCase("beta")) {
            seePage("YanimdaHomePage");
            if (commonlib.mobildriver instanceof AndroidDriver) {
                if (commonlib.checkElementDisplayed("MainAccount")) {
                    commonlib.clickToElement("MainAccount");
                    commonlib.clickToElement("exit button");
                    commonlib.clickToElement("evet button");
                    Thread.sleep(5);
                    seePage("LoginToYanimdaPage");
                    waitElement("GirisYap");
                }
            } else {
                iCloseApplication("yanimda beta");
                iStartApplication("yanimda beta");
                if (commonlib.checkElementDisplayedIOS("MainAccount")) {
                    commonlib. clickToElement("MainAccount");
                    commonlib.clickToElement("exit button");
                    commonlib.clickToElement("evet button");
                    Thread.sleep(1);
                    seePage("LoginToYanimdaPage");
                    waitElement("GirisYap");
                }

            }
        }
    }

    @Given("Login page controls")
    public void loginPageControls() throws IOException, ParseException {
        commonlib.loginPageControls();
    }

    @Then("I do not see {string} element")
    public void iCouldNotSeeElement(String arg0) throws UnsupportedEncodingException {
        WebElement elem = null;
        try {
            elem = wait.until(ExpectedConditions.visibilityOfElementLocated(commonlib.getLocator(arg0)));
        } catch (Exception e) {
        }

        if (elem == null) {
            commonlib.allureReport(StepResultType.PASS, "element is not displayed:" + arg0, true);
        } else {
            commonlib.allureReport(StepResultType.FAIL, "element is displayed:" + arg0, true);
        }
    }

    @Then("I see {string} text")
    public void iSeeText(String arg0) {
        commonlib.waitElement(By.xpath("//*[contains(@text,'" + arg0 + "') or contains(@content-desc,'" + arg0 + "')]"));
    }

    @Then("Delete saved accounts")
    public void deleteSavedAcoounts() throws IOException, ParseException, InterruptedException {
        commonlib.seePage("LoginToYanimdaPage");
        if (commonlib.checkElementDisplayed("edit accounts button")) {
            waitAndClick("edit accounts button");
            List<MobileElement> elements = commonlib.mobildriver.findElements(commonlib.getLocator("account delete icon"));

            for (int i = 0; i < elements.size(); i++) {
                clickIfExist("edit accounts button", 10);
                waitAndClick("account delete icon");
                waitAndClick("remove account approve button");
                waitElement("your account is deleted text");
                waitAndClick("quick close icon");
            }
            waitElement("MobilHesabim");
        }
    }

    @Then("I compare {string} with {string}")
    public void iCompareWith(String arg0, String arg1) throws UnsupportedEncodingException {
        MobileElement myelem=driver.findElement(commonlib.getLocator(arg0));
        String elemText=myelem.getText();
        String minutes=elemText.split(" ")[3].substring(1,5).split(":")[0];
        String seconds=elemText.split(" ")[3].substring(1,6).split(":")[1];

        if(minutes.equalsIgnoreCase(arg1.split(":")[0])&Integer.valueOf(arg1.split(":")[1])<Integer.valueOf(seconds))
        {
            commonlib.allureReport(StepResultType.PASS,arg0+" value is ok",true);
        }
        else {
            commonlib.allureReport(StepResultType.FAIL,arg0+" value is not ok",true);
        }
    }

    @And("I clear {string} textbox")
    public void iClearTextbox(String arg0) throws UnsupportedEncodingException {

        MobileElement myelem=driver.findElement(commonlib.getLocator(arg0));
        try{
            myelem.clear();
            commonlib.allureReport(StepResultType.PASS,arg0+" text is cleared",true);

        }catch (Exception e){
            commonlib.allureReport(StepResultType.FAIL,arg0+" text is not cleared",true);
        }
    }

    @Then("I check length of {string} element text is equal to {int}")
    public void iCheckLengthOfElementTextIsEqualTo(String arg0, int arg1) throws UnsupportedEncodingException {
        MobileElement myelem=driver.findElement(commonlib.getLocator(arg0));
        if(myelem.getText().length()==0)
        {
            commonlib.allureReport(StepResultType.PASS,arg0+" length is equal to "+arg1,true);
        }else{
            commonlib.allureReport(StepResultType.FAIL,arg0+" length is not  equal to "+arg1,true);
        }
    }

    @Then("I wait {string} with this xpath {string}")
    public void iWaitWithThisXpath$(String arg0, String arg1) {
        String myxpath=arg1.split("\\$")[0]+arg0+arg1.split("\\$")[1];
        commonlib.waitElement(By.xpath(myxpath));
    }

    @Then("I clear {string} element text")
    public void iClearElementText(String arg0) throws UnsupportedEncodingException {
        WebElement element=null;
        try{
            element=wait.until(ExpectedConditions.visibilityOfElementLocated(commonlib.getLocator(arg0)));
            element.clear();
        }catch (Exception e){
            commonlib.allureReport(StepResultType.FAIL,"",true);
        }
    }

    @Then("I click {string} element coordinate")
    public void iClickElementCoordinate(String arg0) {
        commonlib.clickElementCoordinate(arg0);
    }

    @And("I click tobi icon IOS")
    public void iClickTobiIconIOS() throws UnsupportedEncodingException {
        commonlib.clickIfExist("close black",10);
        commonlib.clickTobiIconIOS();
       // commonlib.clickToCoordinate(160,756);
    }

    @Then("I wait {string} element version2")
    public void iWaitElementVersion2(String arg0) throws UnsupportedEncodingException {
        commonlib.iWaitElementVersion2(arg0);
    }

    @And("I scroll from {int} to {int}")
    public void iScrollFromTo(int arg0, int arg1) {
        commonlib.scrollDown(arg0,arg1);
    }


    @And("I swipe until find {string} element with {string} located element endPointPercentage:{int} maxScrollCount:{int}")
    public void iSwipeUntilFindElementWithLocatedElementEndPointPercentageMaxScrollCount(String arg0, String arg1, int arg2, int arg3) throws UnsupportedEncodingException {
    commonlib.swipeFromElemLocationUntilFindElement(arg0,arg1,arg2,arg3);
    }


    @Given("^I login with username:([^\"]*) password:([^\"]*)$")
    public void Koctaslogin(String user, String pwd) throws Throwable {

            commonlib.loginKoctasMobile(user, pwd);

    }
}



