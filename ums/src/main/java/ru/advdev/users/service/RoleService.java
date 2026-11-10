package ru.advdev.users.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.advdev.users.dao.UsersRepository;
import ru.advdev.users.model.Role;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final UsersRepository users;

    public List<Role> getRoles() {
        return users.findAllRoles();
    }

    public Role createRole(String name) {
        return users.saveRole(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists"));
    }

    @Transactional
    public void assignRole(int userId, int roleId) {
        lockUser(userId);
        if (!users.roleExists(roleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role ID");
        }
        users.addUserRole(userId, roleId);
    }

    public void revokeRole(int userId, int roleId) {
        users.deleteUserRole(userId, roleId);
    }

    private void lockUser(int userId) {
        if (!users.lockUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
