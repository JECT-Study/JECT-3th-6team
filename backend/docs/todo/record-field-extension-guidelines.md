# Record 필드 확장 가이드라인

## 문제 상황

WaitingQuery record에 새로운 필드를 추가하는 과정에서 다음과 같은 문제들이 발생했습니다:

1. **하위 호환성 문제**: 새 필드 추가로 기존 생성자 호출부가 모두 컴파일 에러 발생
2. **테스트 코드 영향**: 테스트에서 사용하던 생성자들이 모두 깨짐
3. **코드 변경 범위**: 여러 파일에 걸쳐 수정 작업 필요
4. **유지보수 부담**: 매번 필드 추가 시 반복되는 대규모 수정 작업

## 필요한 규칙과 가이드라인

### 1. Record 설계 원칙

#### 1.1 초기 설계 시 확장성 고려
```java
// ❌ 나쁜 예: 확장성을 고려하지 않은 설계
public record UserQuery(Long userId, String name) {}

// ✅ 좋은 예: 확장 가능성을 고려한 설계
public record UserQuery(
    Long userId,        // 필수 식별자
    String name,        // 기본 검색 조건
    // 향후 확장 예정 필드들을 미리 고려
    String email,       // 추가 검색 조건
    Integer limit,      // 페이징
    String sortBy       // 정렬 (맨 마지막에 배치)
) {
    // 기본값 제공하는 정적 팩토리 메서드
    public static UserQuery simple(Long userId) {
        return new UserQuery(userId, null, null, null, null);
    }
}
```

#### 1.2 Record vs Builder 패턴 선택 기준
- **Record 사용**: 필드가 5개 이하, 불변 객체, 단순 데이터 전달
- **Builder 패턴 사용**: 필드가 많거나 복잡한 초기화 로직, 선택적 필드 많음

### 2. 필드 확장 규칙

#### 2.1 필드 추가 위치 규칙
```java
public record WaitingQuery(
    // 핵심 식별자 (변경되지 않음)
    Long waitingId,
    Long memberId,
    
    // 기본 조건들 (안정적)
    Integer size,
    Long lastWaitingId,
    WaitingStatus status,
    SortOrder sortOrder,
    
    // 새로운 필드는 항상 맨 마지막에 추가
    Long popupId  // ← 새 필드
) {
```

**규칙**: 새 필드는 **반드시 맨 마지막에 추가**하여 기존 순서를 유지

#### 2.2 하위 호환성 유지 방법
```java
public record WaitingQuery(
    Long waitingId, Long memberId, Integer size, 
    Long lastWaitingId, WaitingStatus status, 
    SortOrder sortOrder, Long popupId
) {
    
    // 기존 필드만으로 구성된 생성자 제공
    public WaitingQuery(Long waitingId, Long memberId, Integer size, 
                       Long lastWaitingId, WaitingStatus status, 
                       SortOrder sortOrder) {
        this(waitingId, memberId, size, lastWaitingId, 
             status, sortOrder, null); // 새 필드는 기본값
    }
    
    // 점진적 마이그레이션을 위한 @Deprecated 활용
    @Deprecated(since = "1.2", forRemoval = true)
    public WaitingQuery(Long waitingId, Long memberId) {
        this(waitingId, memberId, null, null, null, null, null);
    }
}
```

### 3. 정적 팩토리 메서드 활용

#### 3.1 의미 있는 메서드명 규칙
```java
public record WaitingQuery(...) {
    
    // ✅ 용도가 명확한 팩토리 메서드
    public static WaitingQuery forWaitingId(Long waitingId) { ... }
    public static WaitingQuery forMemberHistory(Long memberId, Integer size) { ... }
    public static WaitingQuery forPopupWaitings(Long popupId, WaitingStatus status) { ... }
    
    // ❌ 의미가 모호한 메서드명
    public static WaitingQuery create(Long id) { ... }
    public static WaitingQuery of(Long a, Long b) { ... }
}
```

