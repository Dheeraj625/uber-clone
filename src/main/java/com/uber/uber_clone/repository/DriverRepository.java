package com.uber.uber_clone.repository;

import com.uber.uber_clone.entity.Driver;
import com.uber.uber_clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUser(User user);

}