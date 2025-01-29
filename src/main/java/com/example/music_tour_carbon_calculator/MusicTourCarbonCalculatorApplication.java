package com.example.music_tour_carbon_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

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
            @RequestParam(value = "origin", defaultValue = "dublin") String origin,
            @RequestParam(value = "destination", defaultValue = "dublin") String destination,
            @RequestParam(value = "mode", defaultValue = "driving") String mode,
            @RequestParam(value = "year", defaultValue = "2025") String year,
            @RequestParam(value = "make", defaultValue = "toyota") String make,
            @RequestParam(value = "caramodel", defaultValue = "corolla") String carmodel,
//            @RequestParam(value = "fuel", defaultValue = "petrol") String vehicleFuel,
//            @RequestParam(value = "consumption", defaultValue = "0") double consumption,
            Model model) throws IOException, ParserConfigurationException, SAXException {

        double distance = Distance.calculateDistance(origin, destination, mode);
        List carInfo = CarInfo.getCarID(year, make, carmodel);

//        double carbonEmissions = Calculator.calculateCarbonEmissions(distance, vehicleFuel, consumption);

        model.addAttribute("distance", distance);
        model.addAttribute("carInfo", carInfo);
//        model.addAttribute("vehicleFuel", vehicleFuel);
//        model.addAttribute("consumption", consumption);
//        model.addAttribute("carbonEmissions", carbonEmissions);

        return "form";
    }

}
