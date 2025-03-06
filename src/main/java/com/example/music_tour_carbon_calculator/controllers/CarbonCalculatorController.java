package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.calculator.*;
import com.example.music_tour_carbon_calculator.objects.carObject;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class CarbonCalculatorController {

    public String thisFuel;
    public String thisConsumption;

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
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
            @RequestParam(value = "isConcertPlane", defaultValue = "no") String concert,
            @RequestParam(value = "seats", defaultValue = "0") String seats,
            HttpSession session,
            Model model) throws ExecutionException, InterruptedException {
        String[] arrOfStr = dep.split(":");
        double lat1 = Double.parseDouble(arrOfStr[0]);
        double lon1 = Double.parseDouble(arrOfStr[1]);
        String  depature = arrOfStr[2];
        arrOfStr = arr.split(":");
        double lat2 = Double.parseDouble(arrOfStr[0]);
        double lon2 = Double.parseDouble(arrOfStr[1]);
        String  arrival = arrOfStr[2];
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(deltaLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        DecimalFormat df_obj = new DecimalFormat("#.###");
        double distance = Double.parseDouble(df_obj.format(6371.0 * c));
        double carbon = getPlaneInfo(distance);
        carbon = Math.round(carbon * 100.0) / 100.0;
        String carbonEmissions = String.format("%.2f", carbon);

        String tourName = (String) session.getAttribute("tourName");
        addUserData(tourName, depature, arrival, "N/A", String.format("%.2f", distance), "N/A", carbonEmissions, concert,seats, "plane", session);
        String distanceS = String.valueOf(distance);
        model.addAttribute("currentLegs", addNewLeg(depature, arrival, distanceS, "plane", carbonEmissions, seats, session));
        model.addAttribute("tourName" , tourName);

        return "newTour";
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

    @GetMapping("/calculateCarbon")
    public String calculateCarbon(
            @RequestParam(value = "origin", defaultValue = "dublin") String origin,
            @RequestParam(value = "destination", defaultValue = "dublin") String destination,
            @RequestParam(value = "selectedVehicle", defaultValue = "car") String vehicle,
            @RequestParam(value = "bus", defaultValue = "coach") String bus,
            @RequestParam(value = "isConcert", defaultValue = "no") String concert,
            @RequestParam(value = "seats", defaultValue = "0") String seats,
            @RequestParam(value = "selectedCar", defaultValue = "") String selectedCar,
            HttpSession session,
            Model model) throws IOException, ExecutionException, InterruptedException {

        List<carObject> userCars = (List<carObject>) session.getAttribute("userCars");
        carObject thiscar = null;
        for(carObject car : userCars){
            if(car.documentId.equalsIgnoreCase(selectedCar)){
                thiscar = car;
            }
        }

        String tourName = (String) session.getAttribute("tourName");
        double distance = Distance.calculateDistance(origin, destination, vehicle);

        if (distance == 0.0) {
            model.addAttribute("alertMessage", "Error: Can not found distance, please check Origin and Destination.");
            return "newTour";
        }

        if(vehicle.equalsIgnoreCase("train")){
            double carbon = 0.28 * distance;
            String carbonEmissions = String.format("%.2f", carbon);
            String distanceS = String.format("%.2f", distance);
            model.addAttribute("currentLegs", addNewLeg(origin, destination, distanceS, vehicle, carbonEmissions, seats, session));
            model.addAttribute("tourName" , tourName);
            addUserData(tourName, origin, destination, "N/A", String.valueOf(distance), "N/A", carbonEmissions, concert,seats, vehicle, session);
            return "newTour";
        }

        if(vehicle.equalsIgnoreCase("bus")){
            getBusInfo(bus);
        }
        String carbonEmissions = Calculator.calculateCarbonEmissions(distance, thiscar.getFuel(), Double.parseDouble(thiscar.getConsumption()));
        String distanceS = String.format("%.2f", distance);
        model.addAttribute("currentLegs", addNewLeg(origin, destination, distanceS, vehicle, carbonEmissions, seats, session));
        model.addAttribute("tourName" , tourName);
        addUserData(tourName, origin, destination, thiscar.getConsumption(), String.valueOf(distance), thiscar.getFuel(), carbonEmissions, concert,seats, vehicle, session);

        return "newTour";
    }

    public tourObject addNewLeg(String departure, String arrival, String distance, String vehicle, String carbon, String seats, HttpSession session){
        tourObject newTour = (tourObject) session.getAttribute("currentTour");
        TourData newLeg = new TourData(departure, arrival, distance, "N/A", "N/A", vehicle, carbon, "N/A", "N/A", seats);
        newTour.add(newLeg);
        session.setAttribute("currentTour", newTour);
        return newTour;
    }

    public void addUserData(String tourName, String departure, String arrival, String consumption,
                            String distance, String vehicleFuel, String carbonEmissions, String isConcert,
                            String seats, String vehicle, HttpSession session) throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();
        String userEmail = (String) session.getAttribute("userEmail");
        Map<String, Object> data = new HashMap<>();
        data.put("Departure", departure);
        data.put("Arrival", arrival);
        data.put("Distance", distance);
        data.put("Carbon_Emissions", carbonEmissions);
        data.put("Concert", isConcert);
        data.put("Seats", seats);
        data.put("Vehicle", vehicle);
        if (!vehicleFuel.equals("N/A")) {
            data.put("Consumption", consumption);
            data.put("Fuel", vehicleFuel);
        }
        CollectionReference toursRef = db.collection(userEmail).document("Tours").collection(tourName);
        ApiFuture<QuerySnapshot> querySnapshotFuture = toursRef.get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        if (querySnapshot.isEmpty()) {
            Map<String, Object> offsetData = new HashMap<>();
            offsetData.put("offset", false);
            CollectionReference offsetRef = db.collection(userEmail).document("Offsets").collection(tourName);
            DocumentReference newOffsetRef = offsetRef.add(offsetData).get();
            System.out.println("Offset data added successfully with ID: " + ((DocumentReference) newOffsetRef).getId());
        }
        DocumentReference newTourRef = toursRef.add(data).get();
        System.out.println("User data added successfully with ID: " + ((DocumentReference) newTourRef).getId());
    }
}
