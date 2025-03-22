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
public class newTourTest{

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
        WebElement vehicleDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vehicleDropdown")));
        assertNotNull(vehicleDropdown, "Vehicle dropdown should be present");
        WebElement tourTable = driver.findElement(By.className("tour-table"));
        assertNotNull(tourTable, "Tour table should be present");
        WebElement addLegButton = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        assertNotNull(addLegButton, "Add Leg button should be present");
    }

    @Test
    @Order(2)
    public void testVehicleDropdownSelection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Train
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        vehicleDropdown.click();
        assertTrue(option.isSelected(), "Train option should be selected");

        // Normal Functionality
        WebElement origin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("origin")));
        assertNotNull(origin, "Origin should be present");
        WebElement destination = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("destination")));
        assertNotNull(destination, "Destination should be present");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isConcertCheck")));
        WebElement checkmark = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/label/span"));
        checkmark.click();
        WebElement checkbox = driver.findElement(By.id("isConcertCheck"));
        Assertions.assertTrue(checkbox.isSelected(), "Checkbox should be checked after clicking the checkmark");
        WebElement seats = driver.findElement(By.id("seats"));
        assertNotNull(seats, "Seats should be present");
        checkmark.click();
        Assertions.assertFalse(checkbox.isSelected(), "Checkbox should be unchecked after clicking again");

        // Bus
        vehicleDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='bus']"));
        option.click();
        vehicleDropdown.click();
        assertTrue(option.isSelected(), "Bus option should be selected");
        WebElement busDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("bus")));
        busDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='mini_bus']"));
        option.click();
        busDropdown.click();
        assertTrue(option.isSelected(), "Mini Bus option should be selected");
        busDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='camper_van']"));
        option.click();
        busDropdown.click();
        assertTrue(option.isSelected(), "Camper Van option should be selected");
        busDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='coach']"));
        option.click();
        busDropdown.click();
        assertTrue(option.isSelected(), "Coach option should be selected");busDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='double_decker']"));
        option.click();
        busDropdown.click();
        assertTrue(option.isSelected(), "Double Decker option should be selected");

        // Plane
        vehicleDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='plane']"));
        option.click();
        vehicleDropdown.click();
        assertTrue(option.isSelected(), "Plane option should be selected");
        WebElement depDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("dep")));
        WebElement arrDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("arr")));
        assertNotNull(depDropdown, "Departure dropdown should be present");
        assertNotNull(arrDropdown, "Arrival Departure should be present");

        // Car
        vehicleDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='car']"));
        option.click();
        vehicleDropdown.click();
        assertTrue(option.isSelected(), "Car option should be selected");
        WebElement carDropdown = driver.findElement(By.id("carDropdown"));
        assertNotNull(carDropdown, "Car Dropdown should be present");
        WebElement addCar = driver.findElement(By.xpath("//*[@id=\"newCarForm\"]/button"));
        assertNotNull(addCar, "Car Button should be present");
        addCar.click();
        wait.until(ExpectedConditions.urlContains("/car"));
        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/car?"),
                "The page should redirect to create car page.");

    }

    @Test
    @Order(3)
    public void addingLeg() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        vehicleDropdown.click();
        WebElement origin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("origin")));
        WebElement destination = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("destination")));
        origin.sendKeys("Trinity College Dublin");
        destination.sendKeys("Cobh Cork");
        WebElement addLegButton = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        addLegButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".tour-table tbody tr")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".tour-table tbody"), "TRINITY COLLEGE DUBLIN"
        ));

        vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        vehicleDropdown.click();
        origin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("origin")));
        destination = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("destination")));
        origin.sendKeys("Isle of Man Airport");
        destination.sendKeys("The Ferryman");
        addLegButton = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        addLegButton.click();
        handleAlert("Error: Cannot find distance, please check Destination.");

        vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        vehicleDropdown.click();
        origin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("origin")));
        destination = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("destination")));
        origin.sendKeys("The Ferryman");
        destination.sendKeys("Isle of Man Airport");
        addLegButton = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        addLegButton.click();
        handleAlert("Error: Cannot find distance, please check Origin.");
    }

    @Test
    @Order(4)
    public void removingLeg() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement entry = driver.findElement(By.xpath("//*[@id=\"legs\"]/table/tbody/tr[1]"));
        entry.click();
        handleAlert("Are you sure you want to delete this tour leg?");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".tour-table tbody")));
        wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".tour-table tbody tr"))));
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
    }

}
