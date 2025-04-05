package com.example.music_tour_carbon_calculator.controllers;

import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.overallTour;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import com.example.music_tour_carbon_calculator.objects.offsetCompanies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestOffsetController {

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private OffsetController offsetController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        offsetController = new OffsetController(firebaseService);
    }

    @Test
    public void testShowOffsets() {
        String userEmail = "test@example.com";
        List<tourObject> mockTours = new ArrayList<>();
        mockTours.add(new tourObject("Tour 1"));

        List<overallTour> mockOverTours = new ArrayList<>();
        mockOverTours.add(new overallTour("Tour 1", 300, 2, 45.0, false));

        when(session.getAttribute("userEmail")).thenReturn(userEmail);
        when(session.getAttribute("userTours")).thenReturn(mockTours);
        when(session.getAttribute("overallTours")).thenReturn(mockOverTours);

        String viewName = offsetController.showOffsets(session, model);

        verify(firebaseService).getAllTours(eq(userEmail), eq(session));
        verify(model).addAttribute(eq("overTours"), eq(mockOverTours));
        verify(model).addAttribute(eq("offsetCompaniesMap"), eq(offsetCompanies.getOffsetLinks()));
        assertEquals("offsets", viewName);
    }

    @Test
    public void testUpdateOffsetsInSession() {
        tourObject tour1 = new tourObject("Tour A");
        tourObject tour2 = new tourObject("Tour B");
        List<tourObject> userTours = Arrays.asList(tour1, tour2);

        overallTour ot1 = new overallTour("Tour A", 300, 2, 45.0, false);
        overallTour ot2 = new overallTour("Tour B", 300, 2, 45.0, false);
        List<overallTour> overTours = Arrays.asList(ot1, ot2);

        when(session.getAttribute("userTours")).thenReturn(userTours);
        when(session.getAttribute("overallTours")).thenReturn(overTours);

        List<String> checkedTours = List.of("Tour A");

        offsetController.showOffsets(session, model);
        offsetController.update(checkedTours);

        assertEquals(true, userTours.get(0).isOffset());
        assertEquals(false, userTours.get(1).isOffset());
        assertEquals(true, overTours.get(0).getOffset());
        assertEquals(false, overTours.get(1).getOffset());
    }

}
