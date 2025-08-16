package com.example.demo.application.service;

import com.example.demo.application.dto.image.ImageUploadResponse;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.max-file-size:10485760}") // 10MB
    private long maxFileSize;

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "webp"};
    
    public ImageUploadResponse uploadImage(MultipartFile file) {
        validateFile(file);
        
        try {
            // 업로드 디렉토리 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 파일명 생성 (UUID + 원본 확장자)
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + "." + extension;
            
            // 파일 저장
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // 접근 가능한 URL 생성
            String imageUrl = "/uploads/" + fileName;
            
            log.info("Image uploaded successfully: {}", fileName);
            
            return new ImageUploadResponse(fileName, imageUrl, file.getSize());
            
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            throw new BusinessException(ErrorType.PARAMETER_VALIDATION_FAILED, "파일 업로드 중 오류가 발생했습니다.");
        }
    }
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorType.PARAMETER_VALIDATION_FAILED, "파일이 비어있습니다.");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new BusinessException(ErrorType.PARAMETER_VALIDATION_FAILED, 
                String.format("파일 크기가 너무 큽니다. (최대: %d bytes)", maxFileSize));
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BusinessException(ErrorType.PARAMETER_VALIDATION_FAILED, "파일명이 유효하지 않습니다.");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean isAllowed = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                isAllowed = true;
                break;
            }
        }
        
        if (!isAllowed) {
            throw new BusinessException(ErrorType.PARAMETER_VALIDATION_FAILED, 
                "지원되지 않는 파일 형식입니다. (jpg, jpeg, png, gif, webp만 가능)");
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            throw new BusinessException(ErrorType.PARAMETER_VALIDATION_FAILED, "파일 확장자가 없습니다.");
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}