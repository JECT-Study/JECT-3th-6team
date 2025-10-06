# WaitingServiceTest 수정 계획

## 작성일: 2025-10-06
## 상태: 작업 계획

---

## 1. 테스트 실패 원인 분석

### 1.1 NullPointerException 발생 테스트 (3건)

**실패 테스트**:
- `test01` - 정상적인 현장 대기 신청 테스트
- `makeVisit_ReorderFirstNumber` - 첫 번째 순번 입장 시 모든 뒤 순번들이 앞당겨짐
- `makeVisit_SingleWaiting` - 단일 대기 입장 시 순번 재배치 없음

**원인**:
- `WaitingService`에 새로운 의존성 추가됨:
  - `BanPort` (제재 확인용)
  - `WaitingStatisticsPort` (예상 대기시간 계산용)
- 테스트 클래스에 `@Mock` 선언 누락
- Mock 객체가 null이어서 NPE 발생

**영향받는 코드**:
```java
// WaitingService.java:30-31
private final BanPort banPort;
private final WaitingStatisticsPort waitingStatisticsPort;

// WaitingService.java:52-57 (대기 신청 시 제재 확인)
boolean notPopupBan = banPort.findByQuery(BanQuery.byMemberAndPopup(...)).isEmpty();
boolean notGlobalBan = banPort.findByQuery(BanQuery.byMemberIdFromAll(...)).isEmpty();

// WaitingService.java:86-87 (대기 신청 시 예상 대기시간 계산)
Integer expectedWaitingTime = waitingStatisticsPort.findCompletedStatisticsByPopupId(...)
        .calculateExpectedWaitingTime(nextWaitingNumber);

// WaitingService.java:209 (입장 후 순번 재정렬 시 통계 조회)
PopupWaitingStatistics updatedStatistics = waitingStatisticsPort.findCompletedStatisticsByPopupId(popupId);
```

---

### 1.2 Mockito Verification 실패 테스트 (2건)

**실패 테스트**:
- `test04` - 대기 정보 저장 시 예외 발생
- `test05` - 대기 번호 조회 시 예외 발생

**원인**:
- 새로운 로직 추가로 인한 메서드 호출 순서 변경
- `test04`: 저장 전에 `banPort.findByQuery()` 호출 추가됨
- `test05`: 대기번호 조회 전에 `banPort.findByQuery()` 호출 추가됨
- verify 시점에 예상했던 메서드가 호출되지 않음

**실제 호출 순서**:
```
1. popupPort.findById()
2. popup.isOpenAt() 검증
3. banPort.findByQuery() (POPUP 제재) ← 새로 추가됨
4. banPort.findByQuery() (GLOBAL 제재) ← 새로 추가됨
5. waitingPort.findByQuery() (중복 체크) ← 새로 추가됨
6. waitingPort.getNextWaitingNumber()
7. memberPort.findById()
8. waitingStatisticsPort.findCompletedStatisticsByPopupId() ← 새로 추가됨
9. waitingPort.save()
```

---

## 2. 수정 작업 계획

### Phase 1: Mock 객체 추가

**작업 내용**:
```java
// WaitingServiceTest.java에 추가
@Mock
private BanPort banPort;

@Mock
private WaitingStatisticsPort waitingStatisticsPort;
```

**위치**: `WaitingServiceTest.java:44-61` (다른 @Mock 선언들과 함께)

---

### Phase 2: 공통 Mock 동작 설정

**2.1 BanPort Mock 설정** (모든 테스트에서 제재 없음으로 설정)

```java
// 각 테스트 메서드에서 또는 @BeforeEach에서 설정
when(banPort.findByQuery(any(BanQuery.class))).thenReturn(List.of());
```

**적용 대상**:
- `Test01.test01` - 정상 대기 신청
- `Test01.test04` - 저장 실패
- `Test01.test05` - 대기번호 조회 실패

**2.2 WaitingStatisticsPort Mock 설정**

