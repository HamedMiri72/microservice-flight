package com.hamedTech.service;

import com.hamedTech.enums.Role;
import com.hamedTech.model.User;
import com.hamedTech.payload.response.UserResponse;
import com.hamedTech.repository.UserRepository;
import com.hamedTech.service.impl.UserServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

// @ExtendWith(MockitoExtension.class) tells JUnit to activate Mockito.
// This is "pure unit test" — no Spring context, no database, very fast.
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    // @Mock creates a fake (mock) of UserRepository.
    // Mockito intercepts every method call on it — nothing hits a real DB.
    @Mock
    private UserRepository userRepository;

    // @InjectMocks creates a real UserServiceimpl and injects the mock above into it.
    // Equivalent to: new UserServiceimpl(userRepository)
    @InjectMocks
    private UserServiceimpl userService;

    // A reusable test User — built once in @BeforeEach and shared across tests.
    private User testUser;
    private LocalDateTime now;

    // @BeforeEach runs before EVERY test method, giving each test a fresh state.
    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2026, 1, 15, 10, 0);
        testUser = User.builder()
                .id(1L)
                .fullName("Hamed Miri")
                .email("hamed@example.com")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .createdAt(now)
                .updatedAt(now)
                .lastLogin(now)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────
    // getUserByEmail
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getUserByEmail: returns mapped UserResponse when user exists")
    void getUserByEmail_whenUserExists_returnsUserResponse() {
        // ARRANGE — tell the mock what to return when findByEmail is called
        given(userRepository.findByEmail("hamed@example.com"))
                .willReturn(Optional.of(testUser));

        // ACT — call the real service method
        UserResponse response = userService.getUserByEmail("hamed@example.com");

        // ASSERT — verify the response is correctly mapped
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFullName()).isEqualTo("Hamed Miri");
        assertThat(response.getEmail()).isEqualTo("hamed@example.com");
        assertThat(response.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getLastLogin()).isEqualTo(now);

        // verify() ensures the repository method was actually called with the right argument
        verify(userRepository).findByEmail("hamed@example.com");
    }

    @Test
    @DisplayName("getUserByEmail: throws RuntimeException when user not found")
    void getUserByEmail_whenUserNotFound_throwsRuntimeException() {
        // ARRANGE — mock returns empty Optional (user doesn't exist)
        given(userRepository.findByEmail("ghost@example.com"))
                .willReturn(Optional.empty());

        // ASSERT + ACT — assertThatThrownBy catches the exception and lets us assert on it
        assertThatThrownBy(() -> userService.getUserByEmail("ghost@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    // ─────────────────────────────────────────────────────────────────
    // getUserById
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getUserById: returns mapped UserResponse when user exists")
    void getUserById_whenUserExists_returnsUserResponse() {
        // ARRANGE
        given(userRepository.findById(1L))
                .willReturn(Optional.of(testUser));

        // ACT
        UserResponse response = userService.getUserById(1L);

        // ASSERT
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("hamed@example.com");
        assertThat(response.getRole()).isEqualTo(Role.ROLE_USER);

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("getUserById: throws RuntimeException when user not found")
    void getUserById_whenUserNotFound_throwsRuntimeException() {
        // ARRANGE
        given(userRepository.findById(99L))
                .willReturn(Optional.empty());

        // ASSERT + ACT
        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    // ─────────────────────────────────────────────────────────────────
    // getAllUsers
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllUsers: returns list of UserResponses for all users")
    void getAllUsers_whenUsersExist_returnsAllMapped() {
        // ARRANGE — build a second user to prove the mapping works on a list
        User secondUser = User.builder()
                .id(2L)
                .fullName("Alice Smith")
                .email("alice@example.com")
                .password("encodedPassword")
                .role(Role.ROLE_AIRLINE_OWNER)
                .createdAt(now)
                .build();

        given(userRepository.findAll())
                .willReturn(List.of(testUser, secondUser));

        // ACT
        List<UserResponse> responses = userService.getAllUsers();

        // ASSERT
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getEmail()).isEqualTo("hamed@example.com");
        assertThat(responses.get(1).getEmail()).isEqualTo("alice@example.com");
        assertThat(responses.get(1).getRole()).isEqualTo(Role.ROLE_AIRLINE_OWNER);

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("getAllUsers: returns empty list when no users exist")
    void getAllUsers_whenNoUsers_returnsEmptyList() {
        // ARRANGE — repository has nothing
        given(userRepository.findAll())
                .willReturn(List.of());

        // ACT
        List<UserResponse> responses = userService.getAllUsers();

        // ASSERT — empty list, not null
        assertThat(responses).isEmpty();
    }
}