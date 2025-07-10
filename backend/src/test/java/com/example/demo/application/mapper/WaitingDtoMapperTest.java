package com.example.demo.application.mapper;

import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.*;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaitingDtoMapperTest {

    @Nested
    @DisplayName("toCreateResponse 테스트")
    class Test01 {
        // 테스트용 유효한 데이터
        private final Popup validPopup = Popup.builder()
                .id(1L)
                .name("테스트 팝업")
                .location(
                        new Location(
                                "서울시 강남구",
                                "서울특별시",
                                "강남구",
                                "역삼동",
                                127.0012,
                                37.5665
                        )
                )
                .schedule(
                        new PopupSchedule(
                                new DateRange(
                                        LocalDate.now(),
                                        LocalDate.now().plusDays(30)
                                ),
                                new WeeklyOpeningHours(
                                        List.of(
                                                new OpeningHours(
                                                        DayOfWeek.MONDAY,
                                                        LocalTime.now(),
                                                        LocalTime.now().plusHours(1)
                                                )
                                        )
                                )
                        )
                )
                .display(
                        new PopupDisplay(
                                List.of("http://image.com"),
                                new PopupContent("팝업 설명", "팝업 공지"),
                                List.of(new Sns("http://image.com", "http://sns.com"))
                        )
                )
                .type(PopupType.EXHIBITION)
                .popupCategories(
                        List.of(new PopupCategory(1L, "예술"))
                )
                .status(PopupStatus.IN_PROGRESS)
                .build();

        private final Member validMember = new Member(1L, "테스트 사용자", "test@example.com");

        @Test
        @DisplayName("정상적인 Waiting을 WaitingCreateResponse로 변환 테스트")
        public void test01() {
            // given
            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1, WaitingStatus.RESERVED, registeredAt
            );

            // when
            WaitingCreateResponse result = new WaitingDtoMapper().toCreateResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.waitingId());
            assertEquals("테스트 팝업", result.popupName());
            assertEquals("홍길동", result.name());
            assertEquals(2, result.peopleCount());
            assertEquals("hong@example.com", result.email());
            assertEquals(1, result.waitingNumber());
            assertEquals(registeredAt, result.registeredAt());
        }

        @Test
        @DisplayName("다른 상태의 Waiting을 WaitingCreateResponse로 변환 테스트")
        public void test02() {
            // given
            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    2L, validPopup, "김철수", validMember,
                    "kim@example.com", 3, 2, WaitingStatus.COMPLETED, registeredAt
            );

            // when
            WaitingCreateResponse result = new WaitingDtoMapper().toCreateResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(2L, result.waitingId());
            assertEquals("테스트 팝업", result.popupName());
            assertEquals("김철수", result.name());
            assertEquals(3, result.peopleCount());
            assertEquals("kim@example.com", result.email());
            assertEquals(2, result.waitingNumber());
            assertEquals(registeredAt, result.registeredAt());
        }
    }

    @Nested
    @DisplayName("toResponse 테스트")
    class Test02 {
        // 테스트용 유효한 데이터
        private final Popup validPopup = Popup.builder()
                .id(1L)
                .name("테스트 팝업")
                .location(
                        new Location(
                                "서울시 강남구",
                                "서울특별시",
                                "강남구",
                                "역삼동",
                                127.0012,
                                37.5665
                        )
                )
                .schedule(
                        new PopupSchedule(
                                new DateRange(
                                        LocalDate.now(),
                                        LocalDate.now().plusDays(30)
                                ),
                                new WeeklyOpeningHours(
                                        List.of(
                                                new OpeningHours(
                                                        DayOfWeek.MONDAY,
                                                        LocalTime.now(),
                                                        LocalTime.now().plusHours(1)
                                                )
                                        )
                                )
                        )
                )
                .display(
                        new PopupDisplay(
                                List.of("http://image.com"),
                                new PopupContent("팝업 설명", "팝업 공지"),
                                List.of(new Sns("http://image.com", "http://sns.com"))
                        )
                )
                .type(PopupType.EXHIBITION)
                .popupCategories(
                        List.of(new PopupCategory(1L, "예술"))
                )
                .status(PopupStatus.IN_PROGRESS)
                .build();

        private final Member validMember = new Member(1L, "테스트 사용자", "test@example.com");

        @Test
        @DisplayName("정상적인 Waiting을 WaitingResponse로 변환 테스트")
        public void test01() {
            // given
            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1, WaitingStatus.RESERVED, registeredAt
            );

            // when
            WaitingResponse result = new WaitingDtoMapper().toResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.waitingId());
            assertEquals(1L, result.popupId());
            assertEquals("테스트 팝업", result.popupName());
            assertEquals("thumbnail1.jpg", result.popupImageUrl()); // 첫 번째 썸네일
            assertEquals("서울특별시, 강남구", result.location());
            assertNotNull(result.rating());
            assertEquals(4.5, result.rating().averageStar());
            assertEquals(100, result.rating().reviewCount());
            assertEquals("6월 10일 ~ 6월 20일", result.period()); // 하드코딩된 값
            assertEquals(1, result.waitingNumber());
            assertEquals("RESERVED", result.status());
        }

        @Test
        @DisplayName("썸네일이 없는 Waiting을 WaitingResponse로 변환 테스트")
        public void test02() {
            // given
            final Popup popupWithoutThumbnails = Popup.builder()
                    .id(1L)
                    .name("테스트 팝업")
                    .location(
                            new Location(
                                    "서울시 강남구",
                                    "서울특별시",
                                    "강남구",
                                    "역삼동",
                                    127.0012,
                                    37.5665
                            )
                    )
                    .schedule(
                            new PopupSchedule(
                                    new DateRange(
                                            LocalDate.now(),
                                            LocalDate.now().plusDays(30)
                                    ),
                                    new WeeklyOpeningHours(
                                            List.of(
                                                    new OpeningHours(
                                                            DayOfWeek.MONDAY,
                                                            LocalTime.now(),
                                                            LocalTime.now().plusHours(1)
                                                    )
                                            )
                                    )
                            )
                    )
                    .display(
                            new PopupDisplay(
                                    Collections.emptyList(),
                                    new PopupContent("팝업 설명", "팝업 공지"),
                                    List.of(new Sns("http://image.com", "http://sns.com"))
                            )
                    )
                    .type(PopupType.EXHIBITION)
                    .popupCategories(
                            List.of(new PopupCategory(1L, "예술"))
                    )
                    .status(PopupStatus.IN_PROGRESS)
                    .build();

            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    2L, popupWithoutThumbnails, "김철수", validMember,
                    "kim@example.com", 3, 2, WaitingStatus.COMPLETED, registeredAt
            );

            // when
            WaitingResponse result = new WaitingDtoMapper().toResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(2L, result.waitingId());
            assertEquals(2L, result.popupId());
            assertEquals("썸네일 없는 팝업", result.popupName());
            assertNull(result.popupImageUrl()); // 썸네일이 없으므로 null
            assertEquals("서울특별시, 강남구", result.location());
            assertNotNull(result.rating());
            assertEquals(3.5, result.rating().averageStar());
            assertEquals(50, result.rating().reviewCount());
            assertEquals("6월 10일 ~ 6월 20일", result.period());
            assertEquals(2, result.waitingNumber());
            assertEquals("COMPLETED", result.status());
        }
    }
} 