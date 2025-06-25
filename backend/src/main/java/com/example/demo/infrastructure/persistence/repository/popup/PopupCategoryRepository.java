package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupCategoryRepository extends JpaRepository<PopupCategoryEntity, Long> {

}
