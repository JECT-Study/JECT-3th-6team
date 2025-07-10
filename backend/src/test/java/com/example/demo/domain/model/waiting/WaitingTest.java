package com.example.demo.domain.model.waiting;

import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaitingTest {
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
    @DisplayName("정상 생성 테스트")
    public void test01() {
        // given
        Long id = 1L;
        String waitingPersonName = "홍길동";
        String contactEmail = "hong@example.com";
        Integer peopleCount = 2;
        Integer waitingNumber = 1;
        WaitingStatus status = WaitingStatus.RESERVED;
        LocalDateTime registeredAt = LocalDateTime.now();
        // when & then
        assertDoesNotThrow(() -> new Waiting(
                id,
                validPopup,
                waitingPersonName,
                validMember,
                contactEmail,
                peopleCount,
                waitingNumber,
                status,
                registeredAt
        ));
    }

    @Test
    @DisplayName("대기자 이름 제약 조건 테스트 - null 이름")
    public void test02() {
        // given
        String nullName = null;
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, nullName, validMember,
                        "test@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기자 이름은 2글자 이상 20글자 이하여야 합니다. 현재: null", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 이름 제약 조건 테스트 - 빈 이름")
    public void test02_2() {
        // given
        String emptyName = "";
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, emptyName, validMember,
                        "test@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기자 이름은 2글자 이상 20글자 이하여야 합니다. 현재: ", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 이름 제약 조건 테스트 - 1글자 이름")
    public void test02_3() {
        // given
        String shortName = "홍";
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, shortName, validMember,
                        "test@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기자 이름은 2글자 이상 20글자 이하여야 합니다. 현재: 홍", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 이름 제약 조건 테스트 - 21글자 이름")
    public void test02_4() {
        // given
        String longName = "가나다라마바사아자차카타파하가나다라마바사아자차카타파하";
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, longName, validMember,
                        "test@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기자 이름은 2글자 이상 20글자 이하여야 합니다. 현재: 가나다라마바사아자차카타파하가나다라마바사아자차카타파하", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 이름 제약 조건 테스트 - 특수문자 포함")
    public void test02_5() {
        // given
        String specialCharName = "홍길동!";
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, specialCharName, validMember,
                        "test@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기자 이름에 특수문자는 포함될 수 없습니다. 현재: 홍길동!", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 수 제약 조건 테스트 - null 인원수")
    public void test03() {
        // given
        Integer nullPeopleCount = null;
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, "홍길동", validMember,
                        "test@example.com", nullPeopleCount, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기 인원수는 1명 이상 6명 이하여야 합니다. 현재: null", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 수 제약 조건 테스트 - 0명")
    public void test03_2() {
        // given
        Integer zeroPeopleCount = 0;
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, "홍길동", validMember,
                        "test@example.com", zeroPeopleCount, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기 인원수는 1명 이상 6명 이하여야 합니다. 현재: 0", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 수 제약 조건 테스트 - 7명")
    public void test03_3() {
        // given
        Integer sevenPeopleCount = 7;
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiting(
                        1L, validPopup, "홍길동", validMember,
                        "test@example.com", sevenPeopleCount, 1, WaitingStatus.RESERVED, LocalDateTime.now()
                )
        );
        assertEquals("대기 인원수는 1명 이상 6명 이하여야 합니다. 현재: 7", exception.getMessage());
    }

    @Test
    @DisplayName("대기자 수 제약 조건 테스트 - 경계값 1명")
    public void test03_4() {
        // given
        Integer onePeopleCount = 1;
        // when & then
        assertDoesNotThrow(() -> new Waiting(
                1L, validPopup, "홍길동", validMember,
                "test@example.com", onePeopleCount, 1, WaitingStatus.RESERVED, LocalDateTime.now()
        ));
    }

    @Test
    @DisplayName("대기자 수 제약 조건 테스트 - 경계값 6명")
    public void test03_5() {
        // given
        Integer sixPeopleCount = 6;
        // when & then
        assertDoesNotThrow(() -> new Waiting(
                1L, validPopup, "홍길동", validMember,
                "test@example.com", sixPeopleCount, 1, WaitingStatus.RESERVED, LocalDateTime.now()
        ));
    }

    @Test
    @DisplayName("대기자 이메일 제약 조건 테스트 - 유효한 이메일")
    public void test04() {
        // given
        String validEmail = "test@example.com";
        // when & then
        assertDoesNotThrow(() -> new Waiting(
                1L, validPopup, "홍길동", validMember,
                validEmail, 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
        ));
    }

    @Test
    @DisplayName("대기자 이메일 제약 조건 테스트 - 유효한 이메일 (한국어 도메인)")
    public void test04_2() {
        // given
        String validEmail = "test@테스트.co.kr";
        // when & then
        assertDoesNotThrow(() -> new Waiting(
                1L, validPopup, "홍길동", validMember,
                validEmail, 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
        ));
    }
}