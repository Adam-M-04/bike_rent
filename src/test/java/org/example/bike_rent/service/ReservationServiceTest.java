package org.example.bike_rent.service;

import org.example.bike_rent.entity.bike.Bike;
import org.example.bike_rent.entity.reservation.Reservation;
import org.example.bike_rent.entity.user.User;
import org.example.bike_rent.repository.BikeRepository;
import org.example.bike_rent.repository.ReservationRepository;
import org.example.bike_rent.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {
    private ReservationRepository reservationRepository;
    private BikeRepository bikeRepository;
    private UserRepository userRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        bikeRepository = Mockito.mock(BikeRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        reservationService = new ReservationService(reservationRepository, bikeRepository, userRepository);
    }

    @Test
    void testGetAllReservations() {
        Reservation r1 = new Reservation(); r1.setId(1L);
        Reservation r2 = new Reservation(); r2.setId(2L);
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(r1, r2));
        List<Reservation> result = reservationService.getAllReservations();
        assertEquals(2, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void testGetReservationById() {
        Reservation r = new Reservation(); r.setId(1L);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        Optional<Reservation> result = reservationService.getReservationById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(reservationRepository).findById(1L);
    }

    @Test
    void testCreateReservation() {
        Reservation r = new Reservation();
        when(reservationRepository.save(r)).thenReturn(r);
        Reservation result = reservationService.createReservation(r);
        assertEquals(r, result);
        verify(reservationRepository).save(r);
    }

    @Test
    void testDeleteReservation() {
        reservationService.deleteReservation(1L);
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void testGetActiveReservationForBike_found() {
        Bike bike = new Bike(); bike.setId(1L);
        Reservation r = new Reservation(); r.setBike(bike); r.setEndDate(LocalDateTime.now().plusDays(1));
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(r));
        Optional<Reservation> result = reservationService.getActiveReservationForBike(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testGetActiveReservationForBike_notFound() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList());
        Optional<Reservation> result = reservationService.getActiveReservationForBike(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateReservationForCurrentUser_success() {
        // Mock authentication
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);
        // Mock user and bike
        User user = new User(); user.setEmail("user@example.com");
        Bike bike = new Bike(); bike.setId(1L); bike.setAvailable(true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(bikeRepository.findById(1L)).thenReturn(Optional.of(bike));
        when(reservationRepository.findAll()).thenReturn(Arrays.asList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));
        Optional<Reservation> result = reservationService.createReservationForCurrentUser(1L);
        assertTrue(result.isPresent());
        assertEquals(user, result.get().getUser());
        assertEquals(bike, result.get().getBike());
    }

    @Test
    void testCreateReservationForCurrentUser_noUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        Optional<Reservation> result = reservationService.createReservationForCurrentUser(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateReservationForCurrentUser_noBike() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = new User(); user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(bikeRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Reservation> result = reservationService.createReservationForCurrentUser(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateReservationForCurrentUser_bikeNotAvailable() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = new User(); user.setEmail("user@example.com");
        Bike bike = new Bike(); bike.setId(1L); bike.setAvailable(false);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(bikeRepository.findById(1L)).thenReturn(Optional.of(bike));
        Optional<Reservation> result = reservationService.createReservationForCurrentUser(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateReservationForCurrentUser_bikeAlreadyReserved() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = new User(); user.setEmail("user@example.com");
        Bike bike = new Bike(); bike.setId(1L); bike.setAvailable(true);
        Reservation existing = new Reservation(); existing.setBike(bike);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(bikeRepository.findById(1L)).thenReturn(Optional.of(bike));
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(existing));
        Optional<Reservation> result = reservationService.createReservationForCurrentUser(1L);
        assertFalse(result.isPresent());
    }
}

