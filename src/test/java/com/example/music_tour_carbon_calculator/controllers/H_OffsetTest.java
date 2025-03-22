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
public class H_OffsetTest {

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
        WebElement offsetsLink = driver.findElement(By.className("offestp"));
        offsetsLink.click();

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
        WebElement totalCarbon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalCarbon")));
        WebElement totalSeats = driver.findElement(By.id("totalSeats"));
        Assertions.assertNotNull(totalCarbon, "Total Carbon text should be displayed");
        Assertions.assertNotNull(totalSeats, "Total Seats text should be displayed");
        List<WebElement> checkboxes = driver.findElements(By.className("offsetCheck"));
        Assertions.assertFalse(checkboxes.isEmpty(), "There should be at least one tour checkbox to offset");
    }

    @Order(2)
    @Test
    public void testSelectToursAndCheckCalculation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> checkboxes = driver.findElements(By.className("offsetCheck"));
        Assertions.assertFalse(checkboxes.isEmpty(), "There should be checkboxes for tours");
        checkboxes.get(0).click();
        WebElement totalCarbon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalCarbon")));
        WebElement totalSeats = driver.findElement(By.id("totalSeats"));
        String carbonText = totalCarbon.getText();
        String seatsText = totalSeats.getText();
        Assertions.assertFalse(carbonText.contains(" 0 kg CO₂"), "Total Carbon should be updated");
        Assertions.assertFalse(seatsText.equals(" 0 "), "Total Seats should be updated");
    }

    @Order(3)
    @Test
    public void testCharityBoxes() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> charityBoxes = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.className("offsetBox")
        ));
        Assertions.assertFalse(charityBoxes.isEmpty(), "There should be at least one charity box");
        WebElement firstCharity = charityBoxes.get(0);
        String charityTitle = firstCharity.getAttribute("title");
        String charityUrl = firstCharity.getAttribute("href");
        Assertions.assertNotNull(charityTitle, "Charity title is missing!");
        Assertions.assertTrue(charityTitle.length() > 0, "Charity title should not be empty");
        Assertions.assertNotNull(charityUrl, "Charity URL is missing!");
        Assertions.assertTrue(charityUrl.length() > 0, "Charity URL should not be empty");
        String originalWindow = driver.getWindowHandle();
        firstCharity.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        Assertions.assertTrue(driver.getCurrentUrl().startsWith(charityUrl), 
            "New window should open with the correct URL");
        driver.close();
        driver.switchTo().window(originalWindow);
        List<WebElement> checkboxes = driver.findElements(By.className("offsetCheck"));
        Assertions.assertFalse(checkboxes.isEmpty(), "There should be tour checkboxes");
        WebElement companyElement = firstCharity.findElement(By.className("company-offset-value"));
        String initialOffset = companyElement.getText();
        checkboxes.get(0).click();
        wait.until(ExpectedConditions.textToBePresentInElement(companyElement, "Euro"));
        String newOffset = companyElement.getText();
        Assertions.assertNotEquals(initialOffset, newOffset, "Offset value should change when tour is selected");
        Assertions.assertTrue(newOffset.contains("Euro"), "Offset value should include Euro amount");
        Assertions.assertTrue(newOffset.contains("per ticket"), "Offset value should include per ticket information");
    }

    @Order(4)
    @Test
    public void testClickOffsetButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> checkboxesBefore  = driver.findElements(By.className("offsetCheck"));
        int initialCheckboxCount = checkboxesBefore.size();
        checkboxesBefore.get(0).click();
        WebElement offsetButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"offsetTourForm\"]/button")));
        offsetButton.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.urlContains("/offsets"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/offsets"), "Should navigate to offsetTourMethod page");
        List<WebElement> checkboxesAfter = driver.findElements(By.className("offsetCheck"));
        int finalCheckboxCount = checkboxesAfter.size();
        Assertions.assertEquals(initialCheckboxCount - 1, finalCheckboxCount, "The number of checkboxes should be 1 less after selecting one checkbox");
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
