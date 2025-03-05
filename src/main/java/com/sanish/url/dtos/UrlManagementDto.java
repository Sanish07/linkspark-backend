package com.sanish.url.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlManagementDto {
    private Long id;

    private String originalUrl;

    private String shortUrl;

    private Integer clickCount;

    private LocalDateTime createdAt;

    private String username;
}
