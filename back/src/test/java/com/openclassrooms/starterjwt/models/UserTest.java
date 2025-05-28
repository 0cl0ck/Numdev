package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testUserBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        
        // When
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.isAdmin()).isFalse();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testUserSettersAndGetters() {
        // Given
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        // When
        user.setId(2L);
        user.setEmail("jane@test.com");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setPassword("newpassword");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // Then
        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getEmail()).isEqualTo("jane@test.com");
        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getPassword()).isEqualTo("newpassword");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testUserEqualsAndHashCode() {
        // Given
        User user1 = User.builder().id(1L).email("test@test.com").firstName("John").lastName("Doe").password("pass").admin(false).build();
        User user2 = User.builder().id(1L).email("different@test.com").firstName("Jane").lastName("Smith").password("diff").admin(true).build();
        User user3 = User.builder().id(2L).email("test@test.com").firstName("John").lastName("Doe").password("pass").admin(false).build();

        // Then
        assertThat(user1).isEqualTo(user2); // Same ID
        assertThat(user1).isNotEqualTo(user3); // Different ID
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode()); // Same ID = same hash
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode()); // Different ID = different hash
    }

    @Test
    void testUserToString() {
        // Given
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        // When
        String toString = user.toString();

        // Then
        assertThat(toString).contains("User");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("email=test@test.com");
        assertThat(toString).contains("firstName=John");
        assertThat(toString).contains("lastName=Doe");
    }

    @Test
    void testUserChainedAccessors() {
        // Given & When
        User user = new User()
                .setId(1L)
                .setEmail("chain@test.com")
                .setFirstName("Chain")
                .setLastName("Test")
                .setPassword("chainpass")
                .setAdmin(true);

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("chain@test.com");
        assertThat(user.getFirstName()).isEqualTo("Chain");
        assertThat(user.getLastName()).isEqualTo("Test");
        assertThat(user.getPassword()).isEqualTo("chainpass");
        assertThat(user.isAdmin()).isTrue();
    }
}
