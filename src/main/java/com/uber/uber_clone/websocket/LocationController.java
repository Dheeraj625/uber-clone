package com.uber.uber_clone.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.uber.uber_clone.service.DriverLocationService;
import com.uber.uber_clone.service.RedisGeoService;

import org.springframework.messaging.handler.annotation.Payload;

@Controller
public class LocationController {

    @Autowired
    private DriverLocationService driverLocationService;

    @Autowired
    private RedisGeoService redisGeoService;

    @MessageMapping("/driver/location")
    @SendTo("/topic/driver/location")
    public LocationMessage broadcastDriverLocation(LocationMessage message) {
        System.out.println("WebSocket message received");
        driverLocationService.saveDriverLocation(
                message.getDriverId(),
                message.getLatitude(),
                message.getLongitude()
        );
        System.out.println("Updating Redis GEO for driver: " + message.getDriverId());
        redisGeoService.updateDriverLocation(
                message.getDriverId(),
                message.getLatitude(),
                message.getLongitude()
        );

        return message;
    }
}