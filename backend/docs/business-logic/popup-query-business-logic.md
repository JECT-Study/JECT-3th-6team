# 팝업 조회 비즈니스 로직

## 개요

팝업 조회 시스템은 다양한 필터링 조건과 검색 방식을 제공하여 사용자가 원하는 팝업을 효율적으로 찾을 수 있도록 지원합니다. 목록 조회와 지도 조회 두 가지 주요 방식을 제공하며, 각각 다른 필터링 조건과 제약사항을 가집니다.

## 팝업 조회 방식

### 1. 목록 조회 (PopupFilterRequest → PopupQuery)

**엔드포인트**: `GET /api/popups`
**용도**: 필터링된 팝업 목록을 커서 기반 페이징으로 조회

#### 지원 필터링 조건

##### 1.1 직접 조회
```java
// PopupFilterRequest.java:12
Long popupId
```
- **제약조건**: 없음
- **처리방식**: 단일 팝업 직접 조회
- **우선순위**: 최고 (다른 모든 조건 무시)

##### 1.2 키워드 검색
```java
// PopupFilterRequest.java:25
String keyword
```
- **제약조건**: 없음 (공백 문자열 허용, 내부에서 trim 처리)
- **처리방식**: 토큰화 후 다중 토큰 OR 검색
- **우선순위**: 2순위 (popupId 제외 모든 조건 무시)
- **토큰화 정책**: 공백 문자로 분리, 중복 제거, 빈 토큰 제거

```java
// PopupDtoMapper.java:194-206 - 키워드 우선순위 정책
if (request.keyword() != null && !request.keyword().trim().isEmpty()) {
    return PopupQuery.withFilters(
            Optional.ofNullable(request.size()).orElse(10),
            null, // types 무시
            null, // categories 무시
            null, // startDate 무시
            null, // endDate 무시
            null, // region1DepthName 무시
            request.lastPopupId(),
            request.keyword().trim()
    );
}
```

##### 1.3 페이징 조건
```java
// PopupFilterRequest.java:13-14, 24
@Min(1) Integer size,  // 기본값: 10
Long lastPopupId       // 커서 기반 페이징
```
- **size 제약조건**: 최소 1 (jakarta.validation.constraints.Min)
- **기본값**: 10 (PopupDtoMapper.java:197, 216)
- **페이징 방식**: 커서 기반 (lastPopupId 이후 데이터 조회)

##### 1.4 팝업 타입 필터
```java
// PopupFilterRequest.java:15-16
@Size(max = 3) List<String> type
```
- **제약조건**: 최대 3개 (jakarta.validation.constraints.Size)
- **허용값**: "체험형", "전시형", "판매형" (PopupType 한글명)
- **변환규칙**: 한글명 → enum명 (체험형 → EXPERIENTIAL)

```java
// PopupType.java:34-41 - 한글명 변환 정책
public static PopupType fromKorean(String value) {
    for (PopupType type : values()) {
        if (type.korean.equals(value)) {
            return type;
        }
    }
    throw new BusinessException(ErrorType.INVALID_POPUP_TYPE, value);
}
```

##### 1.5 카테고리 필터
```java
// PopupFilterRequest.java:17-18
@Size(max = 3) List<String> category
```
- **제약조건**: 최대 3개
- **값 형태**: 카테고리명 문자열
- **처리방식**: DB의 PopupCategoryEntity.name과 직접 매칭

##### 1.6 날짜 범위 필터
```java
// PopupFilterRequest.java:19-22
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
```
- **제약조건**: ISO DATE 형식 (yyyy-MM-dd)
- **비즈니스 로직**: 팝업 운영 기간과 겹치는 구간 검색
- **SQL 조건**: `popup.endDate >= startDate AND popup.startDate <= endDate`

##### 1.7 지역 필터
```java
// PopupFilterRequest.java:23
String region1DepthName
```
- **제약조건**: 없음
- **특별처리**: "전국" 값은 null로 변환 (전체 조회)
- **매칭방식**: PopupLocationEntity.region1DepthName과 정확 일치

```java
// PopupDtoMapper.java:221 - 전국 처리 정책
(request.region1DepthName() == null || "전국".equals(request.region1DepthName())) ? null : request.region1DepthName()
```

### 2. 지도 조회 (PopupMapRequest → PopupMapQuery)

**엔드포인트**: `GET /api/popups/map`
**용도**: 지도 영역 내 팝업을 좌표 기반으로 조회

#### 필수 조건

##### 2.1 좌표 범위 (필수)
```java
// PopupMapRequest.java:24-39
@NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal minLatitude,
@NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal maxLatitude,  
@NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal minLongitude,
@NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal maxLongitude
```
- **제약조건**: 
  - 위도: -90.0 ~ 90.0
  - 경도: -180.0 ~ 180.0
  - 모든 값 필수 (@NotNull)

