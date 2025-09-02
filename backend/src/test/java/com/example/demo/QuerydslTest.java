package com.example.demo;

import com.example.demo.infrastructure.persistence.entity.popup.QPopupEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class QuerydslTest {

    @Test
    void querydsl_동작_확인() {
        // Q클래스가 정상적으로 생성되었는지 확인
        QPopupEntity popupEntity = QPopupEntity.popupEntity;
        
        assertNotNull(popupEntity);
        assertNotNull(popupEntity.id);
        assertNotNull(popupEntity.title);
        assertNotNull(popupEntity.startDate);
        assertNotNull(popupEntity.endDate);
        
        System.out.println("✅ QueryDSL Q클래스 정상 생성됨!");
        System.out.println("PopupEntity 필드들:");
        System.out.println("- id: " + popupEntity.id);
        System.out.println("- title: " + popupEntity.title);
        System.out.println("- startDate: " + popupEntity.startDate);
        System.out.println("- endDate: " + popupEntity.endDate);
    }
}
