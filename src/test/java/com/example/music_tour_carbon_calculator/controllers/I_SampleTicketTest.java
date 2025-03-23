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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.time.Duration;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class I_SampleTicketTest {

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
        loginUser("oreils21@tcd.ie", "test123");
        WebElement sampleTicketLink = driver.findElement(By.className("sample"));
        sampleTicketLink.click();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Order(1)
    @Test
    public void testPageElements() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        Assertions.assertNotNull(searchInput, "Tour Name input field should be visible");
        WebElement ticketImage = driver.findElement(By.id("ticket"));
        WebElement emptyTicketImage = driver.findElement(By.id("emptyTicket"));
        Assertions.assertTrue(ticketImage.isDisplayed(), "Ticket image should be displayed initially.");
        Assertions.assertFalse(emptyTicketImage.isDisplayed(), "Empty ticket image should not be visible initially.");
        WebElement uploadLabel = driver.findElement(By.className("custom-file-upload"));
        Assertions.assertNotNull(uploadLabel, "Upload photo label should be visible");
        WebElement imageUpload = driver.findElement(By.id("imageUpload"));
        Assertions.assertNotNull(imageUpload, "Image upload input field should be visible");
    }

    @Order(2)
    @Test
    public void testEnterTourName() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchInput = driver.findElement(By.id("searchInput"));
        String tourName = "FONTAINES D.C. US TOUR";
        searchInput.sendKeys(tourName);
        WebElement searchButton = driver.findElement(By.id("searchButton"));
        searchButton.click();
        WebElement emptyTicketImage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("emptyTicket")));
        Assertions.assertTrue(emptyTicketImage.isDisplayed(), "Empty ticket image should be displayed after searching");
        WebElement concertNameElement = driver.findElement(By.id("concertName"));
        Assertions.assertEquals(tourName, concertNameElement.getText(), "Concert name should match the entered tour name");

        WebElement imageUploadInput = driver.findElement(By.id("imageUpload"));
        String imagePath = "C:\\Users\\Admin\\Documents\\FifthYear\\dissertation\\project\\music_tour_carbon_calculator\\src\\main\\resources\\static\\images\\fontainesImage.avif";
        imageUploadInput.sendKeys(imagePath);
        WebElement previewImage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("previewImage")));
        Assertions.assertTrue(previewImage.isDisplayed(), "Preview image should be displayed after uploading");
    }


    public static void loginUser(String email, String password){
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("introVideo")));
        wait.until(ExpectedConditions.urlContains("/SelectTour"));
        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/SelectTour"),
                "The page should redirect to SelectTour after the video finishes.");
    }
}
