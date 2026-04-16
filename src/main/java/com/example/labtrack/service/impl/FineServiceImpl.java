package com.example.labtrack.service.impl;

import com.example.labtrack.domain.*;
import com.example.labtrack.repository.DamageReportRepository;
import com.example.labtrack.repository.FineRepository;
import com.example.labtrack.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {
    private final FineRepository fineRepository;
    private final DamageReportRepository damageReportRepository;

    @Override
    public Fine createLateFine(User student, String reason) {
        Fine fine = new Fine();
        fine.setStudent(student);
        fine.setAmount(new BigDecimal("100.00"));
        fine.setReason(reason);
        return fineRepository.save(fine);
    }

    @Override
    public Fine createDamageFine(User student, Long damageReportId, String reason) {
        DamageReport damageReport = damageReportRepository.findById(damageReportId).orElseThrow();
        Fine fine = new Fine();
        fine.setStudent(student);
        fine.setDamageReport(damageReport);
        fine.setAmount(new BigDecimal("500.00"));
        fine.setReason(reason);
        return fineRepository.save(fine);
    }

    @Override public List<Fine> studentFines(User student) { return fineRepository.findByStudent(student); }
    @Override public List<Fine> allFines() { return fineRepository.findAll(); }

    @Override
    public void markPaid(Long fineId) {
        Fine fine = fineRepository.findById(fineId).orElseThrow();
        fine.setStatus(FineStatus.PAID);
        fineRepository.save(fine);
    }
}
