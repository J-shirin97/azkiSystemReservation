package test;

import org.azkiTest.exception.ReservationServiceException;
import org.azkiTest.model.AvailableSlot;
import org.azkiTest.model.Reservation;
import org.azkiTest.model.Users;
import org.azkiTest.repository.AvailableSlotRepository;
import org.azkiTest.repository.ReservationRepository;
import org.azkiTest.repository.UsersRepository;
import org.azkiTest.service.ReservationSlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationSlotServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private AvailableSlotRepository availableSlotRepository;
    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private ReservationSlotService reservationSlotService;

    private AvailableSlot availableSlot;
    private Users user;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock data for testing
        availableSlot = new AvailableSlot();
        availableSlot.setId(1L);
        availableSlot.setIsReserved(false);

        user = new Users();
        user.setId(1L);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setSlot(availableSlot);
        reservation.setUser(user);
        reservation.setReservedAt(LocalDateTime.now());
    }

    @Test
    void testGetAvailableSlots() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AvailableSlot> page = new PageImpl<>(Collections.singletonList(availableSlot));
        when(availableSlotRepository.findAllByIsReservedFalse(pageable)).thenReturn(page);

        Page<AvailableSlot> result = reservationSlotService.getAvailableSlots(10, 0);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testReserveSlot_Success() {
        when(availableSlotRepository.findSlotForReservation(1L)).thenReturn(Optional.of(availableSlot));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reservationRepository.existsByUserAndSlot(user, availableSlot)).thenReturn(false);

        reservationSlotService.reserveSlot(1L, 1L);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        assertTrue(availableSlot.getIsReserved());
    }

    @Test
    void cancelReservation_Success() throws ReservationServiceException {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        AvailableSlot availableSlot = new AvailableSlot();
        reservation.setSlot(availableSlot);
        availableSlot.setReservations(new HashSet<>(Collections.singleton(reservation)));

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        reservationSlotService.cancelReservation(reservationId);

        // Assert
        verify(reservationRepository, times(1)).delete(reservation);
        verify(availableSlotRepository, times(1)).save(availableSlot);
        assertFalse(availableSlot.getIsReserved());
        assertTrue(availableSlot.getReservations().isEmpty());
    }


    @Test
    void testGetReservations() throws ReservationServiceException {
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        List<Reservation> result = reservationSlotService.getReservation();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetReservations_Error() {
        when(reservationRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        ReservationServiceException exception = assertThrows(ReservationServiceException.class, () -> {
            reservationSlotService.getReservation();
        });

        assertEquals("Error fetching reservations", exception.getMessage());
    }


    @Test
    public void testReserveSlot_SuccessfulReservation() {
        // Arrange
        Long slotId = 1L;
        Long userId = 1L;

        AvailableSlot slot = new AvailableSlot();
        slot.setId(slotId);
        slot.setIsReserved(false);

        Users user = new Users();
        user.setId(userId);

        Reservation reservation = new Reservation();
        reservation.setId(slotId);
        reservation.setSlot(slot);
        reservation.setUser(user);
        reservation.setReservedAt(LocalDateTime.now());

        Mockito.when(availableSlotRepository.findSlotForReservation(slotId))
                .thenReturn(Optional.of(slot));
        Mockito.when(usersRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(reservationRepository.existsByUserAndSlot(user, slot))
                .thenReturn(false); // User doesn't have a reservation

        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class)))
                .thenReturn(reservation);

        // Act
        reservationSlotService.reserveSlot(slotId, userId);

        // Assert
        Mockito.verify(reservationRepository, Mockito.times(1)).save(Mockito.any(Reservation.class));
        Mockito.verify(availableSlotRepository, Mockito.times(1)).save(Mockito.any(AvailableSlot.class));
    }


}


