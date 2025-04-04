package com.example.music_tour_carbon_calculator.objects;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public class overallTour {
    public String tourName;

    public List<String> modesOfTransport = new ArrayList<>();
    public int noOfSeats;
    public int noOfConcerts;
    public double carbonEmissions;
    public boolean offset;

    public int rank;

    public overallTour(String tourName, int noOfSeats, int noOfConcerts, double carbonEmissions, boolean offset) {
        this.tourName = tourName;
        this.noOfSeats = noOfSeats;
        this.noOfConcerts = noOfConcerts;
        this.carbonEmissions = carbonEmissions;
        this.offset = offset;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean getOffset() {
        return offset;
    }

    public void setOffset(boolean offset) {
        this.offset = offset;
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
        DecimalFormat df_obj = new DecimalFormat("#.###");
        carbonEmissions = Double.parseDouble(df_obj.format(carbonEmissions + carbon));
    }

    public void updateConcerts(int concerts){
        noOfConcerts = noOfConcerts + concerts;
    }

}
