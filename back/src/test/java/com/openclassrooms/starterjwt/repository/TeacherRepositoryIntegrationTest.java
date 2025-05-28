package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class TeacherRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        testTeacher = Teacher.builder()
                .firstName("Emma")
                .lastName("Watson")
                .build();
    }

    @Test
    void save_ShouldPersistTeacher_WithTimestamps() {
        // Given
        LocalDateTime beforeSave = LocalDateTime.now().minusSeconds(1);
        
        // When
        Teacher savedTeacher = teacherRepository.save(testTeacher);
        
        // Then
        assertThat(savedTeacher.getId()).isNotNull();
        assertThat(savedTeacher.getFirstName()).isEqualTo("Emma");
        assertThat(savedTeacher.getLastName()).isEqualTo("Watson");
        assertThat(savedTeacher.getCreatedAt()).isAfter(beforeSave);
        assertThat(savedTeacher.getUpdatedAt()).isAfter(beforeSave);
        // Vérifier que les timestamps sont proches (à quelques millisecondes près)
        long timeDifferenceMillis = Duration.between(savedTeacher.getCreatedAt(), savedTeacher.getUpdatedAt()).toMillis();
        assertThat(Math.abs(timeDifferenceMillis)).isLessThan(1000); // Moins d'1 seconde de différence
    }

    @Test
    void findById_ShouldReturnTeacher_WhenTeacherExists() {
        // Given
        Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);

        // When
        Optional<Teacher> foundTeacher = teacherRepository.findById(savedTeacher.getId());

        // Then
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getFirstName()).isEqualTo("Emma");
        assertThat(foundTeacher.get().getLastName()).isEqualTo("Watson");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTeacherDoesNotExist() {
        // When
        Optional<Teacher> foundTeacher = teacherRepository.findById(999L);

        // Then
        assertThat(foundTeacher).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllTeachers() {
        // Given
        entityManager.persistAndFlush(testTeacher);
        
        Teacher teacher2 = Teacher.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        entityManager.persistAndFlush(teacher2);

        Teacher teacher3 = Teacher.builder()
                .firstName("Sarah")
                .lastName("Connor")
                .build();
        entityManager.persistAndFlush(teacher3);

        // When
        List<Teacher> teachers = teacherRepository.findAll();

        // Then
        assertThat(teachers).hasSize(3);
        assertThat(teachers).extracting(Teacher::getFirstName)
                .containsExactlyInAnyOrder("Emma", "John", "Sarah");
        assertThat(teachers).extracting(Teacher::getLastName)
                .containsExactlyInAnyOrder("Watson", "Doe", "Connor");
    }

    @Test
    void update_ShouldUpdateTeacher_WithNewTimestamp() throws InterruptedException {
        // Given
        Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);
        entityManager.clear(); // Clear persistence context
        LocalDateTime originalUpdatedAt = savedTeacher.getUpdatedAt();
        
        Thread.sleep(1000); // Wait 1 second to ensure timestamp difference
        
        // When
        Teacher teacherToUpdate = teacherRepository.findById(savedTeacher.getId()).orElseThrow();
        teacherToUpdate.setFirstName("UpdatedFirstName");
        teacherToUpdate.setLastName("UpdatedLastName");
        Teacher updatedTeacher = teacherRepository.saveAndFlush(teacherToUpdate);
        
        // Then
        assertThat(updatedTeacher.getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(updatedTeacher.getLastName()).isEqualTo("UpdatedLastName");
        // Vérifier que le timestamp a été mis à jour (après > avant)
        assertThat(updatedTeacher.getUpdatedAt()).isAfter(originalUpdatedAt);
        
        // Vérifier que le createdAt n'a pas changé (avec une tolérance de quelques secondes)
        long createdAtDifferenceSeconds = Duration.between(updatedTeacher.getCreatedAt(), savedTeacher.getCreatedAt()).abs().toSeconds();
        assertThat(createdAtDifferenceSeconds).isLessThan(2); // Tolérance de 2 secondes
    }

    @Test
    void delete_ShouldRemoveTeacher() {
        // Given
        Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);
        Long teacherId = savedTeacher.getId();

        // When
        teacherRepository.deleteById(teacherId);
        
        // Then
        Optional<Teacher> deletedTeacher = teacherRepository.findById(teacherId);
        assertThat(deletedTeacher).isEmpty();
    }

    @Test
    void exists_ShouldReturnTrue_WhenTeacherExists() {
        // Given
        Teacher savedTeacher = entityManager.persistAndFlush(testTeacher);

        // When
        boolean exists = teacherRepository.existsById(savedTeacher.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void exists_ShouldReturnFalse_WhenTeacherDoesNotExist() {
        // When
        boolean exists = teacherRepository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void count_ShouldReturnCorrectNumber() {
        // Given
        entityManager.persistAndFlush(testTeacher);
        
        Teacher teacher2 = Teacher.builder()
                .firstName("Another")
                .lastName("Teacher")
                .build();
        entityManager.persistAndFlush(teacher2);

        // When
        long count = teacherRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }
}
