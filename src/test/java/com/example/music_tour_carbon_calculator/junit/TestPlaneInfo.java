package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.calculator.PlaneInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class TestPlaneInfo {

    @Test
    void testGetAirports() {
        Map<String, String> airports = PlaneInfo.getAirports();

        assertNotNull(airports, "The airports map should not be null");
        assertFalse(airports.isEmpty(), "The airports map should not be empty");

        String expectedValue = "53.421:-6.270:DUBLIN";
        assertTrue(airports.containsKey("DUBLIN, IRELAND"), "The map should contain the specified key");
        assertEquals(expectedValue, airports.get("DUBLIN, IRELAND"), "The value should match the expected result");
    }

}