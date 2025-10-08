package com.example.demo.domain.model.waiting;

import java.util.List;

/**
 * 팝업별 예상 대기시간 계산을 담당하는 도메인 서비스.
 */
public class PopupWaitingTimeCalculator {

    /**
     * 팝업의 예약 통계 데이터를 기반으로 평균 대기시간(분/명)을 계산한다.
     *
     * @param statistics 완료된 예약들의 통계 데이터
     * @return 평균 대기시간(분/명), 데이터가 없으면 null
     */
    public Double calculateAverageTimePerPerson(List<WaitingStatistics> statistics) {
        if (statistics == null || statistics.isEmpty()) {
            return null;
        }

        List<Double> timePerPersonList = statistics.stream()
                .map(WaitingStatistics::getTimePerPerson)
                .filter(java.util.Objects::nonNull)
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
     * 현재 대기 순번과 평균 대기시간을 기반으로 예상 대기시간(분)을 계산한다.
     *
     * @param currentWaitingNumber 현재 대기 순번
     * @param averageTimePerPerson 평균 대기시간(분/명)
     * @return 예상 대기시간(분)
     */
    public Integer calculateExpectedWaitingTime(int currentWaitingNumber, Double averageTimePerPerson) {
        if (averageTimePerPerson == null || currentWaitingNumber <= 0) {
            return null;
        }

        return (int) Math.ceil(currentWaitingNumber * averageTimePerPerson);
    }
}