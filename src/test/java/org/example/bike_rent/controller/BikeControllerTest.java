package org.example.bike_rent.controller;

import org.example.bike_rent.entity.bike.Bike;
import org.example.bike_rent.service.BikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BikeControllerTest {

    private BikeService bikeService;
    private BikeController bikeController;

    @BeforeEach
    void setUp() {
        bikeService = Mockito.mock(BikeService.class);
        bikeController = new BikeController(bikeService);
    }

    @Test
    void testGetAllBikes() {
        Bike bike1 = new Bike();
        bike1.setId(1L);
        bike1.setModel("Model A");

        Bike bike2 = new Bike();
        bike2.setId(2L);
        bike2.setModel("Model B");

        when(bikeService.getAllBikes()).thenReturn(Arrays.asList(bike1, bike2));

        List<Bike> bikes = bikeController.getAllBikes();

        assertNotNull(bikes);
        assertEquals(2, bikes.size());
        assertEquals("Model A", bikes.get(0).getModel());

        verify(bikeService, times(1)).getAllBikes();
    }

    @Test
    void testGetBikeByIdExists() {
        Bike bike = new Bike();
        bike.setId(1L);
        bike.setModel("Model A");

        when(bikeService.getBikeById(1L)).thenReturn(Optional.of(bike));

        ResponseEntity<Bike> response = bikeController.getBike(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Model A", response.getBody().getModel());

        verify(bikeService, times(1)).getBikeById(1L);
    }

    @Test
    void testGetBikeByIdNotFound() {
        when(bikeService.getBikeById(100L)).thenReturn(Optional.empty());

        ResponseEntity<Bike> response = bikeController.getBike(100L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(bikeService, times(1)).getBikeById(100L);
    }

    @Test
    void testCreateBike() {
        Bike inputBike = new Bike();
        inputBike.setModel("Model X");

        Bike savedBike = new Bike();
        savedBike.setId(1L);
        savedBike.setModel("Model X");

        when(bikeService.saveBike(inputBike)).thenReturn(savedBike);

        Bike result = bikeController.createBike(inputBike);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Model X", result.getModel());

        verify(bikeService, times(1)).saveBike(inputBike);
    }

    @Test
    void testUpdateBike() {
        Bike existingBike = new Bike();
        existingBike.setId(1L);
        existingBike.setModel("Old Model");

        Bike updatedData = new Bike();
        updatedData.setModel("Updated Model");

        Bike updatedBike = new Bike();
        updatedBike.setId(1L);
        updatedBike.setModel("Updated Model");

        when(bikeService.getBikeById(1L)).thenReturn(Optional.of(existingBike));
        when(bikeService.saveBike(any(Bike.class))).thenReturn(updatedBike);

        ResponseEntity<Bike> response = bikeController.updateBike(1L, updatedData);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Updated Model", response.getBody().getModel());

        verify(bikeService, times(1)).getBikeById(1L);
        verify(bikeService, times(1)).saveBike(any(Bike.class));
    }

    @Test
    void testDeleteBike() {
        doNothing().when(bikeService).deleteBike(1L);

        ResponseEntity<?> response = bikeController.deleteBike(1L);
        assertEquals(200, response.getStatusCodeValue());

        verify(bikeService, times(1)).deleteBike(1L);
    }
}