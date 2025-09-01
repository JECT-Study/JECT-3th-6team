# QueryDSL 동적 필터링 구현 테스트 시나리오

## 개요
PopupPortAdapter에서 QueryDSL을 사용한 동적 WHERE 조건 구현을 테스트하기 위한 시나리오들입니다.

## 테스트 환경
- **Base URL**: http://localhost:8080/api/popups
- **Method**: GET
- **Response Format**: JSON
- **서버 상태**: 실행 중

## 초기 데이터 요약
- **팝업 수**: 30개 (ID: 1~30)
- **카테고리**: 패션(100), 예술(101)  
- **팝업 타입**: RETAIL, EXPERIENTIAL
- **지역**: 경기(1개), 서울(29개)
- **날짜 범위**: 2025-06-01 ~ 2025-09-30

## 테스트 시나리오

### 1. 기본 조회 (조건 없음)
```bash
curl "http://localhost:8080/api/popups?size=10"
```
**예상 결과**: 모든 팝업 중 처음 10개 반환, startDate 순 정렬

---

### 2. 특정 팝업 ID 조회 (다른 조건 무시)
```bash
# 특정 팝업 조회 - 다른 필터는 무시되어야 함
curl "http://localhost:8080/api/popups?popupId=5&type=RETAIL&category=패션&size=10"
```
**예상 결과**: ID 5번 팝업만 반환 (스타벅스 리저브), 다른 조건들은 무시

---

### 3. 팝업 타입별 필터링
```bash
# RETAIL 타입만
curl "http://localhost:8080/api/popups?type=RETAIL&size=10"

# EXPERIENTIAL 타입만  
curl "http://localhost:8080/api/popups?type=EXPERIENTIAL&size=10"

# 여러 타입
curl "http://localhost:8080/api/popups?type=RETAIL&type=EXPERIENTIAL&size=10"
```
**예상 결과**: 
- RETAIL: 2,5,6,7,9,10,11,14,16,18,22,23,25,26,28,30번 팝업들
- EXPERIENTIAL: 1,3,4,8,12,13,15,17,19,20,21,24,27,29번 팝업들

---

### 4. 카테고리별 필터링
```bash
# 패션 카테고리
curl "http://localhost:8080/api/popups?category=패션&size=10"

# 예술 카테고리
curl "http://localhost:8080/api/popups?category=예술&size=10"

# 여러 카테고리
curl "http://localhost:8080/api/popups?category=패션&category=예술&size=10"
```
**예상 결과**:
- 패션: 1,2,5,6,11,16,18,22,23,26,27,28,30번 팝업들
- 예술: 1,3,4,7,8,9,10,12,13,14,15,17,19,20,21,24,25,29번 팝업들

---

### 5. 지역별 필터링
```bash
# 서울 지역
curl "http://localhost:8080/api/popups?region1DepthName=서울&size=10"

# 경기 지역  
curl "http://localhost:8080/api/popups?region1DepthName=경기&size=10"
```
**예상 결과**:
- 서울: 2~30번 팝업들 (29개)
- 경기: 1번 팝업 (무신사 X 나이키 팝업스토어)

---

### 6. 날짜 범위 필터링
```bash
# 6월 중 진행되는 팝업
curl "http://localhost:8080/api/popups?startDate=2025-06-01&endDate=2025-06-30&size=10"

# 7월 중 진행되는 팝업
curl "http://localhost:8080/api/popups?startDate=2025-07-01&endDate=2025-07-31&size=10"

# 8월 중 진행되는 팝업
curl "http://localhost:8080/api/popups?startDate=2025-08-01&endDate=2025-08-31&size=10"
```
**예상 결과**: 해당 날짜 범위에 겹치는 팝업들 반환

---

### 7. 복합 조건 필터링
```bash
# 서울 + 패션 카테고리 + RETAIL 타입
curl "http://localhost:8080/api/popups?region1DepthName=서울&category=패션&type=RETAIL&size=10"

# 예술 카테고리 + EXPERIENTIAL 타입 + 7월 날짜 범위
curl "http://localhost:8080/api/popups?category=예술&type=EXPERIENTIAL&startDate=2025-07-01&endDate=2025-07-31&size=10"

# 모든 조건 결합
curl "http://localhost:8080/api/popups?region1DepthName=서울&category=패션&type=RETAIL&startDate=2025-06-01&endDate=2025-08-31&size=5"
```
**예상 결과**: 모든 조건을 만족하는 팝업들만 반환

---

### 8. 커서 기반 페이징 테스트
```bash
# 첫 번째 페이지
curl "http://localhost:8080/api/popups?size=5"

# 다음 페이지 (첫 번째 응답에서 받은 마지막 ID 사용)
curl "http://localhost:8080/api/popups?size=5&lastPopupId=5"
```
**예상 결과**: lastPopupId보다 큰 ID의 팝업들 반환

---

### 9. 키워드 검색 (기존 로직 유지 확인)
```bash
# 키워드 검색
curl "http://localhost:8080/api/popups?keyword=나이키&size=10"

# 키워드 + 다른 필터 (키워드가 우선)
curl "http://localhost:8080/api/popups?keyword=스타벅스&category=패션&size=10"
```
**예상 결과**: 키워드 검색 시 기존 JPA 쿼리 사용, 다른 필터는 무시

---

### 10. 잘못된 조건들 (에러 케이스)
```bash
# 존재하지 않는 팝업 타입
curl "http://localhost:8080/api/popups?type=INVALID_TYPE&size=10"

# 빈 카테고리 리스트
curl "http://localhost:8080/api/popups?category=&size=10"

# 잘못된 날짜 형식
curl "http://localhost:8080/api/popups?startDate=invalid-date&size=10"
```
**예상 결과**: 적절한 에러 응답 또는 조건 무시

---

## 검증 포인트

### 1. QueryDSL 동적 조건 생성 확인
- [ ] 각 조건이 null이거나 빈 값일 때 WHERE 절에서 제외되는지 확인
- [ ] 여러 조건이 AND로 올바르게 결합되는지 확인

### 2. popupId 우선순위 확인  
- [ ] popupId가 있으면 다른 모든 조건이 무시되는지 확인
- [ ] 존재하지 않는 popupId의 경우 빈 결과 반환하는지 확인

### 3. EXISTS 서브쿼리 확인
- [ ] 카테고리 조건이 EXISTS 서브쿼리로 올바르게 처리되는지 확인
- [ ] 지역 조건이 EXISTS 서브쿼리로 올바르게 처리되는지 확인

### 4. 정렬 및 페이징 확인
- [ ] startDate ASC, id ASC 순으로 정렬되는지 확인  
- [ ] size 제한이 올바르게 적용되는지 확인
- [ ] lastPopupId 커서 페이징이 올바르게 동작하는지 확인

### 5. 기존 기능 호환성 확인
- [ ] 키워드 검색 시 기존 로직이 유지되는지 확인
- [ ] 응답 형식이 변경되지 않았는지 확인

## 성능 확인
- 각 쿼리 실행 시 로그를 확인하여 생성되는 SQL이 예상과 일치하는지 검토
- 복잡한 조건일 때도 적절한 성능을 보이는지 확인

## 추가 테스트 가능한 도구
```bash
# 응답 시간 측정
curl -w "@curl-format.txt" "http://localhost:8080/api/popups?category=패션&type=RETAIL&size=10"

# JSON 응답 예쁘게 보기
curl "http://localhost:8080/api/popups?size=3" | jq '.'
```