```java
// 예상 대기시간 계산을 위한 Mock
PopupWaitingStatistics mockStatistics = mock(PopupWaitingStatistics.class);
when(mockStatistics.calculateExpectedWaitingTime(anyInt())).thenReturn(30); // 30분

when(waitingStatisticsPort.findCompletedStatisticsByPopupId(anyLong()))
    .thenReturn(mockStatistics);
```

**적용 대상**:
- `Test01.test01` - 정상 대기 신청
- `Test01.test04` - 저장 실패
- `Test01.test05` - 대기번호 조회 실패
- `MakeVisitTest.makeVisit_ReorderFirstNumber` - 순번 재정렬
- `MakeVisitTest.makeVisit_SingleWaiting` - 단일 대기

**2.3 WaitingPort.findByQuery Mock 설정** (중복 체크용)

```java
// 중복 신청 없음으로 설정
when(waitingPort.findByQuery(argThat(query ->
    query.date() != null && query.memberId() != null && query.popupId() != null
))).thenReturn(List.of()); // 당일 대기 내역 없음
```

**적용 대상**:
- `Test01.test01` - 정상 대기 신청
- `Test01.test04` - 저장 실패
- `Test01.test05` - 대기번호 조회 실패

---

### Phase 3: 개별 테스트 수정

#### 3.1 Test01.test01 (정상 대기 신청)

**기존 문제**:
- BanPort, WaitingStatisticsPort mock 없음
- 중복 체크용 findByQuery mock 없음

**수정 내용**:
```java
@Test
@DisplayName("정상적인 현장 대기 신청 테스트")
public void test01() {
    // given
    Integer nextWaitingNumber = 5;
    LocalDateTime registeredAt = LocalDateTime.now();

    // Mock: 제재 없음
    when(banPort.findByQuery(any(BanQuery.class))).thenReturn(List.of());

    // Mock: 당일 중복 신청 없음
    when(waitingPort.findByQuery(argThat(query -> query.date() != null)))
        .thenReturn(List.of());

    // Mock: 예상 대기시간 계산
    PopupWaitingStatistics mockStatistics = mock(PopupWaitingStatistics.class);
    when(mockStatistics.calculateExpectedWaitingTime(5)).thenReturn(30);
    when(waitingStatisticsPort.findCompletedStatisticsByPopupId(1L))
        .thenReturn(mockStatistics);

    Waiting savedWaiting = new Waiting(
            1L, validPopup, "홍길동", validMember,
            "hong@example.com", 2, nextWaitingNumber,
            WaitingStatus.WAITING, registeredAt,
            null, null, 30 // expectedWaitingTimeMinutes 추가
    );

    // ... 기존 코드 ...

    when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
    when(waitingPort.getNextWaitingNumber(1L)).thenReturn(nextWaitingNumber);
    when(memberPort.findById(1L)).thenReturn(Optional.of(validMember));
    when(waitingPort.save(any(Waiting.class))).thenReturn(savedWaiting);
    when(waitingDtoMapper.toCreateResponse(savedWaiting)).thenReturn(expectedResponse);

    // when
    WaitingCreateResponse response = waitingService.createWaiting(validRequest);

    // then
    assertNotNull(response);
    assertEquals(expectedResponse, response);

    // verify - 새로운 호출 추가
    verify(popupPort).findById(1L);
    verify(banPort, times(2)).findByQuery(any(BanQuery.class)); // POPUP + GLOBAL
    verify(waitingPort).findByQuery(argThat(query -> query.date() != null)); // 중복 체크
    verify(waitingPort).getNextWaitingNumber(1L);
    verify(memberPort).findById(1L);
    verify(waitingStatisticsPort).findCompletedStatisticsByPopupId(1L);
    verify(waitingPort).save(any(Waiting.class));
    verify(waitingDtoMapper).toCreateResponse(savedWaiting);
}
```

---

#### 3.2 Test01.test04 (대기 정보 저장 시 예외)

