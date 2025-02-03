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
import java.util.Map;

@SpringBootApplication
@Controller
public class MusicTourCarbonCalculatorApplication {

    public String thisYear;
    public String thisMake;
    public String thisModel;
    public String thisFuel;
    public String thisConsumption;

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }

    @GetMapping("/main")
    public String showMain() {
        return "main";
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
    public Map<String, String> getFuelSize(@RequestParam("model") String model) throws IOException, ParserConfigurationException, SAXException {
        thisModel = model;
        return CarInfo.getFuelSize(thisYear, thisMake, model);
    }

    @GetMapping("/getFuelInfo")
    public String getFuelInfo(@RequestParam("tank") String tank, Model model) throws IOException, ParserConfigurationException, SAXException {
        List carInfo = CarInfo.getFuelInfo(tank);
        String vehicleFuel = (String) carInfo.get(0);
        String consumption = (String) carInfo.get(1);
        thisFuel = vehicleFuel.toLowerCase();
        thisConsumption = consumption;
        model.addAttribute("vehicleFuel", vehicleFuel);
        model.addAttribute("consumption", consumption);
        return "carbon";
    }

    @GetMapping("/carbon")
    public String showCarbon() {
        return "carbon";
    }

    @GetMapping("/calculateCarbon")
    public String calculateCarbon(
            @RequestParam(value = "origin", defaultValue = "dublin") String origin,
            @RequestParam(value = "destination", defaultValue = "dublin") String destination,
            @RequestParam(value = "mode", defaultValue = "driving") String mode,
            Model model) throws IOException {

        double distance = Distance.calculateDistance(origin, destination, mode);

        String carbonEmissions = Calculator.calculateCarbonEmissions(distance, thisFuel, Double.parseDouble(thisConsumption));

        model.addAttribute("distance", distance);
        model.addAttribute("vehicleFuel", thisFuel);
        model.addAttribute("consumption", thisConsumption);
        model.addAttribute("carbonEmissions", carbonEmissions);

        return "carbon";
    }

}
