package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    private SessionMapper sessionMapper;
    
    @Mock
    private TeacherService teacherService;
    
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        sessionMapper = new SessionMapper();
        // Inject mocks using reflection
        ReflectionTestUtils.setField(sessionMapper, "teacherService", teacherService);
        ReflectionTestUtils.setField(sessionMapper, "userService", userService);
    }

    @Test
    void testToEntity() {
        // Given
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("Morning yoga");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());

        // Mock teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        when(teacherService.findById(1L)).thenReturn(teacher);

        // Mock users
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("User1");
        user1.setLastName("Test");
        user1.setEmail("user1@test.com");
        user1.setPassword("password");
        user1.setAdmin(false);

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("User2");
        user2.setLastName("Test");
        user2.setEmail("user2@test.com");
        user2.setPassword("password");
        user2.setAdmin(false);

        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // When
        Session session = sessionMapper.toEntity(sessionDto);

        // Then
        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getDate(), session.getDate());
        assertEquals(sessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), session.getUpdatedAt());
    }

    @Test
    void testToEntityWithNull() {
        // When
        Session session = sessionMapper.toEntity((SessionDto) null);

        // Then
        assertNull(session);
    }

    @Test
    void testToDto() {
        // Given
        Session session = new Session();
        session.setId(1L);
        session.setName("Pilates Session");
        session.setDescription("Evening pilates");
        session.setDate(new Date());
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        Teacher teacher = new Teacher();
        teacher.setId(2L);
        session.setTeacher(teacher);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        session.setUsers(Arrays.asList(user1, user2));

        // When
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Then
        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(teacher.getId(), sessionDto.getTeacher_id());
        assertEquals(2, sessionDto.getUsers().size());
        assertTrue(sessionDto.getUsers().contains(1L));
        assertTrue(sessionDto.getUsers().contains(2L));
        assertEquals(session.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDto.getUpdatedAt());
    }

    @Test
    void testToDtoWithNull() {
        // When
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        // Then
        assertNull(sessionDto);
    }

    @Test
    void testToDtoWithNullTeacher() {
        // Given
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setTeacher(null);
        session.setUsers(Arrays.asList());

        // When
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Then
        assertNotNull(sessionDto);
        assertNull(sessionDto.getTeacher_id());
        assertNotNull(sessionDto.getUsers());
        assertTrue(sessionDto.getUsers().isEmpty());
    }

    @Test
    void testToDtoWithNullUsers() {
        // Given
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setUsers(null);

        // When
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Then
        assertNotNull(sessionDto);
        assertNotNull(sessionDto.getUsers());
        assertTrue(sessionDto.getUsers().isEmpty());
    }

    @Test
    void testToEntityList() {
        // Given
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");

        List<SessionDto> sessionDtos = Arrays.asList(sessionDto1, sessionDto2);

        // When
        List<Session> sessions = sessionMapper.toEntity(sessionDtos);

        // Then
        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        assertEquals(sessionDto1.getId(), sessions.get(0).getId());
        assertEquals(sessionDto2.getId(), sessions.get(1).getId());
    }

    @Test
    void testToEntityListWithNull() {
        // When
        List<Session> sessions = sessionMapper.toEntity((List<SessionDto>) null);

        // Then
        assertNull(sessions);
    }

    @Test
    void testToDtoList() {
        // Given
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");

        List<Session> sessions = Arrays.asList(session1, session2);

        // When
        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        // Then
        assertNotNull(sessionDtos);
        assertEquals(2, sessionDtos.size());
        assertEquals(session1.getId(), sessionDtos.get(0).getId());
        assertEquals(session2.getId(), sessionDtos.get(1).getId());
    }

    @Test
    void testToDtoListWithNull() {
        // When
        List<SessionDto> sessionDtos = sessionMapper.toDto((List<Session>) null);

        // Then
        assertNull(sessionDtos);
    }
}
