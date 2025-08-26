package com.example.demo.application.dto.image;

public record ImageUploadResponse(
    String fileName,
    String imageUrl,
    long size
) {
}