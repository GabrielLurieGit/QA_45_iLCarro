package tests;

import dto.UserDtoLombok;
import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.RegistrationPage;
import pages.SearchPage;
import utils.TestNGListener;

import java.util.Random;

@Listeners(TestNGListener.class)

public class RegistrationTests extends ApplicationManager {

    RegistrationPage registrationPage;

    @Test
    public void registrationPositiveTest(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .name("Bob")
                .lastName("Doe")
                .email(i+"bob_mail@mail.com")
                .password("Pass123!")
                .build();
        new SearchPage(getDriver()).clickBtnSignUp();
        registrationPage = new RegistrationPage(getDriver());
        registrationPage.typeRegistrationForm(user);
        //new RegistrationPage(getDriver()).typeRegistrationForm(user);
        registrationPage.clickCheckBox();
        registrationPage.clickBtnYalla();
        Assert.assertTrue(registrationPage.isPopUpMessagePresent());
    }

    @Test
    public void registrationNegativeTest_wrongEmail(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .name("Bob")
                .lastName("Doe")
                .email(i+"bob_mail_mail.com")
                .password("Pass123!")
                .build();
        new SearchPage(getDriver()).clickBtnSignUp();
        registrationPage = new RegistrationPage(getDriver());
        registrationPage.typeRegistrationForm(user);
        registrationPage.clickCheckBox();
        registrationPage.clickBtnYalla();
        Assert.assertTrue(registrationPage.validateErrorMessage("Wrong email format"));
    }

    @Test
    public void registrationNegativeTest_EmptyLastName(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .name("Bob")
                .lastName("")
                .email(i+"bob_mail@mail.com")
                .password("Pass123!")
                .build();
        new SearchPage(getDriver()).clickBtnSignUp();
        registrationPage = new RegistrationPage(getDriver());
        registrationPage.typeRegistrationForm(user);
        registrationPage.clickCheckBox();
        registrationPage.clickBtnYalla();
        Assert.assertTrue(registrationPage.validateErrorMessage("Last name is required"));
    }

    @Test
    public void registrationNegativeTest_LastNameSpace(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .name("Bob")
                .lastName(" ")
                .email(i+"bob_mail@mail.com")
                .password("Pass123!")
                .build();
        new SearchPage(getDriver()).clickBtnSignUp();
        registrationPage = new RegistrationPage(getDriver());
        registrationPage.typeRegistrationForm(user);
        registrationPage.clickCheckBox();
        registrationPage.clickBtnYalla();
        Assert.assertTrue(registrationPage.isPopUpMessagePresent("must not be blank"));
    }
    @Test
    public void registrationNegativeTest_WOcheckBox(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .name("Bob")
                .lastName("Marly")
                .email(i+"bob_mail@mail.com")
                .password("Pass123!")
                .build();
        new SearchPage(getDriver()).clickBtnSignUp();
        registrationPage = new RegistrationPage(getDriver());
        registrationPage.typeRegistrationForm(user);
        registrationPage.clickBtnYalla();
        Assert.assertTrue(registrationPage.btnYallaIsDisabled());
    }
}
