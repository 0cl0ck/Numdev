package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final SessionMapper sessionMapper;

    public SessionService(SessionRepository sessionRepository, UserRepository userRepository, TeacherRepository teacherRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.sessionMapper = sessionMapper;
    }

    public Session create(Session session) {
        return this.sessionRepository.save(session);
    }
    
    // New method that validates DTO first
    public SessionDto createFromDto(SessionDto sessionDto) {
        // Validate teacher exists
        if (sessionDto.getTeacher_id() != null && !teacherRepository.existsById(sessionDto.getTeacher_id())) {
            throw new BadRequestException();
        }
        
        Session session = sessionMapper.toEntity(sessionDto);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        
        return sessionMapper.toDto(sessionRepository.save(session));
    }
    
    // New method that validates DTO first for update
    public SessionDto updateFromDto(Long id, SessionDto sessionDto) {
        // Check if session exists
        Session existingSession = sessionRepository.findById(id).orElse(null);
        if (existingSession == null) {
            throw new NotFoundException();
        }
        
        // Validate teacher exists
        if (sessionDto.getTeacher_id() != null && !teacherRepository.existsById(sessionDto.getTeacher_id())) {
            throw new BadRequestException();
        }
        
        Session session = sessionMapper.toEntity(sessionDto);
        session.setId(id);
        session.setCreatedAt(existingSession.getCreatedAt());
        session.setUpdatedAt(LocalDateTime.now());
        
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    public void delete(Long id) {
        this.sessionRepository.deleteById(id);
    }

    public List<Session> findAll() {
        return this.sessionRepository.findAll();
    }

    public Session getById(Long id) {
        return this.sessionRepository.findById(id).orElse(null);
    }

    public Session update(Long id, Session session) {
        // Check if session exists
        if (!sessionRepository.existsById(id)) {
            throw new NotFoundException();
        }
        // Validate teacher exists
        if (session.getTeacher() != null && session.getTeacher().getId() != null && !teacherRepository.existsById(session.getTeacher().getId())) {
            throw new BadRequestException();
        }
        session.setId(id);
        return this.sessionRepository.save(session);
    }

    public void participate(Long id, Long userId) {
        Session session = this.sessionRepository.findById(id).orElse(null);
        User user = this.userRepository.findById(userId).orElse(null);
        if (session == null || user == null) {
            throw new NotFoundException();
        }

        // Initialize users list if null
        if (session.getUsers() == null) {
            session.setUsers(new ArrayList<>());
        }

        boolean alreadyParticipate = session.getUsers().stream().anyMatch(o -> o.getId().equals(userId));
        if(alreadyParticipate) {
            throw new BadRequestException();
        }

        session.getUsers().add(user);

        this.sessionRepository.save(session);
    }

    public void noLongerParticipate(Long id, Long userId) {
        Session session = this.sessionRepository.findById(id).orElse(null);
        if (session == null) {
            throw new NotFoundException();
        }

        // Initialize users list if null
        if (session.getUsers() == null) {
            session.setUsers(new ArrayList<>());
        }

        // Remove user if participating, do nothing if not (idempotent behavior)
        session.setUsers(session.getUsers().stream().filter(user -> !user.getId().equals(userId)).collect(Collectors.toList()));
        this.sessionRepository.save(session);
    }
}
