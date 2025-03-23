package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.openclassrooms.starterjwt.mapper.UserMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @MockBean
    private UserMapper userMapper;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        // Initialisation des objets sans mock
        loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("test!1234");

        user = new User();
        user.setEmail("yoga@studio.com");
        user.setPassword("encoded-password");
        user.setAdmin(true);

        userDetails = UserDetailsImpl.builder()
                .username("yoga@studio.com")
                .password("test!1234")
                .build();
    }

    @Test
    void authenticateUser_Success() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt-token");
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        // When
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }

    @Test
    void registerUser_Success() {
        // Given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encoded-password");

        // When
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Then
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void registerUser_EmailAlreadyTaken() {
        // Given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // When
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Then
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void authenticateUser_InvalidCredentials() {
        // Given
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
    }

    @Test
    void registerUser_MissingFields() {
        // Given
        SignupRequest invalidRequest = new SignupRequest();
        invalidRequest.setEmail("test@test.com");
        invalidRequest.setLastName("Test");
        invalidRequest.setPassword("test!1234");
        invalidRequest.setFirstName(""); // Une chaîne vide au lieu de null
        
        when(userRepository.existsByEmail(invalidRequest.getEmail())).thenReturn(true); // Simuler un email déjà existant
        
        // When
        ResponseEntity<?> response = authController.registerUser(invalidRequest);
        
        // Then
        assertEquals(400, response.getStatusCodeValue());
    }
}