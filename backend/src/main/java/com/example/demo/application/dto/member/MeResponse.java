package com.example.demo.application.dto.member;

import com.example.demo.domain.model.Member;
import lombok.Builder;

@Builder
public record MeResponse(
    String name,
    String email
) {
    public static MeResponse from(Member member) {
        return MeResponse.builder()
            .name(member.name())
            .email(member.email())
            .build();
    }
} 