package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private Session testSession;
    private SessionDto testSessionDto;

    @BeforeEach
    void setUp() {
        // Création d'une session de test
        testSession = new Session();
        testSession.setId(1L);
        testSession.setName("Test Session");
        testSession.setDate(new Date());
        testSession.setDescription("Test Description");
        
        // Création d'un DTO de session de test
        testSessionDto = new SessionDto();
        testSessionDto.setId(1L);
        testSessionDto.setName("Test Session");
        testSessionDto.setDate(new Date());
        testSessionDto.setDescription("Test Description");
    }

    @Test
    void findById_ReturnsSession() {
        // Préparation
        when(sessionService.getById(1L)).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        // Exécution
        ResponseEntity<?> response = sessionController.findById("1");

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testSessionDto, response.getBody());
        verify(sessionService).getById(1L);
    }

    @Test
    void findById_WithInvalidId_ReturnsBadRequest() {
        // Exécution
        ResponseEntity<?> response = sessionController.findById("invalid");

        // Vérification
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void findAll_ReturnsAllSessions() {
        // Préparation
        List<Session> sessions = Arrays.asList(testSession);
        List<SessionDto> sessionDtos = Arrays.asList(testSessionDto);
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Exécution
        ResponseEntity<?> response = sessionController.findAll();

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDtos, response.getBody());
        verify(sessionService).findAll();
    }

    @Test
    void create_CreatesSession() {
        // Préparation
        when(sessionMapper.toEntity(testSessionDto)).thenReturn(testSession);
        when(sessionService.create(testSession)).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        // Exécution
        ResponseEntity<?> response = sessionController.create(testSessionDto);

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testSessionDto, response.getBody());
        verify(sessionService).create(testSession);
    }

    @Test
    void update_UpdatesSession() {
        // Préparation
        when(sessionMapper.toEntity(testSessionDto)).thenReturn(testSession);
        when(sessionService.update(1L, testSession)).thenReturn(testSession);
        when(sessionMapper.toDto(testSession)).thenReturn(testSessionDto);

        // Exécution
        ResponseEntity<?> response = sessionController.update("1", testSessionDto);

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testSessionDto, response.getBody());
        verify(sessionService).update(1L, testSession);
    }

    @Test
    void save_DeletesSession() {
        // Préparation
        when(sessionService.getById(1L)).thenReturn(testSession);

        // Exécution
        ResponseEntity<?> response = sessionController.save("1");

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).delete(1L);
    }

    @Test
    void participate_AddsUserToSession() {
        // Exécution
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).participate(1L, 1L);
    }

    @Test
    void noLongerParticipate_RemovesUserFromSession() {
        // Exécution
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        // Vérification
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).noLongerParticipate(1L, 1L);
    }
}