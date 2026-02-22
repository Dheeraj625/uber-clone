package com.uber.uber_clone.controller;

import com.uber.uber_clone.dto.CreateDriverRequest;
import com.uber.uber_clone.entity.Driver;
import com.uber.uber_clone.service.DriverService;
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
}