#### 선택적 필터

##### 2.2 팝업 타입 (선택)
```java
// PopupMapRequest.java:40
String type  // 콤마 구분 문자열
```
- **형태**: "체험형,전시형" (콤마로 구분)
- **변환**: 콤마 split → PopupType.fromKorean() 변환
- **처리**: null 또는 빈 문자열인 경우 모든 타입 허용

##### 2.3 카테고리 (선택)
```java
// PopupMapRequest.java:41
String category  // 콤마 구분 문자열
```
- **형태**: "카테고리1,카테고리2" (콤마로 구분)
- **처리**: 메모리상에서 필터링 (DB 조건 아님)

##### 2.4 날짜 범위 (선택)
```java
// PopupMapRequest.java:42-45
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
```
- **제약조건**: 둘 다 제공되어야 유효 (하나만 있으면 무시)
- **처리**: DateRange 객체로 변환

## 데이터 변환 흐름

### 1. 목록 조회 변환 흐름

```
PopupFilterRequest (Presentation)
    ↓ [PopupDtoMapper.toQuery()]
PopupQuery (Domain) 
    ↓ [PopupPortAdapter.findByQuery()]
QueryDSL/JPA Query (Infrastructure)
    ↓ [DB 조회 결과]
PopupEntity (Infrastructure)
    ↓ [PopupEntityMapper.toDomain()]
Popup (Domain)
    ↓ [PopupDtoMapper.toPopupSummaryResponse()]
PopupSummaryResponse (Presentation)
```

#### 주요 변환 규칙

##### 1.1 타입 변환 (한글 → enum)
```java
// PopupDtoMapper.java:208-213
List<String> mappedTypes = null;
if (request.type() != null) {
    mappedTypes = request.type().stream()
            .map(type -> PopupType.fromKorean(type).name())  // 체험형 → EXPERIENTIAL
            .toList();
}
```

##### 1.2 지역 변환 ("전국" → null)
```java
// PopupDtoMapper.java:221
(request.region1DepthName() == null || "전국".equals(request.region1DepthName())) 
    ? null : request.region1DepthName()
```

##### 1.3 페이징 기본값 적용
```java
// PopupDtoMapper.java:197, 216
Optional.ofNullable(request.size()).orElse(10)  // size 기본값: 10
```

### 2. 지도 조회 변환 흐름

```
PopupMapRequest (Presentation)
    ↓ [PopupDtoMapper.toPopupMapQuery()]
PopupMapQuery (Domain)
    ↓ [PopupPortAdapter.findByMapQuery()]
JPA Repository Query (Infrastructure)
    ↓ [메모리 필터링]
Filtered PopupEntity List (Infrastructure)
    ↓ [PopupEntityMapper.toDomain()]
Popup (Domain)
    ↓ [PopupDtoMapper.toPopupMapResponse()]
PopupMapResponse (Presentation)
```

#### 주요 변환 규칙

##### 2.1 콤마 분리 문자열 → 리스트 변환
```java
// PopupDtoMapper.java:151-156
// 타입 변환
List<PopupType> types = StringUtils.hasText(request.type()) ?
        Arrays.stream(request.type().split(","))
                .map(PopupType::fromKorean)  // 체험형 → EXPERIENTIAL
                .toList() : null;

// 카테고리 변환  
List<String> categories = StringUtils.hasText(request.category()) ? 
        Arrays.asList(request.category().split(",")) : null;
```

##### 2.2 날짜 조건부 변환
```java
// PopupDtoMapper.java:157-159
DateRange dateRange = (request.startDate() != null && request.endDate() != null)
        ? new DateRange(request.startDate(), request.endDate())
        : null;
```

## 검색 실행 전략

### 1. 목록 조회 실행 전략

#### 1.1 조건 우선순위
1. **직접 조회**: `popupId != null`
2. **키워드 검색**: `keyword != null && !keyword.trim().isEmpty()`  
3. **필터 검색**: 나머지 모든 조건

#### 1.2 키워드 검색 전략
```java
// PopupPortAdapter.java:223-253
private List<PopupEntity> findByKeywordSearch(PopupQuery query, Pageable pageable) {
    // 1. 키워드 토큰화
    List<String> tokens = Arrays.stream(keyword.split("\\s+"))
            .map(String::trim)
            .filter(token -> !token.isEmpty())
            .distinct()
            .toList();
    
    // 2. 각 토큰별 검색 후 합집합
    Set<PopupEntity> resultSet = new LinkedHashSet<>();
    for (String token : tokens) {
        List<PopupEntity> tokenResults = popupJpaRepository.findByKeyword(
                query.popupId(), token, query.lastPopupId(), pageable
        );
        resultSet.addAll(tokenResults);
    }
    
    // 3. 페이징 적용
    return resultSet.stream().limit(query.size() + 1).toList();
}
```

