package org.example.pages;

import org.example.utils.BrowserActions;
import org.example.utils.ElementAction;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {
    private final WebDriver driver;

    private final By loginButton = By.cssSelector("[data-qa='btn_header_signInOrUp-header-desktop']");
    private final By emailInput = By.id("emailInput");
    private final By continueButton = By.id("login-submit");
    private final By otpInputs = By.cssSelector(
            "input[autocomplete='one-time-code'], input[name*='otp' i], input[id*='otp' i], " +
                    "input[placeholder*='code' i], input[inputmode='numeric'], " +
                    "input[maxlength='1'], input[type='tel']");
    private final By LoginOTP = By.cssSelector("button._loginBtn_1dxqd_165");
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToNoon() {
        BrowserActions.navigateURL(driver, "https://www.noon.com/egypt-en/");
    }

    public LoginPage clickLogin() {
        ElementAction.clickFunction(driver, loginButton);
        return this;
    }

    public LoginPage enterEmail(String email) {
        ElementAction.sendData(driver, emailInput, email);
        return this;
    }

    public LoginPage clickContinue() {
        ElementAction.clickFunction(driver, continueButton);
        return this;
    }

    public LoginPage enterOtp(String otp) {
        List<WebElement> inputs = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(webDriver -> {
                    List<WebElement> visibleInputs = webDriver.findElements(otpInputs).stream()
                            .filter(WebElement::isDisplayed)
                            .filter(WebElement::isEnabled)
                            .toList();
                    return visibleInputs.isEmpty() ? null : visibleInputs;
                });

        if (inputs.size() == 1) {
            inputs.getFirst().sendKeys(otp);
        } else {
            if (inputs.size() < otp.length()) {
                throw new IllegalStateException("The OTP input fields do not match the OTP length.");
            }
            for (int i = 0; i < otp.length(); i++) {
                inputs.get(i).sendKeys(String.valueOf(otp.charAt(i)));
            }
        }
        return this;
    }

    public LoginPage clickLoginOtp(){
        ElementAction.clickFunction(driver, LoginOTP);
        return this;
    }
    public LoginPage assertSuccessLogin() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(webDriver -> webDriver.findElements(otpInputs).stream()
                        .noneMatch(WebElement::isDisplayed));
        return this;
    }
}
