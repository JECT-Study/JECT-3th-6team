package com.example.demo.application.service;

import com.example.demo.domain.model.ban.GlobalBan;
import com.example.demo.domain.port.GlobalBanPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 밴 해제 스케줄러 서비스.
 * 매일 자정에 만료된 글로벌 밴을 해제한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledBanReleaseService {

    private final GlobalBanPort globalBanPort;

    /**
     * 매일 자정에 만료된 글로벌 밴을 해제한다.
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void releaseExpiredBans() {
        log.info("밴 해제 스케줄러 시작: {}", LocalDate.now());
        
        try {
            // 만료된 글로벌 밴 조회
            List<GlobalBan> expiredBans = globalBanPort.findExpiredBans();
            
            if (expiredBans.isEmpty()) {
                log.debug("해제할 만료된 밴이 없습니다.");
                return;
            }
            
            log.info("만료된 밴 해제 시작 - 대상: {}명", expiredBans.size());
            
            // 만료된 밴 삭제
            globalBanPort.delete(expiredBans);
            
            log.info("만료된 밴 해제 완료 - 해제된 밴: {}개", expiredBans.size());
            
        } catch (Exception e) {
            log.error("밴 해제 처리 중 오류 발생", e);
        }
        
        log.info("밴 해제 스케줄러 완료: {}", LocalDate.now());
    }
}
