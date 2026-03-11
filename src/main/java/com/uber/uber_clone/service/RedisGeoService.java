package com.uber.uber_clone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

@Service
public class RedisGeoService {

    private static final String DRIVER_GEO_KEY = "drivers:locations";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void updateDriverLocation(Long driverId, double lat, double lon) {
        System.out.println("GEOADD -> driverId=" + driverId + " lat=" + lat + " lon=" + lon);
        redisTemplate.opsForGeo()
                .add(
                    DRIVER_GEO_KEY,
                    new Point(lon, lat),
                    driverId.toString()   // FIX
                );
    }

    public List<String> findNearbyDrivers(double latitude, double longitude, double radiusKm) {

    GeoResults<RedisGeoCommands.GeoLocation<String>> results =
            redisTemplate.opsForGeo().radius(
                    DRIVER_GEO_KEY,
                    new Circle(
                            new Point(longitude, latitude),
                            new Distance(radiusKm, Metrics.KILOMETERS)
                    )
            );

    List<String> drivers = new ArrayList<>();

    if (results != null) {
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
            drivers.add(result.getContent().getName());
        }
    }

    return drivers;
    }
}