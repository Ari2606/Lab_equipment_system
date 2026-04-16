package com.example.labtrack.service;

import com.example.labtrack.domain.DamageReport;
import com.example.labtrack.domain.User;
import com.example.labtrack.dto.DamageReportForm;

import java.util.List;

public interface DamageService {
    DamageReport reportDamage(DamageReportForm form, User reportedBy);
    DamageReport verify(Long damageId, boolean misuse, User verifier);
    List<DamageReport> findAll();
}
