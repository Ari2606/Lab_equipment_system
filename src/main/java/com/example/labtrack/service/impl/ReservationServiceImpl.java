package com.example.labtrack.service.impl;

import com.example.labtrack.domain.*;
import com.example.labtrack.dto.ReservationForm;
import com.example.labtrack.repository.EquipmentRepository;
import com.example.labtrack.repository.ReservationRepository;
import com.example.labtrack.service.FineService;
import com.example.labtrack.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final EquipmentRepository equipmentRepository;
    private final FineService fineService;

    @Override
    @Transactional
    public Reservation createReservation(ReservationForm form, User student) {
        Equipment equipment = equipmentRepository.findById(form.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));

        if (equipment.getConditionStatus() != ConditionStatus.GOOD || equipment.getAvailabilityStatus() == EquipmentAvailabilityStatus.UNAVAILABLE) {
            throw new IllegalStateException("Equipment is not reservable");
        }
        if (form.getEndTime().isBefore(form.getStartTime()) || form.getEndTime().isEqual(form.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (reservationRepository.hasOverlappingReservation(equipment.getId(), form.getStartTime(), form.getEndTime())) {
            throw new IllegalStateException("Overlapping reservation already exists");
        }

        Reservation reservation = new Reservation();
        reservation.setEquipment(equipment);
        reservation.setStudent(student);
        reservation.setStartTime(form.getStartTime());
        reservation.setEndTime(form.getEndTime());
        reservation.setPurpose(form.getPurpose());
        reservation.setStatus(ReservationStatus.PENDING);
        equipment.setAvailabilityStatus(EquipmentAvailabilityStatus.RESERVED);
        equipmentRepository.save(equipment);
        return reservationRepository.save(reservation);
    }

    @Override public List<Reservation> getReservationsForStudent(User student) { return reservationRepository.findByStudent(student); }
    @Override public List<Reservation> getAllReservations() { return reservationRepository.findAll(); }

    @Override
    @Transactional
    public Reservation approve(Long id, User approver) {
        Reservation r = get(id);
        r.setStatus(ReservationStatus.APPROVED);
        r.setApprovedBy(approver);
        return reservationRepository.save(r);
    }

    @Override
    @Transactional
    public Reservation reject(Long id, User approver) {
        Reservation r = get(id);
        r.setStatus(ReservationStatus.REJECTED);
        r.setApprovedBy(approver);
        releaseEquipment(r);
        return reservationRepository.save(r);
    }

    @Override
    @Transactional
    public Reservation issue(Long id, User approver) {
        Reservation r = get(id);
        r.setStatus(ReservationStatus.ISSUED);
        r.setIssueTime(LocalDateTime.now());
        r.setApprovedBy(approver);
        r.getEquipment().setAvailabilityStatus(EquipmentAvailabilityStatus.IN_USE);
        equipmentRepository.save(r.getEquipment());
        return reservationRepository.save(r);
    }

    @Override
    @Transactional
    public Reservation markReturned(Long id, User approver) {
        Reservation r = get(id);
        r.setStatus(ReservationStatus.RETURNED);
        r.setActualReturnTime(LocalDateTime.now());
        releaseEquipment(r);
        Reservation saved = reservationRepository.save(r);
        if (saved.getActualReturnTime().isAfter(saved.getEndTime())) {
            fineService.createLateFine(saved.getStudent(), "Late return for reservation #" + saved.getId());
        }
        return saved;
    }

    @Override
    @Transactional
    public Reservation cancel(Long id, User student) {
        Reservation r = get(id);
        if (!r.getStudent().getId().equals(student.getId())) {
            throw new IllegalStateException("Cannot cancel another student's reservation");
        }
        r.setStatus(ReservationStatus.CANCELLED);
        releaseEquipment(r);
        return reservationRepository.save(r);
    }

    private Reservation get(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    private void releaseEquipment(Reservation r) {
        r.getEquipment().setAvailabilityStatus(EquipmentAvailabilityStatus.AVAILABLE);
        equipmentRepository.save(r.getEquipment());
    }
}
