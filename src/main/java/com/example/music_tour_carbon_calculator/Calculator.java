package com.example.music_tour_carbon_calculator;

import org.springframework.stereotype.Service;

@Service
public class Calculator {
    public static double calculateCarbonEmissions(double distance, String fuel, double consumption){
        double emissionFactor = switch (fuel.toLowerCase()) {
            case "diesel" -> 2.683;
            case "petrol", "premium gasoline", "regular gasoline" -> 2.311;
            default -> 0;
        };

        double used_fuel = consumption * (distance/100);
        return used_fuel * emissionFactor;
    }
}
