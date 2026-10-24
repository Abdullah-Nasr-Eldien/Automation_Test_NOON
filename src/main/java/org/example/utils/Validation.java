package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Validation {
    private Validation() {}
    public static void validateTrue(boolean condition, String message) {
       Assert.assertTrue(condition, message);
    }
    public static void validateFalse(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }
    public static void validateEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }
    public static void validateNotequals(String actual, String expected, String message) {
    Assert.assertNotEquals(actual, expected, message);
    }
    public static void validatePageurl(WebDriver driver, String message) {
        Assert.assertEquals(BrowserActions.getCurrentURL(driver), message);
    }
}
