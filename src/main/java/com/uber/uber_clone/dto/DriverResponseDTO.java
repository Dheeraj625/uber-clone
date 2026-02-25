package com.uber.uber_clone.dto;

public class DriverResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private String vehicleNumber;
    private boolean available;

    public DriverResponseDTO(Long id, UserResponseDTO user, String vehicleNumber, boolean available) {
        this.id = id;
        this.user = user;
        this.vehicleNumber = vehicleNumber;
        this.available = available;
    }

    public Long getId() { return id; }
    public UserResponseDTO getUser() { return user; }
    public String getVehicleNumber() { return vehicleNumber; }
    public boolean isAvailable() { return available; }
}
