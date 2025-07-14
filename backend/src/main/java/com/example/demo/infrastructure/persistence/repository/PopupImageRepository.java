package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupImageRepository extends JpaRepository<PopupImageEntity, Long> {

    List<PopupImageEntity> findAllByPopupIdAndTypeOrderBySortOrderAsc(Long id, PopupImageType popupImageType);
}
