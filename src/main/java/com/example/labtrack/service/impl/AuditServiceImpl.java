package com.example.labtrack.service.impl;

import com.example.labtrack.domain.AuditLog;
import com.example.labtrack.domain.User;
import com.example.labtrack.repository.AuditLogRepository;
import com.example.labtrack.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(User user, String action, String details) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}
