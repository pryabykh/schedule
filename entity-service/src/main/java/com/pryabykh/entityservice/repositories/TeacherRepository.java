package com.pryabykh.entityservice.repositories;

import com.pryabykh.entityservice.models.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByIdAndCreatorId(Long id, Long creatorId);
    Page<Teacher> findAllByCreatorId(Long creatorId, Pageable pageable);
    List<Teacher> findAllByCreatorId(Long creatorId);
    Page<Teacher> findByCreatorIdAndFirstNameContainingIgnoreCase(Long creatorId, String firstName, Pageable pageable);
    Page<Teacher> findByCreatorIdAndPatronymicContainingIgnoreCase(Long creatorId, String patronymic, Pageable pageable);
    Page<Teacher> findByCreatorIdAndLastNameContainingIgnoreCase(Long creatorId, String lastName, Pageable pageable);

}
