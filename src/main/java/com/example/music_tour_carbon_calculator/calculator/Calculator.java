package com.example.music_tour_carbon_calculator.calculator;

import org.springframework.stereotype.Service;

@Service
public class Calculator {
    public static String calculateCarbonEmissions(double distance, String fuel, double consumption){
        double emissionFactor = 0;
        if(fuel != null && fuel != ""){
            emissionFactor = switch (fuel.toLowerCase()) {
                case "diesel" -> 2.683;
                case "petrol", "premium gasoline", "regular gasoline" -> 2.311;
                case "electricity" -> 0.2548;
                default -> 0;
            };
        }

        if(!(distance < 0) && !(consumption < 0)){
            double used_fuel = consumption * (distance/100);
            double emmisions = used_fuel * emissionFactor;
            emmisions = Math.round(emmisions * 100.0) / 100.0;
            return String.format("%.2f", emmisions);
        }else{
            return "0.00";
        }
    }
}
