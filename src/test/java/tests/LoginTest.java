package tests;

import org.example.drivers.DriverManager;
import org.example.pages.LoginPage;
import org.example.pages.WishListPage;
import org.example.utils.GmailOtpReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Instant;

public class LoginTest {
    private WebDriver driver;

    @Test (priority = 1)
    public void successfulLoginTest() throws Exception {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLogin().enterEmail(requiredEnvironmentVariable("GMAIL_EMAIL"));
        Instant otpRequestedAt = Instant.now();
        loginPage.clickContinue();
        loginPage.enterOtp(GmailOtpReader.waitForNoonOtp(otpRequestedAt)).clickLoginOtp()
                .assertSuccessLogin();
    }
    @Test(priority = 2, dependsOnMethods = "successfulLoginTest")
    public void addproducttowishlist() {
        WishListPage wishListPage = new WishListPage(driver);
        wishListPage.searchForProduct("R50i NC by Anker Noise Cancelling Earbuds, Strong and Smart Noise Cancelling, Powerful Bass, 45H Playtime, 2-in-1 Case and Phone Stand, IP54, Wireless Earbuds, Bluetooth 5.4, App Control Black")
                .clickwishlist()
                .clickplusicon();
    }
    @Test(priority = 3, dependsOnMethods = "successfulLoginTest")
    public void removeproductfromwishlist() {
        WishListPage wishListPage = new WishListPage(driver);
                wishListPage.clickheaderwlbtn()
                .clickProductOptions()
                .clickRemoveFromWishlist();
    }

    @BeforeClass
    public void setUp() {
        driver = DriverManager.createInstance("chrome");   // browser wa7ed lel class kolo
        new LoginPage(driver).navigateToNoon();
    }

    @AfterClass
    public void tearDown() {

    }

    private String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set the " + name + " environment variable before running this test.");
        }
        return value;
    }
}
