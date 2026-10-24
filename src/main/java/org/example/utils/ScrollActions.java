package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScrollActions {
    private ScrollActions() {
    }
    public static void scrollToElement(WebDriver driver, By locator) {
        ((JavascriptExecutor) driver).
                executeScript("arguments[0].scrollIntoView(true);", ElementAction.findElement(driver, locator));
    }
}
