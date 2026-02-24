package com.uber.uber_clone.service;

import com.uber.uber_clone.dto.RideRequestDTO;
import com.uber.uber_clone.entity.*;
import com.uber.uber_clone.repository.DriverRepository;
import com.uber.uber_clone.repository.RideRepository;
import com.uber.uber_clone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;

@Service

public class RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    public RideService(RideRepository rideRepository,
                       DriverRepository driverRepository,
                       UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
    }

    public Ride requestRide(RideRequestDTO requestDTO) {

        // 1. Find Rider
        Optional<User> riderOptional = userRepository.findById(requestDTO.getRiderId());
        if (riderOptional.isEmpty()) {
            throw new RuntimeException("Rider not found");
        }
        User rider = riderOptional.get();

        // 2. Find Available Driver
        Driver driver = driverRepository.findFirstByAvailableTrue();
        if (driver == null) {
            throw new RuntimeException("No drivers available");
        }

        // 3. Create Ride
        Ride ride = new Ride();
        ride.setRider(rider);
        ride.setDriver(driver);
        ride.setPickupLocation(requestDTO.getPickupLocation());
        ride.setDropLocation(requestDTO.getDropLocation());
        ride.setStatus(RideStatus.REQUESTED);

        // 4. Mark Driver Busy
        driver.setAvailable(false);
        driverRepository.save(driver);

        // 5. Save Ride
        return rideRepository.save(ride);
    }
    //DAY 6
    public Ride acceptRide(Long rideId) {
        // 1. Find ride
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // 2. Check current status
        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new RuntimeException("Ride cannot be accepted. Current status: " + ride.getStatus());
        }

        // 3. Update status
        ride.setStatus(RideStatus.ACCEPTED);

        // 4. Save and return
        return rideRepository.save(ride);
    }
    public Ride startRide(Long rideId) {
        // 1. Find ride
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // 2. Check current status
        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new RuntimeException("Ride cannot be STARTED. Current status: " + ride.getStatus());
        }

        // 3. Update status
        ride.setStatus(RideStatus.STARTED);

        // 5. Save and return
        return rideRepository.save(ride);
    }

    public Ride completeRide(Long rideId) {
        // 1. Find ride
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // 2. Check current status
        if (ride.getStatus() != RideStatus.STARTED) {
            throw new RuntimeException("Ride cannot be COMPLETED. Current status: " + ride.getStatus());
        }

        // 3. Update status
        ride.setStatus(RideStatus.COMPLETED);

        // 4. Make driver available again
        Driver driver = ride.getDriver();
        driver.setAvailable(true);
        driverRepository.save(driver);
        
        // 5. Save and return
        return rideRepository.save(ride);
    }
    public List<Ride> getRidesByRider(Long riderId) {

        User rider = userRepository.findById(riderId)
            .orElseThrow(() -> new RuntimeException("Rider not found"));

        return rideRepository.findByRider(rider);
    }

    public List<Ride> getRidesByDriver(Long userId) {

        // Step 1: Find user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Find driver using user
        Driver driver = driverRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Step 3: Fetch rides
        return rideRepository.findByDriver(driver);
    }

    public Ride getRideById(Long rideId) {

        return rideRepository.findById(rideId)
            .orElseThrow(() -> new RuntimeException("Ride not found"));
    }

}