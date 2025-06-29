package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupCategoryRepository extends JpaRepository<PopupCategoryEntity, Long> {

    List<PopupCategoryEntity> findAllByPopupId(Long popupId);

}
