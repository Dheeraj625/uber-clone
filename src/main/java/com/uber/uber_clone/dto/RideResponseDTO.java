package com.uber.uber_clone.dto;

public class RideResponseDTO {

    private Long id;
    private String pickupLocation;
    private String dropLocation;
    private String status;
    private double fare;
    private UserResponseDTO rider;
    private DriverResponseDTO driver;

    public RideResponseDTO(Long id, String pickupLocation, String dropLocation,
                           String status, double fare,
                           UserResponseDTO rider, DriverResponseDTO driver) {
        this.id = id;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = status;
        this.fare = fare;
        this.rider = rider;
        this.driver = driver;
    }

    public Long getId() { return id; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropLocation() { return dropLocation; }
    public String getStatus() { return status; }
    public double getFare() { return fare; }
    public UserResponseDTO getRider() { return rider; }
    public DriverResponseDTO getDriver() { return driver; }
}
