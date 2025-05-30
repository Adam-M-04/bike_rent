package org.example.bike_rent.controller;

import org.example.bike_rent.entity.reservation.Reservation;
import org.example.bike_rent.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Reservations", description = "Endpoints for managing reservations")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Get all reservations", description = "Returns a list of all reservations. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @Operation(summary = "Get all active reservations", description = "Returns all reservations that are currently active (end date is null or in the future)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public List<Reservation> getAllActiveReservations() {
        return reservationService.getAllActiveReservations();
    }

    @Operation(summary = "Get all reservations for a user", description = "Returns all reservations for a specific user by their user ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestParam Long bikeId) {
        Optional<Reservation> activeReservation = reservationService.getActiveReservationForBike(bikeId);
        if (activeReservation.isPresent()) {
            return ResponseEntity.badRequest().body("Bike is not available");
        }
        Optional<Reservation> reservation = reservationService.createReservationForCurrentUser(bikeId);
        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservation.get());
        } else {
            return ResponseEntity.badRequest().body("Could not create reservation");
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{id}/end")
    public ResponseEntity<?> endReservation(@PathVariable Long id) {
        Optional<Reservation> reservationOpt = reservationService.getReservationById(id);
        if (reservationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reservation reservation = reservationOpt.get();
        String currentEmail = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        if (!reservation.getUser().getEmail().equals(currentEmail)) {
            return ResponseEntity.status(403).body("You can only end your own reservation");
        }
        if (reservation.getEndDate() != null) {
            return ResponseEntity.badRequest().body("Reservation already ended");
        }
        reservation.setEndDate(java.time.LocalDateTime.now());
        reservationService.createReservation(reservation);
        return ResponseEntity.ok(reservation);
    }
}
