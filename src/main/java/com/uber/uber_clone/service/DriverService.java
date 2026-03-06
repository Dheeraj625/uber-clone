package com.uber.uber_clone.service;

import com.uber.uber_clone.dto.CreateDriverRequest;
import com.uber.uber_clone.entity.Driver;
import com.uber.uber_clone.entity.User;
import com.uber.uber_clone.repository.DriverRepository;
import com.uber.uber_clone.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    public DriverService(DriverRepository driverRepository,
                         UserRepository userRepository) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
    }

    public Driver createDriver(CreateDriverRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Driver driver = new Driver();
        driver.setUser(user);
        driver.setVehicleNumber(request.getVehicleNumber());
        driver.setAvailable(false); // initially offline

        return driverRepository.save(driver);
    }

    public Driver setAvailability(Long userId, boolean available) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Driver profile not found"));

        driver.setAvailable(available);

        return driverRepository.save(driver);
    }
    //day 9 came from driver controller 
    public Driver updateDriverLocation(Long driverId, Double lat, Double lon) {
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setCurrentLatitude(lat);
        driver.setCurrentLongitude(lon);

        return driverRepository.save(driver);
    }
    public Driver getDriverByUserId(Long userId) {
        return driverRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    public List<Driver> findNearbyDrivers(double lat, double lng, double radiusKm) {
        List<Driver> allDrivers = driverRepository.findByAvailableTrue();

        return allDrivers.stream()
            .filter(driver -> {
                double distance = distanceKm(
                        lat, lng,
                        driver.getCurrentLatitude(),
                        driver.getCurrentLongitude()
                );
                return distance <= radiusKm;
            })
            .toList();
    }
    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
            + Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
    //day 16
    public Driver getDriverLocation(Long driverId) {
        return driverRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Driver not found"));
    }
}