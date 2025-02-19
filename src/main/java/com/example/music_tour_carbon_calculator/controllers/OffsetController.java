package com.example.music_tour_carbon_calculator.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OffsetController {
    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }
    @GetMapping("/offsets")
    public String showOffsets() {
        return "offsets";
    }
}
