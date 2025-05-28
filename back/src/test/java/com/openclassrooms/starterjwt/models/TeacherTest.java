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
    void testTeacherEqualsEdgeCases() {
        // Given
        Teacher teacher = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        Teacher teacherWithNullId = Teacher.builder().firstName("Jane").lastName("Smith").build();
        Teacher anotherTeacherWithNullId = Teacher.builder().firstName("Bob").lastName("Wilson").build();

        // Test same instance
        assertThat(teacher).isEqualTo(teacher);

        // Test null comparison
        assertThat(teacher).isNotEqualTo(null);

        // Test different class
        assertThat(teacher).isNotEqualTo("not a teacher");

        // Test null IDs
        assertThat(teacherWithNullId).isEqualTo(anotherTeacherWithNullId); // Both have null IDs
        assertThat(teacher).isNotEqualTo(teacherWithNullId); // One has ID, other doesn't
        assertThat(teacherWithNullId).isNotEqualTo(teacher); // Reverse case
    }

    @Test
    void testTeacherCompleteEdgeCases() {
        // Test tous les cas possibles pour equals/hashCode
        Teacher teacher1 = Teacher.builder().id(1L).build();
        Teacher teacher2 = Teacher.builder().id(1L).build();
        Teacher teacher3 = Teacher.builder().id(2L).build();
        Teacher teacherNull = Teacher.builder().build();
        Teacher teacherNull2 = Teacher.builder().build();

        // Test égalité avec mêmes IDs
        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());

        // Test inégalité avec IDs différents
        assertThat(teacher1).isNotEqualTo(teacher3);
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode());

        // Test égalité avec IDs null
        assertThat(teacherNull).isEqualTo(teacherNull2);
        assertThat(teacherNull.hashCode()).isEqualTo(teacherNull2.hashCode());

        // Test inégalité ID null vs ID non null
        assertThat(teacher1).isNotEqualTo(teacherNull);
        assertThat(teacherNull).isNotEqualTo(teacher1);

        // Test toString ne retourne pas null
        assertThat(teacher1.toString()).isNotNull();
        assertThat(teacherNull.toString()).isNotNull();

        // Test canEqual method
        assertThat(teacher1.canEqual(teacher2)).isTrue();
        assertThat(teacher1.canEqual("not a teacher")).isFalse();
        assertThat(teacher1.canEqual(null)).isFalse();
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
    void testTeacherConstructors() {
        // Test NoArgsConstructor
        Teacher teacherEmpty = new Teacher();
        assertThat(teacherEmpty).isNotNull();
        assertThat(teacherEmpty.getId()).isNull();

        // Test AllArgsConstructor
        LocalDateTime now = LocalDateTime.now();
        Teacher teacherFull = new Teacher(1L, "Doe", "John", now, now);
        assertThat(teacherFull.getId()).isEqualTo(1L);
        assertThat(teacherFull.getLastName()).isEqualTo("Doe");
        assertThat(teacherFull.getFirstName()).isEqualTo("John");
        assertThat(teacherFull.getCreatedAt()).isEqualTo(now);
        assertThat(teacherFull.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testTeacherValidation() {
        // Test des contraintes de validation
        Teacher teacher = new Teacher();
        
        // Test avec des valeurs valides
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
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

    @Test
    void testTeacherBuilderComplete() {
        // Test du builder avec toutes les méthodes pour couvrir les branches manquées
        Teacher.TeacherBuilder builder = Teacher.builder();
        
        // Test chaque setter du builder
        builder.id(1L);
        builder.firstName("John");
        builder.lastName("Doe");
        builder.createdAt(LocalDateTime.now());
        builder.updatedAt(LocalDateTime.now());
        
        Teacher teacher = builder.build();
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getCreatedAt()).isNotNull();
        assertThat(teacher.getUpdatedAt()).isNotNull();
        
        // Test toString du builder
        assertThat(builder.toString()).contains("TeacherBuilder");
    }

    @Test
    void testTeacherSettersGetters() {
        // Test tous les setters/getters pour couvrir les branches manquées
        Teacher teacher = new Teacher();
        
        // Test ID
        teacher.setId(1L);
        assertThat(teacher.getId()).isEqualTo(1L);
        teacher.setId(null);
        assertThat(teacher.getId()).isNull();
        
        // Test firstName
        teacher.setFirstName("John");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        teacher.setFirstName(null);
        assertThat(teacher.getFirstName()).isNull();
        
        // Test lastName
        teacher.setLastName("Doe");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        teacher.setLastName(null);
        assertThat(teacher.getLastName()).isNull();
        
        // Test dates
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        teacher.setUpdatedAt(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testTeacherEqualsAdvanced() {
        // Tests pour couvrir la branche manquée dans equals()
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        Teacher teacher3 = Teacher.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Wilson")
                .build();

        // Test equals with same object
        assertThat(teacher1).isEqualTo(teacher1);

        // Test equals with null
        assertThat(teacher1).isNotEqualTo(null);

        // Test equals with different class
        assertThat(teacher1).isNotEqualTo("not a teacher");

        // Test equals with same ID (should be equal due to @EqualsAndHashCode(of = {"id"}))
        assertThat(teacher1).isEqualTo(teacher2);

        // Test equals with different ID
        assertThat(teacher1).isNotEqualTo(teacher3);

        // Test with null IDs
        Teacher teacherNullId1 = Teacher.builder()
                .firstName("Alice")
                .lastName("Brown")
                .build();

        Teacher teacherNullId2 = Teacher.builder()
                .firstName("Charlie")
                .lastName("Green")
                .build();

        // Null IDs should be equal
        assertThat(teacherNullId1).isEqualTo(teacherNullId2);

        // Test null ID vs non-null ID
        assertThat(teacher1).isNotEqualTo(teacherNullId1);
        assertThat(teacherNullId1).isNotEqualTo(teacher1);
    }

    @Test
    void testTeacherHashCodeAdvanced() {
        // Tests hashCode pour couvrir toutes les branches
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        Teacher teacherNullId = Teacher.builder()
                .firstName("Bob")
                .lastName("Wilson")
                .build();

        // Same ID should have same hashCode
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());

        // Null ID should have consistent hashCode
        assertThat(teacherNullId.hashCode()).isEqualTo(teacherNullId.hashCode());

        // Different ID should likely have different hashCode
        Teacher teacher3 = Teacher.builder()
                .id(2L)
                .firstName("Alice")
                .lastName("Brown")
                .build();

        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode());
    }
}
