package com.uber.uber_clone.service;

import com.uber.uber_clone.dto.RideRequestDTO;
import com.uber.uber_clone.entity.*;
import com.uber.uber_clone.repository.DriverRepository;
import com.uber.uber_clone.repository.RideRepository;
import com.uber.uber_clone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}