**기존 문제**:
- `waitingPort.save()` 전에 `banPort`, `waitingStatisticsPort` 호출됨
- verify에서 기대하는 메서드가 호출되지 않았다고 실패

**수정 내용**:
```java
@Test
@DisplayName("대기 정보 저장 시 예외 발생")
public void test04() {
    // given

    // Mock: 제재 없음
    when(banPort.findByQuery(any(BanQuery.class))).thenReturn(List.of());

    // Mock: 중복 신청 없음
    when(waitingPort.findByQuery(argThat(query -> query.date() != null)))
        .thenReturn(List.of());

    // Mock: 예상 대기시간
    PopupWaitingStatistics mockStatistics = mock(PopupWaitingStatistics.class);
    when(mockStatistics.calculateExpectedWaitingTime(anyInt())).thenReturn(30);
    when(waitingStatisticsPort.findCompletedStatisticsByPopupId(1L))
        .thenReturn(mockStatistics);

    when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
    when(waitingPort.getNextWaitingNumber(1L)).thenReturn(5);
    when(memberPort.findById(1L)).thenReturn(Optional.of(validMember));
    when(waitingPort.save(any(Waiting.class))).thenThrow(new RuntimeException("저장 실패"));

    // when & then
    assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest));

    // verify - 수정
    verify(popupPort).findById(1L);
    verify(banPort, times(2)).findByQuery(any(BanQuery.class));
    verify(waitingPort).findByQuery(argThat(query -> query.date() != null));
    verify(waitingPort).getNextWaitingNumber(1L);
    verify(memberPort).findById(1L);
    verify(waitingStatisticsPort).findCompletedStatisticsByPopupId(1L);
    verify(waitingPort).save(any(Waiting.class));
    verify(waitingDtoMapper, never()).toCreateResponse(any());
}
```

---

#### 3.3 Test01.test05 (대기 번호 조회 시 예외)

**기존 문제**:
- `waitingPort.getNextWaitingNumber()` 전에 제재 확인, 중복 확인 호출됨
- verify에서 기대하는 순서가 맞지 않음

**수정 내용**:
```java
@Test
@DisplayName("대기 번호 조회 시 예외 발생")
public void test05() {
    // given

    // Mock: 제재 없음
    when(banPort.findByQuery(any(BanQuery.class))).thenReturn(List.of());

    // Mock: 중복 신청 없음
    when(waitingPort.findByQuery(argThat(query -> query.date() != null)))
        .thenReturn(List.of());

    when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
    when(waitingPort.getNextWaitingNumber(1L)).thenThrow(new RuntimeException("대기 번호 조회 실패"));

    // when & then
    assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest));

    // verify - 수정
    verify(popupPort).findById(1L);
    verify(banPort, times(2)).findByQuery(any(BanQuery.class));
    verify(waitingPort).findByQuery(argThat(query -> query.date() != null));
    verify(waitingPort).getNextWaitingNumber(1L);
    verify(memberPort, never()).findById(any());
    verify(waitingStatisticsPort, never()).findCompletedStatisticsByPopupId(anyLong());
    verify(waitingPort, never()).save(any());
    verify(waitingDtoMapper, never()).toCreateResponse(any());
}
```

---

#### 3.4 MakeVisitTest.makeVisit_ReorderFirstNumber

**기존 문제**:
- `reorderAfterEntry()` 메서드에서 `waitingStatisticsPort` 호출
- Mock 설정 없어서 NPE 발생

**수정 내용**:
```java
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
    // 입장 처리된 대기 1번 + 순번 변경된 대기 3번 = 총 4번 save 호출
    verify(waitingPort, times(4)).save(any(Waiting.class));
    verify(waitingStatisticsPort).findCompletedStatisticsByPopupId(validPopup.getId());
}
```

---

#### 3.5 MakeVisitTest.makeVisit_SingleWaiting

**기존 문제**:
- `reorderAfterEntry()` 호출되지만 대기자가 없어도 통계는 조회됨
- Mock 설정 없어서 NPE 발생

