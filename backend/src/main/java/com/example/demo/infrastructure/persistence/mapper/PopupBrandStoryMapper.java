package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.popup.Sns;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupSocialEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PopupBrandStoryMapper {
    public Optional<BrandStory> toDomain(List<PopupImageEntity> imageEntities, List<PopupSocialEntity> socialEntities) {
        List<String> imageUrls = imageEntities.stream()
            .map(PopupImageEntity::getUrl)
            .toList();

        List<Sns> snsList = socialEntities.stream()
            .map(e -> new Sns(e.getIconUrl(), e.getLinkUrl()))
            .toList();

        if (imageUrls.isEmpty() && snsList.isEmpty()) return Optional.empty();

        return Optional.of(new BrandStory(imageUrls, snsList));
    }
}