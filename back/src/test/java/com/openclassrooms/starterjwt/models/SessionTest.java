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
}
