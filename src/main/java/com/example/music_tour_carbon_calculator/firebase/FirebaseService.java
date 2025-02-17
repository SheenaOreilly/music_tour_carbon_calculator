package com.example.music_tour_carbon_calculator.firebase;

import com.example.music_tour_carbon_calculator.TourData;
import com.example.music_tour_carbon_calculator.tourObject;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    public static String[] values =  {"Arrival", "Carbon_Emissions", "Consumption", "Departure", "Distance", "Fuel" };

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
                        AddData(collection, foundTour);
                    } else {
                        tourObject createTour = new tourObject(tour);
                        userTours.add(createTour);
                        AddData(collection, createTour);
                    }
                }else{
                    tourObject createTour = new tourObject(tour);
                    userTours.add(createTour);
                    AddData(collection, createTour);
                }
            }
            session.setAttribute("userTours", userTours);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void AddData(CollectionReference collection, tourObject createTour) throws InterruptedException, ExecutionException {
        List<QueryDocumentSnapshot> documents = collection.get().get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            TourData newTour = new TourData();
            Map<String, Object> data = document.getData();
            for(int i = 0; i < values.length; i++){
                add(values[i], data, newTour);
            }
            createTour.add(newTour);
        }
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
