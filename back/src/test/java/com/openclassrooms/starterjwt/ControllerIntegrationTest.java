package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    public void testGetSessionsWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetTeachersWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testGetTeachersWithAuth() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    public void testGetSessionsWithAuth() throws Exception {
        mockMvc.perform(get("/api/sessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    public void testGetSessionByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/sessions/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testGetTeacherByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDeleteSessionNotFound() throws Exception {
        mockMvc.perform(delete("/api/sessions/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testParticipateSessionNotFound() throws Exception {
        mockMvc.perform(post("/api/sessions/99999/participate/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testNoLongerParticipateSessionNotFound() throws Exception {
        mockMvc.perform(delete("/api/sessions/99999/participate/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRegisterWithValidData() throws Exception {
        String jsonRequest = "{\"email\":\"newuser@test.com\",\"firstName\":\"New\",\"lastName\":\"User\",\"password\":\"password123\"}";
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterWithExistingEmail() throws Exception {
        // Create a user first
        User existingUser = User.builder()
                .email("existing@test.com")
                .firstName("Existing")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        userRepository.save(existingUser);

        String jsonRequest = "{\"email\":\"existing@test.com\",\"firstName\":\"Another\",\"lastName\":\"User\",\"password\":\"password123\"}";
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        String jsonRequest = "{\"email\":\"invalid@test.com\",\"password\":\"wrongpassword\"}";
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testCreateSessionWithInvalidData() throws Exception {
        String jsonRequest = "{\"name\":\"\",\"date\":null,\"teacher_id\":null,\"description\":\"\"}";
        
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testUpdateSessionNotFound() throws Exception {
        String jsonRequest = "{\"name\":\"Updated Session\",\"date\":\"2024-12-31\",\"teacher_id\":1,\"description\":\"Updated description\"}";
        
        mockMvc.perform(put("/api/sessions/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testCreateSessionWithValidData() throws Exception {
        // Create a teacher first
        Teacher teacher = Teacher.builder()
                .firstName("Session")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        String jsonRequest = String.format("{\"name\":\"New Session\",\"date\":\"2024-12-31\",\"teacher_id\":%d,\"description\":\"Test session\"}", teacher.getId());
        
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetSessionWithValidId() throws Exception {
        // Create a session first
        Teacher teacher = Teacher.builder()
                .firstName("Get")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Get Session")
                .date(new Date())
                .description("Session to get")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        mockMvc.perform(get("/api/sessions/" + session.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Get Session"));
    }

    @Test
    @WithMockUser
    public void testGetTeacherWithValidId() throws Exception {
        // Create a teacher first
        Teacher teacher = Teacher.builder()
                .firstName("Get")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Get"));
    }

    @Test
    @WithMockUser
    public void testUpdateSessionWithValidData() throws Exception {
        // Create a teacher and session first
        Teacher teacher = Teacher.builder()
                .firstName("Update")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Original Session")
                .date(new Date())
                .description("Original description")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        String jsonRequest = String.format("{\"name\":\"Updated Session\",\"date\":\"2024-12-31\",\"teacher_id\":%d,\"description\":\"Updated description\"}", teacher.getId());
        
        mockMvc.perform(put("/api/sessions/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testDeleteSessionWithValidId() throws Exception {
        // Create a session first
        Teacher teacher = Teacher.builder()
                .firstName("Delete")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Session to Delete")
                .date(new Date())
                .description("This will be deleted")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        mockMvc.perform(delete("/api/sessions/" + session.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testParticipateInSession() throws Exception {
        // Create user, teacher and session
        User user = User.builder()
                .email("participant@test.com")
                .firstName("Participant")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        user = userRepository.save(user);

        Teacher teacher = Teacher.builder()
                .firstName("Participate")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Participation Session")
                .date(new Date())
                .description("Session for participation")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        mockMvc.perform(post("/api/sessions/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testNoLongerParticipateInSession() throws Exception {
        // Create user, teacher and session
        User user = User.builder()
                .email("noparticipant@test.com")
                .firstName("NoParticipant")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        user = userRepository.save(user);

        Teacher teacher = Teacher.builder()
                .firstName("NoParticipate")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("No Participation Session")
                .date(new Date())
                .description("Session for no participation")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        mockMvc.perform(delete("/api/sessions/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetUserWithValidId() throws Exception {
        // Create a user first
        User user = User.builder()
                .email("getuser@test.com")
                .firstName("Get")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        user = userRepository.save(user);

        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Get"));
    }

    @Test
    @WithMockUser
    public void testDeleteUserWithValidId() throws Exception {
        // Create a user first
        User user = User.builder()
                .email("deleteuser@test.com")
                .firstName("Delete")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/user/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testParticipateInSessionWithInvalidUser() throws Exception {
        // Create teacher and session only
        Teacher teacher = Teacher.builder()
                .firstName("Invalid")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Invalid User Session")
                .date(new Date())
                .description("Session with invalid user")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        mockMvc.perform(post("/api/sessions/" + session.getId() + "/participate/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testParticipateInSessionWithInvalidSession() throws Exception {
        // Create user only
        User user = User.builder()
                .email("invalidsession@test.com")
                .firstName("InvalidSession")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        user = userRepository.save(user);

        mockMvc.perform(post("/api/sessions/99999/participate/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testCreateSessionWithInvalidTeacherId() throws Exception {
        String jsonRequest = "{\"name\":\"Invalid Teacher Session\",\"date\":\"2024-12-31\",\"teacher_id\":99999,\"description\":\"Session with invalid teacher\"}";
        
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testUpdateSessionWithInvalidTeacherId() throws Exception {
        // Create a session first
        Teacher teacher = Teacher.builder()
                .firstName("Valid")
                .lastName("Teacher")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        teacher = teacherRepository.save(teacher);

        Session session = Session.builder()
                .name("Valid Session")
                .date(new Date())
                .description("Valid description")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        session = sessionRepository.save(session);

        String jsonRequest = "{\"name\":\"Updated Session\",\"date\":\"2024-12-31\",\"teacher_id\":99999,\"description\":\"Updated with invalid teacher\"}";
        
        mockMvc.perform(put("/api/sessions/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

}
