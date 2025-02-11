package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.firebase.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final FirebaseAuthService firebaseAuthService;

    public AuthController(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/verify")
    public String verifyToken(@RequestHeader("Authorization") String token) {
        try {
            String idToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);
            return "Token is valid for user: " + decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            return "Invalid token: " + e.getMessage();
        }
    }
}