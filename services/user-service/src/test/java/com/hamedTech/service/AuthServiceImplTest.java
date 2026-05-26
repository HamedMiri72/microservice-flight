package com.hamedTech.service;

import com.hamedTech.enums.Role;
import com.hamedTech.model.User;
import com.hamedTech.payload.request.SignupRequest;
import com.hamedTech.payload.response.AuthResponse;
import com.hamedTech.repository.UserRepository;
import com.hamedTech.service.impl.AuthServiceimpl;
import com.hamedTech.service.impl.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    // PasswordEncoder is an interface — Mockito mocks it just like a repository.
    // This lets us control what matches() and encode() return without real crypto.
    @Mock
    private PasswordEncoder passwordEncoder;

    // JwtService is a concrete class, not an interface. Mockito can still mock it.
    // We do this because tests shouldn't depend on a valid JWT secret key.
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceimpl authService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .id(1L)
                .fullName("Hamed Miri")
                .email("hamed@example.com")
                .password("encodedPassword123")
                .role(Role.ROLE_USER)
                .createdAt(LocalDateTime.of(2026, 1, 1, 9, 0))
                .build();
    }

    // ─────────────────────────────────────────────────────────────────
    // login — happy path
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login: returns AuthResponse with token when credentials are valid")
    void login_withValidCredentials_returnsAuthResponse() {
        // ARRANGE
        given(userRepository.findByEmail("hamed@example.com"))
                .willReturn(Optional.of(existingUser));

        // passwordEncoder.matches(rawPassword, storedHash) → true means correct password
        given(passwordEncoder.matches("rawPassword", "encodedPassword123"))
                .willReturn(true);

        given(jwtService.generateToken(anyMap(), eq(existingUser)))
                .willReturn("mocked.jwt.token");

        given(userRepository.save(existingUser))
                .willReturn(existingUser);

        // ACT
        AuthResponse response = authService.login("hamed@example.com", "rawPassword");

        // ASSERT
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("hamed@example.com");
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
        assertThat(response.getMessage()).isEqualTo("you have been logged in");

        // lastLogin must be stamped and the user must be persisted
        verify(userRepository).save(existingUser);
        assertThat(existingUser.getLastLogin()).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────
    // login — failure cases
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login: throws UsernameNotFoundException when email does not exist")
    void login_withUnknownEmail_throwsUsernameNotFoundException() {
        // ARRANGE
        given(userRepository.findByEmail("ghost@example.com"))
                .willReturn(Optional.empty());

        // ASSERT + ACT
        assertThatThrownBy(() -> authService.login("ghost@example.com", "anyPassword"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Invalid email");

        // passwordEncoder and jwtService should never be touched after a missing user
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyMap(), any());
    }

    @Test
    @DisplayName("login: throws BadCredentialsException when password is wrong")
    void login_withWrongPassword_throwsBadCredentialsException() {
        // ARRANGE
        given(userRepository.findByEmail("hamed@example.com"))
                .willReturn(Optional.of(existingUser));

        // matches() returns false → wrong password
        given(passwordEncoder.matches("wrongPassword", "encodedPassword123"))
                .willReturn(false);

        // ASSERT + ACT
        assertThatThrownBy(() -> authService.login("hamed@example.com", "wrongPassword"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid password");

        // JWT must never be generated for a failed login
        verify(jwtService, never()).generateToken(anyMap(), any());
    }

    // ─────────────────────────────────────────────────────────────────
    // signup — happy path
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("signup: saves user with encoded password and returns AuthResponse")
    void signup_withValidRequest_savesUserAndReturnsAuthResponse() {
        // ARRANGE
        SignupRequest request = SignupRequest.builder()
                .email("new@example.com")
                .password("plainPassword")
                .fullName("New User")
                .role(Role.ROLE_USER)
                .build();

        given(userRepository.findByEmail("new@example.com"))
                .willReturn(Optional.empty());

        given(passwordEncoder.encode("plainPassword"))
                .willReturn("hashedPassword");

        // ArgumentCaptor lets us inspect the exact User passed to save().
        // This is the new concept here: instead of just verifying save() was called,
        // we CAPTURE the argument and assert its fields — e.g. that the password was
        // encoded before the user hit the database.
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User savedUser = User.builder()
                .id(10L)
                .fullName("New User")
                .email("new@example.com")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();

        given(userRepository.save(userCaptor.capture()))
                .willReturn(savedUser);

        given(jwtService.generateToken(anyMap(), any()))
                .willReturn("signup.jwt.token");

        // ACT
        AuthResponse response = authService.signup(request);

        // ASSERT — response fields
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEmail()).isEqualTo("new@example.com");
        assertThat(response.getToken()).isEqualTo("signup.jwt.token");
        assertThat(response.getMessage()).isEqualTo("Your account has been signed in");

        // Assert the captured user had its password encoded before save()
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getPassword()).isEqualTo("hashedPassword");
        assertThat(capturedUser.getEmail()).isEqualTo("new@example.com");
    }

    // ─────────────────────────────────────────────────────────────────
    // signup — failure cases
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("signup: throws RuntimeException when email is already registered")
    void signup_withDuplicateEmail_throwsRuntimeException() {
        // ARRANGE
        SignupRequest request = SignupRequest.builder()
                .email("hamed@example.com")
                .password("somePassword")
                .fullName("Hamed Miri")
                .role(Role.ROLE_USER)
                .build();

        given(userRepository.findByEmail("hamed@example.com"))
                .willReturn(Optional.of(existingUser));

        // ASSERT + ACT
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("signup: throws RuntimeException when role is ROLE_SYSTEM_ADMIN")
    void signup_withSystemAdminRole_throwsRuntimeException() {
        // ARRANGE
        SignupRequest request = SignupRequest.builder()
                .email("admin@example.com")
                .password("somePassword")
                .fullName("Admin User")
                .role(Role.ROLE_SYSTEM_ADMIN)
                .build();

        given(userRepository.findByEmail("admin@example.com"))
                .willReturn(Optional.empty());

        // ASSERT + ACT
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("you can't sign up with system admin");

        verify(userRepository, never()).save(any());
    }
}
