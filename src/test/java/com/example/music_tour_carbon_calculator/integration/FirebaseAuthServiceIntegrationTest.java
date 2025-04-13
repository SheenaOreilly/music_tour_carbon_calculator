package com.example.music_tour_carbon_calculator.integration;

import com.example.music_tour_carbon_calculator.controllers.CarbonCalculatorController;
import com.example.music_tour_carbon_calculator.firebase.FirebaseAuthService;
import com.example.music_tour_carbon_calculator.firebase.FirebaseConfig;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

public class FirebaseAuthServiceIntegrationTest {

    private static final String FIREBASE_API_KEY = "AIzaSyBVDgwGXSawlZWOLbSHwg_BN9RNy_4RG2I";
    private FirebaseService firebaseService;

    private CarbonCalculatorController carbonCalculatorController;

    @BeforeEach
    void setUp() {
        firebaseService = new FirebaseService();
        carbonCalculatorController = new CarbonCalculatorController(firebaseService);
    }


    @Test
    void firebaseApp_shouldBeInitialized() throws Exception {
        FirebaseApp firebaseApp = FirebaseConfig.firebaseApp();
        assertNotNull(firebaseApp, "FirebaseApp should be present in the application context");
        assertEquals("[DEFAULT]", firebaseApp.getName(), "FirebaseApp should have name [DEFAULT]");

        String email = "oreils21@tcd.ie";
        String password = "test123";

        String idToken = signInAndGetIdToken(email, password);

        System.out.println("Received ID Token: " + idToken);

        assertNotNull(idToken);

        FirebaseToken decodedToken = FirebaseAuthService.verifyToken(idToken);

        assertNotNull(decodedToken);
        assertEquals(email, decodedToken.getEmail());
    }

    @Test
    void firebaseApp_readData() throws Exception {
        FirebaseApp firebaseApp = FirebaseConfig.firebaseApp();
        assertNotNull(firebaseApp, "FirebaseApp should be present in the application context");
        assertEquals("[DEFAULT]", firebaseApp.getName(), "FirebaseApp should have name [DEFAULT]");

        String email = "oreils21@tcd.ie";
        String password = "test123";

        String idToken = signInAndGetIdToken(email, password);
        FirebaseAuthService.verifyToken(idToken);

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference userCollection = db.collection(email);

        DocumentReference toursDocument = userCollection.document("Tours");
        Iterable<CollectionReference> subCollections = toursDocument.listCollections();
        List<tourObject> userTours = new ArrayList<>();

        for (CollectionReference collection : subCollections) {
            String tour = collection.getId();
            tourObject createTour = new tourObject(tour);
            firebaseService.AddData(collection, createTour, userTours);
        }

    }

    @Test
    void firebaseApp_addData() throws Exception {
        FirebaseApp firebaseApp = FirebaseConfig.firebaseApp();
        assertNotNull(firebaseApp, "FirebaseApp should be present in the application context");
        assertEquals("[DEFAULT]", firebaseApp.getName(), "FirebaseApp should have name [DEFAULT]");

        String email = "oreils21@tcd.ie";
        String password = "test123";

        String idToken = signInAndGetIdToken(email, password);
        FirebaseAuthService.verifyToken(idToken);

        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockSession.getAttribute("userEmail")).thenReturn(email);

        String tourName = "SummerTour2025";
        String departure = "2025-06-01";
        String arrival = "2025-06-10";
        String consumption = "10.5";
        String distance = "500";
        String vehicleFuel = "Gasoline";
        String carbonEmissions = "200";
        String isConcert = "true";
        String seats = "500";
        String vehicle = "Bus";

        carbonCalculatorController.addUserData(tourName, departure, arrival, consumption, distance, vehicleFuel, carbonEmissions, isConcert, seats, vehicle, mockSession);

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference toursRef = db.collection(email).document("Tours").collection(tourName);

        ApiFuture<QuerySnapshot> querySnapshotFuture = toursRef.get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        assertFalse(querySnapshot.isEmpty(), "The tour data should be added to Firestore");

        querySnapshot.getDocuments().forEach(doc -> {
            assertEquals(departure, doc.getString("Departure"));
            assertEquals(arrival, doc.getString("Arrival"));
            assertEquals(consumption, doc.getString("Consumption"));
            assertEquals(distance, doc.getString("Distance"));
            assertEquals(carbonEmissions, doc.getString("Carbon_Emissions"));
            assertEquals(isConcert, doc.getString("Concert"));
            assertEquals(seats, doc.getString("Seats"));
            assertEquals(vehicle, doc.getString("Vehicle"));
        });

        CollectionReference offsetRef = db.collection(email).document("Offsets").collection(tourName);
        ApiFuture<QuerySnapshot> offsetQueryFuture = offsetRef.get();
        QuerySnapshot offsetQuerySnapshot = offsetQueryFuture.get();

        if (offsetQuerySnapshot.isEmpty()) {
            fail("Offset data should have been added for the tour");
        } else {
            offsetQuerySnapshot.getDocuments().forEach(doc -> {
                assertFalse(doc.getBoolean("offset"));
            });
        }


    }



    private String signInAndGetIdToken(String email, String password) throws Exception {
        URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String requestBody = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                email, password
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes());
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP request failed with response code: " + conn.getResponseCode());
        }

        Scanner scanner = new Scanner(conn.getInputStream());
        String response = scanner.useDelimiter("\\A").next();

        String tokenKey = "\"idToken\": \"";
        int startIndex = response.indexOf(tokenKey) + tokenKey.length();
        int endIndex = response.indexOf("\"", startIndex);
        String idToken = "";

        if (startIndex > tokenKey.length() - 1 && endIndex > startIndex) {
            idToken = response.substring(startIndex, endIndex);
            System.out.println("Extracted idToken: " + idToken);
        } else {
            System.out.println("idToken not found in the JSON response.");
        }

        return idToken;
    }

}
