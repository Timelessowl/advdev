package ru.advdev.users.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.advdev.users.dao.UsersRepository;
import ru.advdev.users.dto.PublicUserResponse;
import ru.advdev.users.dto.UpdateUserRequest;
import ru.advdev.users.dto.UserResponse;
import ru.advdev.users.model.Role;
import ru.advdev.users.model.User;
import ru.advdev.users.util.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository users;
    private final UserMapper mapper;

    public PublicUserResponse getUser(int id) {
        return users.findPublicUser(id).orElseThrow(UserService::userNotFound);
    }

    public List<PublicUserResponse> getUsers(List<Integer> ids) {
        return users.findPublicUsers(ids);
    }

    @Transactional
    public UserResponse updateUser(int id, UpdateUserRequest request) {
        User user = users.update(id, request.firstName(), request.lastName(), request.phoneNumber())
                .orElseThrow(UserService::userNotFound);
        List<String> roles = users.findRolesByUserId(id).stream().map(Role::name).toList();
        return mapper.toResponse(user, roles);
    }

    public void deleteUser(int id) {
        users.delete(id);
    }

    private static ResponseStatusException userNotFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
}
