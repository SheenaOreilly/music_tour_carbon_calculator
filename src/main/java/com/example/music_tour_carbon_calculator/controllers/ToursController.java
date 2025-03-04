package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.createTourBlock;
import com.example.music_tour_carbon_calculator.objects.overallTour;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class ToursController {

    private final FirebaseService firebaseService;

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }
    @Autowired
    public ToursController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/setUserEmail")
    public ResponseEntity<?> setUserEmail(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        session.setAttribute("userEmail", email);
        firebaseService.getAllTours(email, session);
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        createTourBlock.createBlock(userTours, session);
        List<overallTour> overTours = (List<overallTour>) session.getAttribute("overallTours");
        sort(overTours, session);
        return ResponseEntity.ok().body("Email set in session");
    }

    public static void sort(List<overallTour> list, HttpSession session) {
        List<overallTour> temp = new ArrayList<>(list);
        temp.sort(Comparator.comparing(
                overallTour::getCarbonEmissions,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));
        int i = 0;
        for(overallTour tour : temp){
            for(overallTour real : list){
                if(real.getTourName().equalsIgnoreCase(tour.getTourName())){
                    real.setRank(i);
                    i++;
                }
            }
        }
        list.sort(Comparator.comparing(
                overallTour::getOffset,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
        session.setAttribute("overallTours", list);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("Session cleared");
    }

    @PostMapping("/load")
    public ResponseEntity<?> load(HttpSession session){
        String userEmail = (String) session.getAttribute("userEmail");
        firebaseService.getAllTours(userEmail, session);
        createTourBlock.createBlock((List<tourObject>) session.getAttribute("userTours"), session);
        return ResponseEntity.ok().body("Loaded Information");
    }

    @GetMapping("/tours")
    public String showTours(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        firebaseService.getAllTours(userEmail, session);
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        createTourBlock.createBlock(userTours, session);
        List<overallTour> overTours = (List<overallTour>) session.getAttribute("overallTours");
        sort(overTours, session);
        model.addAttribute("userTours", userTours);
        model.addAttribute("overTours", overTours);
        model.addAttribute("totalTours", overTours.size());
        return "tours";
    }

    @GetMapping("/getSpecificTour")
    public String showSpecificTour(@RequestParam("tourName") String tourName, HttpSession session, Model model){
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        List<TourData> specificTourData = new ArrayList<>();
        for(tourObject tour : userTours){
            if(tour.tourName.equalsIgnoreCase(tourName)){
                specificTourData = tour.legsOfTour;
            }
        }
        model.addAttribute("tourName", tourName);
        model.addAttribute("specificToursData", specificTourData);
        return "specificTour";
    }

    @GetMapping("/newTour")
    public String showNewTour(HttpSession session, Model model) {
        String tourName = (String) session.getAttribute("tourName");
        model.addAttribute("tourName", tourName);
        model.addAttribute("currentLegs", session.getAttribute("currentTour"));
        return "newTour";
    }
}
