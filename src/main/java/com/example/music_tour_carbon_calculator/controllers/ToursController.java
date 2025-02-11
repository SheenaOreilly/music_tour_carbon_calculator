package com.example.music_tour_carbon_calculator.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class ToursController {
    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }

    @GetMapping("/tours")
    public String showTours() {
        return "tours";
    }

    @GetMapping("/newTour")
    public String showNewTour() {
        return "newTour";
    }
}
