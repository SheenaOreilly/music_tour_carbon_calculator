package com.example.music_tour_carbon_calculator.objects;

import java.util.ArrayList;
import java.util.List;

public class overallTour {
    public String tourName;

    public List<String> modesOfTransport = new ArrayList<>();
    public int noOfSeats;
    public int noOfConcerts;
    public double carbonEmissions;

    public overallTour(String tourName, int noOfSeats, int noOfConcerts, double carbonEmissions) {
        this.tourName = tourName;
        this.noOfSeats = noOfSeats;
        this.noOfConcerts = noOfConcerts;
        this.carbonEmissions = carbonEmissions;
    }

    public int getNoOfSeats(){
        return noOfSeats;
    }

    public int getNoOfConcerts() {
        return noOfConcerts;
    }

    public double getCarbonEmissions() {
        return carbonEmissions;
    }

    public String getTourName() {
        return tourName;
    }

    public void addModes(String mode){
        modesOfTransport.add(mode);
    }

    public List<String> getModes(){
        return modesOfTransport;
    }
    public void updateSeats(int seats){
        noOfSeats = noOfSeats + seats;
    }

    public void updateCarbon(double carbon){
        carbonEmissions = carbonEmissions + carbon;
    }

    public void updateConcerts(int concerts){
        noOfConcerts = noOfConcerts + concerts;
    }

}
