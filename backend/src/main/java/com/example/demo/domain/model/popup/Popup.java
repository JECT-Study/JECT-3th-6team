package com.example.demo.domain.model.popup;

import com.example.demo.domain.model.Location;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
} 