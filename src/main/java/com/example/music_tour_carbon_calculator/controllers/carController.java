package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.calculator.CarInfo;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.objects.carObject;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class carController {
    public String thisYear;
    public String thisMake;
    public String thisModel;
    public String thisFuel;
    public String thisConsumption;

    public String thisCarName;

    public static void main(String[] args) {
        SpringApplication.run(MusicTourCarbonCalculatorApplication.class, args);
    }


    @GetMapping("/getMakes")
    @ResponseBody
    public List getMakes(@RequestParam("year") String year, @RequestParam("carName") String carName) throws IOException, ParserConfigurationException, SAXException {
        thisYear = year;
        thisCarName = carName;
        return CarInfo.getMakes(year);
    }

    @GetMapping("/getModels")
    @ResponseBody
    public List getModels(@RequestParam("make") String make) throws IOException, ParserConfigurationException, SAXException {
        thisMake = make;
        return CarInfo.getModels(thisYear, make);
    }

    @GetMapping("/getFuelSize")
    @ResponseBody
    public Map<String, String> getFuelSize(@RequestParam("model") String model) throws IOException, ParserConfigurationException, SAXException {
        thisModel = model;
        return CarInfo.getFuelSize(thisYear, thisMake, model);
    }

    @GetMapping("/getFuelInfo")
    public String getFuelInfo(@RequestParam("tank") String tank, Model model, HttpSession session) throws IOException, ParserConfigurationException, SAXException, ExecutionException, InterruptedException {
        List carInfo = CarInfo.getFuelInfo(tank);
        String vehicleFuel = (String) carInfo.get(0);
        String consumption = (String) carInfo.get(1);
        thisFuel = vehicleFuel.toLowerCase();
        thisConsumption = consumption;

        Firestore db = FirestoreClient.getFirestore();
        String userEmail = (String) session.getAttribute("userEmail");
        Map<String, Object> data = new HashMap<>();
        data.put("Consumption", thisConsumption);
        data.put("Fuel", thisFuel);
        data.put("Nickname", thisCarName);

        CollectionReference toursRef = db.collection(userEmail).document("Cars").collection("Cars");
        DocumentReference newTourRef = toursRef.add(data).get();

        System.out.println("User data added successfully with ID: " + ((DocumentReference) newTourRef).getId());
        List<carObject> userCars = (List<carObject>) session.getAttribute("userCars");
        carObject newCar = new carObject(thisCarName, thisConsumption, thisFuel, ((DocumentReference) newTourRef).getId());
        userCars.add(newCar);
        session.setAttribute("userCars", userCars);
        model.addAttribute("vehicleFuel", vehicleFuel);
        model.addAttribute("consumption", consumption);
        return "redirect:/newTour";
    }

    @PostMapping("/getCars")
    public ResponseEntity<Map<String, String>> getCars(HttpSession session) throws ExecutionException, InterruptedException {
        String userEmail = (String) session.getAttribute("userEmail");
        List<carObject> userCars = (List<carObject>) session.getAttribute("userCars");
        if (userCars == null) {
            userCars = new ArrayList<>();
            session.setAttribute("userCars", userCars);
        }
        Map<String, String> cars = new LinkedHashMap<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference userCollection = db.collection(userEmail);
        DocumentReference toursDocument = userCollection.document("Cars");

        Iterable<CollectionReference> subCollections = toursDocument.listCollections();

        for (CollectionReference collection : subCollections) {
            List<QueryDocumentSnapshot> documents = collection.get().get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                String documentID = document.getId();
                Map<String, Object> data = document.getData();
                String nickname = (String) data.get("Nickname");
                String consumption = (String) data.get("Consumption");
                String fuel = (String) data.get("Fuel");
                carObject current = new carObject(nickname, consumption, fuel, documentID);
                userCars.add(current);
                cars.put(nickname, documentID);
            }
        }
        session.setAttribute("userCars", userCars);
        return ResponseEntity.ok(cars);
    }


    @GetMapping("/car")
    public String showCar() {
        return "car";
    }
}
