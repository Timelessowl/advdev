package ru.advdev.users.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.advdev.users.dto.CreateRoleRequest;
import ru.advdev.users.model.Role;
import ru.advdev.users.service.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class RoleController {

    private final RoleService roles;

    @GetMapping("/roles")
    List<Role> listRoles() {
        return roles.getRoles();
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    Role createRole(@Valid @RequestBody CreateRoleRequest request) {
        return roles.createRole(request.name());
    }

    @PutMapping("/users/{id}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void assignRole(@PathVariable int id, @PathVariable @Positive int roleId) {
        roles.assignRole(id, roleId);
    }

    @DeleteMapping("/users/{id}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void revokeRole(@PathVariable int id, @PathVariable int roleId) {
        roles.revokeRole(id, roleId);
    }
}
