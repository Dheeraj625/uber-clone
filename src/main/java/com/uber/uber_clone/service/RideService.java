package com.uber.uber_clone.service;

import com.uber.uber_clone.dto.DriverResponseDTO;
import com.uber.uber_clone.dto.RideRequestDTO;
import com.uber.uber_clone.dto.RideResponseDTO;
import com.uber.uber_clone.dto.UserResponseDTO;
import com.uber.uber_clone.entity.*;
import com.uber.uber_clone.repository.DriverRepository;
import com.uber.uber_clone.repository.RideRepository;
import com.uber.uber_clone.repository.UserRepository;
import com.uber.uber_clone.utility.DistanceUtil;
//day 10
import jakarta.annotation.PostConstruct;
import java.util.List;
import com.uber.uber_clone.utility.DistanceUtil;

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

        /*  2. Find Available Driver
        Driver driver = driverRepository.findFirstByAvailableTrue();
        if (driver == null) {
            throw new RuntimeException("No drivers available");
        }*/
        List<Driver> availableDrivers = driverRepository.findByAvailableTrue();
        System.out.println("Available drivers count = " + availableDrivers.size());

        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No drivers available");
        }

    Driver nearestDriver = null;
    double minDistance = Double.MAX_VALUE;

    /*for (Driver d : availableDrivers) {

        double distance = DistanceUtil.calculateDistance(
            d.getCurrentLatitude(),
            d.getCurrentLongitude(),
            requestDTO.getPickupLatitude(),
            requestDTO.getPickupLongitude()
        );

        if (distance < minDistance) {
        minDistance = distance;
        nearestDriver = d;
        }
    }*/
    double searchRadiusKm = 50;   // Only drivers within 5 km

    for (Driver d : availableDrivers) {
        // Skip drivers without location
    if (d.getCurrentLatitude() == null || d.getCurrentLongitude() == null) {
        continue;
    }

    double distance = DistanceUtil.calculateDistance(
        d.getCurrentLatitude(),
        d.getCurrentLongitude(),
        requestDTO.getPickupLatitude(),
        requestDTO.getPickupLongitude()
    );

    // Ignore far drivers
    if (distance > searchRadiusKm) {
        continue;
    }

        if (distance < minDistance) {
            minDistance = distance;
            nearestDriver = d;
        }
    }
    if (nearestDriver == null) {
        throw new RuntimeException("No drivers available nearby");
    }

    Driver driver = nearestDriver;
    System.out.println("Nearest Driver ID = " + driver.getId());
    System.out.println("Distance = " + minDistance + " km");

        // 3. Create Ride
        Ride ride = new Ride();
        ride.setRider(rider);
        ride.setDriver(driver);
        //ride.setPickupLocation(requestDTO.getPickupLocation());
        //ride.setDropLocation(requestDTO.getDropLocation());
        //day 11 change 
        ride.setPickupLocation(
            requestDTO.getPickupLatitude() + ", " + requestDTO.getPickupLongitude()
        );

        ride.setDropLocation(
            requestDTO.getDropLatitude() + ", " + requestDTO.getDropLongitude()
        );

        // ===== Day 9: Save Coordinates =====
        ride.setPickupLatitude(requestDTO.getPickupLatitude());
        ride.setPickupLongitude(requestDTO.getPickupLongitude());
        ride.setDropLatitude(requestDTO.getDropLatitude());
        ride.setDropLongitude(requestDTO.getDropLongitude());

        ride.setStatus(RideStatus.REQUESTED);

        // ===== ADD FARE LOGIC HERE =====
        // ===== Day 10: Distance-based Fare =====
        double tripDistance = DistanceUtil.calculateDistance(
            requestDTO.getPickupLatitude(),
            requestDTO.getPickupLongitude(),
            requestDTO.getDropLatitude(),
            requestDTO.getDropLongitude()
        );

        double baseFare = 50;          // fixed charge
        double perKmRate = 12;         // ₹ per km

        double fare = baseFare + (tripDistance * perKmRate);
        ride.setFare(fare);

        // Debug (optional)
        System.out.println("Trip Distance = " + tripDistance + " km");
        System.out.println("Calculated Fare = ₹" + fare);


        // 4. Mark Driver Busy
        driver.setAvailable(false);
        driverRepository.save(driver);

        // 5. Save Ride
        System.out.println("Before saving ride");

        Ride savedRide = rideRepository.save(ride);

        System.out.println("After saving ride");

        return savedRide;
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
    public RideResponseDTO convertToDTO(Ride ride) {

        User rider = ride.getRider();
        UserResponseDTO riderDTO =
            new UserResponseDTO(rider.getId(), rider.getName(), rider.getEmail(), rider.getRole().name());

        Driver driver = ride.getDriver();
        User driverUser = driver.getUser();
        UserResponseDTO driverUserDTO =
            new UserResponseDTO(driverUser.getId(), driverUser.getName(), driverUser.getEmail(), driverUser.getRole().name());

        DriverResponseDTO driverDTO =
            new DriverResponseDTO(driver.getId(), driverUserDTO, driver.getVehicleNumber(), driver.isAvailable());

        return new RideResponseDTO(
            ride.getId(),
            ride.getPickupLocation(),
            ride.getDropLocation(),
            ride.getStatus().name(),
            ride.getFare(),
            riderDTO,
            driverDTO
        );
    }
    @PostConstruct
    public void testDistance() {
    double distance = DistanceUtil.calculateDistance(
            22.7196, 75.8577,
            23.2599, 77.4126
        );
        System.out.println("Distance Test = " + distance);
    }
}
