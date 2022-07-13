package com.pryabykh.entityservice.repositories;

import com.pryabykh.entityservice.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByNumber(String number);
}
