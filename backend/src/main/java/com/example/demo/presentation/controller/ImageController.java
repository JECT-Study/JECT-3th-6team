package com.example.demo.presentation.controller;

import com.example.demo.application.dto.image.ImageUploadResponse;
import com.example.demo.application.service.ImageService;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        ImageUploadResponse response = imageService.uploadImage(file);
        return ResponseEntity.ok(new ApiResponse<>("이미지 업로드가 성공했습니다.", response));
    }
}