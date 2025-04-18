package com.example.music_tour_carbon_calculator.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public static FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream("carbon-calculator-music-tours-firebase-adminsdk-fbsvc-0538dad743.json");

        if (serviceAccount == null) {
            throw new IOException("Firebase service account file not found in the classpath.");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}