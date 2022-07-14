package com.pryabykh.entityservice.repositories;

import com.pryabykh.entityservice.models.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByNumberAndCreatorId(String number, Long creatorId);
    Page<Classroom> findAllByCreatorId(Long creatorId, Pageable pageable);
    Page<Classroom> findByCreatorIdAndNumberContainingIgnoreCase(Long creatorId, String number, Pageable pageable);
    Page<Classroom> findByCreatorIdAndCapacityContaining(Long creatorId, int capacity, Pageable pageable);
    Page<Classroom> findByCreatorIdAndDescriptionContainingIgnoreCase(Long creatorId, String description, Pageable pageable);
}
