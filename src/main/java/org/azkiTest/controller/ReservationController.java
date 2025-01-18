
package org.azkiTest.controller;

import lombok.RequiredArgsConstructor;
import org.azkiTest.DTOs.ReservationRequest;
import org.azkiTest.exception.ReservationServiceException;
import org.azkiTest.model.AvailableSlot;
import org.azkiTest.model.Reservation;
import org.azkiTest.service.ReservationSlotService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationSlotService reservationSlotService;

    @GetMapping("/slots")
    public ResponseEntity<Page<AvailableSlot>> getAvailableSlots(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Page<AvailableSlot> slots = reservationSlotService.getAvailableSlots(size, page);
        return ResponseEntity.ok(slots);
    }

    @PostMapping("/reservations")
    public ResponseEntity<String> reserveSlot(@RequestBody ReservationRequest reservationRequest,
                                              @RequestHeader(value = "Authorization", required = false) String token ) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }

        reservationSlotService.reserveSlot(reservationRequest.getSlotId(), reservationRequest.getUserId());
        return ResponseEntity.ok("Reservation successful!");
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id,
                                                    @RequestHeader(value = "Authorization", required = false) String token) throws ReservationServiceException {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }

        reservationSlotService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled!");
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations(@RequestHeader(value = "Authorization", required = false) String token) throws ReservationServiceException {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Reservation> reservations = reservationSlotService.getReservation();
        return ResponseEntity.ok(reservations);
    }
}
