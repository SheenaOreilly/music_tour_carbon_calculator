package com.example.music_tour_carbon_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@Controller
public class MusicTourCarbonCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }

    @GetMapping("/main")
    public String showMain() {
        return "main";
    }

    @GetMapping("/calculateCarbon")
    public String calculateCarbon(
            @RequestParam(value = "distance", defaultValue = "0") double distance,
            @RequestParam(value = "fuel", defaultValue = "petrol") String vehicleFuel,
            @RequestParam(value = "consumption", defaultValue = "0") double consumption,
            Model model) {

        double carbonEmissions = Calculator.calculateCarbonEmissions(distance, vehicleFuel, consumption);

        model.addAttribute("distance", distance);
        model.addAttribute("vehicleFuel", vehicleFuel);
        model.addAttribute("consumption", consumption);
        model.addAttribute("carbonEmissions", carbonEmissions);

        return "form";
    }

}
