package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper implements EntityMapper<TeacherDto, Teacher> {

    @Override
    public Teacher toEntity(TeacherDto teacherDto) {
        if (teacherDto == null) {
            return null;
        }
        
        Teacher teacher = new Teacher();
        teacher.setId(teacherDto.getId());
        teacher.setFirstName(teacherDto.getFirstName());
        teacher.setLastName(teacherDto.getLastName());
        teacher.setCreatedAt(teacherDto.getCreatedAt());
        teacher.setUpdatedAt(teacherDto.getUpdatedAt());
        
        return teacher;
    }

    @Override
    public TeacherDto toDto(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacher.getId());
        teacherDto.setFirstName(teacher.getFirstName());
        teacherDto.setLastName(teacher.getLastName());
        teacherDto.setCreatedAt(teacher.getCreatedAt());
        teacherDto.setUpdatedAt(teacher.getUpdatedAt());
        
        return teacherDto;
    }

    @Override
    public List<Teacher> toEntity(List<TeacherDto> dtoList) {
        if (dtoList == null) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherDto> toDto(List<Teacher> entityList) {
        if (entityList == null) {
            return null;
        }
        
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
