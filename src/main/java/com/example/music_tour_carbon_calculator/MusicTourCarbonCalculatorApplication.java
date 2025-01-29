package com.example.music_tour_carbon_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@Controller
public class MusicTourCarbonCalculatorApplication {

    public String thisYear;
    public String thisMake;
    public String thisModel;

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }

    @GetMapping("/main")
    public String showMain() {
        return "main";
    }

    @GetMapping("/car")
    public String showCar() {
        return "car";
    }

    @GetMapping("/getMakes")
    @ResponseBody
    public List getMakes(@RequestParam("year") String year) throws IOException, ParserConfigurationException, SAXException {
        thisYear = year;
        return CarInfo.getMakes(year);
    }

    @GetMapping("/getModels")
    @ResponseBody
    public List getModels(@RequestParam("make") String make) throws IOException, ParserConfigurationException, SAXException {
        thisMake = make;
        return CarInfo.getModels(thisYear, make);
    }

    @GetMapping("/getFuelSize")
    @ResponseBody
    public List getFuelSize( @RequestParam("model") String model) throws IOException, ParserConfigurationException, SAXException {
        thisModel = model;
        return CarInfo.getFuelSize(thisYear, thisMake, model);
    }

    @GetMapping("/calculateCarbon")
    public String calculateCarbon(
            @RequestParam(value = "origin", defaultValue = "dublin") String origin,
            @RequestParam(value = "destination", defaultValue = "dublin") String destination,
            @RequestParam(value = "mode", defaultValue = "driving") String mode,
            @RequestParam(value = "fuel", defaultValue = "petrol") String vehicleFuel,
            @RequestParam(value = "consumption", defaultValue = "0") double consumption,
            Model model) throws IOException {

        double distance = Distance.calculateDistance(origin, destination, mode);

        double carbonEmissions = Calculator.calculateCarbonEmissions(distance, vehicleFuel, consumption);

        model.addAttribute("distance", distance);
        model.addAttribute("vehicleFuel", vehicleFuel);
        model.addAttribute("consumption", consumption);
        model.addAttribute("carbonEmissions", carbonEmissions);

        return "form";
    }

}
