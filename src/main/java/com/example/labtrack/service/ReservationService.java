package com.example.labtrack.service;

import com.example.labtrack.domain.Reservation;
import com.example.labtrack.domain.User;
import com.example.labtrack.dto.ReservationForm;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationForm form, User student);
    List<Reservation> getReservationsForStudent(User student);
    List<Reservation> getAllReservations();
    Reservation approve(Long id, User approver);
    Reservation reject(Long id, User approver);
    Reservation issue(Long id, User approver);
    Reservation markReturned(Long id, User approver);
    Reservation cancel(Long id, User student);
}
