package com.example.demo.application.service;

import com.example.demo.application.dto.member.MeResponse;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.OAuthAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberPort memberPort;
    private final OAuthAccountPort oAuthAccountPort;

    /**
     * 로그인된 회원 정보 조회
     */
    public MeResponse getMe(Long memberId) {
        Member member = memberPort.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorType.MEMBER_NOT_FOUND, String.valueOf(memberId)));
        return MeResponse.from(member);
    }

    /**
     * 회원 탈퇴 (물리적 삭제)
     */
    @Transactional
    public void deleteMember(Long memberId) {
        // 존재 여부 확인 후 삭제
        if (memberPort.findById(memberId).isEmpty()) {
            throw new BusinessException(ErrorType.MEMBER_NOT_FOUND, String.valueOf(memberId));
        }
        oAuthAccountPort.deleteByMemberId(memberId);
        memberPort.deleteById(memberId);
    }
}