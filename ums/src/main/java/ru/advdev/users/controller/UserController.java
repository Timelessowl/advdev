package ru.advdev.users.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.advdev.users.dto.PublicUserResponse;
import ru.advdev.users.dto.UpdateUserRequest;
import ru.advdev.users.dto.UserResponse;
import ru.advdev.users.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
class UserController {

    private final UserService users;

    @GetMapping("/{id}")
    PublicUserResponse getUser(@PathVariable int id) {
        return users.getUser(id);
    }

    @GetMapping
    List<PublicUserResponse> getUsers(@RequestParam @Size(min = 1) List<@NotNull @Positive Integer> ids) {
        return users.getUsers(ids);
    }

    @PatchMapping("/me")
    UserResponse updateUser(Principal actor, @Valid @RequestBody UpdateUserRequest request) {
        return users.updateUser(Integer.parseInt(actor.getName()), request);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(Principal actor) {
        users.deleteUser(Integer.parseInt(actor.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserById(@PathVariable int id) {
        users.deleteUser(id);
    }
}
