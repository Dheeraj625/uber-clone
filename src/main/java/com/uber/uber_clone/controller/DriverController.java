package com.uber.uber_clone.controller;

import com.uber.uber_clone.dto.CreateDriverRequest;
import com.uber.uber_clone.dto.UpdateDriverLocationRequest;
import com.uber.uber_clone.entity.Driver;
import com.uber.uber_clone.service.DriverService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/create")
    public Driver createDriver(@RequestBody CreateDriverRequest request) {
        return driverService.createDriver(request);
    }

    @PostMapping("/availability")
    public Driver setAvailability(@RequestParam Long userId,
                                  @RequestParam boolean available) {
        return driverService.setAvailability(userId, available);
    }

    @PutMapping("/{driverId}/location")
    public Driver updateLocation(
        @PathVariable Long driverId,
        @RequestBody UpdateDriverLocationRequest request) {

        return driverService.updateDriverLocation(
            driverId,
            request.getLatitude(),
            request.getLongitude()
        );
    }
    @GetMapping("/by-user/{userId}")
    public Driver getDriverByUserId(@PathVariable Long userId) {
        return driverService.getDriverByUserId(userId);
    }

    @GetMapping("/nearby")
    public List<Driver> getNearbyDrivers(
        @RequestParam double lat,
        @RequestParam double lng,
        @RequestParam double radiusKm) {

    return driverService.findNearbyDrivers(lat, lng, radiusKm);
    }
}