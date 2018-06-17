package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void openRandomProduct() {
        CustomReporter.logAction("Opened random product");
        waitForContentLoad(By.xpath("//*[@class=\"all-product-link pull-xs-left pull-md-right h4\"]")).click();
        waitForContentLoad(By.xpath("//*[@class=\"h3 product-title\"]")).click();
    }

    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");

        WebElement productNameElementAfterOpen = driver.findElement(By
                .xpath("//*[@id=\"main\"]//*[@class=\"col-md-6\"]/h1[@itemprop=\"name\"]"));
        String productNameAfterOpen = productNameElementAfterOpen.getText();

        WebElement productQuantityElementAfterOpen = driver.findElement(By
                .id("quantity_wanted"));
        String productQuantityAfterOpen = productQuantityElementAfterOpen.getAttribute("value");

        WebElement productPriceElementAfterOpen = driver.findElement(By
                .xpath("//*[@id=\"main\"]//*[@class=\"current-price\"]/span[@itemprop=\"price\"]"));
        String productPriceAfterOpen = productPriceElementAfterOpen.getText();

        return new ProductData(productNameAfterOpen
                , DataConverter.parseStockValue(productQuantityAfterOpen)
                , DataConverter.parsePriceValue(productPriceAfterOpen));
    }

    private WebElement waitForContentLoad(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
