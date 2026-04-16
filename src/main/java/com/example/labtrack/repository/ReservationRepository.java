package com.example.labtrack.repository;

import com.example.labtrack.domain.Reservation;
import com.example.labtrack.domain.ReservationStatus;
import com.example.labtrack.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStudent(User student);
    List<Reservation> findByStatus(ReservationStatus status);

    @Query("""
        select case when count(r) > 0 then true else false end
        from Reservation r
        where r.equipment.id = :equipmentId
          and r.status in ('PENDING','APPROVED','ISSUED')
          and r.startTime < :endTime
          and r.endTime > :startTime
    """)
    boolean hasOverlappingReservation(Long equipmentId, LocalDateTime startTime, LocalDateTime endTime);
}
