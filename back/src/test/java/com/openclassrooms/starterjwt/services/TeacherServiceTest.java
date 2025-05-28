package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacher2 = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_ShouldReturnAllTeachers() {
        // Given
        List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        // When
        List<Teacher> actualTeachers = teacherService.findAll();

        // Then
        assertThat(actualTeachers).hasSize(2);
        assertThat(actualTeachers).containsExactly(teacher1, teacher2);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTeachers() {
        // Given
        when(teacherRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Teacher> actualTeachers = teacherService.findAll();

        // Then
        assertThat(actualTeachers).isEmpty();
    }

    @Test
    void findById_ShouldReturnTeacher_WhenTeacherExists() {
        // Given
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher1));

        // When
        Teacher actualTeacher = teacherService.findById(teacherId);

        // Then
        assertThat(actualTeacher).isNotNull();
        assertThat(actualTeacher.getId()).isEqualTo(teacherId);
        assertThat(actualTeacher.getFirstName()).isEqualTo("John");
        assertThat(actualTeacher.getLastName()).isEqualTo("Doe");
    }

    @Test
    void findById_ShouldReturnNull_WhenTeacherDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(teacherRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        Teacher actualTeacher = teacherService.findById(nonExistentId);

        // Then
        assertThat(actualTeacher).isNull();
    }
}
