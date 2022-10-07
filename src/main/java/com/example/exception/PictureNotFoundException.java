package com.example.exception;

import java.util.Optional;

public class PictureNotFoundException extends RuntimeException {
    public PictureNotFoundException(Integer sol, Optional<String> camera) {
        super(("No picture found for sol=%s".formatted(sol) + camera.map(val -> "and camera=" + val).orElse("")));
    }
}