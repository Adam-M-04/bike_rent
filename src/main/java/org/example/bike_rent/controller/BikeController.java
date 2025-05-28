package org.example.bike_rent.controller;

import java.util.List;
import java.util.Optional;
import org.example.bike_rent.entity.bike.Bike;
import org.example.bike_rent.service.BikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Bikes", description = "Endpoints for managing bikes")
@RestController
@RequestMapping("/api/bikes")
public class BikeController {

    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @Operation(summary = "Get all bikes", description = "Returns a list of all bikes")
    @GetMapping
    public List<Bike> getAllBikes() {
        return bikeService.getAllBikes();
    }

    @Operation(summary = "Get bike by ID", description = "Returns a bike by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Bike> getBike(@PathVariable Long id) {
        Optional<Bike> bike = bikeService.getBikeById(id);
        return bike.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new bike", description = "Creates a new bike (admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Bike createBike(@RequestBody Bike bike) {
        return bikeService.saveBike(bike);
    }

    @Operation(summary = "Update a bike", description = "Updates an existing bike by ID (admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Bike> updateBike(@PathVariable Long id, @RequestBody Bike bikeDetails) {
        Optional<Bike> optionalBike = bikeService.getBikeById(id);
        if (optionalBike.isPresent()) {
            Bike bike = optionalBike.get();
            bike.setModel(bikeDetails.getModel());
            bike.setBrand(bikeDetails.getBrand());
            bike.setColor(bikeDetails.getColor());
            bike.setAvailable(bikeDetails.isAvailable());
            bike.setSerialNumber(bikeDetails.getSerialNumber());
            Bike updatedBike = bikeService.saveBike(bike);
            return ResponseEntity.ok(updatedBike);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a bike", description = "Deletes a bike by ID (admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBike(@PathVariable Long id) {
        bikeService.deleteBike(id);
        return ResponseEntity.ok().build();
    }
}

