package com.example.demo.application.service;

import com.example.demo.application.dto.waiting.WaitingMakeVisitRequest;
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
        log.info("ScheduledWaitingEnterService - enter() 실행");
        List<Long> popupIds = popupJpaRepository.findAll().stream().map(PopupEntity::getId).toList();
        for (Long popupId : popupIds) {
            WaitingQuery query = WaitingQuery.forPopup(popupId, WaitingStatus.WAITING);
            waitingPort.findByQuery(query).stream().filter(
                    it -> it.waitingNumber() == 0L
            ).findFirst().ifPresent(waiting -> {
                waitingService.makeVisit(new WaitingMakeVisitRequest(waiting.id()));
            });
        }
        log.info("ScheduledWaitingEnterService - enter() 완료");
    }
}
