package ru.advdev.users.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import org.springframework.jdbc.core.RowMapper;
import ru.advdev.users.model.User;

final class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet row, int rowNumber) throws SQLException {
        return new User(
                row.getInt("id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getString("email"),
                row.getString("phone_number"),
                row.getString("password_hash"),
                row.getObject("created_at", OffsetDateTime.class).toInstant(),
                row.getObject("updated_at", OffsetDateTime.class).toInstant());
    }
}
