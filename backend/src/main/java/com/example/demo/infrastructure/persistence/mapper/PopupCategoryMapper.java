package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.popup.PopupCategory;
import com.example.demo.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PopupCategoryMapper {

    public PopupCategory toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PopupCategory(entity.getId(), entity.getName());
    }

    public List<PopupCategory> toDomains(List<CategoryEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
} 