#### 1.3 필터 검색 전략 (QueryDSL)
```java
// PopupPortAdapter.java:258-304
private List<PopupEntity> findFilteredPopupsWithQueryDsl(PopupQuery query, Pageable pageable) {
    BooleanBuilder builder = new BooleanBuilder();
    
    // 날짜 범위: 겹치는 구간 검색
    if (query.startDate() != null && query.endDate() != null) {
        builder.and(popupEntity.endDate.goe(query.startDate())
                .and(popupEntity.startDate.loe(query.endDate())));
    }
    
    // 타입 조건: IN 절
    if (query.types() != null && !query.types().isEmpty()) {
        List<PopupType> popupTypes = query.types().stream()
                .map(PopupType::valueOf)
                .toList();
        builder.and(popupEntity.type.in(popupTypes));
    }
    
    // 카테고리 조건: EXISTS 서브쿼리
    if (query.categories() != null && !query.categories().isEmpty()) {
        builder.and(jpaQueryFactory.selectOne()
                .from(popupCategoryEntity)
                .where(popupCategoryEntity.popupId.eq(popupEntity.id)
                    .and(popupCategoryEntity.name.in(query.categories())))
                .exists());
    }
    
    // 지역 조건: EXISTS 서브쿼리
    if (query.region1DepthName() != null) {
        builder.and(jpaQueryFactory.selectOne()
                .from(popupLocationEntity)
                .where(popupLocationEntity.id.eq(popupEntity.popupLocationId)
                    .and(popupLocationEntity.region1DepthName.eq(query.region1DepthName())))
                .exists());
    }
    
    // 커서 기반 페이징
    if (query.lastPopupId() != null) {
        builder.and(popupEntity.id.gt(query.lastPopupId()));
    }
    
    // 정렬: startDate ASC, id ASC
    return jpaQueryFactory
            .selectFrom(popupEntity)
            .where(builder)
            .orderBy(popupEntity.startDate.asc(), popupEntity.id.asc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
}
```

### 2. 지도 조회 실행 전략

#### 2.1 2단계 필터링
1. **1단계**: DB에서 좌표/타입/날짜 조건으로 조회
2. **2단계**: 메모리에서 카테고리 필터링

```java
// PopupPortAdapter.java:313-319
public List<Popup> findByMapQuery(PopupMapQuery query) {
    // 1단계: 좌표와 기본 조건으로 팝업 조회
    List<PopupEntity> popupEntities = findPopupsByConditions(query);
    
    // 2단계: 카테고리 필터링 (메모리에서 처리)
    if (hasCategories(query)) {
        popupEntities = filterByCategories(popupEntities, query.categories());
    }
    // ... 도메인 변환
}
```

## 성능 고려사항

### 1. 인덱스 활용
- **좌표 검색**: latitude, longitude 복합 인덱스 권장
- **날짜 검색**: startDate, endDate 복합 인덱스 권장
- **커서 페이징**: id 인덱스 활용

### 2. 서브쿼리 최적화
- 카테고리, 지역 조건은 EXISTS 서브쿼리로 처리
- N+1 문제 방지를 위한 적극적인 관련 엔티티 로딩

### 3. 메모리 사용
- 지도 조회 시 카테고리 필터링은 메모리에서 수행
- 키워드 검색 시 중복 제거를 위한 Set 사용

## 제약사항 및 예외 처리

### 1. Validation 제약사항
- **size**: 최소 1 (jakarta.validation)
- **type, category**: 최대 3개 (jakarta.validation) 
- **좌표**: 위도(-90~90), 경도(-180~180) 범위 (jakarta.validation)
- **날짜**: ISO DATE 형식 필수 (Spring DateTimeFormat)

### 2. 비즈니스 예외
- **INVALID_POPUP_TYPE**: 지원하지 않는 팝업 타입 (PopupType.java:40)
- **POPUP_NOT_FOUND**: 존재하지 않는 팝업 조회 (PopupService.java:70)

### 3. 데이터 정합성
- 키워드가 있으면 다른 필터 조건 무시
- "전국" 지역은 null로 변환하여 전체 조회
- 날짜 범위는 둘 다 있을 때만 적용

이 비즈니스 로직을 통해 사용자는 다양한 방식으로 팝업을 검색할 수 있으며, 시스템은 효율적이고 일관성 있는 검색 결과를 제공할 수 있습니다.