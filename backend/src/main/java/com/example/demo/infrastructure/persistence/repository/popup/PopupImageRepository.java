package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupImageRepository extends JpaRepository<PopupImageEntity, Long> {

}
