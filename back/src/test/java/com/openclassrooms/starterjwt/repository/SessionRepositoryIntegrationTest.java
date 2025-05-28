package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class SessionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    private Session testSession;
    private Teacher testTeacher;
    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        // Create test teacher
        testTeacher = Teacher.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();
        testTeacher = entityManager.persistAndFlush(testTeacher);

        // Create test users
        testUser1 = User.builder()
                .email("user1@yoga-studio.com")
                .firstName("Alice")
                .lastName("Johnson")
                .password("password123")
                .admin(false)
                .build();
        testUser1 = entityManager.persistAndFlush(testUser1);

        testUser2 = User.builder()
                .email("user2@yoga-studio.com")
                .firstName("Bob")
                .lastName("Wilson")
                .password("password456")
                .admin(false)
                .build();
        testUser2 = entityManager.persistAndFlush(testUser2);

        // Create test session
        testSession = Session.builder()
                .name("Morning Yoga")
                .date(new Date())
                .description("Relaxing morning yoga session")
                .teacher(testTeacher)
                .users(new ArrayList<>())
                .build();
    }

    @Test
    void save_ShouldPersistSession_WithTimestamps() {
        // Given
        LocalDateTime beforeSave = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        
        // When
        Session savedSession = sessionRepository.save(testSession);
        
        // Then
        assertThat(savedSession.getId()).isNotNull();
        assertThat(savedSession.getName()).isEqualTo("Morning Yoga");
        assertThat(savedSession.getTeacher().getId()).isEqualTo(testTeacher.getId());
        assertThat(savedSession.getCreatedAt()).isNotNull();
        assertThat(savedSession.getUpdatedAt()).isNotNull();
        assertThat(savedSession.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isAfterOrEqualTo(beforeSave);
        assertThat(savedSession.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isAfterOrEqualTo(beforeSave);
    }

    @Test
    void findById_ShouldReturnSession_WhenSessionExists() {
        // Given
        Session savedSession = entityManager.persistAndFlush(testSession);

        // When
        Optional<Session> foundSession = sessionRepository.findById(savedSession.getId());

        // Then
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getName()).isEqualTo("Morning Yoga");
        assertThat(foundSession.get().getTeacher().getFirstName()).isEqualTo("Jane");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenSessionDoesNotExist() {
        // When
        Optional<Session> foundSession = sessionRepository.findById(999L);

        // Then
        assertThat(foundSession).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllSessions() {
        // Given
        entityManager.persistAndFlush(testSession);
        
        Session session2 = Session.builder()
                .name("Evening Yoga")
                .date(new Date())
                .description("Calming evening session")
                .teacher(testTeacher)
                .users(new ArrayList<>())
                .build();
        entityManager.persistAndFlush(session2);

        // When
        List<Session> sessions = sessionRepository.findAll();

        // Then
        assertThat(sessions).hasSize(2);
        assertThat(sessions).extracting(Session::getName)
                .containsExactlyInAnyOrder("Morning Yoga", "Evening Yoga");
    }

    @Test
    void update_ShouldUpdateSession_WithNewTimestamp() throws InterruptedException {
        // Given
        Session savedSession = entityManager.persistAndFlush(testSession);
        entityManager.clear(); // Clear persistence context
        LocalDateTime originalUpdatedAt = savedSession.getUpdatedAt();
        
        Thread.sleep(1000); // Wait 1 second to ensure timestamp difference
        
        // When
        Session sessionToUpdate = sessionRepository.findById(savedSession.getId()).orElseThrow();
        sessionToUpdate.setName("Updated Morning Yoga");
        sessionToUpdate.setDescription("Updated description");
        Session updatedSession = sessionRepository.saveAndFlush(sessionToUpdate);
        
        // Then
        assertThat(updatedSession.getName()).isEqualTo("Updated Morning Yoga");
        assertThat(updatedSession.getDescription()).isEqualTo("Updated description");
        // Vérifier que le timestamp a été mis à jour (après > avant)
        assertThat(updatedSession.getUpdatedAt()).isAfter(originalUpdatedAt);
        
        // Vérifier que le createdAt n'a pas changé (tronqué au niveau des secondes)
        assertThat(updatedSession.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isEqualTo(savedSession.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void delete_ShouldRemoveSession() {
        // Given
        Session savedSession = entityManager.persistAndFlush(testSession);
        Long sessionId = savedSession.getId();

        // When
        sessionRepository.deleteById(sessionId);
        
        // Then
        Optional<Session> deletedSession = sessionRepository.findById(sessionId);
        assertThat(deletedSession).isEmpty();
    }

    @Test
    void sessionWithUsers_ShouldPersistRelationships() {
        // Given
        testSession.getUsers().add(testUser1);
        testSession.getUsers().add(testUser2);
        
        // When
        Session savedSession = entityManager.persistAndFlush(testSession);
        
        // Then
        assertThat(savedSession.getUsers()).hasSize(2);
        assertThat(savedSession.getUsers()).extracting(User::getEmail)
                .containsExactlyInAnyOrder("user1@yoga-studio.com", "user2@yoga-studio.com");
    }

    @Test
    void removeUserFromSession_ShouldUpdateRelationship() {
        // Given
        testSession.getUsers().add(testUser1);
        testSession.getUsers().add(testUser2);
        Session savedSession = entityManager.persistAndFlush(testSession);
        
        // When
        savedSession.getUsers().remove(testUser1);
        Session updatedSession = entityManager.persistAndFlush(savedSession);
        
        // Then
        assertThat(updatedSession.getUsers()).hasSize(1);
        assertThat(updatedSession.getUsers().iterator().next().getEmail())
                .isEqualTo("user2@yoga-studio.com");
    }

    @Test
    void findSessionsByTeacher_ShouldReturnTeacherSessions() {
        // Given
        Teacher anotherTeacher = Teacher.builder()
                .firstName("Mike")
                .lastName("Brown")
                .build();
        anotherTeacher = entityManager.persistAndFlush(anotherTeacher);

        entityManager.persistAndFlush(testSession);
        
        Session session2 = Session.builder()
                .name("Advanced Yoga")
                .date(new Date())
                .description("For experienced practitioners")
                .teacher(anotherTeacher)
                .users(new ArrayList<>())
                .build();
        entityManager.persistAndFlush(session2);

        // When
        List<Session> teacherSessions = sessionRepository.findAll().stream()
                .filter(s -> s.getTeacher().getId().equals(testTeacher.getId()))
                .collect(Collectors.toList());

        // Then
        assertThat(teacherSessions).hasSize(1);
        assertThat(teacherSessions.get(0).getName()).isEqualTo("Morning Yoga");
    }
}
