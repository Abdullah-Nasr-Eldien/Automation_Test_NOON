package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    // wait element for present

    public static WebElement waitForElement(WebDriver driver, By locator){
        return  new WebDriverWait(driver, Duration.ofSeconds(10)).
            until(driver1 -> driver1.findElement(locator));
    }

    // wait for element to be visible

    public static WebElement waitForElementVisible(WebDriver driver, By locator){
        return  new WebDriverWait(driver, Duration.ofSeconds(20)).
                until(driver1 -> {
                    WebElement element = waitForElement(driver, locator);
                    return element.isDisplayed() ? element : null;
                });
    }
    // to be invivsible
    public static boolean waitForElementInvisible(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // wait for element clickable

    public static WebElement waitForElementClickable(WebDriver driver, By locator){
        return  new WebDriverWait(driver, Duration.ofSeconds(10)).
                until(driver1 -> {
                    WebElement element = waitForElement(driver, locator);
                    return element.isEnabled() ? element : null;
                });
    }}