package com.uber.uber_clone.controller;

import com.uber.uber_clone.entity.User;
import com.uber.uber_clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//day 3
import com.uber.uber_clone.dto.LoginRequest;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }
    //day 3
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}
