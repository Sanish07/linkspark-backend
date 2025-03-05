package com.sanish.url.services;

import com.sanish.url.dtos.UrlManagementDto;
import com.sanish.url.entities.UrlMapping;
import com.sanish.url.entities.User;
import com.sanish.url.repositories.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UrlManagementService {

    private final UrlMappingRepository urlMappingRepository;

    @Autowired
    public UrlManagementService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public UrlManagementDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateNewShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setShortUrl(shortUrl);

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return mapToUrlMappingDto(savedUrlMapping);
    }

    private UrlManagementDto mapToUrlMappingDto(UrlMapping urlMapping){
        UrlManagementDto urlManagementDto = new UrlManagementDto();
        urlManagementDto.setId(urlMapping.getId());
        urlManagementDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlManagementDto.setShortUrl(urlMapping.getShortUrl());
        urlManagementDto.setUsername(urlMapping.getUser().getUsername());
        urlManagementDto.setClickCount(urlMapping.getClickCount());
        urlManagementDto.setCreatedAt(urlMapping.getCreatedAt());

        return urlManagementDto;
    }

    private String generateNewShortUrl() {
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(7);

        String characterSet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for(int i=0; i<shortUrl.capacity(); i++){
            int randomNumber = random.nextInt(characterSet.length());
            shortUrl.append(characterSet.charAt(randomNumber));
        }

        return shortUrl.toString();
    }
}
