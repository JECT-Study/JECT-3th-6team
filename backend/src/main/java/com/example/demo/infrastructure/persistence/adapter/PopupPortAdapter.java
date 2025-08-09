package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupMapQuery;
import com.example.demo.domain.model.popup.PopupQuery;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import com.example.demo.infrastructure.persistence.entity.popup.PopupLocationEntity;
import com.example.demo.infrastructure.persistence.mapper.PopupEntityMapper;
import com.example.demo.infrastructure.persistence.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PopupPortAdapter implements PopupPort {

    private final PopupJpaRepository popupJpaRepository;
    private final PopupLocationRepository popupLocationRepository;
    private final PopupWeeklyScheduleRepository popupWeeklyScheduleRepository;
    private final PopupImageRepository popupImageRepository;
    private final PopupContentRepository popupContentRepository;
    private final PopupSocialRepository popupSocialRepository;
    private final PopupCategoryRepository popupCategoryRepository;
    private final PopupEntityMapper popupEntityMapper;
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Popup> findById(Long popupId) {
        var popupEntity = popupJpaRepository.findById(popupId).orElse(null);
        if (popupEntity == null) {
            return Optional.empty();
        }

        var locationEntity = popupLocationRepository.findById(popupEntity.getPopupLocationId())
                .orElseThrow(() -> new EntityNotFoundException("PopupLocation not found for popupId: " + popupId));

        var scheduleEntities = popupWeeklyScheduleRepository.findAllByPopupId(popupId); // 정렬 없는 메서드로 변경
        var imageEntities = popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.MAIN);
        var contentEntities = popupContentRepository.findAllByPopupIdOrderBySortOrderAsc(popupId);
        var socialEntities = popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(popupId);
        var categoryEntities = popupCategoryRepository.findAllByPopupId(popupId);

        Popup domain = popupEntityMapper.toDomain(popupEntity, locationEntity, scheduleEntities, imageEntities, contentEntities, socialEntities, categoryEntities);
        return Optional.of(domain);
    }

    @Override
    @Transactional
    public Popup save(Popup popup) {
        // 1) 위치 저장
        var locationEntity = PopupLocationEntity.builder()
                .addressName(popup.getLocation().addressName())
                .region1DepthName(popup.getLocation().region1depthName())
                .region2DepthName(popup.getLocation().region2depthName())
                .region3DepthName(popup.getLocation().region3depthName())
                .longitude(popup.getLocation().longitude())
                .latitude(popup.getLocation().latitude())
                .build();
        locationEntity = popupLocationRepository.save(locationEntity);

        // 2) 팝업 본문 저장
        var popupEntity = popupEntityMapper.toPopupEntity(popup);
        popupEntity = PopupEntity.builder()
                .id(popupEntity.getId())
                .title(popupEntity.getTitle())
                .popupLocationId(locationEntity.getId())
                .type(popupEntity.getType())
                .startDate(popupEntity.getStartDate())
                .endDate(popupEntity.getEndDate())
                .build();
        popupEntity = popupJpaRepository.save(popupEntity);

        Long popupId = popupEntity.getId();

        // 3) 주간 스케줄 저장
        var scheduleEntities = popup.getSchedule().weeklyOpeningHours().toList().stream()
                .map(it -> com.example.demo.infrastructure.persistence.entity.popup.PopupWeeklyScheduleEntity.builder()
                        .popupId(popupId)
                        .dayOfWeek(it.dayOfWeek())
                        .openTime(it.openTime())
                        .closeTime(it.closeTime())
                        .build())
                .toList();
        popupWeeklyScheduleRepository.saveAll(scheduleEntities);

        // 4) 이미지 저장 (모두 MAIN으로 저장하고 정렬 순서 부여)
        List<com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity> imageEntities =
                popup.getDisplay().imageUrls().stream()
                        .map(url -> com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity.builder()
                                .popupId(popupId)
                                .type(PopupImageType.MAIN)
                                .url(url)
                                .sortOrder(popup.getDisplay().imageUrls().indexOf(url) + 1)
                                .build())
                        .toList();
        popupImageRepository.saveAll(imageEntities);

        // 5) 컨텐츠 저장 (introduction=1, notice=2)
        var intro = com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity.builder()
                .popupId(popupId)
                .contentText(popup.getDisplay().content().introduction())
                .sortOrder(1)
                .build();
        var notice = com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity.builder()
                .popupId(popupId)
                .contentText(popup.getDisplay().content().notice())
                .sortOrder(2)
                .build();
        popupContentRepository.saveAll(java.util.List.of(intro, notice));

        // 6) SNS 저장
        List<com.example.demo.infrastructure.persistence.entity.popup.PopupSocialEntity> socialEntities = popup.getDisplay().sns().stream()
                .map(s -> com.example.demo.infrastructure.persistence.entity.popup.PopupSocialEntity.builder()
                        .popupId(popupId)
                        .iconUrl(s.icon())
                        .linkUrl(s.url())
                        .sortOrder(popup.getDisplay().sns().indexOf(s) + 1)
                        .build())
                .toList();
        popupSocialRepository.saveAll(socialEntities);

        // 7) 카테고리 저장
        List<com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity> categoryEntities = popup.getPopupCategories().stream()
                .map(c -> com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity.builder()
                        .popupId(popupId)
                        .categoryId(c.id())
                        .name(c.name() == null ? categoryNameOrNull(c.id()) : c.name())
                        .build())
                .toList();
        popupCategoryRepository.saveAll(categoryEntities);

        // 8) 저장된 것들로 도메인 복원
        var images = popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.MAIN);
        var contents = popupContentRepository.findAllByPopupIdOrderBySortOrderAsc(popupId);
        var socials = popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(popupId);
        var categories = popupCategoryRepository.findAllByPopupId(popupId);
        return popupEntityMapper.toDomain(popupEntity, locationEntity, popupWeeklyScheduleRepository.findAllByPopupId(popupId), images, contents, socials, categories);
    }

    private String categoryNameOrNull(Long categoryId) {
        return categoryJpaRepository.findById(categoryId)
                .map(com.example.demo.infrastructure.persistence.entity.CategoryEntity::getName)
                .orElse(null);
    }

    @Override
    public List<Popup> findByQuery(PopupQuery query) {
        var pageable = PageRequest.of(0, query.size() + 1);

        List<PopupEntity> popupEntities;

        // 키워드 검색이 있는 경우
        if (query.keyword() != null && !query.keyword().trim().isEmpty()) {
            popupEntities = findByKeywordSearch(query, pageable);
        } else {
            // 기존 필터 검색
            popupEntities = popupJpaRepository.findFilteredPopups(
                    query.popupId(),
                    query.startDate(),
                    query.endDate(),
                    query.types().isEmpty() ? null : query.types(),
                    query.categories().isEmpty() ? null : query.categories(),
                    query.region1DepthName(),
                    query.lastPopupId(),
                    pageable
            );
        }

        return popupEntities.stream()
                .map(entity -> {
                    var location = popupLocationRepository.findById(entity.getPopupLocationId()).orElse(null);
                    var schedules = popupWeeklyScheduleRepository.findAllByPopupId(entity.getId());
                    var images = popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(entity.getId(), PopupImageType.MAIN);
                    var contents = popupContentRepository.findAllByPopupIdOrderBySortOrderAsc(entity.getId());
                    var socials = popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(entity.getId());
                    var categories = popupCategoryRepository.findAllByPopupId(entity.getId());
                    return popupEntityMapper.toDomain(entity, location, schedules, images, contents, socials, categories);
                })
                .toList();
    }

    @Override
    public void deleteById(Long popupId) {
        // TODO: 연관된 모든 엔티티(location, schedule 등)를 함께 삭제하는 로직 구현 필요
        popupJpaRepository.deleteById(popupId);
    }

    @Override
    public List<Popup> findByMapQuery(PopupMapQuery query) {
        // 1단계: 좌표와 기본 조건으로 팝업 조회
        List<PopupEntity> popupEntities = findPopupsByConditions(query);

        // 2단계: 카테고리 필터링 (메모리에서 처리)
        if (hasCategories(query)) {
            popupEntities = filterByCategories(popupEntities, query.categories());
        }

        // 3단계: 도메인 객체로 변환
        return popupEntities.stream()
                .map(it -> {
                    PopupLocationEntity popupLocationEntity = popupLocationRepository.findById(it.getPopupLocationId()).orElseThrow();
                    return popupEntityMapper.toDomain(it, popupLocationEntity);
                })
                .collect(Collectors.toList());
    }

    /**
     * 조건에 따라 적절한 JPA 메서드를 호출한다
     */
    private List<PopupEntity> findPopupsByConditions(PopupMapQuery query) {
        boolean hasType = query.type() != null;
        boolean hasDateRange = hasValidDateRange(query);

        if (hasType && hasDateRange) {
            return popupJpaRepository.findByCoordinateRangeAndTypeAndDateRange(
                    query.minLatitude(), query.maxLatitude(),
                    query.minLongitude(), query.maxLongitude(),
                    query.type(),
                    query.dateRange().startDate(), query.dateRange().endDate()
            );
        } else if (hasType) {
            return popupJpaRepository.findByCoordinateRangeAndType(
                    query.minLatitude(), query.maxLatitude(),
                    query.minLongitude(), query.maxLongitude(),
                    query.type()
            );
        } else if (hasDateRange) {
            return popupJpaRepository.findByCoordinateRangeAndDateRange(
                    query.minLatitude(), query.maxLatitude(),
                    query.minLongitude(), query.maxLongitude(),
                    query.dateRange().startDate(), query.dateRange().endDate()
            );
        } else {
            return popupJpaRepository.findByCoordinateRange(
                    query.minLatitude(), query.maxLatitude(),
                    query.minLongitude(), query.maxLongitude()
            );
        }
    }

    /**
     * 유효한 날짜 범위가 있는지 확인한다
     */
    private boolean hasValidDateRange(PopupMapQuery query) {
        return query.dateRange() != null
                && query.dateRange().startDate() != null
                && query.dateRange().endDate() != null;
    }

    /**
     * 카테고리 조건이 있는지 확인한다
     */
    private boolean hasCategories(PopupMapQuery query) {
        return query.categories() != null && !query.categories().isEmpty();
    }

    /**
     * 카테고리로 팝업을 필터링한다 (메모리에서 처리)
     */
    private List<PopupEntity> filterByCategories(List<PopupEntity> popupEntities, List<String> categories) {
        return popupEntities.stream()
                .filter(popup -> hasMatchingCategory(popup.getId(), categories))
                .collect(Collectors.toList());
    }

    /**
     * 팝업이 지정된 카테고리 중 하나라도 포함하는지 확인한다
     */
    private boolean hasMatchingCategory(Long popupId, List<String> categories) {
        return popupCategoryRepository.findAllByPopupId(popupId).stream()
                .anyMatch(category -> categories.contains(category.getName()));
    }

    /**
     * 키워드 검색을 수행한다.
     * 키워드를 토큰화하고 각 토큰에 대해 검색을 수행하여 결과를 통합한다.
     */
    private List<PopupEntity> findByKeywordSearch(PopupQuery query, Pageable pageable) {
        String keyword = query.keyword().trim();

        // 키워드를 띄어쓰기 기준으로 토큰화하고 공백문자 및 의미 없는 문자 제거
        List<String> tokens = Arrays.stream(keyword.split("\\s+"))
                .map(String::trim)
                .filter(token -> !token.isEmpty() && token.length() > 0)
                .distinct()
                .toList();

        if (tokens.isEmpty()) {
            return List.of();
        }

        // 각 토큰에 대해 검색을 수행하고 결과를 통합 (중복 제거)
        Set<PopupEntity> resultSet = new LinkedHashSet<>();

        for (String token : tokens) {
            List<PopupEntity> tokenResults = popupJpaRepository.findByKeyword(
                    query.popupId(),
                    token,
                    query.lastPopupId(),
                    pageable
            );
            resultSet.addAll(tokenResults);
        }

        return resultSet.stream()
                .limit(query.size() + 1) // 페이징 처리
                .toList();
    }
}