#### 3.2 기본값 제공 전략
```java
public static WaitingQuery firstPage(Long memberId, Integer size) {
    return new WaitingQuery(
        null,                                    // waitingId
        memberId,                               // memberId  
        size,                                   // size
        null,                                   // lastWaitingId
        null,                                   // status (전체 조회)
        SortOrder.RESERVED_FIRST_THEN_DATE_DESC, // 기본 정렬
        null                                    // popupId
    );
}
```

### 4. 변경 영향도 관리

#### 4.1 변경 전 체크리스트
- [ ] 기존 생성자 호출부 파악 (IDE 검색 활용)
- [ ] 테스트 코드 영향도 분석
- [ ] 하위 호환 생성자 추가 계획
- [ ] 마이그레이션 순서 결정

#### 4.2 단계별 마이그레이션 전략
```java
// Phase 1: 새 필드 추가 + 하위 호환 생성자
public record WaitingQuery(..., Long popupId) {
    public WaitingQuery(기존_필드들) { 
        this(기존_필드들, null); 
    }
}

// Phase 2: 새로운 사용처에서 새 필드 활용
WaitingQuery query = WaitingQuery.forPopup(popupId, status);

// Phase 3: 기존 코드 점진적 마이그레이션
// 기존: new WaitingQuery(id, null, null, null, null, null)
// 신규: WaitingQuery.forWaitingId(id)

// Phase 4: 구 생성자 @Deprecated 처리 및 제거
```

### 5. 테스트 코드 관리

#### 5.1 테스트 코드 영향 최소화
```java
// ❌ 직접 생성자 사용 (변경에 취약)
@Test
void test() {
    WaitingQuery query = new WaitingQuery(1L, null, null, null, null, null);
    // ...
}

// ✅ 정적 팩토리 메서드 사용 (변경에 안정적)
@Test
void test() {
    WaitingQuery query = WaitingQuery.forWaitingId(1L);
    // ...
}
```

#### 5.2 테스트 유틸리티 클래스 활용
```java
public class WaitingQueryTestFixture {
    public static WaitingQuery basicQuery() {
        return WaitingQuery.forWaitingId(1L);
    }
    
    public static WaitingQuery memberHistoryQuery(Long memberId) {
        return WaitingQuery.forMemberHistory(memberId, 10);
    }
}
```

### 6. 문서화 및 커뮤니케이션

#### 6.1 변경 사항 문서화
- **CHANGELOG**: 필드 추가, 새로운 팩토리 메서드, Deprecated 사항
- **Migration Guide**: 기존 코드 변경 방법, 권장 사항
- **API 문서**: 새 필드의 용도, 기본값, 사용 예시

#### 6.2 팀 내 커뮤니케이션
- 변경 전 리뷰 요청
- 영향받는 팀원들에게 사전 공지
- 마이그레이션 계획 공유

### 7. 도구 및 자동화

#### 7.1 IDE 활용
- Find Usages로 영향도 파악
- Refactor 도구로 일괄 변경
- 컴파일 에러를 통한 누락 방지

#### 7.2 정적 분석 도구
- 사용되지 않는 생성자 탐지
- Deprecated 사용 현황 모니터링

## 결론

Record 필드 확장은 단순해 보이지만 실제로는 많은 영향을 미치는 작업입니다. 이 가이드라인을 따라:

1. **설계 시점**에서 확장성을 고려하고
2. **변경 시점**에서 하위 호환성을 유지하며  
3. **사용 시점**에서 정적 팩토리 메서드를 활용하여

안정적이고 유지보수가 용이한 코드를 작성할 수 있습니다.

---

**다음 액션 아이템**:
- [ ] 기존 Record 클래스들의 확장성 검토
- [ ] 정적 팩토리 메서드 패턴 적용
- [ ] 테스트 코드의 직접 생성자 사용 현황 파악
- [ ] 팀 내 Record 사용 가이드라인 수립