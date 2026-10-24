package tests;

import org.example.drivers.DriverManager;
import org.example.pages.LoginPage;
import org.example.pages.ProceedPayment;
import org.example.pages.WishListPage;
import org.example.utils.GmailOtpReader;
import org.example.utils.TestData;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Instant;

public class E2ETest {
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
        wishListPage.searchForProduct(TestData.get("wishlist.productName"))
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
    @Test(priority = 4, dependsOnMethods = "successfulLoginTest")
    public void proceedPayment() {
        ProceedPayment proceedPayment = new ProceedPayment(driver);
        proceedPayment.searchProduct(TestData.get("proceedPayment.productName"))
                .addToCart()
                .addAddress(TestData.get("proceedPayment.addressSearch"),
                        TestData.get("proceedPayment.roomNumber"),
                        TestData.get("proceedPayment.streetName"),
                        TestData.get("proceedPayment.nickname"),
                        TestData.get("proceedPayment.directions"),
                        TestData.get("proceedPayment.receiverFirstName"),
                        TestData.get("proceedPayment.receiverLastName"),
                        TestData.get("proceedPayment.receiverPhone"));

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
