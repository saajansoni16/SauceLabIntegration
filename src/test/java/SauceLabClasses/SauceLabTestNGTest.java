package SauceLabClasses;

import Library.setUpOptions;
import com.saucelabs.saucebindings.SauceSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SauceLabTestNGTest {

    public RemoteWebDriver driver;
    public SauceSession sauceSession;
    public static JavascriptExecutor jse;

    @BeforeMethod(alwaysRun = true)
    @org.testng.annotations.Parameters(value = { "config", "browserOptions" })
    @SuppressWarnings("unchecked")
    public void setUp(String config_file, String browserOptions, Method m) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/" + config_file));
        JSONObject envs = (JSONObject) config.get("browserOptions");

        Map<String, String> sauceLabOptions = setUpOptions.setSauceLabOption(config, m);
        setUpOptions.setBrowserCapabilities(envs, browserOptions);
        driver = setUpOptions.driver;
        driver.manage().window().maximize();
        driver.get("https://www.google.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {

        //Gets browser logs if available.
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.quit();
    }



}
