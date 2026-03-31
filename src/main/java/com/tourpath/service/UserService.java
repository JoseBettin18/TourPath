package com.tourpath.service;

import com.tourpath.dto.RegisterDTO;
import com.tourpath.model.User;
import com.tourpath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("El nombre de usuario ya existe");
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("El correo ya está registrado");

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .roles(Set.of(User.Role.ROLE_USER))
                .enabled(true)
                .accountNonLocked(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public List<User> getAllUsers()                        { return userRepository.findAll(); }
    public long       countUsers()                         { return userRepository.count(); }
    public Optional<User> getUserById(String id)           { return userRepository.findById(id); }
    public Optional<User> getUserByUsername(String name)   { return userRepository.findByUsername(name); }

    public User save(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateRoles(String id, Set<User.Role> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setRoles(roles);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void toggleStatus(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setEnabled(!user.isEnabled());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void deleteUser(String id) { userRepository.deleteById(id); }

    public void createSuperAdminIfNotExists() {
        if (!userRepository.existsByUsername("superadmin")) {
            User admin = User.builder()
                    .username("superadmin")
                    .email("admin@tourpath.com")
                    .password(passwordEncoder.encode("TourPath2026@"))
                    .fullName("Super Administrador")
                    .roles(Set.of(User.Role.ROLE_SUPER_ADMIN, User.Role.ROLE_USER))
                    .enabled(true)
                    .accountNonLocked(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            System.out.println("✅ SuperAdmin creado: superadmin / TourPath2026@");
        }
    }
}
