package pages;

import dto.UserDtoLombok;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) {
        setDriver(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    @FindBy(id = "email")
    WebElement inputEmail;
    @FindBy(id = "password")
    WebElement inputPassword;
    @FindBy(xpath = "//button[@type='submit']")
    WebElement btnYalla;
//    @FindBy(xpath = "//h2[@class='message']")
//    WebElement popUpMessage;
    @FindBy(xpath = "//div[@class='error']")
    WebElement errorMessage;

    @FindBy(id = "1")
    WebElement btnLetCarWork;
    @FindBy(xpath = "//button[@type='button']")
    WebElement btnOk;

    public void clickBtnOK() {
        clickWait(btnOk, 5);
    }


    public void clickBtnLetCarWork() {
//        pause(3);
//        btnLetCarWork.click();
        clickWait(btnLetCarWork, 5);
    }


    public void typeLoginForm(UserDtoLombok user) {
        inputEmail.sendKeys(user.getUsername());
        inputPassword.sendKeys(user.getPassword());
    }

    public void clickBtnYalla() {
        btnYalla.click();
    }

    public boolean isPopUpLoginMessagePresent(String text) {
        return isTextInElementPresent(popUpMessage, text);
    }

    public boolean validateErrorMessage(String text) {
        pause(2);
        return isTextInElementPresent(errorMessage, text);
    }
}
