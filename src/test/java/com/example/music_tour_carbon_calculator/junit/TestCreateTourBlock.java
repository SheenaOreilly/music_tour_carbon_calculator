package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.createTourBlock;
import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.objects.overallTour;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestCreateTourBlock {

    @Test
    void testCreateBlock() {
        HttpSession mockSession = mock(HttpSession.class);

        TourData leg1 = new TourData("Trinity College Dublin", "Dalkey", "14.7", "16.43", "premium gasoline", "car", "5.58", "vxxXLManxSq2VBUAMO5n", "no", "0");
        TourData leg2 = new TourData("Cardiff", "Newcastle", "5419.49", "N/A", "N/A", "plane", "167.79", "DD8MfS6qYfuDGGee6yqI", "yes", "200");

        tourObject tour = new tourObject("Tour1");
        tour.add(leg1);
        tour.add(leg2);
        tour.setOffset(false);
        List<tourObject> userTours = new ArrayList<>();
        userTours.add(tour);

        createTourBlock.createBlock(userTours, mockSession);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(mockSession).setAttribute(eq("overallTours"), captor.capture());

        List<overallTour> overallTours = captor.getValue();
        assertEquals(1, overallTours.size());
        overallTour newTour = overallTours.get(0);
        assertEquals("Tour1", newTour.getTourName());
        assertEquals(1, newTour.getNoOfConcerts());
        assertEquals(200, newTour.getNoOfSeats());
        assertTrue(newTour.getModes().containsAll(Arrays.asList("plane", "car")));
        assertEquals(173.37, newTour.getCarbonEmissions(), 0.01);
    }
}
