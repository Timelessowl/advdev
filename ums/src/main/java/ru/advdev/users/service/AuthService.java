package ru.advdev.users.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.advdev.users.dao.UsersRepository;
import ru.advdev.users.dto.LoginRequest;
import ru.advdev.users.dto.RegisterRequest;
import ru.advdev.users.dto.TokenResponse;
import ru.advdev.users.dto.UserResponse;
import ru.advdev.users.model.Role;
import ru.advdev.users.model.User;
import ru.advdev.users.util.JwtProvider;
import ru.advdev.users.util.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE = "subscriber";

    private final UsersRepository users;
    private final PasswordEncoder passwords;
    private final JwtProvider tokens;
    private final UserMapper mapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        checkPasswordLength(request.password());
        String passwordHash = passwords.encode(request.password());
        User user = users.save(
                        request.firstName(), request.lastName(), request.email(), request.phoneNumber(), passwordHash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered"));
        users.assignRole(user.getId(), DEFAULT_ROLE);
        return mapper.toResponse(user, List.of(DEFAULT_ROLE));
    }

    public TokenResponse login(LoginRequest request) {
        checkPasswordLength(request.password());
        User user = users.findByEmail(request.email()).orElseThrow(AuthService::invalidCredentials);
        if (!passwords.matches(request.password(), user.getPasswordHash())) {
            throw invalidCredentials();
        }

        int id = user.getId();
        List<String> roles = users.findRolesByUserId(id).stream().map(Role::name).toList();
        return new TokenResponse(tokens.generate(id, roles));
    }

    private static void checkPasswordLength(String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must fit in 72 UTF-8 bytes");
        }
    }

    private static ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}
