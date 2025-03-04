package com.sanish.url.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterReqDto {
    private String username;
    private String email;
    private Set<String> role;
    private String password;
}
