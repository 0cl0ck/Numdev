package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@yoga-studio.com")
                .firstName("John")
                .lastName("Doe")
                .password("hashedPassword123")
                .admin(false)
                .build();
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);

        // When
        Optional<User> foundUser = userRepository.findByEmail("test@yoga-studio.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getEmail()).isEqualTo("test@yoga-studio.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
        assertThat(foundUser.get().isAdmin()).isFalse();
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@test.com");

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailIsNull() {
        // When
        Optional<User> foundUser = userRepository.findByEmail(null);

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        boolean exists = userRepository.existsByEmail("test@yoga-studio.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@test.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistUser_WithTimestamps() {
        // Given
        LocalDateTime beforeSave = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        
        // When
        User savedUser = userRepository.save(testUser);
        
        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@yoga-studio.com");
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getLastName()).isEqualTo("Doe");
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
        assertThat(savedUser.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isAfterOrEqualTo(beforeSave);
        assertThat(savedUser.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isAfterOrEqualTo(beforeSave);
    }

    @Test
    void update_ShouldUpdateTimestamp() throws InterruptedException {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);
        entityManager.clear(); // Clear persistence context
        LocalDateTime originalUpdatedAt = savedUser.getUpdatedAt();
        
        Thread.sleep(1000); // Wait 1 second to ensure timestamp difference
        
        // When
        User userToUpdate = userRepository.findById(savedUser.getId()).orElseThrow();
        userToUpdate.setFirstName("UpdatedFirstName");
        userToUpdate.setLastName("UpdatedLastName");
        User updatedUser = userRepository.saveAndFlush(userToUpdate);
        
        // Then
        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(updatedUser.getLastName()).isEqualTo("UpdatedLastName");
        // Vérifier que le timestamp a été mis à jour (après > avant)
        assertThat(updatedUser.getUpdatedAt()).isAfter(originalUpdatedAt);
        
        // Vérifier que le createdAt n'a pas changé (tronqué au niveau des secondes)
        assertThat(updatedUser.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isEqualTo(savedUser.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void delete_ShouldRemoveUser() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);
        Long userId = savedUser.getId();

        // When
        userRepository.deleteById(userId);
        
        // Then
        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        // Given
        User savedUser = entityManager.persistAndFlush(testUser);

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Given
        entityManager.persistAndFlush(testUser);
        User user2 = User.builder()
                .email("admin@yoga-studio.com")
                .firstName("Admin")
                .lastName("User")
                .password("adminPassword")
                .admin(true)
                .build();
        entityManager.persistAndFlush(user2);

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getEmail)
                .containsExactlyInAnyOrder("test@yoga-studio.com", "admin@yoga-studio.com");
    }
}
