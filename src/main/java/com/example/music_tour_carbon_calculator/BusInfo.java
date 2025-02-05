package com.example.music_tour_carbon_calculator;

import org.springframework.stereotype.Service;

@Service
public class BusInfo {

        public static String getBusEmissions(String type) {
            double consumption = switch (type.toLowerCase()) {
                case "mini_bus" -> 6.9;
                case "camper_van" -> 8;
                case "coach" -> 33;
                case "double_decker" -> 39.2;
                default -> 0;
            };

            return String.format("%.2f", consumption);
        }
}
