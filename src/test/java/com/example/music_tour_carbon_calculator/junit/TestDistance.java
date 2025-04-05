package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.calculator.Distance;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestDistance {

    private static final String VALID_RESPONSE = """
        {
            "rows": [{
                "elements": [{
                    "status": "OK",
                    "distance": { "text": "12.5 km", "value": 12500 }
                }]
            }]
        }""";

    private static final String ORIGIN_NOT_FOUND_RESPONSE = """
        {
            "origin_addresses": [""],
            "destination_addresses": ["Valid Destination"],
            "rows": [{
                "elements": [{
                    "status": "NOT_FOUND"
                }]
            }]
        }""";

    private static final String DESTINATION_NOT_FOUND_RESPONSE = """
        {
            "origin_addresses": ["Valid Origin"],
            "destination_addresses": [""],
            "rows": [{
                "elements": [{
                    "status": "NOT_FOUND"
                }]
            }]
        }""";

    private static final String ZERO_RESULTS_RESPONSE = """
        {
            "rows": [{
                "elements": [{
                    "status": "ZERO_RESULTS"
                }]
            }]
        }""";

    private static final String MALFORMED_RESPONSE = "{}";

    private static final String ORIGIN = "Dublin, Ireland";
    private static final String DESTINATION = "Cork, Ireland";
    private static final String TRANSPORT = "car";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testValidResponse() throws IOException {
        try (MockedStatic<Distance> mockedDistance = Mockito.mockStatic(Distance.class, Mockito.CALLS_REAL_METHODS)) {
            mockedDistance.when(() -> Distance.getJsonObject(Mockito.anyString()))
                    .thenReturn(new JSONObject(VALID_RESPONSE));

            double result = Distance.calculateDistance(ORIGIN, DESTINATION, TRANSPORT);
            assertEquals(12.5, result, 0.01);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testOriginNotFound() throws IOException, JSONException {
        try (MockedStatic<Distance> mockedDistance = Mockito.mockStatic(Distance.class, Mockito.CALLS_REAL_METHODS)) {
            mockedDistance.when(() -> Distance.getJsonObject(Mockito.anyString()))
                    .thenReturn(new JSONObject(ORIGIN_NOT_FOUND_RESPONSE));

            double result = Distance.calculateDistance(ORIGIN, DESTINATION, TRANSPORT);
            assertEquals(-1.0, result);
        }
    }

    @Test
    void testDestinationNotFound() throws IOException {
        try (MockedStatic<Distance> mockedDistance = Mockito.mockStatic(Distance.class, Mockito.CALLS_REAL_METHODS)) {
            mockedDistance.when(() -> Distance.getJsonObject(Mockito.anyString()))
                    .thenReturn(new JSONObject(DESTINATION_NOT_FOUND_RESPONSE));

            double result = Distance.calculateDistance(ORIGIN, DESTINATION, TRANSPORT);
            assertEquals(-2.0, result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testZeroResults() throws IOException, JSONException {
        try (MockedStatic<Distance> mockedDistance = Mockito.mockStatic(Distance.class, Mockito.CALLS_REAL_METHODS)) {
            mockedDistance.when(() -> Distance.getJsonObject(Mockito.anyString()))
                    .thenReturn(new JSONObject(ZERO_RESULTS_RESPONSE));

            double result = Distance.calculateDistance(ORIGIN, DESTINATION, TRANSPORT);
            assertEquals(0.0, result);
        }
    }

    @Test
    void testMalformedResponse() throws JSONException {
        try (MockedStatic<Distance> mockedDistance = Mockito.mockStatic(Distance.class, Mockito.CALLS_REAL_METHODS)) {
            mockedDistance.when(() -> Distance.getJsonObject(Mockito.anyString()))
                    .thenReturn(new JSONObject(MALFORMED_RESPONSE));

            assertThrows(Exception.class, () -> Distance.calculateDistance(ORIGIN, DESTINATION, TRANSPORT));
        }
    }

    @Test
    void testCalculateDistance_ApiException() throws IOException {
        try (MockedStatic<Distance> mockedDistance = Mockito.mockStatic(Distance.class, Mockito.CALLS_REAL_METHODS)) {
            mockedDistance.when(() -> Distance.getJsonObject(Mockito.anyString()))
                    .thenThrow(new IOException("API connection error"));

            assertThrows(IOException.class, () -> Distance.calculateDistance(ORIGIN, DESTINATION, TRANSPORT));
        }
    }
}
