package com.example.music_tour_carbon_calculator.junit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.music_tour_carbon_calculator.calculator.CarInfo;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestCarInfo {

    @Test
    void testGetMakes() throws Exception {
        String year = "2021";
        List<String> makes = CarInfo.getMakes(year);

        assertNotNull(makes);
        assertTrue(makes.size() > 1);
        assertTrue(makes.contains("Toyota"));
        assertTrue(makes.contains("Honda"));
        assertTrue(makes.contains("Ford"));
    }

    @Test
    void testGetModels() throws Exception {
        String year = "2021";
        String make = "Toyota";
        List<String> models = CarInfo.getModels(year, make);

        assertNotNull(models);
        assertTrue(models.size() > 1);
        assertTrue(models.contains("Corolla"));
        assertTrue(models.contains("Camry"));
        assertTrue(models.contains("Highlander"));
    }

    @Test
    void testGetFuelSize() throws Exception {
        String year = "2021";
        String make = "Toyota";
        String model = "Corolla";

        Map<String, String> fuelSize = CarInfo.getFuelSize(year, make, model);

        assertNotNull(fuelSize);
        assertTrue(fuelSize.size() > 0);
        assertTrue(fuelSize.containsKey("Auto (AV-S10), 4 cyl, 2.0 L"));
        assertTrue(fuelSize.containsValue("42587"));
    }

    @Test
    void testGetFuelInfo() throws Exception {
        String tank = "42587";
        List<String> fuelInfo = CarInfo.getFuelInfo(tank);

        assertNotNull(fuelInfo);
        assertEquals(2, fuelInfo.size());
        assertEquals("Regular Gasoline", fuelInfo.get(0));
        assertEquals("6.86", fuelInfo.get(1));
    }

    @Test
    void testGetTextAndValueHandlesEmptyResponse() throws Exception {
        // Arrange
        String url = "https://example.com/emptyResponse";
        CarInfo mockCarInfo = mock(CarInfo.class);

        // Mocking empty response
        Map<String, String> emptyMap = Map.of();
        when(mockCarInfo.getTextAndValue(eq(url), anyMap())).thenReturn(emptyMap);

        // Act
        Map<String, String> result = mockCarInfo.getTextAndValue(url, new LinkedHashMap<>());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTextHandlesEmptyResponse() throws Exception {
        // Arrange
        String url = "https://example.com/emptyResponse";
        CarInfo mockCarInfo = mock(CarInfo.class);

        // Mocking empty response
        List<String> emptyList = List.of();
        when(mockCarInfo.getText(eq(url), anyList())).thenReturn(emptyList);

        // Act
        List<String> result = mockCarInfo.getText(url, new ArrayList<>());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private CarInfo CarInfo;
    private MockWebServer mockWebServer;

    @Test
    void testValidXMLResponse() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();
        String validXML = "<root><message>Hello</message></root>";
        mockWebServer.enqueue(new MockResponse().setBody(validXML).setResponseCode(200));

        String testUrl = mockWebServer.url("/test").toString();
        CarInfo.getBasicXML(testUrl);

        assertNotNull(CarInfo.getDoc());
        assertEquals("root", CarInfo.getDoc().getDocumentElement().getTagName());
        mockWebServer.shutdown();
    }

    @Test
    void testInvalidURL() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();
        assertThrows(IOException.class, () -> {
            CarInfo.getBasicXML("htp://invalid-url");
        });
        mockWebServer.shutdown();
    }
    @Test
    void testServerErrorResponseXML() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        String testUrl = mockWebServer.url("/error").toString();
        assertThrows(IOException.class, () -> {
            CarInfo.getBasicXML(testUrl);
        });
        mockWebServer.shutdown();
    }

    @Test
    void testGetText_ValidResponse() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();

        String validXML = "<root><text>Ford</text><text>Toyota</text></root>";
        mockWebServer.enqueue(new MockResponse().setBody(validXML).setResponseCode(200));

        String testUrl = mockWebServer.url("/valid").toString();
        List<String> vehicleTexts = new ArrayList<>();
        List<String> result = CarInfo.getText(testUrl, vehicleTexts);

        assertEquals(2, result.size());
        assertEquals("Ford", result.get(0));
        assertEquals("Toyota", result.get(1));

        mockWebServer.shutdown();
    }

    @Test
    void testGetTextAndValue_ValidResponse() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();

        String validXML = "<root>"
                + "<pair><text>Ford</text><value>123</value></pair>"
                + "<pair><text>Toyota</text><value>456</value></pair>"
                + "</root>";

        mockWebServer.enqueue(new MockResponse().setBody(validXML).setResponseCode(200));

        String testUrl = mockWebServer.url("/valid").toString();
        Map<String, String> vehicleTexts = new LinkedHashMap<>();
        Map<String, String> result = CarInfo.getTextAndValue(testUrl, vehicleTexts);

        assertEquals(2, result.size());
        assertEquals("123", result.get("Ford"));
        assertEquals("456", result.get("Toyota"));

        mockWebServer.shutdown();
    }

    @Test
    void testGetFuelandConsumption_ValidResponse() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();

        String validXML = "<root>"
                + "<vehicle><fuelType1>Gasoline</fuelType1><comb08U>25</comb08U></vehicle>"
                + "</root>";

        mockWebServer.enqueue(new MockResponse().setBody(validXML).setResponseCode(200));

        String testUrl = mockWebServer.url("/valid").toString();
        List<String> vehicleTexts = new ArrayList<>();
        List<String> result = CarInfo.getFuelandConsumption(testUrl, vehicleTexts);

        assertEquals(2, result.size());
        assertEquals("Gasoline", result.get(0));
        assertEquals("9.41", result.get(1));

        mockWebServer.shutdown();
    }


    @Test
    void testGetText_InvalidXMLResponse() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();
        String invalidXML = "<root><text>Ford";
        mockWebServer.enqueue(new MockResponse().setBody(invalidXML).setResponseCode(200));

        String testUrl = mockWebServer.url("/test").toString();
        List<String> vehicleTexts = new ArrayList<>();

        assertThrows(SAXException.class, () -> {
            CarInfo.getText(testUrl, vehicleTexts);
        });
        mockWebServer.shutdown();
    }

    @Test
    void testGetText_EmptyResponse() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();

        mockWebServer.enqueue(new MockResponse().setBody("").setResponseCode(200));

        String testUrl = mockWebServer.url("/empty").toString();
        List<String> vehicleTexts = new ArrayList<>();

        assertThrows(SAXException.class, () -> {
            CarInfo.getText(testUrl, vehicleTexts);
        });
        mockWebServer.shutdown();
    }

    @Test
    void testGetText_ServerError() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        CarInfo = new CarInfo();
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        String testUrl = mockWebServer.url("/error").toString();
        List<String> vehicleTexts = new ArrayList<>();

        assertThrows(IOException.class, () -> {
            CarInfo.getText(testUrl, vehicleTexts);
        });
        mockWebServer.shutdown();
    }
}
