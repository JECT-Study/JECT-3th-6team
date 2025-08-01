# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Building and Running
- `./gradlew bootRun` - Run the Spring Boot application
- `./gradlew build` - Build the project
- `./gradlew clean build` - Clean and build the project

### Testing
- `./gradlew test` - Run all tests (configured for parallel execution)
- `./gradlew test --tests "com.example.demo.application.service.WaitingServiceTest"` - Run specific test class
- `./gradlew test --tests "*WaitingServiceTest.shouldCreateWaiting"` - Run specific test method

### Database Access
- H2 Console: `http://localhost:8080/h2-console` (when running locally)
- JDBC URL: `jdbc:h2:mem:testdb`, Username: `sa`, Password: (empty)

## Architecture Overview

This is a Spring Boot application following **Hexagonal Architecture** (Ports and Adapters) with clean architecture principles:

### Layer Structure
- **Domain Layer** (`domain/`): Core business logic, models, and domain events
  - `model/`: Domain entities (Member, Popup, Waiting, Notification)
  - `port/`: Output ports (interfaces for external communication)
- **Application Layer** (`application/`): Use case orchestration
  - `service/`: Business logic coordinators and transaction boundaries
  - `dto/`: Data transfer objects for external communication
  - `mapper/`: DTO <-> Domain model transformation
- **Infrastructure Layer** (`infrastructure/`): External adapters
  - `persistence/`: Database adapters, JPA entities, repositories
  - `external/`: External service adapters (SSE notifications)
- **Presentation Layer** (`presentation/`): API controllers and request/response handling

### Key Domain Concepts
- **Popup**: Core entity representing popup stores with scheduling, location, and content
- **Waiting**: Queue management system for popup store visits
- **Notification**: Event-driven notification system with real-time SSE support
- **Member**: User management with OAuth2 (Kakao) integration

### Security Configuration
- JWT-based authentication with access tokens
- OAuth2 integration with Kakao
- Profile-based security configurations (`sse-test` profile for development)
- CORS configured for frontend integration

### Environment Configuration
Environment variables required in `.env` file:
- `OAUTH2_KAKAO_CLIENT_ID`, `OAUTH2_KAKAO_CLIENT_SECRET`
- `JWT_ACCESS_TOKEN_SECRET`, `JWT_ACCESS_TOKEN_EXPIRATION_SECONDS`
- `FRONTEND_URL`, `BACKEND_URL`
- Kakao OAuth2 provider URLs

### Testing Configuration
- Parallel test execution enabled (JUnit 5)
- Tests run concurrently by default for faster execution
- H2 in-memory database for testing

### Key Technical Features
- **SSE (Server-Sent Events)**: Real-time notifications with heartbeat mechanism
- **Scheduled Tasks**: Batch processing for notifications (`@EnableScheduling`)
- **JPA Auditing**: Automatic timestamp management (`@EnableJpaAuditing`)
- **Domain Events**: Event-driven architecture for notification system

## Exception Handling System

### ErrorType Enum Values
The `ErrorType` enum (`common.exception.ErrorType`) contains comprehensive error definitions categorized by domain:

#### Parameter Validation Errors
- `PARAMETER_VALIDATION_FAILED`: General parameter validation failure

#### Pagination and Filtering
- `INVALID_PAGE_SIZE`: Page size validation (NotificationService.java:56, 59)
- `INVALID_READ_STATUS`: Read status validation (NotificationService.java:72)
- `INVALID_SORT_OPTION`: Sort option validation (NotificationService.java:84)

#### Access Control
- `ACCESS_DENIED_NOTIFICATION`: Notification access control (NotificationService.java:95, 110)
- `ACCESS_DENIED_WAITING`: Waiting access control (WaitingService.java:107)

#### Entity Not Found
- `MEMBER_NOT_FOUND`: Member entity missing (MemberController.java:32, NotificationPortAdapter.java:168, ScheduledNotificationPortAdapter.java:110, WaitingService.java:54)
- `NOTIFICATION_NOT_FOUND`: Notification entity missing (NotificationPortAdapter.java:61, NotificationService.java:92, 107)
- `POPUP_NOT_FOUND`: Popup entity missing (PopupService.java:66, WaitingService.java:47)
- `WAITING_NOT_FOUND`: Waiting entity missing (NotificationPortAdapter.java:184, ScheduledNotificationPortAdapter.java:126, WaitingService.java:103)

