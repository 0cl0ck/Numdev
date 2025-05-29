package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sessions")
@Log4j2
public class SessionController {
    private final SessionMapper sessionMapper;
    private final SessionService sessionService;

    public SessionController(SessionService sessionService,
                             SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
        this.sessionService = sessionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        try {
            Session session = this.sessionService.getById(Long.valueOf(id));

            if (session == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().body(this.sessionMapper.toDto(session));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<Session> sessions = this.sessionService.findAll();

        return ResponseEntity.ok().body(this.sessionMapper.toDto(sessions));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody SessionDto sessionDto) {
        try {
            log.info(sessionDto);
            SessionDto createdSession = this.sessionService.createFromDto(sessionDto);
            log.info(createdSession);
            return ResponseEntity.ok().body(createdSession);
        } catch (NumberFormatException | DataIntegrityViolationException | BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody SessionDto sessionDto) {
        try {
            SessionDto updatedSession = this.sessionService.updateFromDto(Long.parseLong(id), sessionDto);
            return ResponseEntity.ok().body(updatedSession);
        } catch (NumberFormatException | DataIntegrityViolationException | BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> save(@PathVariable("id") String id) {
        try {
            Session session = this.sessionService.getById(Long.valueOf(id));

            if (session == null) {
                return ResponseEntity.notFound().build();
            }

            this.sessionService.delete(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{id}/participate/{userId}")
    public ResponseEntity<?> participate(@PathVariable("id") String id, @PathVariable("userId") String userId) {
        try {
            this.sessionService.participate(Long.parseLong(id), Long.parseLong(userId));

            return ResponseEntity.ok().build();
        } catch (NumberFormatException | BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}/participate/{userId}")
    public ResponseEntity<?> noLongerParticipate(@PathVariable("id") String id, @PathVariable("userId") String userId) {
        try {
            this.sessionService.noLongerParticipate(Long.parseLong(id), Long.parseLong(userId));

            return ResponseEntity.ok().build();
        } catch (NumberFormatException | BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
