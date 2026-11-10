package ru.advdev.users.dao;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.advdev.users.dto.PublicUserResponse;
import ru.advdev.users.model.Role;
import ru.advdev.users.model.User;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

    private static final String USER_COLUMNS =
            "id, first_name, last_name, email, phone_number, password_hash, created_at, updated_at";

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    private final JdbcClient jdbc;

    public Optional<PublicUserResponse> findPublicUser(int id) {
        return jdbc.sql("SELECT id, first_name, last_name FROM users WHERE id = :id")
                .param("id", id).query(PublicUserResponse.class).optional();
    }

    public List<PublicUserResponse> findPublicUsers(List<Integer> ids) {
        return jdbc.sql("SELECT id, first_name, last_name FROM users WHERE id IN (:ids) ORDER BY id")
                .param("ids", ids).query(PublicUserResponse.class).list();
    }

    public Optional<User> findByEmail(String email) {
        return jdbc.sql("SELECT " + USER_COLUMNS + " FROM users WHERE email = :email")
                .param("email", email).query(USER_ROW_MAPPER).optional();
    }

    public Optional<User> save(String firstName, String lastName, String email, String phoneNumber,
                               String passwordHash) {
        return jdbc.sql("""
                INSERT INTO users (first_name, last_name, email, phone_number, password_hash)
                VALUES (:firstName, :lastName, :email, :phoneNumber, :passwordHash)
                ON CONFLICT (email) DO NOTHING
                RETURNING %s
                """.formatted(USER_COLUMNS))
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phoneNumber", phoneNumber)
                .param("passwordHash", passwordHash)
                .query(USER_ROW_MAPPER).optional();
    }

    public void assignRole(int userId, String roleName) {
        jdbc.sql("""
                INSERT INTO user_roles (user_id, role_id)
                VALUES (:userId, (SELECT id FROM roles WHERE name = :roleName))
                """).param("userId", userId).param("roleName", roleName).update();
    }

    public Optional<User> update(int id, String firstName, String lastName, String phoneNumber) {
        return jdbc.sql("""
                UPDATE users
                SET first_name = COALESCE(:firstName, first_name),
                    last_name = COALESCE(:lastName, last_name),
                    phone_number = COALESCE(:phoneNumber, phone_number),
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = :id
                RETURNING %s
                """.formatted(USER_COLUMNS))
                .param("id", id)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("phoneNumber", phoneNumber)
                .query(USER_ROW_MAPPER).optional();
    }

    public void delete(int id) {
        jdbc.sql("DELETE FROM users WHERE id = :id").param("id", id).update();
    }

    public List<Role> findAllRoles() {
        return jdbc.sql("SELECT id, name FROM roles ORDER BY id").query(Role.class).list();
    }

    public List<Role> findRolesByUserId(int userId) {
        return jdbc.sql("""
                SELECT roles.id, roles.name
                FROM roles JOIN user_roles ON roles.id = user_roles.role_id
                WHERE user_roles.user_id = :userId
                ORDER BY roles.id
                """).param("userId", userId).query(Role.class).list();
    }

    public Optional<Role> saveRole(String name) {
        return jdbc.sql("""
                INSERT INTO roles (name) VALUES (:name)
                ON CONFLICT (name) DO NOTHING
                RETURNING id, name
                """).param("name", name).query(Role.class).optional();
    }

    public boolean lockUser(int id) {
        return jdbc.sql("SELECT id FROM users WHERE id = :id FOR UPDATE")
                .param("id", id).query(Integer.class).optional().isPresent();
    }

    public boolean roleExists(int roleId) {
        return jdbc.sql("SELECT EXISTS (SELECT 1 FROM roles WHERE id = :roleId)")
                .param("roleId", roleId).query(Boolean.class).single();
    }

    public void addUserRole(int userId, int roleId) {
        jdbc.sql("""
                INSERT INTO user_roles (user_id, role_id)
                VALUES (:userId, :roleId)
                ON CONFLICT (role_id, user_id) DO NOTHING
                """).param("userId", userId).param("roleId", roleId).update();
    }

    public void deleteUserRole(int userId, int roleId) {
        jdbc.sql("DELETE FROM user_roles WHERE user_id = :userId AND role_id = :roleId")
                .param("userId", userId).param("roleId", roleId).update();
    }
}
