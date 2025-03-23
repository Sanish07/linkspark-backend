package com.sanish.url.controllers;

import com.sanish.url.dtos.LoginReqDto;
import com.sanish.url.dtos.RegisterReqDto;
import com.sanish.url.entities.User;
import com.sanish.url.security.jwt.JwtAuthenticationResponse;
import com.sanish.url.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/public/signup")
    public ResponseEntity<String> registerUser(@RequestBody RegisterReqDto registerReqDto){

        //Setting all user details
        User user = new User();
        user.setUsername(registerReqDto.getUsername());
        user.setPassword(registerReqDto.getPassword());
        user.setEmail(registerReqDto.getEmail());
        user.setRole("ROLE_USER");

        userService.registerUser(user);

        return ResponseEntity.ok("User signed up successfully!");
    }

    @PostMapping("/public/login")
    public ResponseEntity<JwtAuthenticationResponse> loginUser(@RequestBody LoginReqDto loginReqDto){
        return ResponseEntity.ok(userService.loginUser(loginReqDto));
    }

    @GetMapping("/total-users")
    public ResponseEntity<String> getGlobalUserCount(){
        String totalUsers = userService.getActiveGlobalUsersCount();
        return new ResponseEntity<>(totalUsers, HttpStatus.OK);
    }
}
