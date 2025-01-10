package pages;

import dto.UserDtoLombok;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class RegistrationPage extends BasePage {

    public RegistrationPage(WebDriver driver) {
        setDriver(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    @FindBy(id = "name")
    WebElement inputName;
    @FindBy(id = "lastName")
    WebElement inputLastName;
    @FindBy(id = "email")
    WebElement inputEmail;
    @FindBy(id = "password")
    WebElement inputPassword;
    @FindBy(xpath = "//label[@for='terms-of-use']")
    WebElement checkBox;
    @FindBy(xpath = "//button[@type='submit']")
    WebElement btnYalla;
    @FindBy(xpath = "//h2[@class='message']")
    WebElement popUpMessage;

    @FindBy(xpath = "//div[@class='error']")
    WebElement errorMessage;

    public void typeRegistrationForm(UserDtoLombok user) {
        inputName.sendKeys(user.getFirstName());
        inputLastName.sendKeys(user.getLastName());
        inputEmail.sendKeys(user.getUsername());
        inputPassword.sendKeys(user.getPassword());
    }

    public void clickCheckBox() {
        //checkBox.click();
        System.out.println(checkBox.getRect().getWidth()
                + "X" + checkBox.getRect().getHeight());
        int widthCheckBox = checkBox.getRect().getWidth();
        int heightCheckBox = checkBox.getRect().getHeight();
        Actions actions = new Actions(driver);
        actions.moveToElement(checkBox, -widthCheckBox / 3, -heightCheckBox / 4)
                .click().perform();
    }

    public void clickBtnYalla() {
        btnYalla.click();
    }

    public boolean isPopUpMessagePresent() {
        return isTextInElementPresent(popUpMessage, "You are logged in success");
    }
    public boolean isPopUpMessagePresent(String text) {
        return isTextInElementPresent(popUpMessage, text);
    }

    public boolean validateErrorMessage(String text){
        return isTextInElementPresent(errorMessage, text);
    }

    public boolean btnYallaIsDisabled(){
        return !btnYalla.isEnabled();
    }
}
