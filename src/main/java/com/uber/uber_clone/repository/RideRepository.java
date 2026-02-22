package com.uber.uber_clone.repository;

import com.uber.uber_clone.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
}