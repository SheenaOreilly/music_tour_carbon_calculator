package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.tourObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
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
        return ResponseEntity.ok().body("Email set in session");
    }

    @GetMapping("/tours")
    public String showTours(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        firebaseService.getAllTours(userEmail, session);
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        session.setAttribute("userTours", userTours);
        model.addAttribute("userTours", userTours);
        return "tours";
    }


    @GetMapping("/newTour")
    public String showNewTour() {
        return "newTour";
    }
}
