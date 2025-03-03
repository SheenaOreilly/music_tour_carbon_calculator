package com.example.music_tour_carbon_calculator.calculator;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PlaneInfo {

    public static Map<String, String> getAirports(){
        Map<String, String> airports = new LinkedHashMap<>();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("C:\\Users\\Admin\\Documents\\FifthYear\\dissertation\\project\\music_tour_carbon_calculator\\src\\main\\resources\\static\\GlobalAirportDatabase.txt"));
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
