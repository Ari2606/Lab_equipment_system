package com.example.labtrack.repository;

import com.example.labtrack.domain.Fine;
import com.example.labtrack.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByStudent(User student);
}
