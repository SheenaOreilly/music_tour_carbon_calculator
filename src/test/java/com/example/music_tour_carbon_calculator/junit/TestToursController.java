package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.controllers.ToursController;
import com.example.music_tour_carbon_calculator.createTourBlock;
import com.example.music_tour_carbon_calculator.firebase.FirebaseService;
import com.example.music_tour_carbon_calculator.objects.tourObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

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
}