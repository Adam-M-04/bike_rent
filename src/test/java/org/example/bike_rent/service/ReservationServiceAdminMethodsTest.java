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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceAdminMethodsTest {
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
    void testGetAllActiveReservations() {
        Reservation active1 = new Reservation();
        active1.setId(1L);
        active1.setEndDate(null);
        Reservation active2 = new Reservation();
        active2.setId(2L);
        active2.setEndDate(LocalDateTime.now().plusDays(1));
        Reservation ended = new Reservation();
        ended.setId(3L);
        ended.setEndDate(LocalDateTime.now().minusDays(1));
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(active1, active2, ended));
        List<Reservation> result = reservationService.getAllActiveReservations();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getId() == 1L));
        assertTrue(result.stream().anyMatch(r -> r.getId() == 2L));
        assertFalse(result.stream().anyMatch(r -> r.getId() == 3L));
    }

    @Test
    void testGetReservationsByUser() {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setUser(user1);
        Reservation r2 = new Reservation();
        r2.setId(2L);
        r2.setUser(user2);
        Reservation r3 = new Reservation();
        r3.setId(3L);
        r3.setUser(user1);
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(r1, r2, r3));
        List<Reservation> result = reservationService.getReservationsByUser(1L);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getUser().getId() == 1));
    }

    @Test
    void testGetReservationsByUser_empty() {
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());
        List<Reservation> result = reservationService.getReservationsByUser(99L);
        assertTrue(result.isEmpty());
    }
}

