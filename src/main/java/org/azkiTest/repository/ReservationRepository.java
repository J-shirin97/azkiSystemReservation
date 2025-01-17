package org.azkiTest.repository;

import org.azkiTest.model.AvailableSlot;
import org.azkiTest.model.Reservation;
import org.azkiTest.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByUserAndSlot(Users user, AvailableSlot slot);

}
