package com.uber.uber_clone.service;

import com.uber.uber_clone.dto.CreateDriverRequest;
import com.uber.uber_clone.entity.Driver;
import com.uber.uber_clone.entity.User;
import com.uber.uber_clone.repository.DriverRepository;
import com.uber.uber_clone.repository.UserRepository;
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
}