**수정 내용**:
```java
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

    // Mock: 예상 대기시간 계산 (빈 리스트여도 통계 조회는 됨)
    PopupWaitingStatistics mockStatistics = mock(PopupWaitingStatistics.class);
    when(mockStatistics.calculateExpectedWaitingTime(anyInt())).thenReturn(30);
    when(waitingStatisticsPort.findCompletedStatisticsByPopupId(validPopup.getId()))
        .thenReturn(mockStatistics);

    when(waitingPort.findByQuery(WaitingQuery.forWaitingId(waitingId)))
            .thenReturn(List.of(singleWaiting));

    when(waitingPort.findByQuery(WaitingQuery.forPopup(validPopup.getId(), WaitingStatus.WAITING)))
            .thenReturn(List.of()); // 다른 대기 없음

    // when
    waitingService.makeVisit(request);

    // then
    // 입장 처리된 대기만 저장
    verify(waitingPort, times(1)).save(any(Waiting.class));
    verify(waitingStatisticsPort).findCompletedStatisticsByPopupId(validPopup.getId());
}
```

---

## 3. 작업 우선순위

### 🔴 High Priority (테스트 실패 해결)
1. **Phase 1 완료** - Mock 객체 추가
2. **Phase 3.1 완료** - test01 수정 (정상 케이스)
3. **Phase 3.4 완료** - makeVisit_ReorderFirstNumber 수정
4. **Phase 3.5 완료** - makeVisit_SingleWaiting 수정

### 🟡 Medium Priority (Verification 수정)
5. **Phase 3.2 완료** - test04 수정
6. **Phase 3.3 완료** - test05 수정

### 🟢 Low Priority (추가 테스트 작성)
7. 제재된 회원 대기 신청 테스트 추가
8. 중복 신청 테스트 추가 (노쇼 0개, 1개, 2개 케이스)
9. 팝업 운영시간 외 신청 테스트 추가

---

## 4. 검증 방법

### 4.1 단위 테스트 실행
```bash
./gradlew test --tests "com.example.demo.application.service.WaitingServiceTest"
```

### 4.2 개별 테스트 실행
```bash
# 정상 대기 신청 테스트
./gradlew test --tests "com.example.demo.application.service.WaitingServiceTest.Test01.test01"

# 입장 처리 테스트
./gradlew test --tests "com.example.demo.application.service.WaitingServiceTest.MakeVisitTest.makeVisit_ReorderFirstNumber"
```

### 4.3 성공 기준
- ✅ 모든 테스트 통과 (20 tests completed, 0 failed)
- ✅ NullPointerException 발생하지 않음
- ✅ Mockito verification 통과
- ✅ 기존 테스트 로직 유지 (비즈니스 로직 변경 없음)

---

## 5. 주의사항

### 5.1 Import 추가 필요
```java
import com.example.demo.domain.model.waiting.PopupWaitingStatistics;
import com.example.demo.domain.port.BanPort;
import com.example.demo.domain.port.WaitingStatisticsPort;
import com.example.demo.domain.model.ban.BanQuery;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
```

### 5.2 ArgumentMatcher 사용
중복 체크용 `findByQuery` Mock 설정 시:
```java
// ✅ 올바른 방법
when(waitingPort.findByQuery(argThat(query ->
    query.date() != null && query.memberId() != null
))).thenReturn(List.of());

// ❌ 잘못된 방법 (WaitingQuery가 record라 equals 비교가 정확해야 함)
when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(List.of());
```

### 5.3 Waiting 생성자 변경
`expectedWaitingTimeMinutes` 필드 추가됨:
```java
// 기존
new Waiting(1L, popup, "홍길동", member, "email", 2, 5, WAITING, now)

// 변경 후
new Waiting(1L, popup, "홍길동", member, "email", 2, 5, WAITING, now, null, null, 30)
```

---

**작성자**: Claude Code Analysis
**검토 필요**: 개발팀
**예상 작업 시간**: 1-2시간
