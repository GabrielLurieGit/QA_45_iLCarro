package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    static WebDriver driver;

    public static void setDriver(WebDriver wd) {
        driver = wd;
    }

    @FindBy(xpath = "//h2[@class='message']")
    WebElement popUpMessage;

    public void pause(int time) {
        try {
            Thread.sleep(time * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTextInElementPresent(WebElement element, String text) {
        return element.getText().contains(text);
    }

    public void clickWait(WebElement element, int time) {
        try {
            new WebDriverWait(driver, time)
                    .until(ExpectedConditions.elementToBeClickable(element)).click();
        }catch (Exception e){
            System.out.println("Click wait created exception");
        }
    }

    public boolean validateUrl(String url, int time) {
        return new WebDriverWait(driver, time).until(ExpectedConditions.urlContains(url));
    }

    public boolean isElementPresentDOM(String locator, int time){
        try {
            new WebDriverWait(driver, time)
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
            return true;
        }catch (Exception e){
            System.out.println("isElementPresentDOM created exception");
            return false;
        }

    }


}
