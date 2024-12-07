package org.example.movieapi.auth.utils;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
