package com.example.music_tour_carbon_calculator;

import java.util.ArrayList;
import java.util.List;


public class tourObject {
    List<TourData> legsOfTour = new ArrayList<>();
    public String tourName;

    public tourObject(String tourName){
        this.tourName = tourName;
    }

    public void add(TourData oneLeg){
        legsOfTour.add(oneLeg);
    }



}
