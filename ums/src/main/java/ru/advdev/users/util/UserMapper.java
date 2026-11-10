package ru.advdev.users.util;

import java.util.List;

import org.springframework.stereotype.Component;
import ru.advdev.users.dto.UserResponse;
import ru.advdev.users.model.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user, List<String> roles) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhoneNumber(), roles, user.getCreatedAt(), user.getUpdatedAt());
    }
}
