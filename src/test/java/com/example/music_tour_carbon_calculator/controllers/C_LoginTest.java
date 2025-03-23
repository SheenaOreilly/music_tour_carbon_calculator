package com.example.music_tour_carbon_calculator.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class C_LoginTest {

    @LocalServerPort
    private int port = 8080;

    private static WebDriver driver;
    private String baseUrl;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1280, 800));
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
        driver.get(baseUrl);
        WebElement loginMainButton = driver.findElement(By.className("login-btn"));
        loginMainButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));
        Assertions.assertTrue(loginForm.isDisplayed(), "Login form should be visible");
    }
    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Order(1)
    @Test
    public void testLoginPageElements() {
        WebElement emailInput = driver.findElement(By.id("email"));
        Assertions.assertTrue(emailInput.isDisplayed(), "Email input should be visible");
        WebElement passwordInput = driver.findElement(By.id("password"));
        Assertions.assertTrue(passwordInput.isDisplayed(), "Password input should be visible");
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
        Assertions.assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        WebElement forgotPasswordLink = driver.findElement(By.xpath("//a[text()='Forgot Password?']"));
        Assertions.assertTrue(forgotPasswordLink.isDisplayed(), "Forgot Password link should be visible");
        WebElement createAccountLink = driver.findElement(By.xpath("//a[text()='Create Account']"));
        Assertions.assertTrue(createAccountLink.isDisplayed(), "Create Account link should be visible");
    }

    @Order(2)
    @Test
    public void testCreateAccountToggle() {
        WebElement createAccountLink = driver.findElement(By.xpath("//a[text()='Create Account']"));
        createAccountLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement signupForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupForm")));
        Assertions.assertTrue(signupForm.isDisplayed(), "Signup form should be visible");
        WebElement backToLoginLink = driver.findElement(By.xpath("//a[text()='Back to Login']"));
        Assertions.assertTrue(backToLoginLink.isDisplayed(), "Back to Login link should be visible");
        backToLoginLink.click();
        WebElement loginForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));
        Assertions.assertTrue(loginForm.isDisplayed(), "Login form should be visible again");
    }

    @Order(3)
    @Test
    public void testCreateAccount() {
        WebElement createAccountLink = driver.findElement(By.xpath("//a[text()='Create Account']"));
        createAccountLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupEmail")));
        WebElement passwordInput = driver.findElement(By.id("signupPassword"));
        WebElement signupButton = driver.findElement(By.xpath("//button[text()='Sign Up']"));
        emailInput.sendKeys("oreillysheena@gmail.com");
        passwordInput.sendKeys("test1");
        signupButton.click();
        handleAlert("Password should be at least 6 characters long and contain a numeric character.");
        passwordInput.clear();
        passwordInput.sendKeys("testxx");
        signupButton.click();
        handleAlert("Password should be at least 6 characters long and contain a numeric character.");
        passwordInput.clear();
        passwordInput.sendKeys("test123");
        signupButton = driver.findElement(By.xpath("//button[text()='Sign Up']"));
        signupButton.click();
        handleAlert("Account created successfully!");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("introVideo")));
        wait.until(ExpectedConditions.urlContains("/SelectTour"));
        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/SelectTour"), 
            "The page should redirect to SelectTour after the video finishes.");
    }

    @Order(4)
    @Test
    public void testCreateAccountEmailError() {
        WebElement createAccountLink = driver.findElement(By.xpath("//a[text()='Create Account']"));
        createAccountLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signupEmail")));
        WebElement passwordInput = driver.findElement(By.id("signupPassword"));
        emailInput.sendKeys("oreillysheena@gmail.com");
        passwordInput.sendKeys("test123");
        WebElement signupButton = driver.findElement(By.xpath("//button[text()='Sign Up']"));
        signupButton.click();
        handleAlert("This email is already in use. Please use a different email or log in.");
    }

    @Order(5)
    @Test
    public void testLoginWithWrongPassword() throws InterruptedException {
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
        emailInput.sendKeys("oreillysheena@gmail.com");
        passwordInput.sendKeys("wrongpassword");
        loginButton.click();
        handleAlert("Incorrect email or password. Please check your credentials or Please sign up");
    }

    @Order(6)
    @Test
    public void testForgotPasswordLink() {
        WebElement forgotPasswordLink = driver.findElement(By.xpath("//a[text()='Forgot Password?']"));
        forgotPasswordLink.click();
        handleAlert("Please enter your email address before resetting the password.");
        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("incorrect@example.com");
        forgotPasswordLink.click();
        handleAlert("Password reset email sent! Check your inbox.");
    }

    @Order(7)
    @Test
    public void testLogin() {
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
        emailInput.sendKeys("oreillysheena@gmail.com");
        passwordInput.sendKeys("test123");
        loginButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("introVideo")));
        wait.until(ExpectedConditions.urlContains("/SelectTour"));
        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/SelectTour"),
                "The page should redirect to SelectTour after the video finishes.");
    }


    private void handleAlert(String expectedMessage) {
        WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        Assertions.assertEquals(expectedMessage, alertText);
        alert.accept();
    }
}
