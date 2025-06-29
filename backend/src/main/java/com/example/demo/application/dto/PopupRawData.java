package com.example.demo.application.dto;

import com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupLocationEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupReviewEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupSocialEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupWeeklyScheduleEntity;
import java.util.List;

public record PopupRawData(
    PopupEntity popup,
    List<PopupImageEntity> mainImages,
    List<PopupCategoryEntity> categories,
    PopupLocationEntity location,
    List<PopupReviewEntity> reviews,
    List<PopupImageEntity> descriptionImages,
    List<PopupSocialEntity> socials,
    List<PopupWeeklyScheduleEntity> schedules,
    List<PopupContentEntity> contents
) {
}
