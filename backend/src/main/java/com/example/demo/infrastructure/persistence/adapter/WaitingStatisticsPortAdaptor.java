package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.waiting.PopupWaitingStatistics;
import com.example.demo.domain.model.waiting.WaitingStatistics;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.WaitingStatisticsPort;
import com.example.demo.infrastructure.persistence.mapper.WaitingEntityMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.infrastructure.persistence.entity.QWaitingEntity.waitingEntity;

@Repository
@RequiredArgsConstructor
public class WaitingStatisticsPortAdaptor implements WaitingStatisticsPort {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PopupWaitingStatistics findCompletedStatisticsByPopupId(Long popupId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(waitingEntity.status.eq(WaitingStatus.VISITED))
                .and(waitingEntity.popupId.eq(popupId));
        List<WaitingStatistics> waitingStatistics = jpaQueryFactory.selectFrom(waitingEntity)
                .where(builder)
                .fetch()
                .stream()
                .map(WaitingEntityMapper::toWaitingStatistics)
                .toList();

        return new PopupWaitingStatistics(popupId, waitingStatistics);
    }
}
