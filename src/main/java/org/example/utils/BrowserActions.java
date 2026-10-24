package org.example.utils;

import org.openqa.selenium.WebDriver;

public class BrowserActions {

    private BrowserActions(){}

    public static void navigateURL(WebDriver driver, String url){
        driver.get(url);
    }

    //get url
    public static String getCurrentURL(WebDriver driver){
        return driver.getCurrentUrl();
    }
}
