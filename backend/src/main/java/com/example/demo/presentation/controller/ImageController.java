package com.example.demo.presentation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 이미지 관련 API
 * 이미지 업로드는 AdminController로 이동되었습니다.
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "이미지 관리", description = "이미지 조회 API")
public class ImageController {
    // 이미지 업로드 기능은 AdminController로 이동되었습니다 (/api/admin/images/upload)
    // 이미지 조회 기능이 필요한 경우 여기에 추가할 수 있습니다
}
