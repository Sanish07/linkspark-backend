package com.sanish.url.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class LoginReqDto {
    private String username;
    private String password;
}
