package org.example.bike_rent.service;

import org.example.bike_rent.entity.reservation.Reservation;
import org.example.bike_rent.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.bike_rent.repository.BikeRepository;
import org.example.bike_rent.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BikeRepository bikeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, BikeRepository bikeRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bikeRepository = bikeRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public Optional<Reservation> getActiveReservationForBike(Long bikeId) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getBike().getId().equals(bikeId) && (r.getEndDate() == null || !r.getEndDate().isBefore(java.time.LocalDateTime.now())))
                .findFirst();
    }

    public Optional<Reservation> createReservationForCurrentUser(Long bikeId) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        org.example.bike_rent.entity.user.User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return Optional.empty();
        org.example.bike_rent.entity.bike.Bike bike = bikeRepository.findById(bikeId).orElse(null);
        if (bike == null || !bike.isAvailable()) return Optional.empty();
        if (getActiveReservationForBike(bikeId).isPresent()) return Optional.empty();
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBike(bike);
        reservation.setStartDate(java.time.LocalDateTime.now());
        reservation.setEndDate(null);
        return Optional.of(reservationRepository.save(reservation));
    }

    public List<Reservation> getAllActiveReservations() {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getEndDate() == null || !r.getEndDate().isBefore(java.time.LocalDateTime.now()))
                .toList();
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getUser() != null && r.getUser().getId() == userId)
                .toList();
    }
}
