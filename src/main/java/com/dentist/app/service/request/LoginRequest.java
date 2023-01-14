package com.dentist.app.service.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @Size(min = 5, max = 15, message = "Username must be 5-15 characters")
    private String userName;
    @Size(min = 8, max = 36, message = "Password must be 8-36 characters")
    private String password;
}
