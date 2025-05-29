package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void testToEntity() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password123");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        // When
        User user = userMapper.toEntity(userDto);

        // Then
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    void testToEntityWithNull() {
        // When
        User user = userMapper.toEntity((UserDto) null);

        // Then
        assertNull(user);
    }

    @Test
    void testToDto() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        user.setPassword("secret456");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // When
        UserDto userDto = userMapper.toDto(user);

        // Then
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }

    @Test
    void testToDtoWithNull() {
        // When
        UserDto userDto = userMapper.toDto((User) null);

        // Then
        assertNull(userDto);
    }

    @Test
    void testToEntityList() {
        // Given
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setEmail("john.doe@example.com");
        userDto1.setPassword("password123");
        userDto1.setAdmin(false);
        userDto1.setCreatedAt(LocalDateTime.now());
        userDto1.setUpdatedAt(LocalDateTime.now());

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");
        userDto2.setEmail("jane.smith@example.com");
        userDto2.setPassword("password456");
        userDto2.setAdmin(true);
        userDto2.setCreatedAt(LocalDateTime.now());
        userDto2.setUpdatedAt(LocalDateTime.now());

        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        // When
        List<User> users = userMapper.toEntity(userDtos);

        // Then
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(userDto1.getId(), users.get(0).getId());
        assertEquals(userDto2.getId(), users.get(1).getId());
        assertEquals(userDto1.getFirstName(), users.get(0).getFirstName());
        assertEquals(userDto2.getFirstName(), users.get(1).getFirstName());
        assertEquals(userDto1.getLastName(), users.get(0).getLastName());
        assertEquals(userDto2.getLastName(), users.get(1).getLastName());
        assertEquals(userDto1.getEmail(), users.get(0).getEmail());
        assertEquals(userDto2.getEmail(), users.get(1).getEmail());
        assertEquals(userDto1.getPassword(), users.get(0).getPassword());
        assertEquals(userDto2.getPassword(), users.get(1).getPassword());
        assertEquals(userDto1.isAdmin(), users.get(0).isAdmin());
        assertEquals(userDto2.isAdmin(), users.get(1).isAdmin());
        assertEquals(userDto1.getCreatedAt(), users.get(0).getCreatedAt());
        assertEquals(userDto2.getCreatedAt(), users.get(1).getCreatedAt());
        assertEquals(userDto1.getUpdatedAt(), users.get(0).getUpdatedAt());
        assertEquals(userDto2.getUpdatedAt(), users.get(1).getUpdatedAt());
    }

    @Test
    void testToEntityListWithNull() {
        // When
        List<User> users = userMapper.toEntity((List<UserDto>) null);

        // Then
        assertNull(users);
    }

    @Test
    void testToDtoList() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password123");
        user1.setAdmin(false);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("password456");
        user2.setAdmin(true);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());

        List<User> users = Arrays.asList(user1, user2);

        // When
        List<UserDto> userDtos = userMapper.toDto(users);

        // Then
        assertNotNull(userDtos);
        assertEquals(2, userDtos.size());
        assertEquals(user1.getId(), userDtos.get(0).getId());
        assertEquals(user2.getId(), userDtos.get(1).getId());
        assertEquals(user1.getFirstName(), userDtos.get(0).getFirstName());
        assertEquals(user2.getFirstName(), userDtos.get(1).getFirstName());
        assertEquals(user1.getLastName(), userDtos.get(0).getLastName());
        assertEquals(user2.getLastName(), userDtos.get(1).getLastName());
        assertEquals(user1.getEmail(), userDtos.get(0).getEmail());
        assertEquals(user2.getEmail(), userDtos.get(1).getEmail());
        assertEquals(user1.getPassword(), userDtos.get(0).getPassword());
        assertEquals(user2.getPassword(), userDtos.get(1).getPassword());
        assertEquals(user1.isAdmin(), userDtos.get(0).isAdmin());
        assertEquals(user2.isAdmin(), userDtos.get(1).isAdmin());
        assertEquals(user1.getCreatedAt(), userDtos.get(0).getCreatedAt());
        assertEquals(user2.getCreatedAt(), userDtos.get(1).getCreatedAt());
        assertEquals(user1.getUpdatedAt(), userDtos.get(0).getUpdatedAt());
        assertEquals(user2.getUpdatedAt(), userDtos.get(1).getUpdatedAt());
    }

    @Test
    void testToDtoListWithNull() {
        // When
        List<UserDto> userDtos = userMapper.toDto((List<User>) null);

        // Then
        assertNull(userDtos);
    }
}
