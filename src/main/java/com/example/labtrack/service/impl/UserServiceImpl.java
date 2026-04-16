package com.example.labtrack.service.impl;

import com.example.labtrack.domain.Role;
import com.example.labtrack.domain.RoleType;
import com.example.labtrack.domain.User;
import com.example.labtrack.dto.RegisterRequest;
import com.example.labtrack.dto.UserUpdateForm;
import com.example.labtrack.repository.RoleRepository;
import com.example.labtrack.repository.UserRepository;
import com.example.labtrack.service.AuditService;
import com.example.labtrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Override
    @Transactional
    public User registerStudent(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role studentRole = roleRepository.findByName(RoleType.STUDENT)
                .orElseThrow(() -> new IllegalStateException("STUDENT role not found. Run the schema SQL first."));
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(studentRole);
        User saved = userRepository.save(user);
        auditService.log(saved, "USER_REGISTERED", "Self registration completed");
        return saved;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateForm form) {
        User user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(form.getRoleType())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.setRole(role);
        user.setStatus(form.getStatus());
        userRepository.save(user);
        auditService.log(user, "USER_UPDATED", "Role/status changed by admin");
    }
}
