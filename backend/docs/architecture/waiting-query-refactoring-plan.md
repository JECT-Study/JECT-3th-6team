# WaitingQuery 리팩토링 계획

## 목적
BanQuery/BanPortAdaptor에서 사용된 sealed class + pattern matching 설계를 WaitingQuery/WaitingPortAdapter에 적용하여 타입 안정성과 유지보수성을 향상시킨다.

## 현재 BanQuery/BanPortAdaptor의 설계 패턴

### BanQuery: sealed class 계층 구조
- 베이스 클래스가 공통 필드를 보유
- 각 쿼리 케이스를 별도의 final 서브클래스로 표현 (ByMemberIdFromAll, ByMemberAndPopup, ByBanType)
- static factory 메서드로 타입 안전한 인스턴스 생성

### BanPortAdaptor.findByQuery: Pattern matching + QueryDSL
- switch expression으로 sealed class의 각 케이스를 처리
- 각 케이스마다 BooleanBuilder로 동적 쿼리 조건 구성
- 타입 안정성과 컴파일 타임 체크 보장

## WaitingQuery/WaitingPortAdapter 적용 계획

### 1단계: WaitingQuery 리팩토링
- 현재 record 구조를 sealed class 계층 구조로 변경
- 기존 static factory 메서드들을 각각 별도의 서브클래스로 분리
  - ForWaitingId
  - ForVisitHistory
  - ForPopup
  - ForDuplicateCheck
  - ForMemberAndPopupOnDate
  - ForStatus

### 2단계: WaitingPortAdapter.findByQuery 리팩토링
- if-else 체인을 switch expression + pattern matching으로 변경
- QueryDSL 도입하여 동적 쿼리 생성 (현재 TODO 해결)
- 각 쿼리 케이스에 맞는 조건 빌더 구현

### 3단계: 테스트 및 검증
- 기존 기능 동작 보장
- 타입 안정성 개선 확인
- 성능 영향 최소화 검증

## 기대 효과
- 컴파일 타임 타입 체크로 런타임 오류 감소
- 쿼리 의도 명확화 및 가독성 향상
- 동적 쿼리 구현으로 유연성 증대
