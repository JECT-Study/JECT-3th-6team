package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupContentRepository extends JpaRepository<PopupContentEntity,Long> {

    List<PopupContentEntity> findAllByPopupIdOrderBySortOrderAsc(Long popupId);

}