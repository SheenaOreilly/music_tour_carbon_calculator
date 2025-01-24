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

    @GetMapping("/hello")
    public String showForm() {
        return "form";
    }

    @GetMapping("/greet")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greet";
    }
}
