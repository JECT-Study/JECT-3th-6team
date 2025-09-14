# 백엔드 코드 컨벤션

## 개요

이 문서는 백엔드 프로젝트의 코드 컨벤션과 네이밍 규칙을 정의합니다. 모든 개발자는 이 규칙을 준수하여 코드의 일관성과 가독성을 유지해야 합니다.

## 필수 규칙

### Import 문 사용

**모든 클래스와 라이브러리는 반드시 import 문을 사용해야 합니다.**

- 패키지 내의 다른 클래스를 참조할 때도 완전한 패키지 경로로 import
- 와일드카드 import (`import com.example.demo.domain.model.popup.*`) 사용 가능하지만 특정 클래스 import 권장
- static import는 필요한 경우에만 사용

```java
// 올바른 예시
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.common.exception.BusinessException;
import org.springframework.stereotype.Service;

// 피해야 할 예시 - import 없이 사용하지 않음
Member member; // X - import 없이 사용 금지
```

## 레이어별 클래스 네이밍 규칙

### 1. Presentation Layer (Controller)

**패키지**: `com.example.demo.presentation.controller`

**네이밍**: `{도메인명}Controller`

```java
public class PopupController
public class WaitingController  
public class NotificationController
public class MemberController
public class OAuthController
public class ImageController
```

### 2. Application Layer (Service)

**패키지**: `com.example.demo.application.service`

**네이밍**: `{도메인명}Service` 또는 `{기능명}Service`

```java
public class PopupService
public class WaitingService
public class NotificationService
public class MemberService
public class OAuth2Service
public class ImageService

// 기능별 서비스
public class EmailTemplateService
public class EmailNotificationService
public class NotificationSseService
public class WaitingNotificationService
public class SseHeartbeatService
public class ScheduledNotificationBatchService
public class ScheduledWaitingEnterService
```

### 3. Domain Layer

#### 3.1 도메인 모델

**패키지**: `com.example.demo.domain.model`

**네이밍**: 단수형 명사, 비즈니스 용어 사용

```java
// 핵심 도메인 모델
public class Member
public class Popup
public class Waiting
public class Notification
public class ScheduledNotification

// 값 객체 (Value Objects)
public class Location
public class DateRange
public class CursorResult<T>
public class BrandStory

// 도메인별 하위 패키지
com.example.demo.domain.model.popup.PopupCategory
com.example.demo.domain.model.popup.PopupContent
com.example.demo.domain.model.popup.PopupDisplay
com.example.demo.domain.model.popup.PopupSchedule
com.example.demo.domain.model.popup.PopupStatus
com.example.demo.domain.model.popup.PopupType
com.example.demo.domain.model.popup.OpeningHours

com.example.demo.domain.model.waiting.WaitingStatus
com.example.demo.domain.model.waiting.WaitingQuery
com.example.demo.domain.model.waiting.WaitingEventType
com.example.demo.domain.model.waiting.WaitingDomainEvent

com.example.demo.domain.model.notification.NotificationQuery
com.example.demo.domain.model.notification.NotificationStatus
com.example.demo.domain.model.notification.NotificationSortOrder
com.example.demo.domain.model.notification.ReadStatus
com.example.demo.domain.model.notification.DomainEvent<T>
com.example.demo.domain.model.notification.ScheduledNotificationTrigger
```

#### 3.2 포트 인터페이스

**패키지**: `com.example.demo.domain.port`

**네이밍**: `{도메인명}Port`

```java
public interface MemberPort
public interface PopupPort
public interface WaitingPort
public interface NotificationPort
public interface ScheduledNotificationPort
public interface OAuthAccountPort
public interface PopupCategoryPort
public interface BrandStoryPort

// 기능별 포트
public interface EmailSendPort
public interface NotificationEventPort
```

### 4. Infrastructure Layer

#### 4.1 영속성 어댑터

**패키지**: `com.example.demo.infrastructure.persistence.adapter`

**네이밍**: `{도메인명}PortAdapter`

```java
public class MemberPortAdapter implements MemberPort
public class PopupPortAdapter implements PopupPort
public class WaitingPortAdapter implements WaitingPort
public class NotificationPortAdapter implements NotificationPort
public class ScheduledNotificationPortAdapter implements ScheduledNotificationPort
public class OAuthAccountPortAdapter implements OAuthAccountPort
public class PopupCategoryPortAdapter implements PopupCategoryPort
public class PopupBrandStoryAdapter implements BrandStoryPort
```

#### 4.2 엔티티

**패키지**: `com.example.demo.infrastructure.persistence.entity`

**네이밍**: `{도메인명}Entity`

```java
public class MemberEntity extends BaseEntity
public class WaitingEntity extends BaseEntity
public class NotificationEntity extends BaseEntity
public class ScheduledNotificationEntity extends BaseEntity
public class OAuthAccountEntity
public class CategoryEntity

// 팝업 관련 엔티티 (하위 패키지)
com.example.demo.infrastructure.persistence.entity.popup.PopupEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupLocationEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupSocialEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupReviewEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupReviewImageEntity
com.example.demo.infrastructure.persistence.entity.popup.PopupWeeklyScheduleEntity
```

#### 4.3 리포지토리

**패키지**: `com.example.demo.infrastructure.persistence.repository`

**네이밍**: `{엔티티명}JpaRepository` (JPA) 또는 `{엔티티명}Repository` (QueryDSL 등)

