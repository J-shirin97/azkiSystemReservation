package org.azkiTest.service;

import org.azkiTest.exception.*;
import org.azkiTest.model.AvailableSlot;
import org.azkiTest.model.Reservation;
import org.azkiTest.model.Users;
import org.azkiTest.repository.AvailableSlotRepository;
import org.azkiTest.repository.ReservationRepository;
import org.azkiTest.repository.UsersRepository;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationSlotService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationSlotService.class);

    private final ReservationRepository reservationRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public ReservationSlotService(ReservationRepository reservationRepository,
                                  AvailableSlotRepository availableSlotRepository,
                                  UsersRepository usersRepository) {
        this.reservationRepository = reservationRepository;
        this.availableSlotRepository = availableSlotRepository;
        this.usersRepository = usersRepository;
    }

    public Page<AvailableSlot> getAvailableSlots(Integer size, Integer page) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return availableSlotRepository.findAllByIsReservedFalse(pageable);
        } catch (Exception e) {
            logger.error("Error fetching available slots: {}", e.getMessage(), e);
            throw new ServiceException("Error fetching available slots", e);
        }
    }

    @Transactional
    public void reserveSlot(Long slotId, Long userId) {
        try {
            AvailableSlot slot = availableSlotRepository.findSlotForReservation(slotId)
                    .orElseThrow(() -> new SlotNotFoundException("Slot not found for ID: " + slotId));

            if (slot.getIsReserved()) {
                throw new SlotAlreadyReservedException("Slot is already reserved. ID: " + slotId);
            }

            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));

            boolean exists = reservationRepository.existsByUserAndSlot(user, slot);
            if (exists) {
                throw new ReservationAlreadyExistsException("This user has already reserved this slot.");
            }

            slot.setIsReserved(true);
            availableSlotRepository.save(slot);

            Reservation reservation = new Reservation();
            reservation.setId(slot.getId());
            reservation.setSlot(slot);
            reservation.setUser(user);
            reservation.setReservedAt(LocalDateTime.now());

            reservationRepository.save(reservation);
            logger.info("Reservation created successfully: {}", reservation);

        } catch (Exception e) {
            logger.error("Error reserving slot: {}", e.getMessage(), e);
            throw new ServiceException("Error reserving slot", e);
        }
    }


    @Transactional
    public void cancelReservation(Long id) throws ReservationServiceException {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for ID: " + id));

            AvailableSlot availableSlot = reservation.getSlot();
            availableSlot.getReservations().remove(reservation);

            if (availableSlot.getReservations().isEmpty()) {
                availableSlot.setIsReserved(false);
            }
            availableSlotRepository.save(availableSlot);
            reservationRepository.delete(reservation);

            logger.info("Reservation canceled successfully. ID: {}", id);

        } catch (Exception e) {
            logger.error("Error canceling reservation: {}", e.getMessage(), e);
            throw new ReservationServiceException("Error canceling reservation", e);
        }
    }

    public List<Reservation> getReservation() throws ReservationServiceException {
        try {
            return reservationRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching reservations: {}", e.getMessage(), e);
            throw new ReservationServiceException("Error fetching reservations", e);
        }
    }

}
