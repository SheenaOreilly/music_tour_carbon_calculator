package com.example.music_tour_carbon_calculator.firebase;

import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    public static String[] values =  {"Arrival", "Carbon_Emissions", "Consumption", "Departure", "Distance", "Fuel", "Concert", "Seats", "Vehicle"};

    public void getAllTours(String userEmail, HttpSession session) {

        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");

        if (userTours == null) {
            userTours = new ArrayList<>();
            session.setAttribute("userTours", userTours);
        }

        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference userCollection = db.collection(userEmail);

            DocumentReference toursDocument = userCollection.document("Tours");

            Iterable<CollectionReference> subCollections = toursDocument.listCollections();

            for (CollectionReference collection : subCollections) {
                String tour = collection.getId();

                if(!userTours.isEmpty()){
                    tourObject foundTour = findTourByName(userTours, tour);

                    if (foundTour != null) {
                        AddData(collection, foundTour, userTours);
                    } else {
                        tourObject createTour = new tourObject(tour);
                        userTours.add(createTour);
                        AddData(collection, createTour, userTours);
                    }
                }else{
                    tourObject createTour = new tourObject(tour);
                    userTours.add(createTour);
                    AddData(collection, createTour, userTours);
                }
            }
            session.setAttribute("userTours", userTours);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void AddData(CollectionReference collection, tourObject createTour, List<tourObject> userTours) throws InterruptedException, ExecutionException {
        List<QueryDocumentSnapshot> documents = collection.get().get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            String documentID = document.getId();
            if (isTourLegDuplicate(userTours, documentID)) {
                continue;
            }
            TourData newTour = new TourData();
            newTour.setDocumentId(documentID);
            Map<String, Object> data = document.getData();
            for(int i = 0; i < values.length; i++){
                add(values[i], data, newTour);
            }
            createTour.add(newTour);
        }
    }
    private boolean isTourLegDuplicate(List<tourObject> userTours, String documentID) {
        for (tourObject tour : userTours) {
            for (TourData leg : tour.legsOfTour) {
                if (leg.getDocumentId() != null && leg.getDocumentId().equals(documentID)) {
                    return true;
                }
            }
        }
        return false;
    }



    public static tourObject findTourByName(List<tourObject> tours, String inputTourName) {
        for (tourObject tour : tours) {
            if (tour.tourName != null && tour.tourName.equals(inputTourName)) {
                return tour;
            }
        }
        return null;
    }


    public void add(String value, Map<String, Object> data, TourData tour){
        if (data.containsKey(value)) {
            String realValue = data.get(value).toString();
            switch(value) {
                case "Arrival":
                    tour.setArrival(realValue);
                    break;
                case "Carbon_Emissions":
                    tour.setCarbonEmissions(realValue);
                    break;
                case "Consumption":
                    tour.setConsumption(realValue);
                    break;
                case "Departure":
                    tour.setDeparture(realValue);
                    break;
                case "Distance":
                    tour.setDistance(realValue);
                    break;
                case "Concert":
                    tour.setConcert(realValue);
                    break;
                case "Seats":
                    tour.setSeats(realValue);
                    break;
                case "Vehicle":
                    tour.setVehicle(realValue);
                    break;
                default:
                    tour.setFuel(realValue);
            }
        } else {
            String realValue = "N/A";
            switch(value) {
                case "Consumption":
                    tour.setConsumption(realValue);
                    break;
                default:
                    tour.setFuel(realValue);
            }
        }
    }
}
