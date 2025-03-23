package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .build();
    }

    @Test
    void getAuthorities_ShouldReturnEmptySet() {
        // When
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void getAdmin_ShouldReturnCorrectValue() {
        // When & Then
        assertTrue(userDetails.getAdmin());

        // Test with false value
        UserDetailsImpl nonAdminUser = UserDetailsImpl.builder()
                .admin(false)
                .build();
        assertFalse(nonAdminUser.getAdmin());
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        // When & Then
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        // When & Then
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        // When & Then
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_ShouldReturnTrue() {
        // When & Then
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        // When & Then
        assertTrue(userDetails.equals(userDetails));
    }

    @Test
    void equals_ShouldReturnFalse_WhenNull() {
        // When & Then
        assertFalse(userDetails.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentClass() {
        // When & Then
        assertFalse(userDetails.equals(new Object()));
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameId() {
        // Given
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(1L)
                .username("other@test.com")
                .build();

        // When & Then
        assertTrue(userDetails.equals(otherUser));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        // Given
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("test@test.com")
                .build();

        // When & Then
        assertFalse(userDetails.equals(otherUser));
    }

    @Test
    void builder_ShouldCreateUserDetailsImpl() {
        // Given
        Long id = 1L;
        String username = "test@test.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password123";
        Boolean admin = true;

        // When
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .admin(admin)
                .build();

        // Then
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(password, user.getPassword());
        assertEquals(admin, user.getAdmin());
    }
}