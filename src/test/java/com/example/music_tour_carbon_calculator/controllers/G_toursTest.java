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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class G_toursTest {

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
    public void testWithTours() {
        loginUser("oreils21@tcd.ie", "test123");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pastToursLink = driver.findElement(By.className("pasttourp"));
        pastToursLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("tours-container")));
        List<WebElement> tours = driver.findElements(By.className("tour-wrapper"));
        Assertions.assertFalse(tours.isEmpty(),
                "Expected tours to be displayed, but none were found.");
    }

    @Order(2)
    @Test
    public void testSearchTours() {
        loginUser("oreils21@tcd.ie", "test123");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pastToursLink = driver.findElement(By.className("pasttourp"));
        pastToursLink.click();
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        List<WebElement> allTours = driver.findElements(By.className("tour-wrapper"));
        Assertions.assertFalse(allTours.isEmpty(), "Tours should exist before searching.");
        String searchQuery = allTours.get(0).findElement(By.className("tour-name")).getText().substring(0, 3);
        searchInput.sendKeys(searchQuery);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.className("tour-name"), searchQuery));

        List<WebElement> filteredTours = driver.findElements(By.className("tour-wrapper"));
        Assertions.assertFalse(filteredTours.isEmpty(),
                "Search should return at least one result.");
    }

    @Order(3)
    @Test
    public void testSpecificTour() {
        loginUser("oreils21@tcd.ie", "test123");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pastToursLink = driver.findElement(By.className("pasttourp"));
        pastToursLink.click();
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchInput")));
        List<WebElement> allTours = driver.findElements(By.className("tour-wrapper"));
        String searchQuery = allTours.get(0).findElement(By.className("tour-name")).getText().substring(0, 3);
        searchInput.sendKeys(searchQuery);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.className("tour-name"), searchQuery));
        List<WebElement> filteredTours = driver.findElements(By.className("tour-wrapper"));
        filteredTours.get(0).click();
        wait.until(ExpectedConditions.urlContains("/getSpecificTour"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/getSpecificTour"),
                "User should be redirected to the specific tour page.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("tour-table")));
        List<WebElement> rows = driver.findElements(By.cssSelector(".tour-table tbody tr"));
        Assertions.assertFalse(rows.isEmpty(), "No tour data found!");

        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            Assertions.assertEquals(9, columns.size(), "Row does not have expected 8 columns!");

            for (WebElement column : columns) {
                Assertions.assertFalse(column.getText().trim().isEmpty(), "Table contains empty fields!");
            }
        }
        WebElement allToursButton = driver.findElement(By.xpath("/html/body/div[6]/div/button"));
        allToursButton.click();
        wait.until(ExpectedConditions.urlContains("/tours"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/tours"), "Did not navigate back to all tours page!");
    }

    @Test
    @Order(4)
    public void testTourIcons() {
        loginUser("oreils21@tcd.ie", "test123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pastToursLink = driver.findElement(By.className("pasttourp"));
        pastToursLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("tour-wrapper")));
        List<WebElement> tours = driver.findElements(By.className("tour-wrapper"));
        Assertions.assertFalse(tours.isEmpty(), "No tours found!");

        WebElement firstTour = tours.get(0);
        WebElement offsetIcon = firstTour.findElement(By.className("tour-status"));
        String offsetText = offsetIcon.getText().trim();
        if (offsetText.equals("Offset Incomplete")) {
            WebElement hourglassIcon = offsetIcon.findElement(By.tagName("i"));
            Assertions.assertEquals(true, hourglassIcon.getAttribute("class").contains("fa-hourglass-end"), "Offset icon incorrect!");
        }

        WebElement fourthTour = tours.get(3);
        offsetIcon = fourthTour.findElement(By.className("tour-status"));
        offsetText = offsetIcon.getText().trim();
        if (offsetText.equals("Offset Complete")) {
            WebElement checkIcon = offsetIcon.findElement(By.tagName("i"));
            Assertions.assertEquals(true, checkIcon.getAttribute("class").contains("fa-check"), "Offset icon incorrect!");
        }

        List<String> expectedActiveModes = new ArrayList<>();
        List<WebElement> transportIcons = firstTour.findElements(By.cssSelector(".transport-icons i"));
        Assertions.assertFalse(transportIcons.isEmpty(), "No transport icons found!");
        for (WebElement icon : transportIcons) {
            String mode = icon.getAttribute("data-mode");
            String color = icon.getCssValue("color");
            boolean isActive = color.equals("rgba(1, 34, 79, 1)");
            boolean isInactive = color.equals("rgba(175, 214, 247, 1)");
            Assertions.assertTrue(isActive || isInactive, "Unexpected color for " + mode + " icon: " + color);
            if (isActive) {
                expectedActiveModes.add(mode);
            }

            System.out.println(mode + " is " + (isActive ? "active." : "inactive."));
        }
        List<String> actualActiveModes = List.of("car", "bus", "plane");
        Assertions.assertEquals(new HashSet<>(actualActiveModes), new HashSet<>(expectedActiveModes),
                "Active transport modes do not match expected ones!");
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
