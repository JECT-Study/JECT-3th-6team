package com.example.demo.application.service;

import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class CanEnterAtScheduleService {

    private final WaitingPort waitingPort;

    private void logEach(Waiting waiting) {
        log.info("입장 가능 시간 설정 처리 시작 - 대기 ID: {}, 회원 ID: {}, 팝업 ID: {}",
                waiting.id(), waiting.member().id(), waiting.popup().getId());
    }

    @Scheduled(fixedRate = 10, timeUnit = SECONDS)
    @Transactional
    public void processCanEnterAt() {
        log.info("입장 가능 시간 설정 스케줄러 시작: {}", LocalDateTime.now());
        List<Waiting> canEnterWaiting = waitingPort.findByQuery(WaitingQuery.forCanEnterWaiting());
        log.info("입장 가능 시간 설정 처리 대상: {}명", canEnterWaiting.size());
        List<Waiting> afterProcess = waitingPort.saveAll(
                canEnterWaiting.stream().peek(this::logEach).map(Waiting::markAsCanEnter).toList()
        );
        log.info("입장 가능 시간 설정 처리 완료: {}명", afterProcess.size());
        log.info("입장 가능 시간 설정 스케줄러 종료: {}", LocalDateTime.now());
    }
}
