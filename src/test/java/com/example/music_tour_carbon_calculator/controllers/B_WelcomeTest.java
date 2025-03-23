package com.example.music_tour_carbon_calculator.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class B_WelcomeTest {

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
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void welcomePage() throws InterruptedException {
        driver.get(baseUrl);
        Assertions.assertEquals("Welcome", driver.getTitle(), "Page title should be 'Welcome'");
        WebElement logo = driver.findElement(By.className("logo"));
        Assertions.assertNotNull(logo, "Logo should be present");
        Assertions.assertEquals("LOGO", logo.getAttribute("alt"), "Logo alt text should be 'LOGO'");
        WebElement mainText = driver.findElement(By.className("text"));
        Assertions.assertTrue(mainText.getText().contains("Eco Tour"), "Main text should contain 'Eco Tour'");
        WebElement loginButton = driver.findElement(By.className("login-btn"));
        Assertions.assertNotNull(loginButton, "Login button should be present");
        Assertions.assertEquals("LOG IN", loginButton.getText(), "Button should say 'LOG IN'");
        WebElement aboutText = driver.findElement(By.className("name"));
        Assertions.assertEquals("ABOUT", aboutText.getText(), "About section should be present");
        WebElement loginIcon = driver.findElement(By.cssSelector(".fa-solid.fa-user"));
        Assertions.assertNotNull(loginIcon, "Login icon should be present");
        loginButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Assertions.assertTrue(driver.getCurrentUrl().endsWith("/loginScreen"),
                "The page should redirect to SelectTour after the video finishes.");
    }
}
