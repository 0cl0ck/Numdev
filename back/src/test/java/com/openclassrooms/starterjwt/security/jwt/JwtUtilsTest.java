package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    private static final String TEST_JWT_SECRET = "bezKoderSecretKey";
    private static final int TEST_JWT_EXPIRATION = 86400000;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", TEST_JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", TEST_JWT_EXPIRATION);
    }

    @Test
    void generateJwtToken_ShouldGenerateValidToken() {
        // Given
        String username = "test@test.com";
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        // When
        String token = jwtUtils.generateJwtToken(authentication);

        // Then
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals(username, jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenTokenIsInvalid() {
        // Given
        String invalidToken = "invalidToken";

        // When & Then
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenTokenIsExpired() {
        // Given
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -86400000); // -1 day
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        String expiredToken = jwtUtils.generateJwtToken(authentication);

        // When & Then
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenTokenIsEmpty() {
        // When & Then
        assertFalse(jwtUtils.validateJwtToken(""));
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenTokenIsMalformed() {
        // When & Then
        assertFalse(jwtUtils.validateJwtToken("malformed.token.here"));
    }

    @Test
    void getUserNameFromJwtToken_ShouldReturnUsername() {
        // Given
        String username = "test@test.com";
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtUtils.generateJwtToken(authentication);

        // When
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Then
        assertEquals(username, extractedUsername);
    }
}