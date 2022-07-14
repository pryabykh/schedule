package com.pryabykh.entityservice.repositories;

import com.pryabykh.entityservice.models.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByNumber(String number);
    Page<Classroom> findAllByCreatorId(Long creatorId, Pageable pageable);
    Page<Classroom> findByNumberContainingIgnoreCase(String number, Pageable pageable);
    Page<Classroom> findByCapacityContaining(int capacity, Pageable pageable);
    Page<Classroom> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