#### Domain Validation
- `INVALID_WAITING_STATUS`: Waiting status validation (WaitingStatus.java:27)
- `INVALID_PEOPLE_COUNT`: People count validation (Waiting.java:43)
- `INVALID_WAITING_PERSON_NAME`: Waiting person name validation (Waiting.java:46, 49)
- `INVALID_OPENING_HOURS`: Opening hours validation (OpeningHours.java:20, WeeklyOpeningHours.java:25)
- `INVALID_POPUP_TYPE`: Popup type validation (PopupType.java:37)

#### Data Integrity
- `NULL_NOTIFICATION_ID`: Null notification ID (NotificationPortAdapter.java:50)
- `INVALID_SOURCE_DOMAIN`: Invalid source domain (NotificationPortAdapter.java:176, ScheduledNotificationPortAdapter.java:118)
- `UNSUPPORTED_NOTIFICATION_TYPE`: Unsupported notification type (NotificationDtoMapper.java:62, NotificationPortAdapter.java:142, ScheduledNotificationPortAdapter.java:77, NotificationEntityMapper.java:123)

### Parameter Validation System
A comprehensive system for handling client request parameter validation errors:

#### Core Components
- **ValidationError** (`common.exception.ValidationError`): Interface for validation error abstraction
- **ParameterValidationError** (`common.exception.ParameterValidationError`): Implementation of ValidationError
- **ParameterValidationException** (`common.exception.ParameterValidationException`): Exception for parameter validation failures
- **ValidationErrorResponse** (`common.exception.ValidationErrorResponse`): Detailed error response for clients

#### Features
- **Validation Method Independent**: Works with `@Valid`, `@Validated`, manual validation, etc.
- **Detailed Error Information**: Provides parameter name, rejected value, and failure reason
- **Comprehensive Coverage**: Handles all Spring validation exceptions:
  - `MethodArgumentNotValidException`: Request body validation
  - `BindException`: Form data validation
  - `ConstraintViolationException`: Method parameter validation
  - `MethodArgumentTypeMismatchException`: Type conversion errors

#### Usage Examples
```java
// Manual validation
throw new ParameterValidationException("pageSize", -1, "페이지 크기는 0보다 커야 합니다");

// Multiple validation errors
List<ValidationError> errors = Arrays.asList(
    ParameterValidationError.of("name", "", "이름은 필수입니다"),
    ParameterValidationError.of("age", -5, "나이는 0 이상이어야 합니다")
);
throw new ParameterValidationException(errors);
```

#### Error Response Format
```json
{
  "code": "PARAMETER_VALIDATION_FAILED",
  "message": "요청 파라미터 검증에 실패했습니다",
  "errors": [
    {
      "parameterName": "pageSize",
      "rejectedValue": -1,
      "reason": "페이지 크기는 0보다 커야 합니다"
    }
  ],
  "timestamp": "2025-01-01T12:00:00",
  "path": "/api/notifications"
}
```

### Unified Error Response System
All exceptions now use a single `ErrorResponse` class for consistent error handling:

#### ErrorResponse Structure
```json
{
  "code": "ERROR_CODE",
  "message": "사용자 친화적 메시지",
  "additionalInfo": "추가 정보 (선택적)",
  "errors": [  // 파라미터 검증 오류 시에만 포함
    {
      "parameterName": "fieldName",
      "rejectedValue": "invalidValue",
      "reason": "검증 실패 사유"
    }
  ],
  "timestamp": "2025-01-01T12:00:00",
  "path": "/api/endpoint"
}
```

#### Error Response Types
1. **Business Errors**: `ErrorResponse.of(BusinessException, path)` - `additionalInfo` 포함
2. **Validation Errors**: `ErrorResponse.of(ParameterValidationException, path)` - `errors` 배열 포함
3. **Generic Errors**: `ErrorResponse.builder()` - 기타 시스템 오류

### Exception Handling Guidelines
- Use `BusinessException` with appropriate `ErrorType` for business logic errors
- Use `ParameterValidationException` for request parameter validation failures
- All exceptions are automatically handled by `GlobalExceptionHandler` and return unified `ErrorResponse`
- Error messages should be user-friendly and in Korean
- Parameter validation errors include detailed field-level information in `errors` array
- Business errors may include additional context in `additionalInfo` field