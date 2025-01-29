package com.example.music_tour_carbon_calculator;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class CarInfo {

    public static List getCarID(String year, String make, String model) throws IOException, ParserConfigurationException, SAXException {
        List<String> vehicleTexts = new ArrayList<>();
        String urlString = String.format(
                "https://www.fueleconomy.gov/ws/rest/vehicle/menu/options?year=%s&make=%s&model=%s",
                year.replace(" ", "%20"),
                make.replace(" ", "%20"),
                model.replace(" ", "%20")
        );

        return getJson(urlString, vehicleTexts);
    }

    private static List getJson(String urlString, List vehicleTexts) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/xml");


        // Read response
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        conn.disconnect();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new java.io.ByteArrayInputStream(response.toString().getBytes()));

        NodeList textNodes = doc.getElementsByTagName("text");
        for (int i = 0; i < textNodes.getLength(); i++) {
            vehicleTexts.add(textNodes.item(i).getTextContent());
        }


        return vehicleTexts;
    }

}
