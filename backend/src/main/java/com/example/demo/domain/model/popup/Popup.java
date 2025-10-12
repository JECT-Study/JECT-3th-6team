package com.example.demo.domain.model.popup;

import com.example.demo.domain.model.Location;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Popup {
    private final Long id;
    private final String name;
    private final Location location;
    private final PopupSchedule schedule;
    private final PopupDisplay display;
    private final PopupType type;
    private final List<PopupCategory> popupCategories;
    private PopupStatus status;
    // TODO: 조기 종료 상태를 별도 boolean 필드로 관리하는 대신, PopupStatus에 EARLY_CLOSED 상태를 추가하는 것을 검토
    private boolean earlyClosed;

    public boolean isOpenAt(LocalDateTime dateTime) {
        if (earlyClosed) {
            return false;
        }

        LocalDateTime endOfDay = schedule.dateRange().endDate().atTime(23, 59, 59);
        if (dateTime.isBefore(schedule.dateRange().startDate().atStartOfDay()) ||
                dateTime.isAfter(endOfDay)) {
            return false;
        }

        return schedule.weeklyOpeningHours()
                .getOpeningHours(dateTime.getDayOfWeek())
                .map(openingHours -> {
                    LocalDateTime openDateTime = dateTime.toLocalDate().atTime(openingHours.openTime());
                    LocalDateTime closeDateTime = dateTime.toLocalDate().atTime(openingHours.closeTime());

                    return !dateTime.isBefore(openDateTime) && dateTime.isBefore(closeDateTime);
                })
                .orElse(false);
    }

    public void closeEarly() {
        this.earlyClosed = true;
    }
}