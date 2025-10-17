package com.example.demo.application.service;

import com.example.demo.application.dto.popup.LocationResponse;
import com.example.demo.application.dto.popup.PopupSummaryResponse;
import com.example.demo.application.dto.popup.SearchTagsResponse;
import com.example.demo.application.dto.waiting.*;
import com.example.demo.application.mapper.WaitingDtoMapper;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.*;
import com.example.demo.domain.model.waiting.PopupWaitingStatistics;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @Mock
    private WaitingPort waitingPort;

    @Mock
    private PopupPort popupPort;

    @Mock
    private MemberPort memberPort;

    @Mock
    private WaitingDtoMapper waitingDtoMapper;

    @Mock
    private WaitingNotificationService waitingNotificationService;

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private BanPort banPort;

    @Mock
    private WaitingStatisticsPort waitingStatisticsPort;

    @InjectMocks
    private WaitingService waitingService;

    @Nested
    @DisplayName("createWaiting 테스트")
    class Test01 {
        LocalDateTime now = LocalDateTime.of(2024, 6, 10, 10, 0);
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
                                        now.toLocalDate(),
                                        now.toLocalDate().plusDays(30)
                                ),
                                new WeeklyOpeningHours(
                                        List.of(
                                                new OpeningHours(
                                                        DayOfWeek.MONDAY,
                                                        now.toLocalTime(),
                                                        now.toLocalTime().plusHours(1)
                                                )
                                        )
                                )
                        )
                )
                .display(
                        new PopupDisplay(
                                List.of("https://example.com/image1.jpg"),
                                List.of("https://example.com/brand1.jpg"),
                                new PopupContent("소개", "공지"),
                                List.of(new Sns("https://example.com/icon1.jpg", "https://instagram.com/example"))
                        )
                )
                .type(PopupType.EXHIBITION)
                .popupCategories(
                        List.of(new PopupCategory(1L, "예술"))
                )
                .status(PopupStatus.IN_PROGRESS)
                .build();

        private final Member validMember = new Member(1L, "테스트 사용자", "test@example.com");

        private final WaitingCreateRequest validRequest = new WaitingCreateRequest(
                1L, 1L, "홍길동", 2, "hong@example.com"
        );

        @Test
        @DisplayName("정상적인 현장 대기 신청 테스트")
        public void test01() {
            // given
            Integer nextWaitingNumber = 5;
            LocalDateTime registeredAt = LocalDateTime.now();

            // Mock: 제재 없음
            when(banPort.findByQuery(any())).thenReturn(List.of());

            // Mock: 당일 중복 신청 없음
            when(waitingPort.findByQuery(argThat(query -> query.getDate() != null)))
                    .thenReturn(List.of());

            // Mock: 예상 대기시간 계산
            PopupWaitingStatistics mockStatistics = mock(PopupWaitingStatistics.class);
            when(mockStatistics.calculateExpectedWaitingTime(nextWaitingNumber)).thenReturn(30);
            when(waitingStatisticsPort.findCompletedStatisticsByPopupId(1L))
                    .thenReturn(mockStatistics);

            Waiting savedWaiting = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, nextWaitingNumber,
                    WaitingStatus.WAITING, registeredAt,
                    null, null, 30
            );
            Location location = validPopup.getLocation();
            LocationResponse locationResponse = new LocationResponse(
                    location.addressName(),
                    location.region1depthName(),
                    location.region2depthName(),
                    location.region3depthName(),
                    location.longitude(),
                    location.latitude()
            );

            WaitingCreateResponse expectedResponse = new WaitingCreateResponse(
                    1L, "테스트 팝업", "홍길동", 2, "hong@example.com", nextWaitingNumber, registeredAt, locationResponse, validPopup.getDisplay().mainImageUrls().getFirst()
            );

            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenReturn(nextWaitingNumber);
            when(memberPort.findById(1L)).thenReturn(Optional.of(validMember));
            when(waitingPort.save(any(Waiting.class))).thenReturn(savedWaiting);
            when(waitingDtoMapper.toCreateResponse(savedWaiting)).thenReturn(expectedResponse);

            // when
            WaitingCreateResponse response = waitingService.createWaiting(validRequest, now);

            // then
            assertNotNull(response);
            assertEquals(expectedResponse, response);

            // verify
            verify(popupPort).findById(1L);
            verify(banPort, times(2)).findByQuery(any());
            verify(waitingPort).findByQuery(argThat(query -> query.getDate() != null));
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort).findById(1L);
            verify(waitingStatisticsPort).findCompletedStatisticsByPopupId(1L);
            verify(waitingPort).save(any(Waiting.class));
            verify(waitingDtoMapper).toCreateResponse(savedWaiting);
        }

        @Test
        @DisplayName("존재하지 않는 팝업으로 대기 신청 시 예외 발생")
        public void test02() {
            // given
            when(popupPort.findById(999L)).thenReturn(Optional.empty());

            WaitingCreateRequest invalidRequest = new WaitingCreateRequest(
                    999L, 1L, "홍길동", 2, "hong@example.com"
            );

            // when & then
            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> waitingService.createWaiting(invalidRequest, now)
            );

            assertEquals(ErrorType.POPUP_NOT_FOUND, exception.getErrorType());

            // verify
            verify(popupPort).findById(999L);
            verify(waitingPort, never()).getNextWaitingNumber(any());
            verify(memberPort, never()).findById(any());
            verify(waitingPort, never()).save(any());
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("존재하지 않는 회원으로 대기 신청 시 예외 발생")
        public void test03() {
            // given
            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenReturn(5);
            when(memberPort.findById(999L)).thenReturn(Optional.empty());

            WaitingCreateRequest invalidRequest = new WaitingCreateRequest(
                    1L, 999L, "홍길동", 2, "hong@example.com"
            );

            // when & then
            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> waitingService.createWaiting(invalidRequest, now)
            );

            assertEquals(ErrorType.MEMBER_NOT_FOUND, exception.getErrorType());

            // verify
            verify(popupPort).findById(1L);
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort).findById(999L);
            verify(waitingPort, never()).save(any());
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("대기 정보 저장 시 예외 발생")
        public void test04() {
            // given

            // Mock: 제재 없음
            when(banPort.findByQuery(any())).thenReturn(List.of());

            // Mock: 당일 중복 신청 없음
            when(waitingPort.findByQuery(argThat(query -> query.getDate() != null)))
                    .thenReturn(List.of());

            // Mock: 예상 대기시간 계산
            PopupWaitingStatistics mockStatistics = mock(PopupWaitingStatistics.class);
            when(mockStatistics.calculateExpectedWaitingTime(anyInt())).thenReturn(30);
            when(waitingStatisticsPort.findCompletedStatisticsByPopupId(1L))
                    .thenReturn(mockStatistics);

            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenReturn(5);
            when(memberPort.findById(1L)).thenReturn(Optional.of(validMember));
            when(waitingPort.save(any(Waiting.class))).thenThrow(new RuntimeException("저장 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest, now));

            // verify
            verify(popupPort).findById(1L);
            verify(banPort, times(2)).findByQuery(any());
            verify(waitingPort).findByQuery(argThat(query -> query.getDate() != null));
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort).findById(1L);
            verify(waitingStatisticsPort).findCompletedStatisticsByPopupId(1L);
            verify(waitingPort).save(any(Waiting.class));
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("대기 번호 조회 시 예외 발생")
        public void test05() {
            // given

            // Mock: 제재 없음
            when(banPort.findByQuery(any())).thenReturn(List.of());

            // Mock: 당일 중복 신청 없음
            when(waitingPort.findByQuery(argThat(query -> query.getDate() != null)))
                    .thenReturn(List.of());

            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenThrow(new RuntimeException("대기 번호 조회 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest, now));

            // verify
            verify(popupPort).findById(1L);
            verify(banPort, times(2)).findByQuery(any());
            verify(waitingPort).findByQuery(argThat(query -> query.getDate() != null));
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort, never()).findById(any());
            verify(waitingStatisticsPort, never()).findCompletedStatisticsByPopupId(anyLong());
            verify(waitingPort, never()).save(any());
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }
    }

    @Nested
    @DisplayName("getVisitHistory 테스트")
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
                                List.of("https://example.com/image1.jpg"),
                                List.of("https://example.com/brand1.jpg"),
                                new PopupContent("소개", "공지"),
                                List.of(new Sns("https://example.com/icon1.jpg", "https://instagram.com/example"))
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
        @DisplayName("정상적인 방문 내역 조회 테스트 - 첫 페이지")
        public void test01() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = null;

            LocalDateTime now = LocalDateTime.now();

            Waiting waiting1 = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            Waiting waiting2 = new Waiting(
                    2L, validPopup, "김철수", validMember,
                    "kim@example.com", 3, 2,
                    WaitingStatus.VISITED, now.minusDays(1));

            List<Waiting> waitings = List.of(waiting1, waiting2);

            PopupSummaryResponse popupDto1 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse1 = new WaitingResponse(
                    1L, 1, "RESERVED", "홍길동", 2, "hong@example.com", popupDto1, now, null, 5
            );

            PopupSummaryResponse popupDto2 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse2 = new WaitingResponse(
                    2L, 2, "COMPLETED", "김철수", 3, "kim@example.com", popupDto2, now, null, 3
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(any(Waiting.class), any(Integer.class))).thenReturn(waitingResponse1, waitingResponse2);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingPort, atLeast(1)).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, atLeast(1)).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 다음 페이지 존재")
        public void test02() {
            // given
            Integer size = 2;
            Long lastWaitingId = null;
            String status = null;

            LocalDateTime now = LocalDateTime.now();
            Waiting waiting1 = new Waiting(1L, validPopup, "홍길동", validMember, "hong@example.com", 2, 1, WaitingStatus.WAITING, now);
            Waiting waiting2 = new Waiting(2L, validPopup, "김철수", validMember, "kim@example.com", 3, 2, WaitingStatus.VISITED, now.minusDays(1));

            List<Waiting> waitings = List.of(waiting1, waiting2);

            PopupSummaryResponse popupDto1 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );
            WaitingResponse waitingResponse1 = new WaitingResponse(1L, 1, "RESERVED", "홍길동", 2, "hong@example.com", popupDto1, now, null, 5);

            PopupSummaryResponse popupDto2 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );
            WaitingResponse waitingResponse2 = new WaitingResponse(2L, 2, "COMPLETED", "김철수", 3, "kim@example.com", popupDto2, now.minusDays(1), null, 3);

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(any(Waiting.class), any(Integer.class))).thenReturn(waitingResponse1, waitingResponse2);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());
            assertTrue(response.hasNext());

            // verify
            verify(waitingPort, atLeast(1)).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, atLeast(1)).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 빈 결과")
        public void test03() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = null;

            List<Waiting> emptyWaitings = List.of();

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(emptyWaitings);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(0, response.content().size());
            assertNull(response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingPort, atLeast(1)).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, never()).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 상태 필터링 (RESERVED)")
        public void test04() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = "WAITING";

            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            List<Waiting> waitings = List.of(waiting);

            PopupSummaryResponse popupDto = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse = new WaitingResponse(
                    1L, 1, status, "홍길동", 2, "hong@example.com", popupDto, now, null, 5
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(any(Waiting.class), any(Integer.class))).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals("WAITING", response.content().getFirst().status());

            // verify
            verify(waitingPort, atLeast(1)).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, atLeast(1)).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 커서 기반 페이지네이션")
        public void test05() {
            // given
            Integer size = 10;
            Long lastWaitingId = 5L;
            String status = null;

            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    6L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 6,
                    WaitingStatus.WAITING, now
            );

            List<Waiting> waitings = List.of(waiting);

            PopupSummaryResponse popupDto = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse = new WaitingResponse(
                    6L, 6, "RESERVED", "홍길동", 2, "hong@example.com", popupDto, now, null, 5
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(any(Waiting.class), any(Integer.class))).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());

            // verify
            verify(waitingPort, atLeast(1)).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, atLeast(1)).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 잘못된 상태 문자열")
        public void test06() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String invalidStatus = "INVALID_STATUS";

            // when & then
            assertThrows(BusinessException.class, () ->
                    waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, invalidStatus, null)
            );

            // verify
            verify(waitingPort, never()).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, never()).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("대기 단건 조회 테스트")
        public void test07() {
            // given
            Long waitingId = 1L;
            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    waitingId, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            PopupSummaryResponse popupDto = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse = new WaitingResponse(
                    waitingId, 1, "WAITING", "홍길동", 2, "hong@example.com", popupDto, now, null, 5
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(List.of(waiting));
            when(waitingDtoMapper.toResponse(any(Waiting.class), any(Integer.class))).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), 10, null, null, waitingId);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals(waitingId, response.content().getFirst().waitingId());
            assertEquals(waitingId, response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingPort).findByQuery(WaitingQuery.forWaitingId(waitingId));
            verify(waitingDtoMapper, atLeast(1)).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("대기 단건 조회 테스트 - 존재하지 않는 대기")
        public void test08() {
            // given
            Long waitingId = 999L;

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(List.of());

            // when & then
            assertThrows(BusinessException.class, () ->
                    waitingService.getVisitHistory(validMember.id(), 10, null, null, waitingId)
            );

            // verify
            verify(waitingPort).findByQuery(WaitingQuery.forWaitingId(waitingId));
            verify(waitingDtoMapper, never()).toResponse(any(Waiting.class), any(Integer.class));
        }

        @Test
        @DisplayName("대기 단건 조회 테스트 - 다른 사용자의 대기")
        public void test09() {
            // given
            Long waitingId = 1L;
            Member otherMember = new Member(2L, "다른 사용자", "other@example.com");
            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    waitingId, validPopup, "홍길동", otherMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(List.of(waiting));

            // when & then
            assertThrows(BusinessException.class, () ->
                    waitingService.getVisitHistory(validMember.id(), 10, null, null, waitingId)
            );

            // verify
            verify(waitingPort).findByQuery(WaitingQuery.forWaitingId(waitingId));
            verify(waitingDtoMapper, never()).toResponse(any(Waiting.class), any(Integer.class));
        }
    }

    @DisplayName("makeVisit 메서드 테스트")
    @Nested
    class MakeVisitTest {
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
                                List.of("https://example.com/image1.jpg"),
                                List.of("https://example.com/brand1.jpg"),
                                new PopupContent("소개", "공지"),
                                List.of(new Sns("https://example.com/icon1.jpg", "https://instagram.com/example"))
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
        @DisplayName("존재하지 않는 대기 ID로 입장 처리 시 WAITING_NOT_FOUND 예외")
        void makeVisit_WaitingNotFound() {
            // given
            Long waitingId = 999L;
            WaitingMakeVisitRequest request = new WaitingMakeVisitRequest(waitingId);

            when(waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId)))
                    .thenReturn(List.of()); // 빈 리스트 반환

            // when & then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> waitingService.makeVisit(request));

            assertEquals(ErrorType.WAITING_NOT_FOUND, exception.getErrorType());
            assertEquals(String.valueOf(waitingId), exception.getAdditionalInfo());
        }

        @Test
        @DisplayName("이미 방문 완료된 대기에서 입장 처리 시 INVALID_WAITING_STATUS 예외")
        void makeVisit_AlreadyVisited() {
            // given
            Long waitingId = 1L;
            WaitingMakeVisitRequest request = new WaitingMakeVisitRequest(waitingId);

            Waiting visitedWaiting = new Waiting(
                    waitingId, validPopup, "홍길동", validMember,
                    "test@example.com", 2, 0, WaitingStatus.VISITED,
                    LocalDateTime.now(), LocalDateTime.now()
            );

            when(waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId)))
                    .thenReturn(List.of(visitedWaiting));

            // when & then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> waitingService.makeVisit(request));

            assertEquals(ErrorType.INVALID_WAITING_STATUS, exception.getErrorType());
        }

        @Test
        @DisplayName("취소된 대기에서 입장 처리 시 INVALID_WAITING_STATUS 예외")
        void makeVisit_CanceledWaiting() {
            // given
            Long waitingId = 1L;
            WaitingMakeVisitRequest request = new WaitingMakeVisitRequest(waitingId);

            Waiting canceledWaiting = new Waiting(
                    waitingId, validPopup, "홍길동", validMember,
                    "test@example.com", 2, 0, WaitingStatus.CANCELED,
                    LocalDateTime.now()
            );

            when(waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId)))
                    .thenReturn(List.of(canceledWaiting));

            // when & then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> waitingService.makeVisit(request));

            assertEquals(ErrorType.INVALID_WAITING_STATUS, exception.getErrorType());
        }

        @Test
        @DisplayName("중간 순번 입장 시 WAITING_NOT_READY 예외 발생")
        void makeVisit_ReorderMiddleNumber() {
            // given
            Long waitingId = 3L;
            WaitingMakeVisitRequest request = new WaitingMakeVisitRequest(waitingId);

            // 3번 대기가 입장 처리됨
            Waiting targetWaiting = new Waiting(
                    3L, validPopup, "김삼번", validMember,
                    "kim3@example.com", 1, 3, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );

            // 같은 팝업의 다른 대기들 (1, 2, 4, 5번)
            Waiting waiting1 = new Waiting(
                    1L, validPopup, "김영번", validMember,
                    "kim1@example.com", 1, 0, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );
            Waiting waiting2 = new Waiting(
                    2L, validPopup, "김일번", validMember,
                    "kim2@example.com", 1, 1, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );
            Waiting waiting4 = new Waiting(
                    4L, validPopup, "김이번", validMember,
                    "kim4@example.com", 1, 2, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );
            Waiting waiting5 = new Waiting(
                    5L, validPopup, "김사번", validMember,
                    "kim5@example.com", 1, 4, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );

            when(waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId)))
                    .thenReturn(List.of(targetWaiting));

            // when & then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> waitingService.makeVisit(request));

            assertEquals(ErrorType.WAITING_NOT_READY, exception.getErrorType());
        }

        @Test
        @DisplayName("첫 번째 순번 입장 시 모든 뒤 순번들이 앞당겨짐")
        void makeVisit_ReorderFirstNumber() {
            // given
            Long waitingId = 1L;
            WaitingMakeVisitRequest request = new WaitingMakeVisitRequest(waitingId);

            // 1번 대기가 입장 처리됨
            Waiting targetWaiting = new Waiting(
                    1L, validPopup, "김영번", validMember,
                    "kim1@example.com", 1, 0, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );

            // 같은 팝업의 다른 대기들 (2, 3, 4번)
            Waiting waiting2 = new Waiting(
                    2L, validPopup, "김일번", validMember,
                    "kim2@example.com", 1, 1, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );
            Waiting waiting3 = new Waiting(
                    3L, validPopup, "김이번", validMember,
                    "kim3@example.com", 1, 2, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );
            Waiting waiting4 = new Waiting(
                    4L, validPopup, "김삼번", validMember,
                    "kim4@example.com", 1, 3, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );

            // Mock: 예상 대기시간 계산
            PopupWaitingStatistics mockStatistics = mock(PopupWaitingStatistics.class);
            when(mockStatistics.calculateExpectedWaitingTime(anyInt())).thenReturn(30);
            when(waitingStatisticsPort.findCompletedStatisticsByPopupId(validPopup.getId()))
                    .thenReturn(mockStatistics);

            when(waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId)))
                    .thenReturn(List.of(targetWaiting));

            when(waitingPort.findByQuery(WaitingQuery.forPopup(validPopup.getId(), WaitingStatus.WAITING)))
                    .thenReturn(List.of(waiting2, waiting3, waiting4));

            // when
            waitingService.makeVisit(request);

            // then
            // 입장 처리된 대기 1번 save + 순번 변경된 대기들 saveAll 1번 = 총 2번 호출
            verify(waitingPort, times(1)).save(any(Waiting.class));
            verify(waitingPort, times(1)).saveAll(anyList());
            verify(waitingStatisticsPort).findCompletedStatisticsByPopupId(validPopup.getId());
        }

        @Test
        @DisplayName("단일 대기 입장 시 순번 재배치 없음")
        void makeVisit_SingleWaiting() {
            // given
            Long waitingId = 1L;
            WaitingMakeVisitRequest request = new WaitingMakeVisitRequest(waitingId);

            Waiting singleWaiting = new Waiting(
                    1L, validPopup, "김유일", validMember,
                    "only@example.com", 1, 0, WaitingStatus.WAITING,
                    LocalDateTime.now()
            );

            when(waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId)))
                    .thenReturn(List.of(singleWaiting));

            when(waitingPort.findByQuery(WaitingQuery.forPopup(validPopup.getId(), WaitingStatus.WAITING)))
                    .thenReturn(List.of()); // 다른 대기 없음

            // when
            waitingService.makeVisit(request);

            // then
            // 입장 처리된 대기 1번 save + 빈 리스트로 saveAll 1번
            verify(waitingPort, times(1)).save(any(Waiting.class));
            verify(waitingPort, times(1)).saveAll(List.of());
        }
    }
}
