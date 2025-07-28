package com.example.demo.application.dto.member;

import com.example.demo.domain.model.Member;
import lombok.Builder;

@Builder
public record MeResponse(
    Long id,
    String name,
    String email
) {
    public static MeResponse from(Member member) {
        return MeResponse.builder()
            .id(member.id())
            .name(member.name())
            .email(member.email())
            .build();
    }
} 