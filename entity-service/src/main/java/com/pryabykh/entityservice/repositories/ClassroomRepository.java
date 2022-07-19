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

    @Query(value = "select * from classrooms where creator_id = :creatorId and CAST(capacity as text) like LOWER(CONCAT('%', :capacity, '%'))",
            nativeQuery = true)
    Page<Classroom> findByCreatorIdAndCapacityContaining(@Param("creatorId") Long creatorId,
                                                         @Param("capacity") String capacity,
                                                         Pageable pageable);

    Page<Classroom> findByCreatorIdAndDescriptionContainingIgnoreCase(Long creatorId, String description, Pageable pageable);

    @Query(value = "select * from classrooms c \n" +
            "join teachers t on t.id = c.in_charge \n" +
            "where c.creator_id = :creatorId\n" +
            "and lower(CONCAT(t.last_name, ' ', t.first_name, ' ', t.patronymic)) like lower(CONCAT('%', :inCharge, '%'))",
            nativeQuery = true)
    Page<Classroom> findByCreatorIdAndInChargeContaining(@Param("creatorId") Long creatorId,
                                                         @Param("inCharge") String inCharge,
                                                         Pageable pageable);
}
