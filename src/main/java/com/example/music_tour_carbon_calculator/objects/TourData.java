package com.example.music_tour_carbon_calculator.objects;

public class TourData {
    private String departure;
    private String arrival;
    private String distance;
    private String consumption;
    private String fuel;
    private String carbonEmissions;

    private String concert;
    private String seats;

    private String documentId;

    private String vehicle;

    public TourData() {}

    public TourData(String departure, String arrival, String distance, String consumption, String fuel, String vehicle, String carbonEmissions, String documentId, String concert, String seats) {
        this.departure = departure;
        this.arrival = arrival;
        this.distance = distance;
        this.consumption = consumption;
        this.fuel = fuel;
        this.carbonEmissions = carbonEmissions;
        this.documentId = documentId;
        this.concert = concert;
        this.seats = seats;
        this.vehicle = vehicle;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public void setConcert(String concert) {
        this.concert = concert;
    }

    public String getConcert() {
        return concert;
    }

    public String getSeats() {
        return seats;
    }

    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getArrival() { return arrival; }
    public void setArrival(String arrival) { this.arrival = arrival; }

    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }

    public String getConsumption() { return consumption; }
    public void setConsumption(String consumption) { this.consumption = consumption; }

    public String getFuel() { return fuel; }
    public void setFuel(String fuel) { this.fuel = fuel; }

    public String getCarbonEmissions() { return carbonEmissions; }
    public void setCarbonEmissions(String carbonEmissions) { this.carbonEmissions = carbonEmissions; }

    @Override
    public String toString() {
        return "TourData{" +
                "departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", distance='" + distance + '\'' +
                ", consumption='" + consumption + '\'' +
                ", fuel='" + fuel + '\'' +
                ", carbonEmissions='" + carbonEmissions + '\'' +
                '}';
    }
}
