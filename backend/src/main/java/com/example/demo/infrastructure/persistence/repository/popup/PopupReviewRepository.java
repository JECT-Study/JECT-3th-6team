package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupReviewRepository extends JpaRepository<PopupReviewEntity, Long> {

}
