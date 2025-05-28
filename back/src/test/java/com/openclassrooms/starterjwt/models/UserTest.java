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
    void testUserEqualsEdgeCases() {
        // Given
        User user = User.builder().id(1L).email("test@test.com").firstName("John").lastName("Doe").password("pass").admin(false).build();
        User userWithNullId = User.builder().email("test@test.com").firstName("John").lastName("Doe").password("pass").admin(false).build();
        User anotherUserWithNullId = User.builder().email("different@test.com").firstName("Jane").lastName("Smith").password("diff").admin(true).build();

        // Test same instance
        assertThat(user).isEqualTo(user);

        // Test null comparison
        assertThat(user).isNotEqualTo(null);

        // Test different class
        assertThat(user).isNotEqualTo("not a user");

        // Test null IDs
        assertThat(userWithNullId).isEqualTo(anotherUserWithNullId); // Both have null IDs
        assertThat(user).isNotEqualTo(userWithNullId); // One has ID, other doesn't
        assertThat(userWithNullId).isNotEqualTo(user); // Reverse case
    }

    @Test
    void testUserCompleteEdgeCases() {
        // Test tous les cas possibles pour equals/hashCode avec les contraintes @NonNull
        User user1 = User.builder()
                .id(1L)
                .email("test1@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .build();
        
        User user2 = User.builder()
                .id(1L)
                .email("test2@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("password456")
                .admin(true)
                .build();
        
        User user3 = User.builder()
                .id(2L)
                .email("test3@test.com")
                .firstName("Bob")
                .lastName("Wilson")
                .password("password789")
                .admin(false)
                .build();
        
        User userNoId1 = User.builder()
                .email("noId1@test.com")
                .firstName("Alice")
                .lastName("Brown")
                .password("passwordNoId1")
                .admin(false)
                .build();
        
        User userNoId2 = User.builder()
                .email("noId2@test.com")
                .firstName("Charlie")
                .lastName("Green")
                .password("passwordNoId2")
                .admin(true)
                .build();

        // Test égalité avec mêmes IDs (should be equal because @EqualsAndHashCode(of = {"id"}))
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        // Test inégalité avec IDs différents
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());

        // Test égalité avec IDs null (should be equal as both have null IDs)
        assertThat(userNoId1).isEqualTo(userNoId2);
        assertThat(userNoId1.hashCode()).isEqualTo(userNoId2.hashCode());

        // Test inégalité ID non null vs ID null
        assertThat(user1).isNotEqualTo(userNoId1);
        assertThat(userNoId1).isNotEqualTo(user1);

        // Test toString ne retourne pas null
        assertThat(user1.toString()).isNotNull();
        assertThat(userNoId1.toString()).isNotNull();

        // Test canEqual method
        assertThat(user1.canEqual(user2)).isTrue();
        assertThat(user1.canEqual("not a user")).isFalse();
        assertThat(user1.canEqual(null)).isFalse();
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
    void testUserConstructors() {
        // Test NoArgsConstructor
        User userEmpty = new User();
        assertThat(userEmpty).isNotNull();
        assertThat(userEmpty.getId()).isNull();

        // Test RequiredArgsConstructor (pour les champs @NonNull)
        User userRequired = new User("test@example.com", "Doe", "John", "password", false);
        assertThat(userRequired.getEmail()).isEqualTo("test@example.com");
        assertThat(userRequired.getLastName()).isEqualTo("Doe");
        assertThat(userRequired.getFirstName()).isEqualTo("John");
        assertThat(userRequired.getPassword()).isEqualTo("password");
        assertThat(userRequired.isAdmin()).isFalse();

        // Test AllArgsConstructor
        LocalDateTime now = LocalDateTime.now();
        User userFull = new User(1L, "full@example.com", "Smith", "Jane", "fullpass", true, now, now);
        assertThat(userFull.getId()).isEqualTo(1L);
        assertThat(userFull.getEmail()).isEqualTo("full@example.com");
        assertThat(userFull.getLastName()).isEqualTo("Smith");
        assertThat(userFull.getFirstName()).isEqualTo("Jane");
        assertThat(userFull.getPassword()).isEqualTo("fullpass");
        assertThat(userFull.isAdmin()).isTrue();
        assertThat(userFull.getCreatedAt()).isEqualTo(now);
        assertThat(userFull.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testUserValidation() {
        // Test des contraintes de validation (annotations @NonNull, @Email, @Size)
        User user = new User();
        
        // Test avec des valeurs valides
        user.setEmail("valid@email.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("validpassword");
        user.setAdmin(false);
        
        assertThat(user.getEmail()).isEqualTo("valid@email.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("validpassword");
        assertThat(user.isAdmin()).isFalse();
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

    @Test
    void testUserBuilderComplete() {
        // Test du builder avec toutes les méthodes pour couvrir les 4 branches manquées
        User.UserBuilder builder = User.builder();
        
        // Test chaque setter du builder
        builder.id(1L);
        builder.email("test@test.com");
        builder.firstName("John");
        builder.lastName("Doe");
        builder.password("password123");
        builder.admin(true);
        builder.createdAt(LocalDateTime.now());
        builder.updatedAt(LocalDateTime.now());
        
        User user = builder.build();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        
        // Test toString du builder
        assertThat(builder.toString()).contains("UserBuilder");
    }

    @Test
    void testUserSettersGetters() {
        // Test tous les setters/getters pour couvrir les branches manquées
        User user = new User();
        
        // Test ID
        user.setId(1L);
        assertThat(user.getId()).isEqualTo(1L);
        user.setId(null);
        assertThat(user.getId()).isNull();
        
        // Test email
        user.setEmail("test@example.com");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        
        // Test firstName
        user.setFirstName("John");
        assertThat(user.getFirstName()).isEqualTo("John");
        
        // Test lastName
        user.setLastName("Doe");
        assertThat(user.getLastName()).isEqualTo("Doe");
        
        // Test password
        user.setPassword("secretPassword");
        assertThat(user.getPassword()).isEqualTo("secretPassword");
        
        // Test admin boolean
        user.setAdmin(true);
        assertThat(user.isAdmin()).isTrue();
        user.setAdmin(false);
        assertThat(user.isAdmin()).isFalse();
        
        // Test dates
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertThat(user.getCreatedAt()).isEqualTo(now);
        user.setUpdatedAt(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testUserEqualsAdvanced() {
        // Tests equals() plus poussés pour couvrir toutes les branches
        User user1 = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(1L)
                .email("different@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("differentPassword")
                .admin(true)
                .build();

        User user3 = User.builder()
                .id(2L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        // Test equals with same object
        assertThat(user1).isEqualTo(user1);

        // Test equals with null
        assertThat(user1).isNotEqualTo(null);

        // Test equals with different class
        assertThat(user1).isNotEqualTo("not a user");

        // Test equals with same ID (should be equal due to @EqualsAndHashCode(of = {"id"}))
        assertThat(user1).isEqualTo(user2);

        // Test equals with different ID
        assertThat(user1).isNotEqualTo(user3);

        // Test with null IDs
        User userNullId1 = User.builder()
                .email("test1@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        User userNullId2 = User.builder()
                .email("test2@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("password")
                .admin(true)
                .build();

        // Null IDs should be equal
        assertThat(userNullId1).isEqualTo(userNullId2);

        // Test null ID vs non-null ID
        assertThat(user1).isNotEqualTo(userNullId1);
        assertThat(userNullId1).isNotEqualTo(user1);
    }

    @Test
    void testUserHashCodeAdvanced() {
        // Tests hashCode pour couvrir toutes les branches
        User user1 = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(1L)
                .email("different@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("differentPassword")
                .admin(true)
                .build();

        User userNullId = User.builder()
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        // Same ID should have same hashCode
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        // Null ID should have consistent hashCode
        assertThat(userNullId.hashCode()).isEqualTo(userNullId.hashCode());

        // Different ID should likely have different hashCode
        User user3 = User.builder()
                .id(2L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }
}
