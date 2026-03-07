package com.uber.uber_clone.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uber.uber_clone.service.DriverLocationService;

@Controller
public class LocationController {
    @Autowired
    private DriverLocationService driverLocationService;

    @MessageMapping("/driver/location")
    @SendTo("/topic/driver/location")
    public LocationMessage broadcastDriverLocation(LocationMessage message) {
        // Save location in Redis
        driverLocationService.saveDriverLocation(
                message.getDriverId(),
                message.getLatitude(),
                message.getLongitude()
        );

        // Send update to riders
        return message;
    }
}