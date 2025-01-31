package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();
    }

    @Test
    void findById_ExistingUser_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User result = userService.findById(1L);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_NonExistingUser_ReturnsNull() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        User result = userService.findById(999L);
        assertNull(result);
        verify(userRepository).findById(999L);
    }

    @Test
    void delete_ExistingUser_DeletesSuccessfully() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void delete_NonExistingUser_NoException() {
        Long userId = 999L;
        doNothing().when(userRepository).deleteById(userId);
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }
}
