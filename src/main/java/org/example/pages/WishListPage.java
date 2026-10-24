package org.example.pages;

import org.example.utils.ElementAction;
import org.example.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WishListPage {
    private final WebDriver driver;
    public WishListPage(WebDriver driver) {
        this.driver = driver;
    }
    // locators
    private final By search_bar = By.id("search-input");
    private final By wishlistButton = By.xpath(
            "//div[@data-qa='plp-product-box'][.//a[contains(@href,'/N70147950V/p/')]]//button[.//img[@alt='Wishlist']]"
    );
    private final By plusIcon = By.cssSelector("span._iconButton_e1p6v_224 img[src*='plus-blue.svg']");
    private final By headerwlBtn = By.cssSelector("a[data-qa='btn_wishlistLink-Header-Desktop']");
    private final By productOptionsButton = By.cssSelector("button[aria-label='Product options']");
    private final By removeFromWishlistButton = By.xpath(
            "//button[@role='menuitem'][.//span[normalize-space()='Remove from wishlist']]"
    );


    // actions
    public WishListPage searchForProduct(String productName) {
        ElementAction.sendData(driver, search_bar, productName);
        ElementAction.pressEnter(driver);
        return this;
    }
    public WishListPage clickwishlist(){
        ElementAction.clickFunction(driver,wishlistButton);
        return this;
    }
    public WishListPage clickplusicon(){
        ElementAction.clickFunction(driver,plusIcon);
        return this;
    }
    public WishListPage clickheaderwlbtn(){
        ElementAction.clickFunction(driver,headerwlBtn);
        return this;
    }
    public WishListPage clickProductOptions(){
        ElementAction.clickFunction(driver, productOptionsButton);
        return this;
    }

    public WishListPage clickRemoveFromWishlist(){
        ElementAction.clickFunction(driver, removeFromWishlistButton);
        return this;
    }

    // validations
}
