package com.sanish.url.services;

import com.sanish.url.dtos.LoginReqDto;
import com.sanish.url.entities.User;
import com.sanish.url.exceptions.RegistrationFieldException;
import com.sanish.url.repositories.UserRepository;
import com.sanish.url.security.jwt.JwtAuthenticationResponse;
import com.sanish.url.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public User registerUser(User user){
        Optional<User> emailAvailable = userRepository.findByEmail(user.getEmail());
        Optional<User> usernameAvailable = userRepository.findByUsername(user.getUsername());
        if(emailAvailable.isPresent()){
            throw new RegistrationFieldException("Linkspark account already registered with email : "+user.getEmail());
        } else if(usernameAvailable.isPresent()){
            throw new RegistrationFieldException("Username : "+user.getUsername()+", is already taken. Please consider using another one..");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

    public JwtAuthenticationResponse loginUser(LoginReqDto loginReqDto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReqDto.getUsername(), loginReqDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth); //Update security context if auth object was initialized

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String jwtToken = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwtToken);
    }

    public User findUserByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username : "+name)
        );
    }
}
