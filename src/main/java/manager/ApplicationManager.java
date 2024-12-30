package manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.WDListener;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
    //private WebDriver driver;
    private EventFiringWebDriver driver;
    public Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setup(Method method) {
        //logger.info("Start testing --> " + method.getName());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en");
        //driver = new ChromeDriver(options);
        driver = new EventFiringWebDriver(new ChromeDriver());
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.register(new WDListener());

    }

    @AfterMethod
    public void tearDown(Method method) {
        //logger.info("Stop testing --> " + method.getName());
//        if (driver != null)
//            driver.quit();
    }
}
