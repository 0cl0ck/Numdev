package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTest {

    @Test
    void testSessionBuilder() {
        // Given
        Date sessionDate = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<User> users = Arrays.asList(new User(), new User());

        // When
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(sessionDate)
                .description("A relaxing yoga session")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Yoga Session");
        assertThat(session.getDate()).isEqualTo(sessionDate);
        assertThat(session.getDescription()).isEqualTo("A relaxing yoga session");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).hasSize(2);
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testSessionSettersAndGetters() {
        // Given
        Session session = new Session();
        Date sessionDate = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        teacher.setId(2L);
        List<User> users = Arrays.asList(new User());

        // When
        session.setId(2L);
        session.setName("Advanced Yoga");
        session.setDate(sessionDate);
        session.setDescription("Advanced yoga techniques");
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        // Then
        assertThat(session.getId()).isEqualTo(2L);
        assertThat(session.getName()).isEqualTo("Advanced Yoga");
        assertThat(session.getDate()).isEqualTo(sessionDate);
        assertThat(session.getDescription()).isEqualTo("Advanced yoga techniques");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).hasSize(1);
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testSessionEqualsAndHashCode() {
        // Given
        Session session1 = Session.builder().id(1L).name("Session 1").build();
        Session session2 = Session.builder().id(1L).name("Session 2").build();
        Session session3 = Session.builder().id(2L).name("Session 1").build();

        // Then
        assertThat(session1).isEqualTo(session2); // Same ID
        assertThat(session1).isNotEqualTo(session3); // Different ID
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode()); // Same ID = same hash
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode()); // Different ID = different hash
    }

    @Test
    void testSessionEqualsEdgeCases() {
        // Given
        Session session = Session.builder().id(1L).name("Test Session").build();
        Session sessionWithNullId = Session.builder().name("No ID Session").build();
        Session anotherSessionWithNullId = Session.builder().name("Another No ID").build();

        // Test same instance
        assertThat(session).isEqualTo(session);

        // Test null comparison
        assertThat(session).isNotEqualTo(null);

        // Test different class
        assertThat(session).isNotEqualTo("not a session");

        // Test null IDs
        assertThat(sessionWithNullId).isEqualTo(anotherSessionWithNullId); // Both have null IDs
        assertThat(session).isNotEqualTo(sessionWithNullId); // One has ID, other doesn't
        assertThat(sessionWithNullId).isNotEqualTo(session); // Reverse case
    }

    @Test
    void testSessionCompleteEdgeCases() {
        // Test tous les cas possibles pour equals/hashCode
        Session session1 = Session.builder().id(1L).build();
        Session session2 = Session.builder().id(1L).build();
        Session session3 = Session.builder().id(2L).build();
        Session sessionNull = Session.builder().build();
        Session sessionNull2 = Session.builder().build();

        // Test égalité avec mêmes IDs
        assertThat(session1).isEqualTo(session2);
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());

        // Test inégalité avec IDs différents
        assertThat(session1).isNotEqualTo(session3);
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());

        // Test égalité avec IDs null
        assertThat(sessionNull).isEqualTo(sessionNull2);
        assertThat(sessionNull.hashCode()).isEqualTo(sessionNull2.hashCode());

        // Test inégalité ID null vs ID non null
        assertThat(session1).isNotEqualTo(sessionNull);
        assertThat(sessionNull).isNotEqualTo(session1);

        // Test toString ne retourne pas null
        assertThat(session1.toString()).isNotNull();
        assertThat(sessionNull.toString()).isNotNull();

        // Test canEqual method
        assertThat(session1.canEqual(session2)).isTrue();
        assertThat(session1.canEqual("not a session")).isFalse();
        assertThat(session1.canEqual(null)).isFalse();
    }

    @Test
    void testSessionToString() {
        // Given
        Session session = Session.builder()
                .id(1L)
                .name("Test Session")
                .description("Test description")
                .build();

        // When
        String toString = session.toString();

        // Then
        assertThat(toString).contains("Session");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("name=Test Session");
        assertThat(toString).contains("description=Test description");
    }

    @Test
    void testSessionConstructors() {
        // Test NoArgsConstructor
        Session sessionEmpty = new Session();
        assertThat(sessionEmpty).isNotNull();
        assertThat(sessionEmpty.getId()).isNull();

        // Test AllArgsConstructor
        Date sessionDate = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<User> users = Arrays.asList(new User(), new User());

        Session sessionFull = new Session(1L, "Full Session", sessionDate, "Full description", teacher, users, now, now);
        assertThat(sessionFull.getId()).isEqualTo(1L);
        assertThat(sessionFull.getName()).isEqualTo("Full Session");
        assertThat(sessionFull.getDate()).isEqualTo(sessionDate);
        assertThat(sessionFull.getDescription()).isEqualTo("Full description");
        assertThat(sessionFull.getTeacher()).isEqualTo(teacher);
        assertThat(sessionFull.getUsers()).isEqualTo(users);
        assertThat(sessionFull.getCreatedAt()).isEqualTo(now);
        assertThat(sessionFull.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testSessionValidation() {
        // Test des contraintes de validation
        Session session = new Session();
        
        // Test avec des valeurs valides
        session.setName("Valid Session Name");
        session.setDescription("Valid description");
        session.setDate(new Date());
        
        assertThat(session.getName()).isEqualTo("Valid Session Name");
        assertThat(session.getDescription()).isEqualTo("Valid description");
        assertThat(session.getDate()).isNotNull();
    }

    @Test
    void testSessionChainedAccessors() {
        // Given
        Date sessionDate = new Date();
        Teacher teacher = new Teacher();
        List<User> users = Arrays.asList(new User());

        // When
        Session session = new Session()
                .setId(1L)
                .setName("Chained Session")
                .setDate(sessionDate)
                .setDescription("Chained description")
                .setTeacher(teacher)
                .setUsers(users);

        // Then
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Chained Session");
        assertThat(session.getDate()).isEqualTo(sessionDate);
        assertThat(session.getDescription()).isEqualTo("Chained description");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).isEqualTo(users);
    }

    @Test
    void testSessionBuilderComplete() {
        // Test du builder avec toutes les méthodes pour couvrir les branches manquées
        Session.SessionBuilder builder = Session.builder();
        
        // Test chaque setter du builder
        builder.id(1L);
        builder.name("Yoga Session");
        builder.date(new Date());
        builder.description("A relaxing yoga session");
        builder.teacher(new Teacher());
        builder.users(Arrays.asList(new User(), new User()));
        builder.createdAt(LocalDateTime.now());
        builder.updatedAt(LocalDateTime.now());
        
        Session session = builder.build();
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Yoga Session");
        assertThat(session.getDate()).isNotNull();
        assertThat(session.getDescription()).isEqualTo("A relaxing yoga session");
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getUsers()).hasSize(2);
        assertThat(session.getCreatedAt()).isNotNull();
        assertThat(session.getUpdatedAt()).isNotNull();
        
        // Test toString du builder
        assertThat(builder.toString()).contains("SessionBuilder");
    }

    @Test
    void testSessionSettersGetters() {
        // Test tous les setters/getters pour couvrir les branches manquées
        Session session = new Session();
        
        // Test ID
        session.setId(1L);
        assertThat(session.getId()).isEqualTo(1L);
        session.setId(null);
        assertThat(session.getId()).isNull();
        
        // Test name
        session.setName("Test Session");
        assertThat(session.getName()).isEqualTo("Test Session");
        
        // Test date
        Date now = new Date();
        session.setDate(now);
        assertThat(session.getDate()).isEqualTo(now);
        
        // Test description
        session.setDescription("Test Description");
        assertThat(session.getDescription()).isEqualTo("Test Description");
        
        // Test teacher
        Teacher teacher = new Teacher();
        session.setTeacher(teacher);
        assertThat(session.getTeacher()).isEqualTo(teacher);
        
        // Test users list
        List<User> users = Arrays.asList(new User(), new User());
        session.setUsers(users);
        assertThat(session.getUsers()).hasSize(2);
        
        // Test dates
        LocalDateTime nowLocal = LocalDateTime.now();
        session.setCreatedAt(nowLocal);
        assertThat(session.getCreatedAt()).isEqualTo(nowLocal);
        session.setUpdatedAt(nowLocal);
        assertThat(session.getUpdatedAt()).isEqualTo(nowLocal);
    }

    @Test
    void testSessionEqualsAdvanced() {
        // Tests pour couvrir la branche manquée dans equals()
        Session session1 = Session.builder()
                .id(1L)
                .name("Session 1")
                .date(new Date())
                .description("Description 1")
                .build();

        Session session2 = Session.builder()
                .id(1L)
                .name("Session 2")
                .date(new Date(System.currentTimeMillis() + 86400000))
                .description("Description 2")
                .build();

        Session session3 = Session.builder()
                .id(2L)
                .name("Session 3")
                .date(new Date())
                .description("Description 3")
                .build();

        // Test equals with same object
        assertThat(session1).isEqualTo(session1);

        // Test equals with null
        assertThat(session1).isNotEqualTo(null);

        // Test equals with different class
        assertThat(session1).isNotEqualTo("not a session");

        // Test equals with same ID (should be equal due to @EqualsAndHashCode(of = {"id"}))
        assertThat(session1).isEqualTo(session2);

        // Test equals with different ID
        assertThat(session1).isNotEqualTo(session3);

        // Test with null IDs
        Session sessionNullId1 = Session.builder()
                .name("Session Null 1")
                .date(new Date())
                .description("Description Null 1")
                .build();

        Session sessionNullId2 = Session.builder()
                .name("Session Null 2")
                .date(new Date(System.currentTimeMillis() + 86400000))
                .description("Description Null 2")
                .build();

        // Null IDs should be equal
        assertThat(sessionNullId1).isEqualTo(sessionNullId2);

        // Test null ID vs non-null ID
        assertThat(session1).isNotEqualTo(sessionNullId1);
        assertThat(sessionNullId1).isNotEqualTo(session1);
    }
}
