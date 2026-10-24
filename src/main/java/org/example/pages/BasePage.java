//package org.example.pages;
//
//import org.example.utils.WaitUtils;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//
//public class BasePage {
//
//    protected WebDriver driver;
//    protected WaitUtils waitUtils;
//
//    public BasePage(WebDriver driver) {
//        this.driver = driver;
//        this.waitUtils = new WaitUtils(driver);
//    }
//
//    protected void click(By locator) {
//        waitUtils.waitForClickable(locator).click();
//    }
//
//    protected void type(By locator, String text) {
//        waitUtils.waitForVisible(locator).sendKeys(text);
//    }
//
//    protected String getText(By locator) {
//        return waitUtils.waitForVisible(locator).getText();
//    }
//}