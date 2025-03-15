package com.example.music_tour_carbon_calculator.objects;

import java.util.ArrayList;
import java.util.List;


public class tourObject {
    public List<TourData> legsOfTour = new ArrayList<>();
    public String tourName;
    boolean offset;

    public tourObject(String tourName){
        this.tourName = tourName;
    }

    public void add(TourData oneLeg){
        legsOfTour.add(oneLeg);
    }

    public boolean isOffset() {
        return offset;
    }

    public void setOffset(boolean offset) {
        this.offset = offset;
    }
}
