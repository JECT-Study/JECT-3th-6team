package com.example.demo.application.service;

import com.example.demo.application.dto.notification.WaitingEntryNotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTemplateServiceTest {

    private EmailTemplateService emailTemplateService;

    @BeforeEach
    void setUp() {
        emailTemplateService = new EmailTemplateService();
    }

    @Test
    void buildWaitingEntryTemplate_정상적으로_HTML_템플릿을_생성한다() {
        // given
        LocalDateTime waitingDateTime = LocalDateTime.of(2025, 6, 26, 13, 0);
        WaitingEntryNotificationRequest request = new WaitingEntryNotificationRequest(
            "스타벅스 강남점",
            "이윤재",
            3,
            "lki3532@naver.com",
            waitingDateTime,
            "https://maps.google.com"
        );

        // when
        String template = emailTemplateService.buildWaitingEntryTemplate(request);

        // then
        assertThat(template)
            .contains("[스타벅스 강남점] 입장 알림")
            .contains("이윤재")
            .contains("3")
            .contains("lki3532@naver.com")
            .contains("오후 1:00")
            .contains("2025/6/26")
            .contains("https://maps.google.com")
            .contains("스팟잇")
            .contains("spot it!")
            .contains("10분 이내에 입장")
            .contains("매장 위치 보기")
            .contains("스팟잇에서 입장하라고 안내받았어요");
    }

    @Test
    void buildWaitingEntryTemplate_오전_시간이_올바르게_표시된다() {
        // given
        LocalDateTime morningTime = LocalDateTime.of(2025, 6, 26, 9, 30);
        WaitingEntryNotificationRequest request = new WaitingEntryNotificationRequest(
                "테스트 매장",
                "테스트 사용자",
                1,
                "test@example.com",
                morningTime,
                "https://test.com"
        );

        // when
        String template = emailTemplateService.buildWaitingEntryTemplate(request);

        // then
        assertThat(template).contains("오전 9:30");
    }

    @Test
    void buildWaitingEntryTemplate_오후_시간이_올바르게_표시된다() {
        // given
        LocalDateTime afternoonTime = LocalDateTime.of(2025, 6, 26, 15, 45);
        WaitingEntryNotificationRequest request = new WaitingEntryNotificationRequest(
                "테스트 매장",
                "테스트 사용자",
                1,
                "test@example.com",
                afternoonTime,
                "https://test.com"
        );

        // when
        String template = emailTemplateService.buildWaitingEntryTemplate(request);

        // then
        assertThat(template).contains("오후 3:45");
    }
}
