package com.example.music_tour_carbon_calculator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test") public String health()
    {
        return "Hello and welcome";
    }
}