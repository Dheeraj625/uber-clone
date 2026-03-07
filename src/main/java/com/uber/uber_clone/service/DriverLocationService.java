package com.uber.uber_clone.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DriverLocationService {

    private final RedisTemplate<String, Object> redisTemplate;

    public DriverLocationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveDriverLocation(Long driverId, Double lat, Double lng) {

        String key = "driver:" + driverId;

        redisTemplate.opsForHash().put(key, "lat", lat);
        redisTemplate.opsForHash().put(key, "lng", lng);
    }
}
