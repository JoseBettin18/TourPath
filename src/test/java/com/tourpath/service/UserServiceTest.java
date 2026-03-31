package com.tourpath.service;

import com.tourpath.dto.RegisterDTO;
import com.tourpath.model.User;
import com.tourpath.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Tests unitarios")
class UserServiceTest {

    @Mock private UserRepository  userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    private RegisterDTO registerDTO;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setFullName("Juan Viajero");
        registerDTO.setUsername("juanviajero");
        registerDTO.setEmail("juan@tourpath.com");
        registerDTO.setPassword("Pass2026@");
        registerDTO.setConfirmPassword("Pass2026@");

        sampleUser = User.builder()
                .id("user-001")
                .username("juanviajero")
                .email("juan@tourpath.com")
                .fullName("Juan Viajero")
                .roles(Set.of(User.Role.ROLE_USER))
                .enabled(true)
                .accountNonLocked(true)
                .build();
    }

    @Test
    @DisplayName("Debe registrar usuario nuevo con ROLE_USER")
    void registerUser_success_withRoleUser() {
        when(userRepository.existsByUsername("juanviajero")).thenReturn(false);
        when(userRepository.existsByEmail("juan@tourpath.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$encoded");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User result = userService.registerUser(registerDTO);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("juanviajero");
        verify(userRepository).save(argThat(u ->
                u.getRoles().contains(User.Role.ROLE_USER) &&
                u.isEnabled() && u.isAccountNonLocked()
        ));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el username ya existe")
    void registerUser_duplicateUsername_throws() {
        when(userRepository.existsByUsername("juanviajero")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(registerDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("usuario ya existe");
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email ya existe")
    void registerUser_duplicateEmail_throws() {
        when(userRepository.existsByUsername("juanviajero")).thenReturn(false);
        when(userRepository.existsByEmail("juan@tourpath.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(registerDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("correo ya está registrado");
    }

    @Test
    @DisplayName("Debe alternar estado activo del usuario")
    void toggleStatus_invertsEnabled() {
        sampleUser.setEnabled(true);
        when(userRepository.findById("user-001")).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any())).thenReturn(sampleUser);

        userService.toggleStatus("user-001");

        assertThat(sampleUser.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Debe actualizar roles del usuario")
    void updateRoles_changesRoles() {
        when(userRepository.findById("user-001")).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any())).thenReturn(sampleUser);

        Set<User.Role> newRoles = Set.of(User.Role.ROLE_OWNER, User.Role.ROLE_USER);
        userService.updateRoles("user-001", newRoles);

        assertThat(sampleUser.getRoles()).contains(User.Role.ROLE_OWNER);
    }

    @Test
    @DisplayName("Debe crear superadmin si no existe")
    void createSuperAdminIfNotExists_createsAdmin() {
        when(userRepository.existsByUsername("superadmin")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$encoded");
        when(userRepository.save(any())).thenReturn(sampleUser);

        userService.createSuperAdminIfNotExists();

        verify(userRepository).save(argThat(u ->
                "superadmin".equals(u.getUsername()) &&
                u.getRoles().contains(User.Role.ROLE_SUPER_ADMIN)
        ));
    }

    @Test
    @DisplayName("No debe crear superadmin si ya existe")
    void createSuperAdminIfNotExists_skipsIfExists() {
        when(userRepository.existsByUsername("superadmin")).thenReturn(true);

        userService.createSuperAdminIfNotExists();

        verify(userRepository, never()).save(any());
    }
}
