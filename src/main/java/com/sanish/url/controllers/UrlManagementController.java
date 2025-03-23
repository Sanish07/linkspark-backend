package com.sanish.url.controllers;

import com.sanish.url.dtos.ClicksInfoDto;
import com.sanish.url.dtos.UrlManagementDto;
import com.sanish.url.entities.User;
import com.sanish.url.services.UrlManagementService;
import com.sanish.url.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/active-urls")
    public ResponseEntity<List<UrlManagementDto>> getUsersAllActiveUrls(Principal principal){
        User user = userService.findUserByUsername(principal.getName());
        List<UrlManagementDto> allUrls = urlManagementService.getAllUserUrls(user);

        return ResponseEntity.ok(allUrls);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stats/{shortUrl}")
    public ResponseEntity<List<ClicksInfoDto>> getShortUrlStats(@PathVariable String shortUrl,
                                                                @RequestParam("fromDate") String fromDate,
                                                                @RequestParam("toDate") String toDate){

        LocalDateTime from = urlManagementService.parseDateTime(fromDate);
        LocalDateTime to = urlManagementService.parseDateTime(toDate);
        List<ClicksInfoDto> statsResponse = urlManagementService.getClicksInfoByDate(shortUrl, from, to);

        return ResponseEntity.ok(statsResponse);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/totalClicks")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
                                                                @RequestParam("fromDate") String fromDate,
                                                                @RequestParam("toDate") String toDate){

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        LocalDate from = LocalDate.parse(fromDate, formatter);
        LocalDate to = LocalDate.parse(toDate, formatter);

        User user = userService.findUserByUsername(principal.getName());

        Map<LocalDate, Long> clicksResponse = urlManagementService.getUsersTotalClicksByDate(user, from, to);

        return ResponseEntity.ok(clicksResponse);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{shortUrl}")
    public ResponseEntity<String> deleteShortUrlAndItsData(@PathVariable String shortUrl){
        urlManagementService.deleteShortUrlAndItsData(shortUrl);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

}
