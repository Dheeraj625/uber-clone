package com.uber.uber_clone.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {

    @MessageMapping("/driver/location")
    @SendTo("/topic/driver/location")
    public LocationMessage broadcastDriverLocation(LocationMessage message) {

        return message;
    }
}