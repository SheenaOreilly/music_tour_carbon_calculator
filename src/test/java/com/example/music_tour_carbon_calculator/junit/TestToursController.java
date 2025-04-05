package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.controllers.ToursController;
import com.example.music_tour_carbon_calculator.createTourBlock;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.TourData;
import com.example.music_tour_carbon_calculator.objects.overallTour;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestToursController {

    private ToursController toursController;

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private createTourBlock createTourBlock;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        toursController = new ToursController(firebaseService);
        session = new MockHttpSession();
    }

    @Test
    void testSetUserEmail() {
        String email = "test@example.com";
        Map<String, String> request = Map.of("email", email);

        doNothing().when(firebaseService).getAllTours(eq(email), any(MockHttpSession.class));

        session.setAttribute("userTours", List.of(new tourObject("Ireland Tour")));
        session.setAttribute("overallTours", List.of());

        var response = toursController.setUserEmail(request, session);
        assertEquals("Email set in session", response.getBody());
        assertEquals(email, session.getAttribute("userEmail"));

        verify(firebaseService, times(1)).getAllTours(eq(email), any(MockHttpSession.class));
    }

    @Test
    void testLogout() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "test@example.com");
        ToursController controller = new ToursController(null);
        ResponseEntity<?> response = controller.logout(session);

        assertEquals("Session cleared", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(session.isInvalid(), "The session should be invalidated after logout.");
    }

    @Test
    void testShowSpecificTour() {
        String tourName = "MyTour";
        tourObject tour = new tourObject("MyTour");
        tour.add(new TourData());

        tourObject tour2 = new tourObject("MyTour2");
        tour2.add(new TourData());
        List<tourObject> mockUserTours = List.of(
                tour,
                tour2
        );
        session.setAttribute("userTours", mockUserTours);

        Model model = mock(Model.class);

        String viewName = toursController.showSpecificTour(tourName, session, model);
        assertEquals("specificTour", viewName);
        ArgumentCaptor<List<TourData>> captor = ArgumentCaptor.forClass(List.class);
        verify(model, times(1)).addAttribute(eq("tourName"), eq(tourName));
        verify(model, times(1)).addAttribute(eq("specificToursData"), captor.capture());

        List<TourData> capturedTourData = captor.getValue();
        assertEquals(1, capturedTourData.size());
        assertNotNull(capturedTourData);
    }
    @Test
    void testShowNewTour() {
        String mockTourName = "NewTour";
        List<TourData> mockCurrentTour = List.of(new TourData());
        session.setAttribute("tourName", mockTourName);
        session.setAttribute("currentTour", mockCurrentTour);

        Model model = mock(Model.class);

        String viewName = toursController.showNewTour(session, model);
        assertEquals("newTour", viewName);
        verify(model, times(1)).addAttribute("tourName", mockTourName);
        verify(model, times(1)).addAttribute("currentLegs", mockCurrentTour);
    }

}