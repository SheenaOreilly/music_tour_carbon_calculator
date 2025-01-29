package com.example.music_tour_carbon_calculator;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class CarInfo {

    public static Document doc;

    public static List getMakes(String year) throws IOException, ParserConfigurationException, SAXException {
        List<String> vehicleTexts = new ArrayList<>();
        String urlString = String.format(
                "https://www.fueleconomy.gov/ws/rest/vehicle/menu/make?year=%s",
                year.replace(" ", "%20")
        );

        List test = getText(urlString, vehicleTexts);
        return getText(urlString, vehicleTexts);
    }

    public static List getModels(String year, String make) throws IOException, ParserConfigurationException, SAXException {
        List<String> vehicleTexts = new ArrayList<>();
        String urlString = String.format(
                "https://www.fueleconomy.gov/ws/rest/vehicle/menu/model?year=%s&make=%s",
                year.replace(" ", "%20"),
                make.replace(" ", "%20")
        );

        return getText(urlString, vehicleTexts);
    }
    public static Map<String, String> getFuelSize(String year, String make, String model) throws IOException, ParserConfigurationException, SAXException {
        Map<String, String> vehicleTexts = new LinkedHashMap<>();
        String urlString = String.format(
                "https://www.fueleconomy.gov/ws/rest/vehicle/menu/options?year=%s&make=%s&model=%s",
                year.replace(" ", "%20"),
                make.replace(" ", "%20"),
                model.replace(" ", "%20")
        );
        return getTextAndValue(urlString, vehicleTexts);
    }

    public static List getFuelInfo(String tank) throws IOException, ParserConfigurationException, SAXException {
        List<String> vehicleTexts = new ArrayList<>();
        String urlString = String.format(
                "https://www.fueleconomy.gov/ws/rest/vehicle/%s",
                tank.replace(" ", "%20")
        );
        return getFuelandConsumption(urlString, vehicleTexts);
    }

    private static List getText(String urlString, List vehicleTexts) throws IOException, ParserConfigurationException, SAXException {
        getBasicXML(urlString);
        NodeList textNodes = doc.getElementsByTagName("text");
        for (int i = 0; i < textNodes.getLength(); i++) {
            vehicleTexts.add(textNodes.item(i).getTextContent());
        }

        return vehicleTexts;
    }

    public static Map<String, String> getTextAndValue(String urlString, Map<String, String> vehicleTexts) throws IOException, ParserConfigurationException, SAXException {
        getBasicXML(urlString);
        NodeList textNodes = doc.getElementsByTagName("text");
        NodeList valueNodes = doc.getElementsByTagName("value");

        for (int i = 0; i < textNodes.getLength(); i++) {
            String text = textNodes.item(i).getTextContent();
            String value = valueNodes.item(i).getTextContent();
            vehicleTexts.put(text, value);
        }

        return vehicleTexts;
    }

    public static List<String> getFuelandConsumption(String urlString, List<String> vehicleTexts) throws IOException, ParserConfigurationException, SAXException {
        getBasicXML(urlString);
        NodeList fuelNodes = doc.getElementsByTagName("fuelType1");
        NodeList consumptionNodes = doc.getElementsByTagName("comb08U");

        for (int i = 0; i < fuelNodes.getLength(); i++) {
            String fuel = fuelNodes.item(i).getTextContent();
            String consumption = consumptionNodes.item(i).getTextContent();
            double conversion = Double.parseDouble(consumption);
            conversion = 235.2145 / conversion;
            consumption = String.format("%.2f", conversion);
            vehicleTexts.add(fuel);
            vehicleTexts.add(consumption);
        }

        return vehicleTexts;
    }

    public static void getBasicXML(String urlString) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/xml");

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        conn.disconnect();

        // Parse the XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(new java.io.ByteArrayInputStream(response.toString().getBytes()));
    }

}
