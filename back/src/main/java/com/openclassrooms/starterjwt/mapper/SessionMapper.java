package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionMapper implements EntityMapper<SessionDto, Session> {

    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private UserService userService;

    @Override
    public Session toEntity(SessionDto sessionDto) {
        if (sessionDto == null) {
            return null;
        }
        
        Session session = new Session();
        session.setId(sessionDto.getId());
        session.setName(sessionDto.getName());
        session.setDescription(sessionDto.getDescription());
        session.setDate(sessionDto.getDate());
        session.setCreatedAt(sessionDto.getCreatedAt());
        session.setUpdatedAt(sessionDto.getUpdatedAt());
        
        // Map teacher
        if (sessionDto.getTeacher_id() != null) {
            session.setTeacher(teacherService.findById(sessionDto.getTeacher_id()));
        }
        
        // Map users
        if (sessionDto.getUsers() != null && !sessionDto.getUsers().isEmpty()) {
            List<User> users = sessionDto.getUsers().stream()
                    .map(userId -> userService.findById(userId))
                    .filter(user -> user != null)
                    .collect(Collectors.toList());
            session.setUsers(users);
        } else {
            session.setUsers(new ArrayList<>());
        }
        
        return session;
    }

    @Override
    public SessionDto toDto(Session session) {
        if (session == null) {
            return null;
        }
        
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(session.getId());
        sessionDto.setName(session.getName());
        sessionDto.setDescription(session.getDescription());
        sessionDto.setDate(session.getDate());
        sessionDto.setCreatedAt(session.getCreatedAt());
        sessionDto.setUpdatedAt(session.getUpdatedAt());
        
        // Map teacher_id
        if (session.getTeacher() != null) {
            sessionDto.setTeacher_id(session.getTeacher().getId());
        }
        
        // Map user IDs
        if (session.getUsers() != null && !session.getUsers().isEmpty()) {
            List<Long> userIds = session.getUsers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            sessionDto.setUsers(userIds);
        } else {
            sessionDto.setUsers(new ArrayList<>());
        }
        
        return sessionDto;
    }

    @Override
    public List<Session> toEntity(List<SessionDto> dtoList) {
        if (dtoList == null) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionDto> toDto(List<Session> entityList) {
        if (entityList == null) {
            return null;
        }
        
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}