package com.example.demo.presentation.dto;

import com.example.demo.domain.model.Sns;

public record SnsDto(
    String icon,
    String url
) {
    public static SnsDto from(Sns sns) {
        return new SnsDto(
            sns.icon(),
            sns.url()
        );
    }
}

