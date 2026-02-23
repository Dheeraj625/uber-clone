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

    @PostMapping("/accept/{rideId}")
    public Ride acceptRide(@PathVariable Long rideId) {
        return rideService.acceptRide(rideId);
    }
    @PostMapping("/start/{rideId}")
    public Ride startRide(@PathVariable Long rideId) {
        return rideService.startRide(rideId);
    }
    @PostMapping("/complete/{rideId}")
    public Ride completeRide(@PathVariable Long rideId) {
        return rideService.completeRide(rideId);
    }
    @GetMapping("/")
    public String home() {
        return "Uber Backend Running";
    }
}
