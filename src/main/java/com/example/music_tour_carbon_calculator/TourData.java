package com.example.music_tour_carbon_calculator;

public class oneEntryObject {
    String arrival;
    double carbon_emissions;
    String departure;
    double distance;
    String fuel;
    String consumption;

    public oneEntryObject() {}

    public oneEntryObject(String arrival, double carbon_emissions, String departure, double distance, String fuel, String consumption){
        this.arrival = arrival;
        this.carbon_emissions = carbon_emissions;
        this.departure = departure;
        this.distance = distance;
        this.fuel = fuel;
        this.consumption = consumption;
    }

    public String getArrival() {
        return arrival;
    }

    public double getCarbon_emissions() {
        return carbon_emissions;
    }

    public String getDeparture() {
        return departure;
    }

    public double getDistance() {
        return distance;
    }

    public String getFuel() {
        return fuel;
    }

    public String getConsumption() {
        return consumption;
    }
}
