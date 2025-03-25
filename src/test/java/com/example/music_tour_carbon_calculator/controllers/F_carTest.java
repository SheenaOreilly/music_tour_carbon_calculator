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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class F_carTest {

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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginForm")));
        loginUser();
        navigateToPage();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testPageElements() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement carName = wait.until(ExpectedConditions.elementToBeClickable(By.id("carName")));
        assertNotNull(carName, "Car Nickname should be present");
        carName.sendKeys("Sheena's Car");
        WebElement ModelYear = wait.until(ExpectedConditions.elementToBeClickable(By.id("year")));
        assertNotNull(ModelYear, "Model Year should be present");
        ModelYear.sendKeys("2020");
        WebElement makeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("make")));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(makeDropdown.findElements(By.tagName("option")).size() > 1, "Make dropdown should contain more options than just '-- Choose a Make --'");
        WebElement specificMakeOption = driver.findElement(By.xpath("//option[text()='Toyota']"));
        specificMakeOption.click();
        WebElement modelDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("model")));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(modelDropdown.findElements(By.tagName("option")).size() > 1, "Model options should be populated.");
        WebElement modelOption = driver.findElement(By.xpath("//*[@id=\"model\"]/option[19]"));
        modelOption.click();
        WebElement tankDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("tank")));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(tankDropdown.findElements(By.tagName("option")).size() > 1, "Tank size options should be populated.");
        WebElement tankOption = driver.findElement(By.xpath("//*[@id=\"tank\"]/option[4]"));
        tankOption.click();
        WebElement saveCarButton = driver.findElement(By.xpath("//button[text()='SAVE CAR']"));
        saveCarButton.click();
        wait.until(ExpectedConditions.urlContains("/newTour"));
        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/newTour"),
                "The page should redirect to new tour page.");
    }

    private void handleAlert(String expectedMessage) {
        WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        Assertions.assertEquals(expectedMessage, alertText);
        alert.accept();
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
    }

    public void navigateToPage(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tourInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tourName")));
        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("go_button")));
        tourInput.sendKeys("Test Tour");
        goButton.click();
        wait.until(ExpectedConditions.urlContains("/newTour"));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='car']"));
        option.click();
        WebElement addCar = driver.findElement(By.xpath("//*[@id=\"newCarForm\"]/button"));
        addCar.click();
        wait.until(ExpectedConditions.urlContains("/car"));
    }

}
