package ru.advdev.users.dto;

import java.time.Instant;
import java.util.List;

public record UserResponse(int id, String firstName, String lastName, String email, String phoneNumber,
                           List<String> roles, Instant createdAt, Instant updatedAt) {}
