package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupWeeklyScheduleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupWeeklyScheduleRepository extends JpaRepository<PopupWeeklyScheduleEntity, Long> {

    List<PopupWeeklyScheduleEntity> findAllByPopupId(Long popupId);

}
