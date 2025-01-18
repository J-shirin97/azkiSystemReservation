package org.azkiTest.repository;

import org.azkiTest.model.AvailableSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;


@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long> {

   Page<AvailableSlot> findAllByIsReservedFalse(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AvailableSlot s WHERE s.id = :slotId")
    Optional<AvailableSlot> findSlotForReservation(@Param("slotId") Long slotId);

}
