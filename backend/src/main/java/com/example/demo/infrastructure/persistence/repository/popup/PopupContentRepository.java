package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupContentRepository extends JpaRepository<PopupContentEntity,Long> {

}
