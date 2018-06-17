package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.GeneralActions;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;

public class PlaceOrderTest extends BaseTest {

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version

        CustomReporter.logAction("get main page");
        driver.get(Properties.getBaseUrl());

        WebElement webElement = driver.findElement(By.xpath("//*[@class=\"hidden-md-up text-xs-center mobile\"]"));

        CustomReporter.logAction("Check site version");
        if (isMobileTesting("chrome")) {
            Assert.assertTrue(webElement.isDisplayed());

            CustomReporter.logAction("A desktop version of the site was loaded");
        }
        else {
            Assert.assertFalse(webElement.isDisplayed());

            CustomReporter.logAction("A mobile version of the site was downloaded");
        }
    }

    @Test (dependsOnMethods = "checkSiteVersion")
    public void createNewOrder() {
        // TODO implement order creation test

        // open random product

        // save product parameters

        // add product to Cart and validate product information in the Cart

        // proceed to order creation, fill required information

        // place new order and validate order summary

        // check updated In Stock value
        GeneralActions generalActions = new GeneralActions(driver);
        generalActions.openRandomProduct();


//        System.out.println(generalActions.getOpenedProductInfo().getName() + " " + generalActions.getOpenedProductInfo()
//                .getQty() + " " + generalActions.getOpenedProductInfo().getPrice());
        ProductData productData = generalActions.getOpenedProductInfo();

        waitForContentLoad(By.xpath("//*[@class=\"btn btn-primary add-to-cart\"]")).click();

        clickOnInvisibleElement(waitForContentLoad(By
                .xpath("//*[@class=\"cart-content\"]/a[@class=\"btn btn-primary\"]")), driver);

        By productNameLocator = By
                .xpath("//*[@class=\"product-line-info\"]//*[@class=\"label\"]");
        WebElement productNameElementElement = waitForContentLoad(productNameLocator);
        String productName = productNameElementElement.getText().toUpperCase();
        Assert.assertEquals(productName, productData.getName());

        WebElement productPriceElement = driver.findElement(By
                .xpath("//*[@class=\"product-line-info\"]//*[@class=\"value\"]"));
        String productPrice = productPriceElement.getText();
        Assert.assertEquals(DataConverter.parsePriceValue(productPrice), productData.getPrice());

        WebElement productQuantityElement = driver.findElement(By
                .xpath("//*[@class=\"input-group bootstrap-touchspin\"]//*[@class=\"js-cart-line-product-quantity form-control\"]"));
        String productQuantity = productQuantityElement.getAttribute("value");
        Assert.assertEquals(DataConverter.parseStockValue(productQuantity), productData.getQty());

        driver.findElement(By
                .xpath("//*[@class=\"checkout cart-detailed-actions card-block\"]//*[@class=\"text-xs-center\"]"))
                .click();

        waitForContentLoad(By.name("firstname")).sendKeys("sdasdasda");
        driver.findElement(By.name("lastname")).sendKeys("rtertewtert");
        driver.findElement(By.name("email")).sendKeys("webinar.test@gmail.com");
        driver.findElement(By.name("continue")).click();

        waitForContentLoad(By.name("address1")).sendKeys("fjfjfjfjfjfjfjfjfjfjfjfjfjf");
        driver.findElement(By.name("postcode")).sendKeys("11111");
        driver.findElement(By.name("city")).sendKeys("Cherdf");
        driver.findElement(By.name("confirm-addresses")).click();

        waitForContentLoad(By.name("confirmDeliveryOption")).click();

        waitForContentLoad(By.id("payment-option-1")).click();
        waitForContentLoad(By.id("conditions_to_approve[terms-and-conditions]")).click();
        waitForContentLoad(By.xpath("//*[@id=\"payment-confirmation\"]//button[@type=\"submit\"]")).click();

        WebElement materialIconDoneElement = waitForContentLoad(By.xpath("//*[@class=\"material-icons done\"]"));
        Assert.assertFalse(Objects.isNull(materialIconDoneElement));


        By productNameAfterOrderingLocator = By
                .xpath("//*[@id=\"order-items\"]//*[@class=\"col-sm-4 col-xs-9 details\"]/span");
        WebElement productNameAfterOrderingElement = waitForContentLoad(productNameAfterOrderingLocator);
        String productNameAfterOrdering = productNameAfterOrderingElement.getText()
                .substring(0, productData.getName().length()).toUpperCase();

        Assert.assertEquals(productNameAfterOrdering, productData.getName());

        WebElement productPriceAfterOrderingElement = driver.findElement(By
                .xpath("//*[@id=\"order-items\"]//*[@class=\"col-xs-5 text-sm-right text-xs-left\"]"));
        String productPriceAfterOrdering = productPriceAfterOrderingElement.getText();
        Assert.assertEquals(DataConverter.parsePriceValue(productPriceAfterOrdering), productData.getPrice());

        WebElement productQuantityAfterOrderingElement = driver.findElement(By
                .xpath("//*[@id=\"order-items\"]//*[@class=\"col-xs-2\"]"));
        String productQuantityAfterOrdering = productQuantityAfterOrderingElement.getText();
        Assert.assertEquals(DataConverter.parseStockValue(productQuantityAfterOrdering), productData.getQty());

        WebElement serchField = waitForContentLoad(By.xpath("//*[@id=\"search_widget\"]//*[@name=\"s\"]"));


        new Actions(driver).moveToElement(serchField).click(serchField).build().perform();

        StringSelection selection = new StringSelection(productData.getName());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        serchField.sendKeys(Keys.BACK_SPACE);
        serchField.sendKeys(Keys.CONTROL + "v");
        serchField.submit();




        driver.findElement(By.xpath("//*[@class=\"nav-item\"]//*[@class=\"nav-link active\"]"));

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private WebElement waitForContentLoad(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static void clickOnInvisibleElement(WebElement element, WebDriver driver) {

        String script = "var object = arguments[0];"
                + "var theEvent = document.createEvent(\"MouseEvent\");"
                + "theEvent.initMouseEvent(\"click\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
                + "object.dispatchEvent(theEvent);"
                ;

        ((JavascriptExecutor)driver).executeScript(script, element);
    }

}