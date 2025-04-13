package com.example.music_tour_carbon_calculator.junit;

import com.example.music_tour_carbon_calculator.firebase.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestFirebaseAuthService {

    @Test
    void testVerifyTokenWithValidToken() throws FirebaseAuthException {
        FirebaseToken mockToken = mock(FirebaseToken.class);
        try (MockedStatic<FirebaseAuth> firebaseAuthMock = mockStatic(FirebaseAuth.class)) {
            FirebaseAuth mockAuth = mock(FirebaseAuth.class);
            when(mockAuth.verifyIdToken("validToken")).thenReturn(mockToken);
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockAuth);

            FirebaseToken result = FirebaseAuthService.verifyToken("validToken");
            assertNotNull(result);
            assertEquals(mockToken, result);
        }
    }
}
