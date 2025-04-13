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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class J_AddCarbonTest {

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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginMainButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("login-btn")));
        loginMainButton.click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    @Order(1)
    @Test
    public void testCar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='car']"));
        option.click();
        WebElement addCar = driver.findElement(By.xpath("//*[@id=\"newCarForm\"]/button"));
        addCar.click();
        wait.until(ExpectedConditions.urlContains("/car"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement carName = wait.until(ExpectedConditions.elementToBeClickable(By.id("carName")));
        carName.sendKeys("Test Car");
        WebElement ModelYear = wait.until(ExpectedConditions.elementToBeClickable(By.id("year")));
        ModelYear.sendKeys("2020");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("make")));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement specificMakeOption = driver.findElement(By.xpath("//option[text()='Toyota']"));
        specificMakeOption.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("model")));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement modelOption = driver.findElement(By.xpath("//*[@id=\"model\"]/option[19]"));
        modelOption.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("tank")));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement tankOption = driver.findElement(By.xpath("//*[@id=\"tank\"]/option[4]"));
        tankOption.click();
        WebElement saveCarButton = driver.findElement(By.xpath("//button[text()='SAVE CAR']"));
        saveCarButton.click();
        wait.until(ExpectedConditions.urlContains("/newTour"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='car']"));
        option.click();
        WebElement carDropwdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("carDropdown")));
        carDropwdown.click();
        option = driver.findElement(By.xpath("//*[@id=\"carDropdown\"]/option[2]"));
        option.click();
        WebElement origin = driver.findElement(By.id("origin"));
        origin.sendKeys("Dublin");
        WebElement destination = driver.findElement(By.id("destination"));
        destination.sendKeys("Wexford");

        WebElement button = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        button.click();
    }

    @Order(2)
    @Test
    public void testDelete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = driver.findElement(By.xpath("//*[@id=\"legs\"]/table/tbody/tr[1]"));
        button.click();
        handleAlert("Are you sure you want to delete this tour leg?");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> rows = driver.findElements(By.cssSelector(".tour-table tbody tr"));
        Assertions.assertTrue(rows.isEmpty(), "No tour data found!");
    }

    @Order(3)
    @Test
    public void testBus() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        for(int i = 2; i < 6; i++){
            bus(i);
        }
    }

    private void bus(int bus){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='bus']"));
        option.click();
        WebElement origin = driver.findElement(By.id("origin"));
        origin.sendKeys("Dublin");
        WebElement destination = driver.findElement(By.id("destination"));
        destination.sendKeys("Wexford");

        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"bus\"]")));
        dropdown.click();
        if(bus == 2){
            option = driver.findElement(By.xpath("//*[@id=\"bus\"]/option[2]"));
        }else if(bus == 3)
        {
            option = driver.findElement(By.xpath("//*[@id=\"bus\"]/option[3]"));
        } else if(bus == 4){
            option = driver.findElement(By.xpath("//*[@id=\"bus\"]/option[4]"));
        }else{
            option = driver.findElement(By.xpath("//*[@id=\"bus\"]/option[5]"));
        }
        option.click();
        WebElement button = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        button.click();
    }

    @Order(4)
    @Test
    public void testTrain() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        WebElement origin = driver.findElement(By.id("origin"));
        origin.sendKeys("Dublin");
        WebElement destination = driver.findElement(By.id("destination"));
        destination.sendKeys("Wexford");
        WebElement button = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        button.click();
    }

    @Order(5)
    @Test
    public void testPlane() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        for(int i = 0; i < 3; i++){
            plane(i);
        }

    }

    @Order(6)
    @Test
    public void testError() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        WebElement origin = driver.findElement(By.id("origin"));
        origin.sendKeys("Isle of Man Airport");
        WebElement destination = driver.findElement(By.id("destination"));
        destination.sendKeys("The Ferryman");
        WebElement button = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        button.click();
        handleAlert("Error: Cannot find distance, please check Destination.");

        vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        origin = driver.findElement(By.id("origin"));
        origin.sendKeys("blackrock");
        destination = driver.findElement(By.id("destination"));
        destination.sendKeys("Trinity College Dublin");
        button = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        button.click();
        handleAlert("Error: Cannot find distance, please check Origin.");
    }

    @Order(7)
    @Test
    public void testSeats() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='train']"));
        option.click();
        WebElement origin = driver.findElement(By.id("origin"));
        origin.sendKeys("Dublin");
        WebElement destination = driver.findElement(By.id("destination"));
        destination.sendKeys("Wexford");

        WebElement label = driver.findElement(By.cssSelector("label[for='isConcertCheck']"));
        label.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('seats').value = 200;");

        WebElement button = driver.findElement(By.xpath("//*[@id=\"carbonSubmitForm\"]/button"));
        button.click();
    }

    private void plane(int i) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement vehicleDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleDropdown")));
        vehicleDropdown.click();
        WebElement option = driver.findElement(By.xpath("//option[@value='plane']"));
        option.click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        WebElement dropdown = driver.findElement(By.className("select2-selection--single"));
        dropdown.click();

        Thread.sleep(2000);

        WebElement search_box = driver.findElement(By.className("select2-search__field"));
        search_box.sendKeys("DUBLIN, IRELAND");

        Thread.sleep(1000);
        search_box.sendKeys(Keys.ENTER);

        WebElement arrivalDropdown = driver.findElement(By.xpath("//span[@aria-labelledby='select2-arr-container']"));
        arrivalDropdown.click();

        Thread.sleep(2000);
        WebElement arrivalSearchBox = driver.findElement(By.className("select2-search__field"));

        if(i == 0){
            arrivalSearchBox.sendKeys("BRISTOL, ENGLAND");
        } else if(i == 1){
            arrivalSearchBox.sendKeys("JOHN F KENNEDY INTERNATIONAL, USA");
        } else{
            arrivalSearchBox.sendKeys("BRISBANE INTERNATIONAL, AUSTRALIA");
        }


        Thread.sleep(1000);
        arrivalSearchBox.sendKeys(Keys.ENTER);

        WebElement button = driver.findElement(By.xpath("//*[@id=\"planeCarbonForm\"]/button"));
        button.click();
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
