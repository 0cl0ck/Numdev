package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        
        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());
    }

    @Test
    void findAll_ShouldReturnAllSessions() {
        // Given
        List<Session> sessions = List.of(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // When
        List<Session> result = sessionService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sessionRepository).findAll();
    }

    @Test
    void participate_ShouldAddUserToSession() {
        // Given
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // When
        sessionService.participate(1L, 1L);

        // Then
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_ShouldThrowNotFoundException_WhenSessionNotFound() {
        // Given
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    @Test
    void participate_ShouldThrowBadRequestException_WhenUserAlreadyParticipates() {
        // Given
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    @Test
    void noLongerParticipate_ShouldRemoveUserFromSession() {
        // Given
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // When
        sessionService.noLongerParticipate(1L, 1L);

        // Then
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void getById_ShouldReturnSession() {
        // Given
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // When
        Session result = sessionService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void create_ShouldReturnCreatedSession() {
        // Given
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // When
        Session result = sessionService.create(session);

        // Then
        assertNotNull(result);
        verify(sessionRepository).save(session);
    }

    @Test
    void update_ShouldReturnUpdatedSession() {
        // Given
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // When
        Session result = sessionService.update(1L, session);

        // Then
        assertNotNull(result);
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_ShouldDeleteSession() {
        // When
        sessionService.delete(1L);

        // Then
        verify(sessionRepository).deleteById(1L);
    }
}