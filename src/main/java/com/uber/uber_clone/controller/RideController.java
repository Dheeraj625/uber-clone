package com.uber.uber_clone.controller;

import com.uber.uber_clone.dto.RideRequestDTO;
import com.uber.uber_clone.entity.Ride;
import com.uber.uber_clone.service.RideService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/request")
    public Ride requestRide(@RequestBody RideRequestDTO requestDTO) {
        return rideService.requestRide(requestDTO);
    }
}
