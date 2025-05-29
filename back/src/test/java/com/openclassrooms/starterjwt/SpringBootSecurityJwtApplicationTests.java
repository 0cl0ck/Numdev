package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class SpringBootSecurityJwtApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Test
	public void contextLoads() {
		// Context loads successfully
	}

	@Test
	public void testUserRepositoryIntegration() {
		// Given
		User user = User.builder()
			.email("integration@test.com")
			.firstName("Integration")
			.lastName("Test")
			.password("password123")
			.admin(false)
			.build();

		// When
		User saved = userRepository.save(user);
		Optional<User> found = userRepository.findById(saved.getId());

		// Then
		assertThat(found).isPresent();
		assertThat(found.get().getEmail()).isEqualTo("integration@test.com");
	}

	@Test
	public void testTeacherRepositoryIntegration() {
		// Given
		LocalDateTime now = LocalDateTime.now();
		Teacher teacher = Teacher.builder()
			.firstName("John")
			.lastName("Doe")
			.createdAt(now)
			.updatedAt(now)
			.build();

		// When
		Teacher saved = teacherRepository.save(teacher);
		Optional<Teacher> found = teacherRepository.findById(saved.getId());

		// Then
		assertThat(found).isPresent();
		assertThat(found.get().getFirstName()).isEqualTo("John");
	}

	@Test
	public void testSessionRepositoryIntegration() {
		// Given
		Teacher teacher = Teacher.builder()
			.firstName("Yoga")
			.lastName("Master")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		teacher = teacherRepository.save(teacher);

		Session session = Session.builder()
			.name("Morning Yoga")
			.date(new Date())
			.description("Relaxing morning session")
			.teacher(teacher)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		// When
		Session saved = sessionRepository.save(session);
		Optional<Session> found = sessionRepository.findById(saved.getId());

		// Then
		assertThat(found).isPresent();
		assertThat(found.get().getName()).isEqualTo("Morning Yoga");
		assertThat(found.get().getTeacher().getFirstName()).isEqualTo("Yoga");
	}

	@Test
	public void testFindUserByEmailIntegration() {
		// Given
		User user = User.builder()
			.email("findme@test.com")
			.firstName("Find")
			.lastName("Me")
			.password("password123")
			.admin(false)
			.build();
		userRepository.save(user);

		// When
		Optional<User> found = userRepository.findByEmail("findme@test.com");

		// Then
		assertThat(found).isPresent();
		assertThat(found.get().getFirstName()).isEqualTo("Find");
	}

	@Test
	public void testSessionsApiEndpointIntegration() throws Exception {
		// When & Then
		mockMvc.perform(get("/api/sessions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized()); // sans JWT token
	}

	@Test
	public void testTeachersApiEndpointIntegration() throws Exception {
		// When & Then
		mockMvc.perform(get("/api/teacher")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized()); // sans JWT token
	}

	@Test
	public void testUserExistsIntegration() {
		// Given
		User user = User.builder()
			.email("exists@test.com")
			.firstName("Exists")
			.lastName("Test")
			.password("password123")
			.admin(false)
			.build();
		userRepository.save(user);

		// When
		Boolean exists = userRepository.existsByEmail("exists@test.com");

		// Then
		assertThat(exists).isTrue();
	}

	@Test
	public void testUserDoesNotExistIntegration() {
		// When
		Boolean exists = userRepository.existsByEmail("nonexistent@test.com");

		// Then
		assertThat(exists).isFalse();
	}

	@Test
	public void testFindAllTeachersIntegration() {
		// Given
		LocalDateTime now = LocalDateTime.now();
		Teacher teacher1 = Teacher.builder()
			.firstName("Teacher1")
			.lastName("LastName1")
			.createdAt(now)
			.updatedAt(now)
			.build();
		Teacher teacher2 = Teacher.builder()
			.firstName("Teacher2")
			.lastName("LastName2")
			.createdAt(now)
			.updatedAt(now)
			.build();
		
		teacherRepository.save(teacher1);
		teacherRepository.save(teacher2);

		// When
		List<Teacher> teachers = teacherRepository.findAll();

		// Then
		assertThat(teachers).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	public void testFindAllSessionsIntegration() {
		// When
		List<Session> sessions = sessionRepository.findAll();

		// Then
		assertThat(sessions).isNotNull();
	}

	@Test
	public void testDeleteUserIntegration() {
		// Given
		User user = User.builder()
			.email("deleteme@test.com")
			.firstName("Delete")
			.lastName("Me")
			.password("password123")
			.admin(false)
			.build();
		User saved = userRepository.save(user);

		// When
		userRepository.deleteById(saved.getId());

		// Then
		Optional<User> found = userRepository.findById(saved.getId());
		assertThat(found).isEmpty();
	}

	@Test
	public void testDeleteTeacherIntegration() {
		// Given
		LocalDateTime now = LocalDateTime.now();
		Teacher teacher = Teacher.builder()
			.firstName("ToDelete")
			.lastName("Teacher")
			.createdAt(now)
			.updatedAt(now)
			.build();
		Teacher saved = teacherRepository.save(teacher);

		// When
		teacherRepository.deleteById(saved.getId());

		// Then
		Optional<Teacher> found = teacherRepository.findById(saved.getId());
		assertThat(found).isEmpty();
	}

	@Test
	public void testDeleteSessionIntegration() {
		// Given
		Teacher teacher = Teacher.builder()
			.firstName("Session")
			.lastName("Teacher")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		teacher = teacherRepository.save(teacher);

		Session session = Session.builder()
			.name("Session to Delete")
			.date(new Date())
			.description("This session will be deleted")
			.teacher(teacher)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		Session saved = sessionRepository.save(session);

		// When
		sessionRepository.deleteById(saved.getId());

		// Then
		Optional<Session> found = sessionRepository.findById(saved.getId());
		assertThat(found).isEmpty();
	}

	@Test
	public void testUpdateUserIntegration() {
		// Given
		User user = User.builder()
			.email("update@test.com")
			.firstName("Original")
			.lastName("Name")
			.password("password123")
			.admin(false)
			.build();
		User saved = userRepository.save(user);

		// When
		saved.setFirstName("Updated");
		saved.setLastName("NewName");
		User updated = userRepository.save(saved);

		// Then
		assertThat(updated.getFirstName()).isEqualTo("Updated");
		assertThat(updated.getLastName()).isEqualTo("NewName");
	}

	@Test
	public void testUpdateTeacherIntegration() {
		// Given
		LocalDateTime now = LocalDateTime.now();
		Teacher teacher = Teacher.builder()
			.firstName("Original")
			.lastName("Teacher")
			.createdAt(now)
			.updatedAt(now)
			.build();
		Teacher saved = teacherRepository.save(teacher);

		// When
		saved.setFirstName("Updated");
		saved.setLastName("NewTeacher");
		Teacher updated = teacherRepository.save(saved);

		// Then
		assertThat(updated.getFirstName()).isEqualTo("Updated");
		assertThat(updated.getLastName()).isEqualTo("NewTeacher");
	}

	@Test
	public void testEntityManagerPersistUserIntegration() {
		// Given
		User user = User.builder()
			.email("entitymanager@test.com")
			.firstName("Entity")
			.lastName("Manager")
			.password("password123")
			.admin(false)
			.build();

		// When
		entityManager.persistAndFlush(user);
		User found = entityManager.find(User.class, user.getId());

		// Then
		assertThat(found).isNotNull();
		assertThat(found.getEmail()).isEqualTo("entitymanager@test.com");
	}

	@Test
	public void testEntityManagerPersistTeacherIntegration() {
		// Given
		LocalDateTime now = LocalDateTime.now();
		Teacher teacher = Teacher.builder()
			.firstName("EntityManager")
			.lastName("Teacher")
			.createdAt(now)
			.updatedAt(now)
			.build();

		// When
		entityManager.persistAndFlush(teacher);
		Teacher found = entityManager.find(Teacher.class, teacher.getId());

		// Then
		assertThat(found).isNotNull();
		assertThat(found.getFirstName()).isEqualTo("EntityManager");
	}

}
