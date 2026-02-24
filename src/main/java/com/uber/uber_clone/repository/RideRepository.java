package com.uber.uber_clone.repository;

import com.uber.uber_clone.entity.Ride;
import com.uber.uber_clone.entity.User;
import com.uber.uber_clone.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    // Get rides by rider
    List<Ride> findByRider(User rider);

    // Get rides by driver
    List<Ride> findByDriver(Driver driver);
}