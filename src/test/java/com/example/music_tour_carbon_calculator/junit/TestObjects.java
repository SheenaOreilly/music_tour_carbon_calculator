package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.objects.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestObjects {

    @Test
    void TestTourData() {
        // create with values
        TourData tourData = new TourData("Dublin", "Cardiff", "5419.49", "N/A", "premium gasoline", "car", "167.79", "ABCDEF", "yes", "200");
        assertEquals("Dublin", tourData.getDeparture());
        assertEquals("Cardiff", tourData.getArrival());
        assertEquals("5419.49", tourData.getDistance());
        assertEquals("N/A", tourData.getConsumption());
        assertEquals("premium gasoline", tourData.getFuel());

        // to string test
        String result = tourData.toString();
        String expected = "TourData{departure='Dublin', arrival='Cardiff', distance='5419.49', consumption='N/A', fuel='premium gasoline', carbonEmissions='167.79'}";
        assertEquals(expected, result);

        // create without values
        tourData = new TourData();
        tourData.setDeparture("London");
        tourData.setArrival("Newcastle");
        tourData.setDistance("500");
        tourData.setConsumption("12.5");
        tourData.setFuel("diesel");
        tourData.setCarbonEmissions("150");
        tourData.setDocumentId("AB1234");
        tourData.setConcert("no");
        tourData.setSeats("100");
        tourData.setVehicle("bus");
        assertEquals("150", tourData.getCarbonEmissions());
        assertEquals("AB1234", tourData.getDocumentId());
        assertEquals("no", tourData.getConcert());
        assertEquals("100", tourData.getSeats());
        assertEquals("bus", tourData.getVehicle());
    }

    @Test
    void TestTourObject(){
        // tour name
        String tourName = "Tour1";
        tourObject tour = new tourObject(tourName);
        assertEquals(tourName, tour.tourName);
        assertNotNull(tour.legsOfTour);
        assertTrue(tour.legsOfTour.isEmpty());

        // tour legs
        TourData leg1 = new TourData("Dublin", "Cardiff", "100", "20", "diesel", "car", "100", "ID1", "yes", "100");
        TourData leg2 = new TourData("Cardiff", "London", "150", "30", "gasoline", "car", "150", "ID2", "yes", "150");
        tour.add(leg1);
        tour.add(leg2);
        assertEquals(2, tour.legsOfTour.size());
        assertTrue(tour.legsOfTour.contains(leg1));
        assertTrue(tour.legsOfTour.contains(leg2));

        // offset
        assertFalse(tour.isOffset());
        tour.setOffset(true);
        assertTrue(tour.isOffset());
    }

    @Test
    void TestOverallTour() {
        // constructor
        String tourName = "Tour1";
        int noOfSeats = 200;
        int noOfConcerts = 5;
        double carbonEmissions = 500.0;
        boolean offset = true;
        overallTour tour = new overallTour(tourName, noOfSeats, noOfConcerts, carbonEmissions, offset);
        assertEquals(tourName, tour.getTourName());
        assertEquals(noOfSeats, tour.getNoOfSeats());
        assertEquals(noOfConcerts, tour.getNoOfConcerts());
        assertEquals(carbonEmissions, tour.getCarbonEmissions(), 0.001);
        assertTrue(tour.getOffset());

        // modes
        tour.addModes("plane");
        tour.addModes("car");
        assertEquals(2, tour.getModes().size());
        assertTrue(tour.getModes().contains("plane"));
        assertTrue(tour.getModes().contains("car"));

        // seats
        tour.updateSeats(50);
        assertEquals(250, tour.getNoOfSeats());

        // carbon
        tour.updateCarbon(100.0);
        assertEquals(600.0, tour.getCarbonEmissions(), 0.001);

        // concerts
        tour.updateConcerts(2);
        assertEquals(7, tour.getNoOfConcerts());

        // rank
        tour.setRank(1);
        assertEquals(1, tour.getRank());

        // offset
        tour.setOffset(false);
        assertFalse(tour.getOffset());
    }

    @Test
    void TestOffsetCompanies(){
        // values
        assertEquals(6.0 / 240.0, offsetCompanies.atmosfair, 0.0001);
        assertEquals(18.15 / 1000, offsetCompanies.carbonBalanced, 0.0001);
        assertEquals(26.0 / 1000, offsetCompanies.myClimate, 0.0001);
        assertEquals(17.2 / 1000, offsetCompanies.nativeEco, 0.0001);
        assertEquals(15.28 / 1000, offsetCompanies.sustainableTravelInternational, 0.0001);
        assertEquals(16.77 / 1000, offsetCompanies.planetair, 0.0001);

        // links
        Map<String, String> offsetLinks = offsetCompanies.getOffsetLinks();
        assertEquals(6, offsetLinks.size());
        assertTrue(offsetLinks.containsKey("Atmosfair"));
        assertTrue(offsetLinks.containsKey("Carbon Balanced"));
        assertTrue(offsetLinks.containsKey("My Climate"));
        assertTrue(offsetLinks.containsKey("Sustainable Travel International"));
        assertTrue(offsetLinks.containsKey("Planetair"));
        assertTrue(offsetLinks.containsKey("Native Energy"));
        assertTrue(offsetLinks.get("Atmosfair").contains("https://www.atmosfair.de/en/donate/"));
        assertTrue(offsetLinks.get("Carbon Balanced").contains("https://www.worldlandtrust.org/carbon-calculator/individual/fixed/fixed-quick-offset/"));
        assertTrue(offsetLinks.get("My Climate").contains("https://co2.myclimate.org/en/contribution_calculators/new"));
    }

    @Test
    void TestCarObject(){
        String nickname = "MyCar";
        String consumption = "15";
        String fuel = "Gasoline";
        String documentId = "123ABC";
        carObject car = new carObject(nickname, consumption, fuel, documentId);
        assertEquals(nickname, car.getNickname());
        assertEquals(consumption, car.getConsumption());
        assertEquals(fuel, car.getFuel());
        assertEquals(documentId, car.getDocumentId());
    }
}