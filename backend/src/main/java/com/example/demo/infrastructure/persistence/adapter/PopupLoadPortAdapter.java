package com.example.demo.infrastructure.persistence.adapter;

import static java.util.stream.Collectors.toMap;

import com.example.demo.application.dto.PopupRawData;
import com.example.demo.application.mapper.PopupDetailMapper;
import com.example.demo.application.port.out.PopupLoadPort;
import com.example.demo.domain.model.CapacitySchedule;
import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.domain.model.Popup;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import com.example.demo.infrastructure.persistence.entity.popup.PopupTimeSlotEntity;
import com.example.demo.infrastructure.persistence.repository.popup.PopupCategoryRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupContentRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupImageRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupLocationRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupReviewRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupSocialRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupTimeSlotRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupWeeklyScheduleRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopupLoadPortAdapter implements PopupLoadPort {
    private final PopupRepository popupRepository;
    private final PopupTimeSlotRepository popupTimeSlotRepository;

    private final PopupImageRepository popupImageRepository;
    private final PopupCategoryRepository popupCategoryRepository;
    private final PopupContentRepository popupContentRepository;
    private final PopupLocationRepository popupLocationRepository;
    private final PopupReviewRepository popupReviewRepository;
    private final PopupSocialRepository popupSocialRepository;
    private final PopupWeeklyScheduleRepository popupWeeklyScheduleRepository;
    private final PopupDetailMapper popupDetailMapper;



    @Override
    public Optional<Popup> findById(Long popupId) {
        Map<DateTimeSlot, Integer> data = popupTimeSlotRepository.findAllByPopupId(popupId)
                .stream()
                .collect(toMap(
                        it -> new DateTimeSlot(it.getSlotDate(), it.getSlotTime()),
                        PopupTimeSlotEntity::getAvailableCount
                ));

        return popupRepository.findById(popupId)
                .map(it -> {
                    return new Popup(
                            it.getId(),
                            it.getTitle(),
                            null, // TODO 관련 인프라 구성 요소 추가 필요
                            new CapacitySchedule(data),
                            null // TODO 도메인에 요일 별 영업 시간 기능 구현 안되어있는 부분 수정 필요
                    );
                });
    }
    @Override
    public Optional<PopupDetail> findDetailById(Long popupId) {
        return popupRepository.findById(popupId).map(popup -> {

            PopupRawData rawData = new PopupRawData(
                popup,
                popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.MAIN),
                popupCategoryRepository.findAllByPopupId(popupId),
                popupLocationRepository.findById(popup.getPopupLocationId())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 팝업 위치입니다.")),
                popupReviewRepository.findAllByPopupId(popupId),
                popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.DESCRIPTION),
                popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(popupId),
                popupWeeklyScheduleRepository.findAllByPopupId(popupId),
                popupContentRepository.findAllByPopupIdOrderBySortOrderAsc(popupId)
            );
            return popupDetailMapper.toDomain(rawData);
        });
    }
}
