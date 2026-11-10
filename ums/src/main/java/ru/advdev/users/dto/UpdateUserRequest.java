package ru.advdev.users.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Pattern(regexp = "(?s).*\\S.*") @Size(max = 255) String firstName,
        @Pattern(regexp = "(?s).*\\S.*") @Size(max = 255) String lastName,
        @Size(max = 20) String phoneNumber) {}
