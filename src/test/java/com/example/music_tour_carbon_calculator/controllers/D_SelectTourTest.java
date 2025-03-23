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
public class D_SelectTourTest {

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
        loginUser();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Order(1)
    @Test
    public void testHeaderElements() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement editAddTourLink = driver.findElement(By.className("toursp"));
        WebElement pastToursLink = driver.findElement(By.className("pasttourp"));
        WebElement offsetsLink = driver.findElement(By.className("offestp"));
        WebElement sampleTicketLink = driver.findElement(By.className("sample"));
        Assertions.assertTrue(editAddTourLink.isDisplayed(), "Edit/Add Tour link should be visible");
        Assertions.assertTrue(pastToursLink.isDisplayed(), "Past Tours link should be visible");
        Assertions.assertTrue(offsetsLink.isDisplayed(), "Offsets link should be visible");
        Assertions.assertTrue(sampleTicketLink.isDisplayed(), "Sample Ticket link should be visible");
        WebElement userIcon = driver.findElement(By.className("fa-user"));
        userIcon.click();
        WebElement userInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userInfo")));
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("logoutButton")));
        Assertions.assertTrue(userInfo.isDisplayed(), "User info should be visible");
        Assertions.assertTrue(logoutButton.isDisplayed(), "Logout button should be visible");
    }

    @Order(2)
    @Test
    public void testPageElements(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tourInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tourName")));
        Assertions.assertTrue(tourInput.isDisplayed(), "Tour name input should be visible");
        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("go_button")));
        Assertions.assertTrue(goButton.isDisplayed(), "Go button should be visible");
        WebElement tourImage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@src='images/tourImage.png']")));
        Assertions.assertTrue(tourImage.isDisplayed(), "Tour image should be visible");
        int imageWidth = tourImage.getSize().getWidth();
        int imageHeight = tourImage.getSize().getHeight();
        Assertions.assertEquals(600, imageWidth, "Tour image width should be 600px");
        Assertions.assertEquals(600, imageHeight, "Tour image height should be 600px");
        String imageSrc = tourImage.getAttribute("src");
        Assertions.assertTrue(imageSrc.contains("images/tourImage.png"), "Image source should be 'images/tourImage.png'");
    }

    @Order(3)
    @Test
    public void createTourName(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tourInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tourName")));
        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("go_button")));
        tourInput.sendKeys("Sample Tour");
        goButton.click();
        wait.until(ExpectedConditions.urlContains("/newTour"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/newTour"), "Should navigate to newTour page");
    }

    public static void loginUser(){
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
}
