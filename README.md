<<<<<<< HEAD
# Noun Automation Script

Selenium and TestNG automation for Noon Egypt, using Maven and Java 21.

## Requirements

- Java 21
- Maven
- Google Chrome
- A Gmail account with IMAP enabled and a Gmail App Password

## Configuration

The login test reads these environment variables:

- `GMAIL_EMAIL`: Gmail address used to receive Noon login OTPs.
- `GMAIL_APP_PASSWORD`: Gmail App Password used by the IMAP OTP reader.

Do not commit these credentials to the repository or share them in chat. Configure them in IntelliJ under **Run → Edit Configurations… → Environment variables**.

## Run tests

Run the TestNG test class from IntelliJ, or run it with Maven:

```bash
mvn -Dtest=LoginTest test
```

`LoginTest` logs in with the email OTP, then runs the wishlist and checkout test methods that depend on successful login.

## Test data

Test data is stored in `src/main/resources/testData.json`. Maven includes it on the classpath, and `org.example.utils.TestData` reads nested keys using dot notation, for example:

```java
TestData.get("wishlist.productName");
TestData.get("proceedPayment.addressSearch");
```

## Project layout

```text
src/
├── main/
│   ├── java/org/example/
│   │   ├── drivers/       # WebDriver setup
│   │   ├── pages/         # Page objects
│   │   └── utils/         # Browser, wait, test-data, and Gmail OTP helpers
│   └── resources/
│       └── testData.json
└── test/
    └── java/tests/        # TestNG test classes
```
=======
# Automation_Test_NOON
>>>>>>> 1bf2b2bfd22bfcd9f0fd90f4457a62d294512ded
