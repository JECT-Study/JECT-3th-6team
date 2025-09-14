package com.example.demo.presentation.controller;

import com.example.demo.application.dto.image.ImageUploadResponse;
import com.example.demo.application.service.ImageService;
import com.example.demo.presentation.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "이미지 관리", description = "이미지 업로드 및 관리 API")
public class ImageController {

    private final ImageService imageService;
    @Value("${custom.admin.password}")
    private String adminPassword;

    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드", description = "이미지 파일을 서버에 업로드합니다.")
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(
            @Parameter(description = "업로드할 이미지 파일") @RequestParam("file") MultipartFile file,
            @Parameter(description = "관리자 비밀번호") @RequestHeader("Authorization") String password
    ) {
        if (!password.equals(adminPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("비밀번호가 잘못되었습니다.", null));
        }
        ImageUploadResponse response = imageService.uploadImage(file);
        return ResponseEntity.ok(new ApiResponse<>("이미지 업로드가 성공했습니다.", response));
    }
}
