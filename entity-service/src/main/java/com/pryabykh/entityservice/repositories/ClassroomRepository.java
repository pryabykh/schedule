package com.pryabykh.entityservice.repositories;

import com.pryabykh.entityservice.models.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByNumberAndCreatorId(String number, Long creatorId);

    Page<Classroom> findAllByCreatorId(Long creatorId, Pageable pageable);

    Page<Classroom> findByCreatorIdAndNumberContainingIgnoreCase(Long creatorId, String number, Pageable pageable);

    @Query(value = "select c from Classroom c where c.creatorId = :creatorId and CAST(c.capacity as text) like LOWER(CONCAT('%', :capacity, '%'))")
    Page<Classroom> findByCreatorIdAndCapacityContaining(@Param("creatorId") Long creatorId,
                                                         @Param("capacity") String capacity,
                                                         Pageable pageable);

    Page<Classroom> findByCreatorIdAndDescriptionContainingIgnoreCase(Long creatorId, String description, Pageable pageable);

    @Query(value = "select c from Classroom c \n" +
            "join c.teacher t\n" +
            "where c.creatorId = :creatorId\n" +
            "and lower(CONCAT(t.lastName, ' ', t.firstName, ' ', t.patronymic)) like lower(CONCAT('%', :teacherId, '%'))")
    Page<Classroom> findByCreatorIdAndTeacherIdContaining(@Param("creatorId") Long creatorId,
                                                          @Param("teacherId") String teacherId,
                                                          Pageable pageable);
}
