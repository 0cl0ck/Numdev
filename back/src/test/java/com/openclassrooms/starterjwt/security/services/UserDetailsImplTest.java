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
                .admin(false)
                .password("password")
                .build();
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testGetPassword() {
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void testGetUsername() {
        assertEquals("test@test.com", userDetails.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testGetId() {
        assertEquals(1L, userDetails.getId());
    }

    @Test
    void testGetFirstName() {
        assertEquals("John", userDetails.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals("Doe", userDetails.getLastName());
    }

    @Test
    void testGetAdmin() {
        assertFalse(userDetails.getAdmin());
    }

    @Test
    void testEquals() {
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertEquals(userDetails, userDetails2);
    }

    @Test
    void testNotEquals() {
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(2L)
                .username("different@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .admin(true)
                .password("differentPassword")
                .build();

        assertNotEquals(userDetails, userDetails2);
    }

    @Test
    void testEqualsSameInstance() {
        // Test la branche "this == o"
        assertEquals(userDetails, userDetails);
    }

    @Test
    void testEqualsWithNull() {
        // Test la branche "o == null"
        assertNotEquals(userDetails, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Test la branche "getClass() != o.getClass()"
        String differentClassObject = "This is a string";
        assertNotEquals(userDetails, differentClassObject);
    }

    @Test
    void testEqualsWithNullId() {
        // Test avec id null
        UserDetailsImpl userDetailsWithNullId = UserDetailsImpl.builder()
                .id(null)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        UserDetailsImpl anotherUserDetailsWithNullId = UserDetailsImpl.builder()
                .id(null)
                .username("different@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .admin(true)
                .password("differentPassword")
                .build();

        // Deux objets avec id null devraient être égaux selon la logique equals
        assertEquals(userDetailsWithNullId, anotherUserDetailsWithNullId);
    }

    @Test
    void testNotEqualsNullIdWithNonNullId() {
        // Test id null vs id non null
        UserDetailsImpl userDetailsWithNullId = UserDetailsImpl.builder()
                .id(null)
                .username("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertNotEquals(userDetailsWithNullId, userDetails);
        assertNotEquals(userDetails, userDetailsWithNullId);
    }
}
