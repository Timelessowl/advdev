package ru.advdev.users.model;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class User {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String passwordHash;
    private final Instant createdAt;
    private final Instant updatedAt;
}
