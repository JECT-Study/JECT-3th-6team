package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.port.BrandStoryPort;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import com.example.demo.infrastructure.persistence.mapper.PopupBrandStoryMapper;
import com.example.demo.infrastructure.persistence.repository.PopupImageRepository;
import com.example.demo.infrastructure.persistence.repository.PopupSocialRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopupBrandStoryAdapter implements BrandStoryPort {
    private final PopupImageRepository popupImageRepository;
    private final PopupSocialRepository popupSocialRepository;
    private final PopupBrandStoryMapper popupBrandStoryMapper;

    @Override
    public Optional<BrandStory> findByPopupId(Long popupId) {
        var imageEntities = popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.DESCRIPTION);
        var socialEntities = popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(popupId);
        return popupBrandStoryMapper.toDomain(imageEntities, socialEntities);
    }
}