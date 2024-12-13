package org.example.movieapi.auth.utils;

public record ChangePassword(
        String password,
        String repeatPassword
) {
}
