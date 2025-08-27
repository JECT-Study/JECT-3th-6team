# JECT 3th 6team Project

팝업 예약 서비스 프로젝트입니다.

## 프로젝트 구조

### Backend (Spring Boot)

클린 아키텍처 패턴을 적용한 백엔드 구조입니다.

```
src/main/java/com/example/demo/
├── config/                          // 💡 애플리케이션 전반의 구성 및 Bean 정의
├── domain/                          // ❤️ 비즈니스 심장부 (프레임워크 비의존적)
│   ├── model/                       // - 도메인 모델 (Entity, Value Object)
│   └── port/                        // - 외부와 소통하는 인터페이스 (Output Ports)
├── application/                     // ⚙️ 응용 계층 (유스케이스 구현)
│   ├── dto/                         // - 외부와의 데이터 전송 객체
│   ├── mapper/                      // - DTO <-> Domain Model 변환
│   └── service/                     // - 유스케이스를 실행하는 서비스
├── presentation/                    // ✨ 사용자 인터페이스 계층 (Inbound Adapter)
│   └── controller/                  // - REST API 컨트롤러 (HTTP 요청 처리)
└── infrastructure/                  // 🔌 외부 기술 연동 계층 (Outbound Adapters)
    └── persistence/                 // - 데이터베이스 연동 구현
        ├── adapter/                 //   - Port 인터페이스 구현체
        ├── entity/                  //   - JPA 전용 엔티티
        ├── mapper/                  //   - Domain Model <-> JPA Entity 변환
        └── repository/              //   - Spring Data JPA 인터페이스
```

#### 패키지별 상세 설명

##### 🔌 `infrastructure.persistence` 내부의 역할 분담

- **`entity`**: DB 테이블과 1:1로 매핑되는 JPA 전용 객체입니다. `@Entity`, `@Table`, `@Id`와 같은 JPA 어노테이션을 포함하며, 오직 데이터베이스와의 통신만을 위해 존재합니다.
- **`repository`**: `JpaRepository`를 상속받는 Spring Data JPA의 인터페이스입니다. 실제 DB 쿼리를 실행하는 역할을 합니다.
- **`mapper`**: **`domain.model`의 순수 도메인 객체**와 **`persistence.entity`의 JPA 엔티티 객체** 사이의 변환을 책임집니다.
- **`adapter`**: `domain.port`의 영속성 인터페이스를 구현하는 클래스입니다. 내부적으로 `mapper`와 `repository`를 사용하여 실제 작업을 수행합니다.

#### 데이터 흐름 예시 (DB 저장)

1. **`application.service`**가 `domain.model` 객체를 `domain.port` 인터페이스에 전달하며 저장을 요청합니다.
2. DI에 의해 주입된 **`persistence.adapter`**가 호출됩니다.
3. `adapter`는 **`persistence.mapper`**를 사용하여 전달받은 `domain.model` 객체를 `persistence.entity` 객체로 변환합니다.
4. `adapter`는 변환된 `persistence.entity` 객체를 **`persistence.repository`**(Spring Data JPA)에 전달하여 DB에 저장(save)합니다.

이 구조를 통해 **도메인 모델(`domain.model`)은 JPA라는 특정 기술에 전혀 오염되지 않고 순수함을 유지**할 수 있으며, `persistence` 계층은 자신의 역할(DB 연동)에만 완벽하게 집중할 수 있게 됩니다. 이것이 바로 클린 아키텍처가 추구하는 **관심사의 분리(Separation of Concerns)** 입니다.

### Frontend (Next.js)

React 기반의 프론트엔드 애플리케이션입니다.

```
frontend/
├── src/
│   ├── app/                         // Next.js App Router
│   ├── components/                  // 재사용 가능한 UI 컴포넌트
│   ├── features/                    // 기능별 컴포넌트 및 로직
│   ├── lib/                         // 유틸리티 함수들
│   └── shared/                      // 공통 컴포넌트 및 상수
└── public/                          // 정적 파일들
```

## 기술 스택

### Backend
- Spring Boot 3.x
- Spring Data JPA
- Gradle
- H2 Database (개발용)

### Frontend
- Next.js 14
- TypeScript
- Tailwind CSS
- shadcn/ui

## 개발 환경 설정

### Backend 실행
```bash
cd backend
./gradlew bootRun
```

### Frontend 실행
```bash
cd frontend
npm install
npm run dev
```

#### 빌드 후 실행
```
npm run build
npm run start
```

## 아키텍처 원칙

- **클린 아키텍처**: 의존성 역전 원칙을 통한 관심사 분리
- **도메인 주도 설계**: 비즈니스 로직을 도메인 모델에 집중
- **SOLID 원칙**: 단일 책임, 개방-폐쇄, 리스코프 치환, 인터페이스 분리, 의존성 역전 원칙 