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
    public String currentVehicle;

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
    @GetMapping("/getPlanes")
    @ResponseBody
    public Map<String, String> getPlanes()  {
        return PlaneInfo.getAirports();
    }

    @GetMapping("/getPlaneCarbon")
    public String getPlaneCarbon(
            @RequestParam(value = "dep", defaultValue = "dublin") String dep,
            @RequestParam(value = "arr", defaultValue = "donegal") String arr,
            Model model) {
        String[] arrOfStr = dep.split(":");
        double lat1 = Double.parseDouble(arrOfStr[0]);
        double lon1 = Double.parseDouble(arrOfStr[0]);
        arrOfStr = arr.split(":");
        double lat2 = Double.parseDouble(arrOfStr[0]);
        double lon2 = Double.parseDouble(arrOfStr[0]);
        double distance = Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;
        double carbon = getPlaneInfo(distance);
        String carbonEmissions = String.format("%.2f", carbon);

        model.addAttribute("distance", String.format("%.2f", distance));
        model.addAttribute("vehicleFuel", "N/A");
        model.addAttribute("consumption", "N/A");
        model.addAttribute("carbonEmissions", carbonEmissions);

        return "carbon";
    }

    public void getBusInfo(@RequestParam("bus") String bus){
        thisConsumption = BusInfo.getBusEmissions(bus);
        thisFuel = "diesel";
    }
    public double getPlaneInfo(double distance){
        if(distance < 1600){
            return 0.4 * distance;
        } else if(distance > 4000){
            return 0.27 * distance;
        } else{
            return 0.25 * distance;
        }
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
            @RequestParam(value = "vehicle", defaultValue = "train") String vehicle,
            @RequestParam(value = "bus", defaultValue = "coach") String bus,
            Model model) throws IOException {

        double distance = Distance.calculateDistance(origin, destination, mode);

        if(vehicle.equalsIgnoreCase("train")){
            double carbon = 0.28 * distance;
            String carbonEmissions = String.format("%.2f", carbon);
            model.addAttribute("distance", distance);
            model.addAttribute("vehicleFuel", "N/A");
            model.addAttribute("consumption", "N/A");
            model.addAttribute("carbonEmissions", carbonEmissions);
            return "carbon";
        }

        if(vehicle.equalsIgnoreCase("bus")){
            getBusInfo(bus);
        }

        String carbonEmissions = Calculator.calculateCarbonEmissions(distance, thisFuel, Double.parseDouble(thisConsumption));

        model.addAttribute("distance", distance);
        model.addAttribute("vehicleFuel", thisFuel);
        model.addAttribute("consumption", thisConsumption);
        model.addAttribute("carbonEmissions", carbonEmissions);

        return "carbon";
    }

}
