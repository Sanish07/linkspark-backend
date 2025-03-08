package com.sanish.url.controllers;

import com.sanish.url.entities.UrlMapping;
import com.sanish.url.services.UrlManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectionController {

    private final UrlManagementService urlManagementService;

    @Autowired
    public RedirectionController(UrlManagementService urlManagementService) {
        this.urlManagementService = urlManagementService;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectShortUrlToOriginal(@PathVariable String shortUrl){
        UrlMapping getUrl = urlManagementService.getCorrespondingOriginalUrl(shortUrl);

        if(getUrl != null){ //If original url corresponding to short url exists in database
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location", getUrl.getOriginalUrl());
            return ResponseEntity.status(302).headers(httpHeaders).build();
        } else{
            return ResponseEntity.notFound().build();
        }
    }
}
