package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapper();
    }

    @Test
    void testToEntity() {
        // Given
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        // When
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Then
        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    void testToEntityWithNull() {
        // When
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        // Then
        assertNull(teacher);
    }

    @Test
    void testToDto() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        // When
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Then
        assertNotNull(teacherDto);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
    }

    @Test
    void testToDtoWithNull() {
        // When
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);

        // Then
        assertNull(teacherDto);
    }

    @Test
    void testToEntityList() {
        // Given
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");

        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        // When
        List<Teacher> teachers = teacherMapper.toEntity(teacherDtos);

        // Then
        assertNotNull(teachers);
        assertEquals(2, teachers.size());
        assertEquals(teacherDto1.getId(), teachers.get(0).getId());
        assertEquals(teacherDto2.getId(), teachers.get(1).getId());
    }

    @Test
    void testToEntityListWithNull() {
        // When
        List<Teacher> teachers = teacherMapper.toEntity((List<TeacherDto>) null);

        // Then
        assertNull(teachers);
    }

    @Test
    void testToDtoList() {
        // Given
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        // When
        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        // Then
        assertNotNull(teacherDtos);
        assertEquals(2, teacherDtos.size());
        assertEquals(teacher1.getId(), teacherDtos.get(0).getId());
        assertEquals(teacher2.getId(), teacherDtos.get(1).getId());
    }

    @Test
    void testToDtoListWithNull() {
        // When
        List<TeacherDto> teacherDtos = teacherMapper.toDto((List<Teacher>) null);

        // Then
        assertNull(teacherDtos);
    }
}
