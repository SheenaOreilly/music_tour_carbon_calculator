package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.firebase.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.http.HttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication(scanBasePackages = "com.example.music_tour_carbon_calculator")
@Controller
public class MusicTourCarbonCalculatorApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MusicTourCarbonCalculatorApplication.class);
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestHeader("Authorization") String token, HttpSession session) {
        try {
            String idToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            FirebaseToken decodedToken = FirebaseAuthService.verifyToken(idToken);
            session.setAttribute("userEmail", decodedToken.getEmail());
            return "User authenticated: " + decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            return "Invalid token: " + e.getMessage();
        }
    }

    @GetMapping("/loginScreen")
    public String showLogin() {
        return "loginScreen";
    }
}
