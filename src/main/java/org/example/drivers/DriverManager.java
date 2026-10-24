package org.example.drivers;

import org.openqa.selenium.WebDriver;

import static org.testng.Assert.fail;

public class DriverManager {
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<WebDriver>();
    private DriverManager() {}
    public static WebDriver getDriver() {
        if  (driverThreadLocal.get() == null) {
            fail("driver is null");
        }
        return driverThreadLocal.get();
    }
    public static WebDriver createInstance(String browserName) {
        WebDriver driver = BrowserFactory.getBrowser(browserName);
        setDriver(driver);
        return getDriver();
    }
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }
}

