package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupTimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupTimeSlotRepository extends JpaRepository<PopupTimeSlotEntity, Long> {

}
