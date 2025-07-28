package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupReviewEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupReviewRepository extends JpaRepository<PopupReviewEntity, Long> {

    List<PopupReviewEntity> findAllByPopupId(Long popupId);
}
