package com.example.music_tour_carbon_calculator.calculator;

import org.json.JSONArray;
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
        String transport;
        switch (transportType){
            case "car", "bus":
                transport = "transit";
                break;
            default:
                transport = "public transport";
                break;
        }
        String apiKey = "AIzaSyBXYRQP5e4qehb6sluG6m7Ao6JotPR0o6M";
        String urlString = String.format(
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&units=metric&mode=%s&key=%s",
                origin.replace(" ", "%20"),
                destination.replace(" ", "%20"),
                transport.replace(" ", "%20"),
                apiKey
        );

        JSONObject jsonObject = getJsonObject(urlString);

        if (jsonObject.getJSONArray("rows").length() == 0) {
            System.out.println("Error: No data found in 'rows'. Origin and destination may be invalid.");
            return 0.0;
        }

        JSONArray elements = jsonObject.getJSONArray("rows")
                .getJSONObject(0)
                .getJSONArray("elements");

        if (!elements.getJSONObject(0).getString("status").equals("NOT_FOUND") && !elements.getJSONObject(0).getString("status").equals("ZERO_RESULTS") && !elements.getJSONObject(0).getString("status").equals("INVALID_REQUEST")) {
            String distanceText = elements.getJSONObject(0)
                    .getJSONObject("distance")
                    .getString("text");
            String distanceValue = distanceText.replaceAll("[^\\d.]", "");
            return Double.parseDouble(distanceValue);
        } else {
            System.out.println("Error: Distance information not found.");
            if(jsonObject.has("origin_addresses") && jsonObject.getJSONArray("origin_addresses").getString(0).isEmpty()){
                return -1.0;
            }
            if(jsonObject.has("destination_addresses") && jsonObject.getJSONArray("destination_addresses").getString(0).isEmpty()){
                return -2.0;
            }
            return 0.0;
        }
    }

    public static JSONObject getJsonObject(String urlString) throws IOException {
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
