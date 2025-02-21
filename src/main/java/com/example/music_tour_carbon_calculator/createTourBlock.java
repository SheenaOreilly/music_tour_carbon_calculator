package com.example.music_tour_carbon_calculator;

import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.objects.overallTour;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class createTourBlock {

    public static void createBlock(List<tourObject> userTours, HttpSession session){
        List<overallTour> overallTours = new ArrayList<>();

        for(tourObject tour : userTours){
            overallTour newTour = new overallTour(tour.tourName, 0, 0, 0.0);
            for(TourData leg : tour.legsOfTour){
                newTour.updateCarbon(Double.parseDouble(leg.getCarbonEmissions()));
                if(leg.getConcert().equalsIgnoreCase("yes")){
                    newTour.updateConcerts(1);
                }
                newTour.updateSeats(Integer.parseInt(leg.getSeats()));
                if(!newTour.getModes().contains(leg.getVehicle())){
                    newTour.addModes(leg.getVehicle());
                }
            }
            overallTours.add(newTour);
        }
        session.setAttribute("overallTours", overallTours);
    }
}
