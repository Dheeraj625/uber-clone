package com.uber.uber_clone.service;

import com.uber.uber_clone.entity.Driver;
import com.uber.uber_clone.entity.User;
import com.uber.uber_clone.repository.DriverRepository;
import com.uber.uber_clone.repository.UserRepository;
import com.uber.uber_clone.utility.DistanceUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//day 3 added
import com.uber.uber_clone.dto.LoginRequest;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DriverRepository driverRepository;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        // ===== Day 13 Logic =====
        if (savedUser.getRole().name().equals("DRIVER")) {

            Driver driver = new Driver();
            driver.setUser(savedUser);
            driver.setAvailable(false);
            driver.setCurrentLatitude(0.0);
            driver.setCurrentLongitude(0.0);
            driver.setVehicleNumber("TEMP-" + savedUser.getId());

            driverRepository.save(driver);
        }
        // ========================
        return savedUser;
    }
    //day 3
    public User login(LoginRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}



