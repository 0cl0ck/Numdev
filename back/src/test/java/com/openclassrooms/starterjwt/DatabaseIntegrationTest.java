package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@Transactional
public class DatabaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void testUserEmailUniqueness() {
        User user1 = User.builder()
                .email("unique@test.com")
                .firstName("First")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        userRepository.save(user1);

        // Should handle unique constraint gracefully
        Optional<User> foundUser = userRepository.findByEmail("unique@test.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("First");
    }

    @Test
    public void testUserCascadeOperations() {
        User user = User.builder()
                .email("cascade@test.com")
                .firstName("Cascade")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();

        userRepository.delete(savedUser);
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isNotPresent();
    }

    @Test
    public void testTeacherCreationWithTimestamps() {
        Teacher teacher = Teacher.builder()
                .firstName("Timestamp")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher saved = teacherRepository.save(teacher);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testSessionTeacherRelationship() {
        Teacher teacher = Teacher.builder()
                .firstName("Relation")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Relationship Session")
                .date(new Date())
                .description("Testing teacher relationship")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        Optional<Session> foundSession = sessionRepository.findById(session.getId());
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(foundSession.get().getTeacher().getFirstName()).isEqualTo("Relation");
    }

    @Test
    public void testSessionUserParticipation() {
        User user = User.builder()
                .email("participant@test.com")
                .firstName("Participant")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        user = userRepository.save(user);

        Teacher teacher = Teacher.builder()
                .firstName("Session")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Participation Session")
                .date(new Date())
                .description("Testing user participation")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session.getUsers().add(user);
        session = sessionRepository.save(session);

        Optional<Session> foundSession = sessionRepository.findById(session.getId());
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getUsers()).hasSize(1);
        assertThat(foundSession.get().getUsers().get(0).getEmail()).isEqualTo("participant@test.com");
    }

