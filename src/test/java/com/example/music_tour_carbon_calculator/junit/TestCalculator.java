package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.calculator.Calculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCalculator {

    @Test
    void testDiesel() {
        double distance = 100.0;
        String fuel = "diesel";
        double consumption = 8.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("21.46", emissions);
    }

    @Test
    void testPetrol() {
        double distance = 200.0;
        String fuel = "petrol";
        double consumption = 6.5;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("30.04", emissions);

        fuel = "premium gasoline";
        emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("30.04", emissions);

        fuel = "regular gasoline";
        emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("30.04", emissions);
    }

    @Test
    void testElectricity() {
        double distance = 150.0;
        String fuel = "electricity";
        double consumption = 15.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("5.73", emissions);
    }

    @Test
    void testInvalidFuel() {
        double distance = 50.0;
        String fuel = "unknownFuel";
        double consumption = 5.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("0.00", emissions);
    }

    @Test
    void testZeroDistance() {
        double distance = 0.0;
        String fuel = "diesel";
        double consumption = 8.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("0.00", emissions);
    }

    @Test
    void testZeroConsumption() {
        double distance = 100.0;
        String fuel = "diesel";
        double consumption = 0.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("0.00", emissions);
    }

    @Test
    void testLargeValues() {
        double distance = 1000000.0;
        String fuel = "petrol";
        double consumption = 15000.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("346650000.00", emissions);
    }

    @Test
    void testNullEmptyFuel() {
        double distance = 100.0;
        String fuel = null;
        double consumption = 8.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("0.00", emissions);

        fuel = "";
        emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("0.00", emissions);
    }

    @Test
    void testNegative() {
        double distance = -100.0;
        String fuel = "diesel";
        double consumption = 8.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("0.00", emissions);

        consumption = -2.0;
        distance = 100;

        emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("0.00", emissions);
    }

    @Test
    void testCapitol() {
        double distance = 100.0;
        String fuel = "PETROL";
        double consumption = 8.0;

        String emissions = Calculator.calculateCarbonEmissions(distance, fuel, consumption);
        assertEquals("18.49", emissions);
    }
}
