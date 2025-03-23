package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.calculator.*;
import com.example.music_tour_carbon_calculator.objects.carObject;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class CarbonCalculatorController {

    public String thisFuel;
    public String thisConsumption;

    public String thisDocumentID;

    private final FirebaseService firebaseService;

    @Autowired
    public CarbonCalculatorController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

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
        model.addAttribute("currentLegs", addNewLeg(tourName, depature, arrival, distanceS, "plane", carbonEmissions, seats, session));
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
            Model model, RedirectAttributes redirectAttributes) throws IOException, ExecutionException, InterruptedException {

        List<carObject> userCars = (List<carObject>) session.getAttribute("userCars");
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        carObject thiscar = null;
        for(carObject car : userCars){
            if(car.documentId.equalsIgnoreCase(selectedCar)){
                thiscar = car;
            }
        }

        String tourName = (String) session.getAttribute("tourName");
        double distance = Distance.calculateDistance(origin, destination, vehicle);

        if (distance <= 0.0) {
            if(distance == -1.0){
                redirectAttributes.addFlashAttribute("alertMessage", "Error: Cannot find distance, please check Origin.");
            }else if(distance == -2.0){
                redirectAttributes.addFlashAttribute("alertMessage", "Error: Cannot find distance, please check Destination.");
            } else{
                redirectAttributes.addFlashAttribute("alertMessage", "Error: Cannot find distance, please check Origin and Destination.");
            }
            return "redirect:/newTour";
        }

        if(vehicle.equalsIgnoreCase("train")){
            double carbon = 0.28 * distance;
            String carbonEmissions = String.format("%.2f", carbon);
            String distanceS = String.format("%.2f", distance);
            addUserData(tourName, origin, destination, "N/A", String.valueOf(distance), "N/A", carbonEmissions, concert,seats, vehicle, session);
            model.addAttribute("currentLegs", addNewLeg(tourName, origin, destination, distanceS, vehicle, carbonEmissions, seats, session));
            model.addAttribute("tourName" , tourName);
            return "newTour";
        }

        String carbonEmissions = " ";
        if(vehicle.equalsIgnoreCase("bus")){
            getBusInfo(bus);
            carbonEmissions = Calculator.calculateCarbonEmissions(distance, thisFuel, Double.parseDouble(thisConsumption));
            addUserData(tourName, origin, destination, thisConsumption, String.valueOf(distance), thisFuel, carbonEmissions, concert,seats, vehicle, session);
        }
        else{
            carbonEmissions = Calculator.calculateCarbonEmissions(distance, thiscar.getFuel(), Double.parseDouble(thiscar.getConsumption()));
            addUserData(tourName, origin, destination, thiscar.getConsumption(), String.valueOf(distance), thiscar.getFuel(), carbonEmissions, concert,seats, vehicle, session);
        }
        String distanceS = String.format("%.2f", distance);
        model.addAttribute("currentLegs", addNewLeg(tourName, origin, destination, distanceS, vehicle, carbonEmissions, seats, session));
        model.addAttribute("tourName" , tourName);

        return "newTour";
    }

    public tourObject addNewLeg(String tourName, String departure, String arrival, String distance, String vehicle, String carbon, String seats, HttpSession session){
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        tourObject specificTourData = new tourObject(tourName);
        for(tourObject tour : userTours){
            if(tour.tourName.equalsIgnoreCase(tourName)){
                specificTourData.legsOfTour = new ArrayList<>(tour.legsOfTour);

            }
        }
        TourData newLeg = new TourData(departure, arrival, distance, "N/A", "N/A", vehicle, carbon, thisDocumentID, "N/A", seats);
        specificTourData.add(newLeg);
        session.setAttribute("currentTour", specificTourData);

        String userEmail = (String) session.getAttribute("userEmail");
        firebaseService.getAllTours(userEmail, session);

        return specificTourData;
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
        thisDocumentID = ((DocumentReference) newTourRef).getId();
        System.out.println("User data added successfully with ID: " + ((DocumentReference) newTourRef).getId());
    }

    @GetMapping("/deleteLeg")
    public String deleteLeg( @RequestParam(value = "documentId", defaultValue = "123") String documentId , @RequestParam(value = "tourName" , defaultValue = "dublin") String tourName, HttpSession session) throws ExecutionException, InterruptedException {
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        tourObject specificTourData = new tourObject(tourName);
        String userEmail = (String) session.getAttribute("userEmail");

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference offsetTourCollection = db.collection(userEmail).document("Tours").collection(tourName);
        ApiFuture<QuerySnapshot> future = offsetTourCollection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for(tourObject tours : userTours){
            if(tours.tourName.equalsIgnoreCase(tourName)){
                specificTourData.legsOfTour = tours.legsOfTour;
                List<TourData> legs = tours.legsOfTour;
                for(TourData leg : legs){
                    if(leg.getDocumentId().equalsIgnoreCase(documentId)){
                        legs.remove(leg);
                        for(QueryDocumentSnapshot doc : documents){
                            DocumentReference docRef = doc.getReference();
                            if (docRef.getId().equals(documentId)) {
                                docRef.delete();
                                tours.legsOfTour = legs;
                                session.setAttribute("userTours", userTours);
                                session.setAttribute("currentTour", specificTourData);
                                return "redirect:/newTour";
                            }
                        }
                    }
                }
            }
        }
        return "newTour";
    }
}