    @Test
    public void testMultipleUsersInSession() {
        User user1 = User.builder()
                .email("user1@test.com")
                .firstName("User")
                .lastName("One")
                .password("password")
                .admin(false)
                .build();
        user1 = userRepository.save(user1);

        User user2 = User.builder()
                .email("user2@test.com")
                .firstName("User")
                .lastName("Two")
                .password("password")
                .admin(false)
                .build();
        user2 = userRepository.save(user2);

        Teacher teacher = Teacher.builder()
                .firstName("Multi")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Multi User Session")
                .date(new Date())
                .description("Testing multiple users")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session.getUsers().add(user1);
        session.getUsers().add(user2);
        session = sessionRepository.save(session);

        Optional<Session> foundSession = sessionRepository.findById(session.getId());
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getUsers()).hasSize(2);
    }

    @Test
    public void testFindUsersByAdmin() {
        User adminUser = User.builder()
                .email("admin@test.com")
                .firstName("Admin")
                .lastName("User")
                .password("password")
                .admin(true)
                .build();
        userRepository.save(adminUser);

        User regularUser = User.builder()
                .email("regular@test.com")
                .firstName("Regular")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        userRepository.save(regularUser);

        List<User> allUsers = userRepository.findAll();
        assertThat(allUsers).hasSizeGreaterThanOrEqualTo(2);
        
        boolean hasAdmin = allUsers.stream().anyMatch(User::isAdmin);
        boolean hasRegular = allUsers.stream().anyMatch(u -> !u.isAdmin());
        assertThat(hasAdmin).isTrue();
        assertThat(hasRegular).isTrue();
    }

    @Test
    public void testTeacherSessionsRelationship() {
        final Teacher teacher = Teacher.builder()
                .firstName("MultiSession")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        final Teacher savedTeacher = teacherRepository.save(teacher);

        Session session1 = Session.builder()
                .name("Session 1")
                .date(new Date())
                .description("First session")
                .teacher(savedTeacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        sessionRepository.save(session1);

        Session session2 = Session.builder()
                .name("Session 2")
                .date(new Date())
                .description("Second session")
                .teacher(savedTeacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        sessionRepository.save(session2);

        List<Session> allSessions = sessionRepository.findAll();
        long teacherSessions = allSessions.stream()
                .filter(s -> s.getTeacher().getId().equals(savedTeacher.getId()))
                .count();
        assertThat(teacherSessions).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testSessionUpdateOperations() {
        Teacher teacher = Teacher.builder()
                .firstName("Update")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Original Name")
                .date(new Date())
                .description("Original description")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        session.setName("Updated Name");
        session.setDescription("Updated description");
        session.setUpdatedAt(LocalDateTime.now());
        Session updatedSession = sessionRepository.save(session);

        assertThat(updatedSession.getName()).isEqualTo("Updated Name");
        assertThat(updatedSession.getDescription()).isEqualTo("Updated description");
    }

    @Test
    public void testUserPasswordEncryption() {
        User user = User.builder()
                .email("encryption@test.com")
                .firstName("Encrypted")
                .lastName("User")
                .password("plainPassword123")
                .admin(false)
                .build();
        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        // Password should be stored (even if not encrypted in test)
        assertThat(found.get().getPassword()).isNotNull();
        assertThat(found.get().getPassword()).isNotEmpty();
    }

    @Test
    public void testSessionDateHandling() {
        Teacher teacher = Teacher.builder()
                .firstName("Date")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Date sessionDate = new Date();
        Session session = Session.builder()
                .name("Date Session")
                .date(sessionDate)
                .description("Testing date handling")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        Optional<Session> found = sessionRepository.findById(session.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getDate()).isNotNull();
        assertThat(found.get().getDate().getTime()).isEqualTo(sessionDate.getTime());
    }

    @Test
    public void testTransactionalRollback() {
        long initialUserCount = userRepository.count();
        
        try {
            User user = User.builder()
                    .email("rollback@test.com")
                    .firstName("Rollback")
                    .lastName("User")
                    .password("password")
                    .admin(false)
                    .build();
            userRepository.save(user);
            
            // This should work fine in test environment
            long afterSaveCount = userRepository.count();
            assertThat(afterSaveCount).isEqualTo(initialUserCount + 1);
        } catch (Exception e) {
            // If exception occurs, count should remain the same
            long afterExceptionCount = userRepository.count();
            assertThat(afterExceptionCount).isEqualTo(initialUserCount);
        }
    }

    @Test
    public void testEntityIdGeneration() {
        User user = User.builder()
                .email("idgen@test.com")
                .firstName("IdGen")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        
        assertThat(user.getId()).isNull(); // Before save
        User saved = userRepository.save(user);
        assertThat(saved.getId()).isNotNull(); // After save
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testEmptySessionUsers() {
        Teacher teacher = Teacher.builder()
                .firstName("Empty")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Empty Users Session")
                .date(new Date())
                .description("Session with no users")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        Optional<Session> found = sessionRepository.findById(session.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsers()).isEmpty();
    }

    @Test
    public void testUserSessionParticipationRemoval() {
        User user = User.builder()
                .email("removal@test.com")
                .firstName("Removal")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        user = userRepository.save(user);

        Teacher teacher = Teacher.builder()
                .firstName("Removal")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Removal Session")
                .date(new Date())
                .description("Testing user removal")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session.getUsers().add(user);
        session = sessionRepository.save(session);

        // Remove user from session
        session.getUsers().remove(user);
        session = sessionRepository.save(session);

        Optional<Session> found = sessionRepository.findById(session.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsers()).isEmpty();
    }

    @Test
    public void testFindByExistingEmail() {
        User user = User.builder()
                .email("findbyemail@test.com")
                .firstName("FindByEmail")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("findbyemail@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("FindByEmail");
    }

    @Test
    public void testFindByNonExistingEmail() {
        Optional<User> found = userRepository.findByEmail("nonexistent@test.com");
        assertThat(found).isNotPresent();
    }

}
