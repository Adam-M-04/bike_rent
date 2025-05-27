package org.example.bike_rent.controller;

import org.example.bike_rent.entity.reservation.Reservation;
import org.example.bike_rent.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    private ReservationService reservationService;
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        reservationService = Mockito.mock(ReservationService.class);
        reservationController = new ReservationController(reservationService);
    }

    @Test
    void testGetAllReservations() {
        Reservation res1 = new Reservation();
        res1.setId(1L);
        res1.setStartDate(LocalDateTime.now());

        Reservation res2 = new Reservation();
        res2.setId(2L);
        res2.setStartDate(LocalDateTime.now().plusDays(1));

        when(reservationService.getAllReservations()).thenReturn(Arrays.asList(res1, res2));

        List<Reservation> reservations = reservationController.getAllReservations();

        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        assertEquals(1L, reservations.get(0).getId());

        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void testGetReservationByIdExists() {
        Reservation res = new Reservation();
        res.setId(1L);

        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(res));

        ResponseEntity<Reservation> response = reservationController.getReservationById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());

        verify(reservationService, times(1)).getReservationById(1L);
    }

    @Test
    void testGetReservationByIdNotFound() {
        when(reservationService.getReservationById(100L)).thenReturn(Optional.empty());

        ResponseEntity<Reservation> response = reservationController.getReservationById(100L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(reservationService, times(1)).getReservationById(100L);
    }
}