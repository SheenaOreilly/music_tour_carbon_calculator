package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.createTourBlock;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.overallTour;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.example.music_tour_carbon_calculator.offsetCompanies;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Controller
public class OffsetController {

    private final FirebaseService firebaseService;

    private HttpSession session;
    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }
    @Autowired
    public OffsetController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
    @GetMapping("/offsets")
    public String showOffsets(HttpSession session, Model model) {
        this.session = session;
        String userEmail = (String) session.getAttribute("userEmail");
        firebaseService.getAllTours(userEmail, session);
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        createTourBlock.createBlock(userTours, session);
        List<overallTour> overTours = (List<overallTour>) session.getAttribute("overallTours");
        model.addAttribute("overTours", overTours);
        model.addAttribute("offsetCompaniesMap", offsetCompanies.getOffsetLinks());
        return "offsets";
    }

    @GetMapping("/offsetTourMethod")
    public String offsetTourMethod(@RequestParam(value = "checkedTours") List<String> checkedTours) throws ExecutionException, InterruptedException {
        String userEmail = (String) session.getAttribute("userEmail");
        for(String tourNameOffset : checkedTours){
            if(!Objects.equals(tourNameOffset, "")){
                Firestore db = FirestoreClient.getFirestore();
                CollectionReference offsetTourCollection = db.collection(userEmail).document("Offsets").collection(tourNameOffset);
                ApiFuture<QuerySnapshot> future = offsetTourCollection.get();
                List<QueryDocumentSnapshot> documents = future.get().getDocuments();
                for(QueryDocumentSnapshot doc : documents){
                    DocumentReference docRef = doc.getReference();
                    Map<String, Object> offsetData = new HashMap<>();
                    offsetData.put("offset", true);
                    docRef.update(offsetData).get();
                }
            }
        }
        update(checkedTours);
        return "offsets";
    }

    public void update(List<String> checkedTours){
        List<tourObject> userTours = (List<tourObject>) session.getAttribute("userTours");
        List<overallTour> overTours = (List<overallTour>) session.getAttribute("overallTours");
        for(String tourNameOffset : checkedTours) {
            for (tourObject tour : userTours) {
                if (tourNameOffset.equalsIgnoreCase(tour.tourName)) {
                    tour.setOffset(true);
                }
            }
            session.setAttribute("userTours", userTours);

            for (overallTour tour : overTours) {
                if (tourNameOffset.equalsIgnoreCase(tour.tourName)) {
                    tour.setOffset(true);
                }
            }
        }
    }

}
