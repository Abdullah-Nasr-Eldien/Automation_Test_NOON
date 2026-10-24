package org.example.pages;

import org.example.utils.ElementAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProceedPayment {
    private final WebDriver driver;
    public ProceedPayment(WebDriver driver) {
        this.driver = driver;
    }
    // locators
    private final By searchInput = By.id("search-input");
    private final By addtoCartBtn = By.xpath("//a[contains(@href,'/N70147950V/p/')]//button[.//img[@alt='add-to-cart']]");
    private final By cartLink = By.cssSelector("a[data-qa='btn_cartLink-Header-Desktop']");
    private final By checkoutButton = By.xpath("//button[.//span[normalize-space()='Checkout']]");
    private final By editAddressButton = By.xpath("//span[normalize-space()='Edit Address']");
    private final By addNewAddressButton = By.cssSelector("button[data-qa='address-add-new']");
    private final By clickSearchInput = By.cssSelector("input[placeholder='Search for your national address, building...']");
    private final By sendsearchtext = By.cssSelector("input[data-qa='address-search-input']");
    private final By addressSearchResult = By.xpath("//button[contains(@class,'_searchItem')]//span[contains(@class,'_highlightedText') and normalize-space()='New Cairo']/ancestor::button");
    private final By confirmLocationButton = By.cssSelector("button[data-qa='address-confirm-location']");
    private final By roomNumberInput = By.id("room-no");
    private final By streetNameInput = By.id("street-name");
    private final By nicknameInput = By.id("nickname");
    private final By directionsInput = By.id("directions");
    private final By receiverFirstNameInput = By.id("receiver-first-name");
    private final By receiverLastNameInput = By.id("receiver-last-name");
    private final By receiverPhoneInput = By.id("receiverPhoneInput");

    // actions
    public ProceedPayment searchProduct(String producttestName) {
        ElementAction.sendData(driver, searchInput, producttestName);
        ElementAction.pressEnter(driver);
        return this;
    }
    public ProceedPayment addToCart() {
        ElementAction.clickFunction(driver, addtoCartBtn);
        ElementAction.clickFunction(driver, cartLink);
        ElementAction.clickFunction(driver, checkoutButton);

        return this;
    }
    public ProceedPayment addAddress(String addsearch, String roomnumber, String streetname,
                                     String nickname, String directions,
                                     String receiverFirstName, String receiverLastName,
                                     String receiverPhone) {

        ElementAction.clickFunction(driver, editAddressButton);
        ElementAction.clickFunction(driver, addNewAddressButton);
        ElementAction.clickFunction(driver, clickSearchInput);
        ElementAction.sendData(driver, sendsearchtext, addsearch);
        ElementAction.clickFunction(driver, addressSearchResult);
        ElementAction.clickFunction(driver, confirmLocationButton);
        ElementAction.sendData(driver, roomNumberInput, roomnumber);
        ElementAction.sendData(driver, streetNameInput, streetname);
        ElementAction.sendData(driver, nicknameInput, nickname);
        ElementAction.sendData(driver, directionsInput, directions);
        ElementAction.sendData(driver, receiverFirstNameInput, receiverFirstName);
        ElementAction.sendData(driver, receiverLastNameInput, receiverLastName);
        ElementAction.sendData(driver, receiverPhoneInput, receiverPhone);
        return this;
    }
    // validations
}