```java
// JPA 리포지토리
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long>
public interface PopupJpaRepository extends JpaRepository<PopupEntity, Long>
public interface WaitingJpaRepository extends JpaRepository<WaitingEntity, Long>
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long>
public interface ScheduledNotificationJpaRepository extends JpaRepository<ScheduledNotificationEntity, Long>
public interface OAuthAccountJpaRepository extends JpaRepository<OAuthAccountEntity, Long>
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long>

// QueryDSL 리포지토리
public interface PopupRepository
public interface PopupLocationRepository
public interface PopupContentRepository
public interface PopupImageRepository
public interface PopupCategoryRepository
public interface PopupSocialRepository
public interface PopupReviewRepository
public interface PopupWeeklyScheduleRepository
```

#### 4.4 엔티티 매퍼

**패키지**: `com.example.demo.infrastructure.persistence.mapper`

**네이밍**: `{도메인명}EntityMapper`

```java
public class MemberEntityMapper
public class PopupEntityMapper
public class WaitingEntityMapper
public class NotificationEntityMapper
public class ScheduledNotificationEntityMapper
public class OAuthAccountEntityMapper
public class PopupCategoryMapper
public class PopupBrandStoryMapper
```

#### 4.5 외부 어댑터

**패키지**: `com.example.demo.infrastructure.external`

**네이밍**: `{기능명}{외부시스템명}Adapter`

```java
public class NotificationSseAdapter implements NotificationEventPort
public class GmailSmtpEmailAdapter implements EmailSendPort
```

### 5. Application Layer DTO 및 매퍼

#### 5.1 DTO

**패키지**: `com.example.demo.application.dto`

**네이밍**: `{도메인명}{용도}{Request|Response}`

```java
// 요청 DTO
public record PopupCreateRequest
public record PopupFilterRequest
public record PopupMapRequest
public record WaitingCreateRequest
public record WaitingMakeVisitRequest
public record NotificationListRequest
public record NotificationReadRequest
public record NotificationDeleteRequest

// 응답 DTO
public record PopupDetailResponse
public record PopupCreateResponse
public record PopupCursorResponse
public record PopupMapResponse
public record PopupSummaryResponse
public record WaitingCreateResponse
public record WaitingResponse
public record NotificationListResponse
public record NotificationResponse
public record MeResponse
public record ImageUploadResponse
```

#### 5.2 Application 매퍼

**패키지**: `com.example.demo.application.mapper`

**네이밍**: `{도메인명}DtoMapper`

```java
public class PopupDtoMapper
public class WaitingDtoMapper
public class NotificationDtoMapper
```

### 6. Common 및 Config

#### 6.1 예외 처리

**패키지**: `com.example.demo.common.exception`

```java
public class BusinessException
public class ParameterValidationException
public class ParameterValidationError
public enum ErrorType
public class ErrorResponse
public class GlobalExceptionHandler
public interface ValidationError
```

#### 6.2 보안

**패키지**: `com.example.demo.common.security`, `com.example.demo.common.jwt`

```java
public class UserPrincipal
public class CustomAuthenticationEntryPoint
public class CustomAccessDeniedHandler
public class JwtTokenProvider
public class JwtAuthenticationFilter
public class JwtProperties
public enum TokenValidationResult
```

#### 6.3 설정

**패키지**: `com.example.demo.config`

**네이밍**: `{기능명}Config` 또는 `{기능명}Properties`

```java
public class SecurityConfig
public class SwaggerConfig
public class WebConfig
public class AsyncConfig
public class RestTemplateConfig
public class QueryDslConfig
public class AppProperties
public class JwtProperties
public class GmailSmtpProperties
```

## 패키지별 import 규칙

### Controller Layer
```java
// 필수 import
import com.example.demo.application.service.*;
import com.example.demo.application.dto.*;
import com.example.demo.presentation.ApiResponse;
import com.example.demo.common.security.UserPrincipal;

// Spring 관련
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;

// Swagger
import io.swagger.v3.oas.annotations.*;
```

### Service Layer
```java
// 필수 import
import com.example.demo.domain.model.*;
import com.example.demo.domain.port.*;
import com.example.demo.application.dto.*;
import com.example.demo.application.mapper.*;
import com.example.demo.common.exception.*;

// Spring 관련
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
```

### Adapter Layer
```java
// 필수 import
import com.example.demo.domain.model.*;
import com.example.demo.domain.port.*;
import com.example.demo.infrastructure.persistence.entity.*;
import com.example.demo.infrastructure.persistence.repository.*;
import com.example.demo.infrastructure.persistence.mapper.*;
import com.example.demo.common.exception.*;

// Spring 관련
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
```

## 추가 규칙

### 1. 메서드 네이밍
- `find{조건}By{필드명}`: 조회 메서드
- `save`: 저장 메서드 (생성/수정 통합)
- `delete{조건}By{필드명}`: 삭제 메서드
- `create`: 새 엔티티 생성
- `update`: 기존 엔티티 수정

### 2. 상수 네이밍
- 모든 상수는 `UPPER_SNAKE_CASE` 사용
- enum 값도 `UPPER_SNAKE_CASE` 사용

### 3. 변수 네이밍
- `camelCase` 사용
- boolean 변수는 `is`, `has`, `can` 접두사 사용

이 컨벤션을 준수하여 코드의 일관성과 가독성을 유지하고, 헥사고날 아키텍처의 계층 분리 원칙을 명확히 표현해야 합니다.