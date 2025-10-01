package com.example.demo.domain.model.popup;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 주간 운영 시간 정보를 관리하는 값 객체.
 */
public class WeeklyOpeningHours {

    private final Map<DayOfWeek, OpeningHours> weeklyHours;

    public WeeklyOpeningHours(List<OpeningHours> openingHours) {
        this.weeklyHours = new EnumMap<>(validateAndToMap(openingHours));
    }

    private Map<DayOfWeek, OpeningHours> validateAndToMap(List<OpeningHours> openingHours) {
        Map<DayOfWeek, OpeningHours> map = openingHours.stream()
                .collect(Collectors.toMap(OpeningHours::dayOfWeek, Function.identity(), (first, second) -> {
                    throw new BusinessException(ErrorType.INVALID_OPENING_HOURS, "요일 중복: " + first.dayOfWeek());
                }));
        return new EnumMap<>(map);
    }

    public Optional<OpeningHours> getOpeningHours(DayOfWeek dayOfWeek) {
        return Optional.ofNullable(weeklyHours.get(dayOfWeek));
    }

    public List<OpeningHours> toList() {
        return List.copyOf(weeklyHours.values());
    }
} 