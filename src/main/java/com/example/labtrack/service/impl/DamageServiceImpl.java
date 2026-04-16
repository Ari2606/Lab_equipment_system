package com.example.labtrack.service.impl;

import com.example.labtrack.domain.*;
import com.example.labtrack.dto.DamageReportForm;
import com.example.labtrack.repository.DamageReportRepository;
import com.example.labtrack.repository.EquipmentRepository;
import com.example.labtrack.repository.ReservationRepository;
import com.example.labtrack.service.DamageService;
import com.example.labtrack.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DamageServiceImpl implements DamageService {
    private final DamageReportRepository damageReportRepository;
    private final EquipmentRepository equipmentRepository;
    private final ReservationRepository reservationRepository;
    private final FineService fineService;

    @Override
    @Transactional
    public DamageReport reportDamage(DamageReportForm form, User reportedBy) {
        DamageReport report = new DamageReport();
        Equipment equipment = equipmentRepository.findById(form.getEquipmentId()).orElseThrow();
        report.setEquipment(equipment);
        report.setReportedBy(reportedBy);
        report.setDescription(form.getDescription());
        if (form.getReservationId() != null) {
            report.setReservation(reservationRepository.findById(form.getReservationId()).orElseThrow());
        }
        equipment.setConditionStatus(ConditionStatus.DAMAGED);
        equipment.setAvailabilityStatus(EquipmentAvailabilityStatus.UNAVAILABLE);
        equipmentRepository.save(equipment);
        return damageReportRepository.save(report);
    }

    @Override
    @Transactional
    public DamageReport verify(Long damageId, boolean misuse, User verifier) {
        DamageReport report = damageReportRepository.findById(damageId).orElseThrow();
        report.setVerified(true);
        report.setMisuseConfirmed(misuse);
        report.setVerifiedBy(verifier);
        if (misuse && report.getReservation() != null) {
            fineService.createDamageFine(report.getReservation().getStudent(), report.getId(), "Damage due to misuse");
        }
        return damageReportRepository.save(report);
    }

    @Override public List<DamageReport> findAll() { return damageReportRepository.findAll(); }
}
