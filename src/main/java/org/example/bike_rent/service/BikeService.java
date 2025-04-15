package org.example.bike_rent.service;

import java.util.List;
import java.util.Optional;
import org.example.bike_rent.entity.bike.Bike;
import org.example.bike_rent.repository.BikeRepository;
import org.springframework.stereotype.Service;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public Optional<Bike> getBikeById(Long id) {
        return bikeRepository.findById(id);
    }

    public Bike saveBike(Bike bike) {
        return bikeRepository.save(bike);
    }

    public void deleteBike(Long id) {
        bikeRepository.deleteById(id);
    }
}