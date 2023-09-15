package Library;

import org.json.simple.JSONObject;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class setUpOptions {

    public static Map<String, String> SauceOptions;
    public static RemoteWebDriver driver;

    public static void setBrowserCapabilities(JSONObject envs, String browserOptions) throws MalformedURLException {

        Map<String, String> options = (Map<String, String>) envs.get(browserOptions);
        Iterator it = options.entrySet().iterator();
        URL url = new URL("https://oauth-saajansoni-76084:5962e977-20c1-4991-8d0c-90516cbf84ac@ondemand.eu-central-1.saucelabs.com:443/wd/hub");

        if (browserOptions.equalsIgnoreCase("Chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability("sauce:options", SauceOptions);
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                chromeOptions.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
            driver = new RemoteWebDriver(url, chromeOptions);
        }else if (browserOptions.equalsIgnoreCase("Firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setCapability("sauce:options", SauceOptions);
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                firefoxOptions.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
            driver = new RemoteWebDriver(url, firefoxOptions);
        }else if (browserOptions.equalsIgnoreCase("Firefox")) {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.setCapability("sauce:options", SauceOptions);
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                edgeOptions.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
            driver = new RemoteWebDriver(url, edgeOptions);
        }
    }

    public static Map<String, String> setSauceLabOption(JSONObject config, Method m){
        SauceOptions = (Map<String, String>) config.get("sauceOptions");
        Iterator itt = SauceOptions.entrySet().iterator();
        while (itt.hasNext()) {
            Map.Entry pair = (Map.Entry) itt.next();
            SauceOptions.put(pair.getKey().toString(), pair.getValue().toString());
        }
        SauceOptions = setMethodName(m, SauceOptions);
        return SauceOptions;
    }

    public static Map<String, String> setMethodName(Method m, Map<String, String> SauceOptions){
        SauceOptions.put("Name", m.getName());
        return SauceOptions;
    }
}
