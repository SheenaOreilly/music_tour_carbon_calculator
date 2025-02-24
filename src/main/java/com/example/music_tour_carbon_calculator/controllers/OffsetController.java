package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.createTourBlock;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.overallTour;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.example.music_tour_carbon_calculator.offsetCompanies;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OffsetController {

    private final FirebaseService firebaseService;
    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }
    @Autowired
    public OffsetController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
    @GetMapping("/offsets")
    public String showOffsets(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        firebaseService.getAllTours(userEmail, session);
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        createTourBlock.createBlock(userTours, session);
        List<overallTour> overTours = (List<overallTour>) session.getAttribute("overallTours");
        model.addAttribute("overTours", overTours);
        model.addAttribute("offsetCompaniesMap", offsetCompanies.getOffsetLinks());
        return "offsets";
    }
}
