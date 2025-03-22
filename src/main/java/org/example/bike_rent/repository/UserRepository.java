package org.example.bike_rent.repository;

import org.example.bike_rent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}