package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<PopupEntity, Long> {
}
