package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ElementAction {
    private ElementAction(){}

    // actions fin keys

    public static void sendData(WebDriver driver, By locator, String data){
        WaitUtils.waitForElementVisible(driver, locator);
        driver.findElement(locator).sendKeys(data);

    }

    public static void clickFunction(WebDriver driver, By locator){
        WaitUtils.waitForElementClickable(driver, locator);
        findElement(driver, locator).click();
    }

    public static String getText(WebDriver driver, By locator){
        WaitUtils.waitForElementVisible(driver, locator);
        return findElement(driver, locator).getText();
    }
    // find element
    public static WebElement findElement(WebDriver driver, By locator){
        return driver.findElement(locator);
    }
    public static void pressEnter(WebDriver driver) {
        new Actions(driver)
                .sendKeys(Keys.ENTER)
                .perform();
    }
}
