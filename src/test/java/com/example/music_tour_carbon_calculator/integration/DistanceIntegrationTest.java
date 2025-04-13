package com.example.music_tour_carbon_calculator.integration;

import com.example.music_tour_carbon_calculator.calculator.Distance;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class DistanceIntegrationTest {

    private static final String ORIGIN = "New York, NY";
    private static final String DESTINATION = "Los Angeles, CA";
    private static final String TRANSPORT = "car";

    @Test
    void testCalculateDistance_RealAPI() throws IOException {
        double result = Distance.calculateDistance(ORIGIN, DESTINATION, TRANSPORT);

        assertTrue(result > 0, "Distance should be a positive number");

        assertTrue(result >= 4000 && result <= 6000, "Distance should be within expected range");
    }

    @Test
    void testCalculateDistance_OriginNotFound() throws IOException {
        String invalidOrigin = "Invalid Origin";
        double result = Distance.calculateDistance(invalidOrigin, DESTINATION, TRANSPORT);

        assertEquals(-1.0, result, "Distance should be -1.0 when origin is not found");
    }

    @Test
    void testCalculateDistance_DestinationNotFound() throws IOException {
        String invalidDestination = "Invalid Destination";
        double result = Distance.calculateDistance(ORIGIN, invalidDestination, TRANSPORT);

        assertEquals(-2.0, result, "Distance should be -2.0 when destination is not found");
    }

    @Test
    void testCalculateDistance_EmptyAddresses() throws IOException {
        String emptyOrigin = "";
        String emptyDestination = "";
        double result = Distance.calculateDistance(emptyOrigin, emptyDestination, TRANSPORT);

        assertEquals(0.0, result, "Distance should be 0.0 if both origin and destination are empty");
    }
}
