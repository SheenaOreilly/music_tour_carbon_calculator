package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.TourData;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.tourObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LegOfTourController {
    private final FirebaseService firebaseService;

    public LegOfTourController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }

    @GetMapping("/SelectTour")
    public String showNewTour() {
        return "SelectTour";
    }

    @GetMapping("/selectTourName")
    public String selectNewTour(@RequestParam("tourName") String tourName, HttpSession session) {
        session.setAttribute("tourName", tourName);

        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        tourObject specificTourData = new tourObject(tourName);
        for(tourObject tour : userTours){
            if(tour.tourName.equalsIgnoreCase(tourName)){
                specificTourData = tour;
            }
        }
        session.setAttribute("currentTour", specificTourData);
        return "redirect:/newTour";
    }
}
