package com.example.labtrack.service;

import com.example.labtrack.domain.Fine;
import com.example.labtrack.domain.User;

import java.util.List;

public interface FineService {
    Fine createLateFine(User student, String reason);
    Fine createDamageFine(User student, Long damageReportId, String reason);
    List<Fine> studentFines(User student);
    List<Fine> allFines();
    void markPaid(Long fineId);
}
