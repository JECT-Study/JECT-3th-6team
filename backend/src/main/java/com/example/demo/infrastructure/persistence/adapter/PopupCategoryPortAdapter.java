package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.popup.PopupCategory;
import com.example.demo.domain.port.PopupCategoryPort;
import com.example.demo.infrastructure.persistence.mapper.PopupCategoryMapper;
import com.example.demo.infrastructure.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PopupCategoryPortAdapter implements PopupCategoryPort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final PopupCategoryMapper popupCategoryMapper;

    @Override
    public List<PopupCategory> findAll() {
        var entities = categoryJpaRepository.findAll();
        return popupCategoryMapper.toDomains(entities);
    }

    @Override
    public List<PopupCategory> findByIds(List<Long> categoryIds) {
        var entities = categoryJpaRepository.findByIdIn(categoryIds);
        return popupCategoryMapper.toDomains(entities);
    }
} 