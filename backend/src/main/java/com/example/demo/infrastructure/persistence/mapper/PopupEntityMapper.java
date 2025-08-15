package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.popup.*;
import com.example.demo.infrastructure.persistence.entity.popup.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PopupEntityMapper {

    public Popup toDomain(PopupEntity popupEntity, PopupLocationEntity locationEntity) {
        if (popupEntity == null) {
            return null;
        }

        Location location = locationEntity != null ? toLocationDomain(locationEntity) : null;

        return Popup.builder()
                .id(popupEntity.getId())
                .name(popupEntity.getTitle())
                .location(location)
                .type(popupEntity.getType())
                .build();
    }

    // Entity List -> Domain
    public Popup toDomain(
            PopupEntity popupEntity,
            PopupLocationEntity locationEntity,
            List<PopupWeeklyScheduleEntity> scheduleEntities,
            List<PopupImageEntity> imageEntities,
            List<PopupContentEntity> contentEntities,
            List<PopupSocialEntity> socialEntities,
            List<PopupCategoryEntity> categoryEntities
    ) {
        return Popup.builder()
                .id(popupEntity.getId())
                .name(popupEntity.getTitle())
                .location(toLocationDomain(locationEntity))
                .schedule(toScheduleDomain(popupEntity, scheduleEntities))
                .display(toDisplayDomain(imageEntities, contentEntities, socialEntities))
                .type(popupEntity.getType()) // Type conversion
                .popupCategories(toCategoriesDomain(categoryEntities))
                .status(null) // Status는 DB에 저장되지 않고, 도메인 로직으로 결정
                .build();
    }

    private Location toLocationDomain(PopupLocationEntity entity) {
        return new Location(entity.getAddressName(), entity.getRegion1DepthName(), entity.getRegion2DepthName(), entity.getRegion3DepthName(), entity.getLongitude(), entity.getLatitude());
    }

    private PopupSchedule toScheduleDomain(PopupEntity popupEntity, List<PopupWeeklyScheduleEntity> scheduleEntities) {
        DateRange dateRange = new DateRange(popupEntity.getStartDate(), popupEntity.getEndDate());
        List<OpeningHours> openingHours = scheduleEntities.stream()
                .map(e -> new OpeningHours(e.getDayOfWeek(), e.getOpenTime(), e.getCloseTime()))
                .collect(Collectors.toList());
        return new PopupSchedule(dateRange, new WeeklyOpeningHours(openingHours));
    }

    private PopupDisplay toDisplayDomain(List<PopupImageEntity> imageEntities, List<PopupContentEntity> contentEntities, List<PopupSocialEntity> socialEntities) {
        // 메인 이미지와 브랜드 스토리 이미지 분리
        List<String> mainImageUrls = imageEntities.stream()
                .filter(e -> e.getType() == PopupImageType.MAIN)
                .map(PopupImageEntity::getUrl)
                .collect(Collectors.toList());
        
        List<String> brandStoryImageUrls = imageEntities.stream()
                .filter(e -> e.getType() == PopupImageType.DESCRIPTION)
                .map(PopupImageEntity::getUrl)
                .collect(Collectors.toList());
        
        String introduction = contentEntities.stream().filter(e -> e.getSortOrder() == 1).findFirst().map(PopupContentEntity::getContentText).orElse("");
        String notice = contentEntities.stream().filter(e -> e.getSortOrder() == 2).findFirst().map(PopupContentEntity::getContentText).orElse("");
        PopupContent content = new PopupContent(introduction, notice);
        List<Sns> sns = socialEntities.stream().map(e -> new Sns(e.getIconUrl(), e.getLinkUrl())).collect(Collectors.toList());
        return new PopupDisplay(mainImageUrls, brandStoryImageUrls, content, sns);
    }

    private List<PopupCategory> toCategoriesDomain(List<PopupCategoryEntity> categoryEntities) {
        return categoryEntities.stream()
                .map(e -> new PopupCategory(e.getCategoryId(), e.getName()))
                .collect(Collectors.toList());
    }

    // Domain -> Entity
    public PopupEntity toPopupEntity(Popup popup) {
        return PopupEntity.builder()
                .id(popup.getId())
                .title(popup.getName())
                .popupLocationId(null) // location은 별도 저장 후 ID를 설정해야 함
                .type(popup.getType()) // Type conversion
                .startDate(popup.getSchedule().dateRange().startDate())
                .endDate(popup.getSchedule().dateRange().endDate())
                .build();
    }
} 