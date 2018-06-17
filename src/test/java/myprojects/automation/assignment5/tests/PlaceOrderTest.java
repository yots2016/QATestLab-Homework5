package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaceOrderTest extends BaseTest {

    private WebDriverWait wait = new WebDriverWait(driver, 30);

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version

        CustomReporter.logAction("get main page");
        driver.get(Properties.getBaseUrl());

        WebElement webElement = driver.findElement(By.xpath("//*[@class=\"hidden-md-up text-xs-center mobile\"]"));

        CustomReporter.logAction("Check site version");
        if (!isMobileTesting("chrome")) {
            Assert.assertTrue(webElement.isDisplayed());

            CustomReporter.logAction("A mobile version of the site was downloaded");
        }
        else {
            Assert.assertFalse(webElement.isDisplayed());

            CustomReporter.logAction("A desktop version of the site was loaded");
        }


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

        waitForContentLoad(By.xpath("//*[@class=\"all-product-link pull-xs-left pull-md-right h4\"]"));



    }

    private WebElement waitForContentLoad(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

}