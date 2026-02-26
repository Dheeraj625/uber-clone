package com.uber.uber_clone.entity;

import jakarta.persistence.*;

@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double fare;

    // Rider (User)
    @ManyToOne
    @JoinColumn(name = "rider_id")
    private User rider;

    // Driver
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private String pickupLocation;
    private String dropLocation;

    // NEW COORDINATES day 9
    private Double pickupLatitude;
    private Double pickupLongitude;

    private Double dropLatitude;
    private Double dropLongitude;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    public Ride() {
    }

    public Ride(User rider, Driver driver, String pickupLocation, String dropLocation, RideStatus status) {
        this.rider = rider;
        this.driver = driver;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = status;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }
    //day 8
    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    } 
    //day 9

    public Double getPickupLatitude() {
    return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
    this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
    return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
    this.pickupLongitude = pickupLongitude;
    }

    public Double getDropLatitude() {
    return dropLatitude;
    }

    public void setDropLatitude(Double dropLatitude) {
    this.dropLatitude = dropLatitude;
    }

    public Double getDropLongitude() {
    return dropLongitude;
    }

    public void setDropLongitude(Double dropLongitude) {
    this.dropLongitude = dropLongitude;
    }
}