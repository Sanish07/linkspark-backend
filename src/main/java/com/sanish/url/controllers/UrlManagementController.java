package com.sanish.url.controllers;

import com.sanish.url.dtos.UrlManagementDto;
import com.sanish.url.entities.User;
import com.sanish.url.services.UrlManagementService;
import com.sanish.url.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/api/urls")
@RestController
public class UrlManagementController {

    private final UrlManagementService urlManagementService;
    private final UserService userService;

    @Autowired
    public UrlManagementController(UrlManagementService urlManagementService, UserService userService) {
        this.urlManagementService = urlManagementService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/shorten")
    public ResponseEntity<UrlManagementDto> generateShortUrl(@RequestBody Map<String, String> request,
                                                             Principal principal){
        String originalUrl = request.get("originalUrl");
        User user = userService.findUserByUsername(principal.getName());
        UrlManagementDto urlManagementDto =  urlManagementService.createShortUrl(originalUrl, user);

        return ResponseEntity.ok(urlManagementDto);
    }
}
