# Noon Egypt UI Automation

Java UI automation for selected Noon Egypt shopping flows. The project uses Selenium WebDriver to control the browser, TestNG to organize and sequence tests, Maven to manage dependencies and builds, and page-object classes to keep page interactions separate from test scenarios.

## Project goals and current coverage

The current end-to-end test is `tests.E2ETest`. It is designed to:

1. Sign in to Noon Egypt using an email address and a one-time password (OTP).
2. Add a product to the wishlist.
3. Remove a product from the wishlist.
4. Search for a product, add it to the cart, and enter checkout address details.

This is browser automation against a live website. Website content, selectors, account requirements, OTP delivery, and checkout behavior can change. The checkout flow currently fills address details; it should not be treated as a completed purchase test.

## Technology and architecture

```text
TestNG test (src/test/java/tests/E2ETest.java)
    ├── DriverManager / BrowserFactory create and configure WebDriver
    ├── Page objects model Noon pages and user actions
    │     ├── LoginPage
    │     ├── WishListPage
    │     └── ProceedPayment
    └── Utilities provide shared browser actions, waits, data, and OTP retrieval
          ├── ElementAction / WaitUtils
          ├── BrowserActions / ScrollActions
          ├── TestData (loads JSON from the classpath)
          └── GmailOtpReader (reads the recent OTP through Gmail IMAP)
```

### Maven layout

```text
pom.xml
src/
├── main/
│   ├── java/org/example/
│   │   ├── drivers/
│   │   │   ├── BrowserFactory.java
│   │   │   └── DriverManager.java
│   │   ├── pages/
│   │   │   ├── LoginPage.java
│   │   │   ├── ProceedPayment.java
│   │   │   └── WishListPage.java
│   │   └── utils/
│   │       ├── BrowserActions.java
│   │       ├── ElementAction.java
│   │       ├── GmailOtpReader.java
│   │       ├── ScrollActions.java
│   │       ├── TestData.java
│   │       ├── Validation.java
│   │       └── WaitUtils.java
│   └── resources/
│       └── testData.json
└── test/
    └── java/tests/
        └── E2ETest.java
```

`src/main/java` contains reusable automation code, `src/main/resources` contains runtime data, and `src/test/java` contains TestNG test classes. Maven copies resources to the test/runtime classpath during the build. Do not put test resources under `src/main/java`.

## How a test run works

1. TestNG calls `E2ETest.setUp()` once for the class. It creates a Chrome driver through `DriverManager` and navigates to the Noon Egypt homepage.
2. TestNG runs methods according to their `priority`. Login has priority 1; wishlist and checkout methods depend on `successfulLoginTest`, so they should not run if login fails.
3. `LoginPage` enters the configured Gmail address and requests an OTP. The test captures the request time before clicking Continue, then asks `GmailOtpReader` to wait for a new Noon OTP email.
4. `GmailOtpReader` connects to Gmail IMAP, searches recent inbox messages, extracts an OTP, and returns it to the test. `LoginPage` enters the OTP and submits it.
5. The remaining page objects perform the wishlist and checkout interactions, using data loaded from `testData.json`.
6. TestNG calls `E2ETest.tearDown()` after the class. **Ensure this method calls `driver.quit()`** so Chrome closes even when tests finish or fail. Do not leave browser cleanup commented out.

The test methods share the class's browser session. Run the whole `E2ETest` class when testing the dependency chain; running a dependent method by itself may not perform the login first.

## Requirements

- JDK 21
- Maven 3.x
- Google Chrome
- Network access to Noon Egypt and Gmail IMAP (`imap.gmail.com:993`)
- A Gmail account that can use IMAP and an App Password (if permitted by the account or Workspace administrator)

The Maven project declares Selenium Java 4.27.0, TestNG 7.12.0, Jakarta Mail 2.0.1, Jackson Databind 2.20.1, and Monte Media screen recorder 17.2.1.

## Credentials and local configuration

The login flow reads these environment variables:

