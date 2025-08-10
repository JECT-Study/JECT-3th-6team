package com.example.demo.application.service;

import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.WaitingPort;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import com.example.demo.infrastructure.persistence.repository.PopupJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
//임시 로직이므로 나중에 삭제 예정
public class ScheduledWaitingEnterService {

    private final WaitingService waitingService;
    private final WaitingPort waitingPort;
    private final PopupJpaRepository popupJpaRepository;


    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void enter() {
        log.info("팝업 자동 입장 스케줄러 시작 : %s".formatted(LocalDateTime.now()));
        List<Long> popupIds = popupJpaRepository.findAll().stream().map(PopupEntity::getId).toList();
        for (Long popupId : popupIds) {
            log.info("%d번 팝업 자동 입장 시작: %s".formatted(popupId, LocalDateTime.now()));
            WaitingQuery query = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);
            List<Waiting> allWaitingInPopup = waitingPort.findByQuery(query);
            Waiting zeroWaitingNumperWaiting = allWaitingInPopup.stream().filter(it -> it.waitingNumber() == 0).findFirst().orElseThrow();

            Waiting enter = zeroWaitingNumperWaiting.enter();
            allWaitingInPopup.stream()
                    .filter(it -> it.waitingNumber() != 0)
                    .map(Waiting::minusWaitingNumber)
                    .forEach(waitingPort::save);

            waitingPort.save(enter);
        }
        log.info("팝업 자동 입장 스케줄러 종료 : %s".formatted(LocalDateTime.now()));
    }
}
