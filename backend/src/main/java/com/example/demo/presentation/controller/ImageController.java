package com.example.demo.presentation.controller;

import com.example.demo.application.dto.image.ImageUploadResponse;
import com.example.demo.application.service.ImageService;
import com.example.demo.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    //TODO 임시 코드 삭제 필요
    @Value("${custom.admin.password}")
    private String adminPassword;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String password
    ) {
        //TODO 임시 코드 삭제 필요
        if (!password.equals(adminPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("비밀번호가 잘못되었습니다.", null));
        }
        ImageUploadResponse response = imageService.uploadImage(file);
        return ResponseEntity.ok(new ApiResponse<>("이미지 업로드가 성공했습니다.", response));
    }
}
