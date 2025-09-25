package com.example.demo.domain.model.waiting;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 특정 팝업의 대기 통계 컬렉션을 관리하는 일급 컬렉션.
 */
public class PopupWaitingStatistics {
    private final Long popupId;
    private final List<WaitingStatistics> statistics;

    public PopupWaitingStatistics(Long popupId, List<WaitingStatistics> statistics) {
        this.popupId = Objects.requireNonNull(popupId, "popupId는 null일 수 없습니다");
        this.statistics = validateAndCopy(statistics);
    }

    private List<WaitingStatistics> validateAndCopy(List<WaitingStatistics> statistics) {
        if (statistics == null) {
            return Collections.emptyList();
        }

        // 모든 통계가 같은 팝업에 대한 것이고 입장 완료된 것인지 검증
        for (WaitingStatistics stat : statistics) {
            if (!Objects.equals(stat.getPopupId(), this.popupId)) {
                throw new IllegalArgumentException("모든 통계는 같은 팝업 ID를 가져야 합니다: " + this.popupId);
            }
            if (stat.getEnteredAt() == null) {
                throw new IllegalArgumentException("입장 완료된 통계만 허용됩니다");
            }
        }

        return List.copyOf(statistics);
    }

    /**
     * 평균 대기시간(분/명)을 계산한다.
     */
    public Double calculateAverageTimePerPerson() {
        List<Double> timePerPersonList = statistics.stream()
                .map(WaitingStatistics::getTimePerPerson)
                .filter(Objects::nonNull)
                .toList();

        if (timePerPersonList.isEmpty()) {
            return null;
        }

        return timePerPersonList.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * 현재 대기 순번을 기반으로 예상 대기시간(분)을 계산한다.
     */
    public Integer calculateExpectedWaitingTime(int currentWaitingNumber) {
        Double averageTimePerPerson = calculateAverageTimePerPerson();

        if (averageTimePerPerson == null || currentWaitingNumber <= 0) {
            return null;
        }

        return (int) Math.ceil(currentWaitingNumber * averageTimePerPerson);
    }
}