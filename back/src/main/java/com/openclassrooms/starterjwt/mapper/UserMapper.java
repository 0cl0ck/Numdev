package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper implements EntityMapper<UserDto, User> {

    @Override
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAdmin(userDto.isAdmin());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());
        
        return user;
    }

    @Override
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAdmin(user.isAdmin());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        
        return userDto;
    }

    @Override
    public List<User> toEntity(List<UserDto> dtoList) {
        if (dtoList == null) {
            return null;
        }
        
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> toDto(List<User> entityList) {
        if (entityList == null) {
            return null;
        }
        
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
