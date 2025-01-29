package com.example.music_tour_carbon_calculator;

import org.springframework.stereotype.Service;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class Distance {

    public static double calculateDistance(String origin, String destination, String transportType) throws IOException {
        String apiKey = "AIzaSyBXYRQP5e4qehb6sluG6m7Ao6JotPR0o6M";
        String urlString = String.format(
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&units=metric&mode=%s&key=%s",
                origin.replace(" ", "%20"),
                destination.replace(" ", "%20"),
                transportType.replace(" ", "%20"),
                apiKey
        );

        JSONObject jsonObject = getJsonObject(urlString);

        String distanceText = jsonObject.getJSONArray("rows")
                .getJSONObject(0)
                .getJSONArray("elements")
                .getJSONObject(0)
                .getJSONObject("distance")
                .getString("text");

        String distanceValue = distanceText.replaceAll("[^\\d.]", "");
        return Double.parseDouble(distanceValue);
    }

    private static JSONObject getJsonObject(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONObject(response.toString());
    }
}
