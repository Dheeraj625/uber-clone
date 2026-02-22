package com.uber.uber_clone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one with User
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String vehicleNumber;

    private boolean available;
}