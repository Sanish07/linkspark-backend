package com.sanish.url.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ClicksInfoDto {

    private LocalDate clickDate;
    private Long clickCount;
}
