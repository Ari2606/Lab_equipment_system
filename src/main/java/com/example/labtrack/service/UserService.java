package com.example.labtrack.service;

import com.example.labtrack.domain.User;
import com.example.labtrack.dto.RegisterRequest;
import com.example.labtrack.dto.UserUpdateForm;

import java.util.List;

public interface UserService {
    User registerStudent(RegisterRequest request);
    User findByUsername(String username);
    List<User> findAll();
    void updateUser(UserUpdateForm form);
}