| Variable | Purpose |
|---|---|
| `GMAIL_EMAIL` | Gmail address that receives Noon OTP messages; also used as the login email in the current test. |
| `GMAIL_APP_PASSWORD` | Gmail App Password used only for the IMAP connection. |

Never put real credentials, OTP values, or tokens in Java code, JSON test data, README files, or committed IDE configuration. Configure variables locally in IntelliJ using **Run → Edit Configurations… → Environment variables** for the configuration used to run `E2ETest`. If a credential has been shared or committed, revoke it and create a replacement.

Before using the IMAP OTP flow, enable IMAP in Gmail and ensure the account allows App Passwords. Use an App Password, not the normal Google account password. If an organization blocks IMAP/App Passwords, the Gmail API with OAuth 2.0 is the appropriate alternative.

## Test data

Edit `src/main/resources/testData.json` to change non-secret inputs such as product names and address form values. The `TestData` utility loads `testData.json` as a classpath resource and supports dot-separated paths:

```java
TestData.get("wishlist.productName");
TestData.get("proceedPayment.addressSearch");
TestData.get("proceedPayment.receiverPhone");
```

Keep the JSON keys aligned with the paths requested by the tests. Missing keys or non-scalar values raise an error rather than silently returning a default. Do not place credentials or OTPs in this file.

## Build and run

From the project root:

```bash
# Compile and package without launching the browser tests
mvn -DskipTests package

# Run the end-to-end TestNG class
mvn -Dtest=E2ETest test
```

The same class can be run from IntelliJ by selecting `tests.E2ETest` as the TestNG class. Configure the environment variables on that exact run configuration. Do not run the live checkout flow against an account or address without authorization.

## Adding or updating automation

### Add a test scenario

1. Add or update a TestNG method in `E2ETest` (or create a separate class under `src/test/java/tests`).
2. Express ordering with TestNG `priority` and `dependsOnMethods` only where a scenario genuinely needs prior state, such as a signed-in session.
3. Keep the test method focused on scenario steps. Put page-specific locators and interactions in the appropriate page object.
4. Reuse `TestData.get("section.key")` for changeable, non-secret inputs.
5. Run the targeted TestNG class and check the result before running the full suite.

### Change a page interaction

Update the locator and action in its page object (`LoginPage`, `WishListPage`, or `ProceedPayment`). Use `ElementAction` and `WaitUtils` for common interaction and waiting behavior rather than adding arbitrary sleeps. When a website change breaks a selector, inspect the live page and update the selector based on the actual element; do not assume a failure is caused by the test data without checking the exception and failing step.

### Add a utility

Put shared, domain-independent browser or validation helpers in `org.example.utils`. Keep driver creation/configuration in `org.example.drivers`, and keep website-specific selectors/actions in `org.example.pages`.

## Current implementation notes

- `DriverManager` stores the driver in a `ThreadLocal`, while `E2ETest` also keeps a direct driver field. If parallel execution is introduced, review driver lifecycle and test isolation first.
- The current `E2ETest.tearDown()` should quit the browser and clear the `DriverManager` thread-local value. Confirm cleanup is active before relying on repeated local or CI runs.
- `WaitUtils.waitForElementClickable` currently waits for the element to be enabled; it does not use Selenium's full clickable condition (visible and unobstructed). Click interception may therefore still occur.
- Gmail OTP matching depends on the message arriving after the request time and on the email content matching the extraction pattern. A login failure, Gmail authentication error, IMAP timeout, and OTP extraction timeout have different causes; use the first exception line and failing action to diagnose them.
- Selectors on Noon can change without notice. Keep selectors centralized in page objects and verify them against the current site when an element is not found or cannot be interacted with.
- `Validation` and the screen-recording dependency exist in the project, but the current end-to-end class does not use them.

## Team workflow recommendations

- Keep changes small and reviewable; avoid mixing selector changes, test-data edits, and unrelated refactoring in one change.
- Never commit credentials, local environment files, browser profiles, or generated `target/` output.
- For pull requests, describe the scenario, list files changed, note whether it was run locally, and include failures or known live-site limitations.
- Use a dedicated authorized test account and non-production checkout data. Stop before final payment unless the test has explicit approval to place an order.
