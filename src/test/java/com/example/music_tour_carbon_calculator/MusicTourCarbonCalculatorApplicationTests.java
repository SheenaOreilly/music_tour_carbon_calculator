package com.example.music_tour_carbon_calculator;

import com.example.music_tour_carbon_calculator.controllers.MusicTourCarbonCalculatorApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MusicTourCarbonCalculatorApplication.class)
class MusicTourCarbonCalculatorApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        // The test will pass if the context loads successfully
    }
}
