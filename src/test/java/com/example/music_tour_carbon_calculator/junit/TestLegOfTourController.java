package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.controllers.LegOfTourController;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestLegOfTourController {

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    private LegOfTourController legOfTourController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        legOfTourController = new LegOfTourController(firebaseService);
    }

    @Test
    public void testShowNewTour() {
        String view = legOfTourController.showNewTour();
        assertEquals("SelectTour", view);
    }

    @Test
    public void testSelectNewTour() {
        String tourName = "SummerTour2025";
        List<tourObject> userTours = new ArrayList<>();
        userTours.add(new tourObject("WinterTour"));
        userTours.add(new tourObject("SummerTour2025"));

        when(session.getAttribute("userTours")).thenReturn(userTours);

        String redirect = legOfTourController.selectNewTour(tourName, session, redirectAttributes);

        verify(session).setAttribute(eq("tourName"), eq(tourName));
        verify(session).setAttribute(eq("currentTour"), any(tourObject.class));
        assertEquals("redirect:/newTour", redirect);
    }

    @Test
    public void testSelectNewTour_InvalidTourNameWithSlash() {
        String tourName = "Summer/Tour2025";

        String redirect = legOfTourController.selectNewTour(tourName, session, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("alertMessage"), contains("Tour Name can not include"));
        assertEquals("redirect:/SelectTour", redirect);
    }
}

