package SauceLabClasses;

import com.saucelabs.saucebindings.SauceSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        ChromeOptions chromeOptions = new ChromeOptions();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        EdgeOptions edgeOptions = new EdgeOptions();
        Map<String, Object> sauceOptions = new HashMap<>();

        Map<String, String> options = (Map<String, String>) envs.get(browserOptions);
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (browserOptions.equalsIgnoreCase("Chrome")){
                chromeOptions.setCapability(pair.getKey().toString(), pair.getValue().toString());
            } else if (browserOptions.equalsIgnoreCase("Firefox")) {
                firefoxOptions.setCapability(pair.getKey().toString(), pair.getValue().toString());
            } else if (browserOptions.equalsIgnoreCase("Edge")) {
                edgeOptions.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
        }

        Map<String, String> SauceOptions = (Map<String, String>) config.get("sauceOptions");
        Iterator itt = SauceOptions.entrySet().iterator();
        while (itt.hasNext()) {
            Map.Entry pair = (Map.Entry) itt.next();
            SauceOptions.put(pair.getKey().toString(), pair.getValue().toString());
        }
        SauceOptions.put("Name", m.getName());
        if (browserOptions.equalsIgnoreCase("Chrome")){
            chromeOptions.setCapability("sauce:options", SauceOptions);
        } else if (browserOptions.equalsIgnoreCase("Firefox")) {
            firefoxOptions.setCapability("sauce:options", SauceOptions);
        } else if (browserOptions.equalsIgnoreCase("Edge")) {
            edgeOptions.setCapability("sauce:options", SauceOptions);
        }

        URL url = new URL("https://saajansoni:433e1f60-4cd1-4e90-8279-8460c7141637@ondemand.us-west-1.saucelabs.com:443/wd/hub");
        if (browserOptions.equalsIgnoreCase("Chrome")){
            driver = new RemoteWebDriver(url, chromeOptions);
        } else if (browserOptions.equalsIgnoreCase("Firefox")) {
            driver = new RemoteWebDriver(url, firefoxOptions);
        } else if (browserOptions.equalsIgnoreCase("Edge")) {
            driver = new RemoteWebDriver(url, edgeOptions);
        }



        driver.manage().window().maximize();
        driver.get("https://www.google.com/");
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {

        //Gets browser logs if available.
        ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.quit();
    }



}
