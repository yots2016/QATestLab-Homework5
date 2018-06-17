package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.GeneralActions;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;

public class PlaceOrderTest extends BaseTest {

    @Test
    public void checkSiteVersion() {
        CustomReporter.logAction("Get main page");
        driver.get(Properties.getBaseUrl());

        WebElement mobileVersionElement = driver
                .findElement(By.xpath("//*[@class=\"hidden-md-up text-xs-center mobile\"]"));

        CustomReporter.logAction("Check site version");
        if (isMobileTesting("chrome")) {
            Assert.assertTrue(mobileVersionElement.isDisplayed(), "Used desktop version of the site");

            CustomReporter.logAction("A mobile version of the site was loaded");
        }
        else {
            Assert.assertFalse(mobileVersionElement.isDisplayed(), "Used desktop version of the site");

            CustomReporter.logAction("A mobile version of the site was downloaded");
        }
    }

    @Test (dependsOnMethods = "checkSiteVersion")
    public void createNewOrder() {
        CustomReporter.logAction("Create new order");

        GeneralActions generalActions = new GeneralActions(driver);
        generalActions.openRandomProduct();

        ProductData productData = generalActions.getOpenedProductInfo();

        waitForContentLoad(By.xpath("//*[@class=\"nav nav-tabs\"]//*[@href=\"#product-details\"]")).click();

        WebElement productQuantityBeforeSaleElement = waitForContentLoad(By
                .xpath("//*[@id=\"product-details\"]/div[@class=\"product-quantities\"]/span"));

        int productQuantityBeforeSaleValue = DataConverter
                .parseStockValue(productQuantityBeforeSaleElement.getAttribute("innerHTML"));

        waitForContentLoad(By.xpath("//*[@class=\"btn btn-primary add-to-cart\"]")).click();

        clickOnInvisibleElement(waitForContentLoad(By
                .xpath("//*[@class=\"cart-content\"]/a[@class=\"btn btn-primary\"]")), driver);

        By productNameLocator = By
                .xpath("//*[@class=\"product-line-info\"]//*[@class=\"label\"]");
        WebElement productNameElementElement = waitForContentLoad(productNameLocator);
        String productName = productNameElementElement.getText().toUpperCase();
        CustomReporter.logAction("Starting check items in the basket");
        Assert.assertEquals(productName, productData.getName()
                , "In the basket is incorrectly displayed - product name");

        WebElement productPriceElement = driver.findElement(By
                .xpath("//*[@class=\"product-line-info\"]//*[@class=\"value\"]"));
        String productPrice = productPriceElement.getText();
        Assert.assertEquals(DataConverter.parsePriceValue(productPrice), productData.getPrice()
                , "In the basket is incorrectly displayed - product price");

        WebElement productQuantityElement = driver.findElement(By
                .xpath("//*[@class=\"input-group bootstrap-touchspin\"]//*[@class=\"js-cart-line-product-quantity form-control\"]"));
        String productQuantity = productQuantityElement.getAttribute("value");
        Assert.assertEquals(DataConverter.parseStockValue(productQuantity), productData.getQty()
                , "In the basket is incorrectly displayed - product quantity");
        CustomReporter.logAction("Ending check items in the basket");

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
        CustomReporter.logAction("Starting check the message confirming the order of the goods");
        Assert.assertFalse(Objects.isNull(materialIconDoneElement));
        CustomReporter.logAction("Ending check the message confirming the order of the goods");

        By productNameAfterOrderingLocator = By
                .xpath("//*[@id=\"order-items\"]//*[@class=\"col-sm-4 col-xs-9 details\"]/span");
        WebElement productNameAfterOrderingElement = waitForContentLoad(productNameAfterOrderingLocator);
        String productNameAfterOrdering = productNameAfterOrderingElement.getText()
                .substring(0, productData.getName().length()).toUpperCase();

        CustomReporter.logAction("Starting check details of the order");
        Assert.assertEquals(productNameAfterOrdering, productData.getName());

        WebElement productPriceAfterOrderingElement = driver.findElement(By
                .xpath("//*[@id=\"order-items\"]//*[@class=\"col-xs-5 text-sm-right text-xs-left\"]"));
        String productPriceAfterOrdering = productPriceAfterOrderingElement.getText();
        Assert.assertEquals(DataConverter.parsePriceValue(productPriceAfterOrdering), productData.getPrice());

        WebElement productQuantityAfterOrderingElement = driver.findElement(By
                .xpath("//*[@id=\"order-items\"]//*[@class=\"col-xs-2\"]"));
        String productQuantityAfterOrdering = productQuantityAfterOrderingElement.getText();
        Assert.assertEquals(DataConverter.parseStockValue(productQuantityAfterOrdering), productData.getQty());
        CustomReporter.logAction("Ending check details of the order");

        WebElement serchField = waitForContentLoad(By.xpath("//*[@id=\"search_widget\"]//*[@name=\"s\"]"));

        new Actions(driver).moveToElement(serchField).click(serchField).build().perform();

        StringSelection selection = new StringSelection(productData.getName());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        serchField.sendKeys(Keys.BACK_SPACE);
        serchField.sendKeys(Keys.CONTROL + "v");
        serchField.submit();

        waitForContentLoad(By.xpath("//*[@id=\"js-product-list\"]//*[@class=\"h3 product-title\"]/a")).click();

        waitForContentLoad(By.xpath("//*[@class=\"nav nav-tabs\"]//*[@href=\"#product-details\"]")).click();

        WebElement productQuantityAfterSaleElement = waitForContentLoad(By
                .xpath("//*[@id=\"product-details\"]/div[@class=\"product-quantities\"]/span"));


        int productQuantityAfterSaleValue = DataConverter
                .parseStockValue(productQuantityAfterSaleElement.getAttribute("innerHTML"));

        CustomReporter.logAction("Starting check of reduction of quantity of goods per unit");
        Assert.assertEquals(1, (productQuantityBeforeSaleValue - productQuantityAfterSaleValue));
        CustomReporter.logAction("Ending check of reduction of quantity of goods per unit");
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