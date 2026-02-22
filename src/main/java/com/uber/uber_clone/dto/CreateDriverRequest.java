package com.uber.uber_clone.dto;

import lombok.Data;

@Data
public class CreateDriverRequest {
    private Long userId;
    private String vehicleNumber;
}