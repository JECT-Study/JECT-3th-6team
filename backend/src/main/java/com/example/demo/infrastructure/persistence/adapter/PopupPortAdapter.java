package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupMapQuery;
import com.example.demo.domain.model.popup.PopupQuery;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import com.example.demo.infrastructure.persistence.entity.popup.PopupLocationEntity;
import com.example.demo.infrastructure.persistence.mapper.PopupEntityMapper;
import com.example.demo.infrastructure.persistence.repository.PopupCategoryRepository;
import com.example.demo.infrastructure.persistence.repository.PopupContentRepository;
import com.example.demo.infrastructure.persistence.repository.PopupImageRepository;
import com.example.demo.infrastructure.persistence.repository.PopupJpaRepository;
import com.example.demo.infrastructure.persistence.repository.PopupLocationRepository;
import com.example.demo.infrastructure.persistence.repository.PopupSocialRepository;
import com.example.demo.infrastructure.persistence.repository.PopupWeeklyScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        // TODO: Domain -> Entities 변환 및 저장 로직 구현
        // 1. Location, Content 등 연관 엔티티 먼저 저장
        // 2. 저장된 엔티티의 ID를 사용하여 PopupEntity 생성 및 저장
        // 3. 저장된 최종 엔티티들을 다시 도메인으로 변환하여 반환
        throw new UnsupportedOperationException("Save operation is not yet implemented");
    }

    @Override
    public List<Popup> findByQuery(PopupQuery query) {
        var pageable = PageRequest.of(0, query.size() + 1);

        List<PopupEntity> popupEntities = popupJpaRepository.findFilteredPopups(
            query.popupId(),
            query.startDate(),
            query.endDate(),
            query.types().isEmpty() ? null : query.types(),
            query.categories().isEmpty() ? null : query.categories(),
            query.region1DepthName(),
            query.lastPopupId(),
            pageable
        );

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
        List<PopupEntity> popupEntities = popupJpaRepository.findByMapQuery(query);
        return popupEntities.stream()
                .map(it -> {
                    PopupLocationEntity popupLocationEntity = popupLocationRepository.findById(it.getPopupLocationId()).orElseThrow();
                    return popupEntityMapper.toDomain(it, popupLocationEntity);
                })
                .collect(Collectors.toList());
    }
}
