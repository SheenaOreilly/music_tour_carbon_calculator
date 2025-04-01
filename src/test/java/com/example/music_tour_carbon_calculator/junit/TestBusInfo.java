package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.calculator.BusInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBusInfo {

    @Test
    void testMiniBus() {
        String busType = "mini_bus";
        String emissions = BusInfo.getBusEmissions(busType);
        assertEquals("6.90", emissions);
    }

    @Test
    void testCamperVan() {
        String busType = "camper_van";
        String emissions = BusInfo.getBusEmissions(busType);
        assertEquals("8.00", emissions);
    }

    @Test
    void testCoach() {
        String busType = "coach";
        String emissions = BusInfo.getBusEmissions(busType);
        assertEquals("33.00", emissions);
    }

    @Test
    void testDoubleDecker() {
        String busType = "double_decker";
        String emissions = BusInfo.getBusEmissions(busType);
        assertEquals("39.20", emissions);
    }

    @Test
    void testInvalidBusType() {
        String busType = "unknown_bus";
        String emissions = BusInfo.getBusEmissions(busType);
        assertEquals("0.00", emissions);
    }

    @Test
    void testCase() {
        String busTypeLower = "mini_bus";
        String busTypeUpper = "MINI_BUS";

        String emissionsLower = BusInfo.getBusEmissions(busTypeLower);
        String emissionsUpper = BusInfo.getBusEmissions(busTypeUpper);

        assertEquals("6.90", emissionsLower);
        assertEquals("6.90", emissionsUpper);
    }

    @Test
    void testGetBusEmissionsEmptyString() {
        String busType = "";
        String emissions = BusInfo.getBusEmissions(busType);
        assertEquals("0.00", emissions);

        busType = null;
        emissions = BusInfo.getBusEmissions(busType);
        assertEquals("0.00", emissions);
    }
}
