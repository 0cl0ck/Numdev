package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherTest {

    @Test
    void testTeacherBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testTeacherSettersAndGetters() {
        // Given
        Teacher teacher = new Teacher();
        LocalDateTime now = LocalDateTime.now();

        // When
        teacher.setId(2L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        // Then
        assertThat(teacher.getId()).isEqualTo(2L);
        assertThat(teacher.getFirstName()).isEqualTo("Jane");
        assertThat(teacher.getLastName()).isEqualTo("Smith");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testTeacherEqualsAndHashCode() {
        // Given
        Teacher teacher1 = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        Teacher teacher2 = Teacher.builder().id(1L).firstName("Jane").lastName("Smith").build();
        Teacher teacher3 = Teacher.builder().id(2L).firstName("John").lastName("Doe").build();

        // Then
        assertThat(teacher1).isEqualTo(teacher2); // Same ID
        assertThat(teacher1).isNotEqualTo(teacher3); // Different ID
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode()); // Same ID = same hash
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode()); // Different ID = different hash
    }

    @Test
    void testTeacherToString() {
        // Given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        // When
        String toString = teacher.toString();

        // Then
        assertThat(toString).contains("Teacher");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("firstName=John");
        assertThat(toString).contains("lastName=Doe");
    }

    @Test
    void testTeacherChainedAccessors() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("Chain")
                .setLastName("Test")
                .setCreatedAt(now)
                .setUpdatedAt(now);

        // Then
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("Chain");
        assertThat(teacher.getLastName()).isEqualTo("Test");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }
}
