package com.example.music_tour_carbon_calculator.objects;

public class carObject {

    public String nickname;
    public String consumption;
    public String fuel;
    public String documentId;

    public carObject(String nickname, String consumption, String fuel, String documentId){
        this.nickname = nickname;
        this.consumption = consumption;
        this.fuel = fuel;
        this.documentId = documentId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getConsumption() {
        return consumption;
    }

    public String getFuel() {
        return fuel;
    }

    public String getDocumentId() {
        return documentId;
    }

}
