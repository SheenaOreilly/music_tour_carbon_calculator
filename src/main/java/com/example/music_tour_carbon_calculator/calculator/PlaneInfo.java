package com.example.music_tour_carbon_calculator.calculator;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PlaneInfo {

    public static Map<String, String> getAirports(){
        Map<String, String> airports = new LinkedHashMap<>();
        BufferedReader reader;
        try{
            InputStream inputStream = PlaneInfo.class.getClassLoader().getResourceAsStream("GlobalAirportDatabase.txt");

            if (inputStream == null) {
                throw new FileNotFoundException("GlobalAirportDatabase.txt file not found in classpath.");
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));


            String line = reader.readLine();

            while(line != null){
                String str = line;
                String[] arrOfStr = str.split(":");
                String airport = arrOfStr[2];
                String country = arrOfStr[4];
                String combinedString = airport + ", " + country;
                if(airport.equalsIgnoreCase("N/A")){
                    airport = arrOfStr[3];
                }
                String lat = arrOfStr[14];
                String lon = arrOfStr[15];
                String combined = lat + ":" + lon + ":" + airport;
                airports.put(combinedString, combined);
                line = reader.readLine();
            }
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        return airports;
    }


}
