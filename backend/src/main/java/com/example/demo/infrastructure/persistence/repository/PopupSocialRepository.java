package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupSocialEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupSocialRepository extends JpaRepository<PopupSocialEntity,Long> {

    List<PopupSocialEntity> findAllByPopupIdOrderBySortOrderAsc(Long popupId);

}