package com.example.labtrack.service;

import com.example.labtrack.domain.User;

public interface AuditService {
    void log(User user, String action, String details);
}
