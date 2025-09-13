# OAuth2 로그인 비즈니스 로직

## 개요

OAuth2 로그인 시스템은 카카오 OAuth 2.0 프로토콜을 사용하여 사용자 인증을 처리하는 핵심 비즈니스 로직입니다. 인증 코드 교환, 사용자 정보 조회, 기존 회원 여부 확인, 신규 회원 생성, JWT 토큰 발급 등의 복잡한 프로세스를 거치며, 각 단계에서 실패 시 구체적인 예외 처리와 리다이렉트 로직을 제공합니다.

## OAuth2 로그인 프로세스

### 전체 프로세스 흐름

```
콜백 수신 → 에러 체크 → 토큰 교환 → 사용자 정보 조회 → 회원 조회/생성 → JWT 발급 → 쿠키 설정 → 리다이렉트
```

### 단계별 상세 로직

#### 1. 콜백 요청 처리
```java
// OAuthController.java:36-47
@GetMapping("/callback")
public void kakaoCallback(
    @RequestParam(required = false) String code,
    @RequestParam(required = false) String error,
    @RequestParam String state,
    HttpServletResponse response) throws IOException {
    
    if (error != null) {
        oAuth2FailureHandler.handleFailure(response, new BusinessException(ErrorType.OAUTH_ERROR_RECEIVED, error));
        return;
    }
```
- **목적**: 카카오 OAuth 서버로부터의 콜백 요청 수신
- **파라미터**: 
  - `code`: 인증 성공 시 전달되는 authorization code
  - `error`: 인증 실패 시 전달되는 에러 메시지
  - `state`: 리다이렉트 URL 정보
- **에러 처리**: `error` 파라미터 존재 시 즉시 실패 처리

#### 2. 카카오 액세스 토큰 교환
```java
// OAuth2Service.java:67-85
private String getKakaoAccessToken(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", kakaoClientId);
    params.add("client_secret", kakaoClientSecret);
    params.add("redirect_uri", kakaoRedirectUri);
    params.add("code", code);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
    KakaoTokenResponse response = restTemplate.postForObject(kakaoTokenUri, request, KakaoTokenResponse.class);

    if (response == null) {
        throw new BusinessException(ErrorType.OAUTH_TOKEN_REQUEST_FAILED);
    }
    return response.accessToken();
}
```
- **목적**: Authorization Code를 Access Token으로 교환
- **카카오 API**: `POST https://kauth.kakao.com/oauth/token`
- **필수 파라미터**: grant_type, client_id, client_secret, redirect_uri, code
- **응답 검증**: null 응답 시 `OAUTH_TOKEN_REQUEST_FAILED` 예외

#### 3. 카카오 사용자 정보 조회
```java
// OAuth2Service.java:88-101
private KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
    HttpEntity<?> request = new HttpEntity<>(headers);
    KakaoUserInfoResponse response = restTemplate.postForObject(kakaoUserInfoUri, request, KakaoUserInfoResponse.class);

    if (response == null) {
        throw new BusinessException(ErrorType.OAUTH_USER_INFO_REQUEST_FAILED);
    }
    return response;
}
```
- **목적**: 액세스 토큰으로 사용자 정보 조회
- **카카오 API**: `POST https://kapi.kakao.com/v2/user/me`
- **인증 방식**: Bearer Token 사용
- **응답 검증**: null 응답 시 `OAUTH_USER_INFO_REQUEST_FAILED` 예외

#### 4. 사용자 정보 추출
```java
// KakaoUserInfoResponse.java:26-34
public String getNickname() {
    return Optional.ofNullable(properties.get("nickname"))
        .map(Object::toString)
        .orElse("unknown");
}

public String getEmail() {
    return this.kakaoAccount.email();
}
```
- **닉네임 추출**: `properties.nickname` 사용, null일 경우 "unknown"
- **이메일 추출**: `kakao_account.email` 사용
- **카카오 ID**: 응답의 `id` 필드를 `providerId`로 사용

#### 5. 기존 회원 여부 확인 및 처리
```java
// OAuth2Service.java:47-64
@Transactional
public Member processKakaoLogin(String authorizationCode) {
    String accessToken = getKakaoAccessToken(authorizationCode);
    KakaoUserInfoResponse userInfo = getKakaoUserInfo(accessToken);

    String providerId = userInfo.id().toString();
    return oAuthAccountPort.findByProviderAndProviderId(OAuthProvider.KAKAO, providerId)
        .map(oauthAccount -> memberPort.findById(oauthAccount.memberId())
            .orElseThrow(() -> new BusinessException(ErrorType.OAUTH_MEMBER_NOT_FOUND)))
        .orElseGet(() -> {
            String nickname = userInfo.getNickname();
            String email = userInfo.getEmail();

            Member newMember = memberPort.save(new Member(null, nickname, email));
            OAuthAccount newOAuthAccount = new OAuthAccount(null, OAuthProvider.KAKAO, providerId, newMember.id());
            oAuthAccountPort.save(newOAuthAccount);

            return newMember;
        });
}
```

**기존 회원인 경우**:
- `OAuthAccountPort.findByProviderAndProviderId()`로 OAuth 계정 조회
- 연결된 `memberId`로 회원 정보 조회
- 회원 정보가 없으면 `OAUTH_MEMBER_NOT_FOUND` 예외

**신규 회원인 경우**:
- 카카오 사용자 정보로 새로운 `Member` 생성
- 새로운 `OAuthAccount` 생성하여 연결
- 두 엔티티 모두 저장 후 회원 정보 반환

#### 6. JWT 토큰 발급 및 쿠키 설정
```java
// OAuth2SuccessHandler.java:25-37
public void onAuthenticationSuccess(HttpServletResponse response, Member member, String state, String frontendUrl) throws IOException {
    UserPrincipal principal = UserPrincipal.create(member.id(), member.email());
    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    String accessToken = jwtTokenProvider.createToken(authentication);

    ResponseCookie responseCookie = CookieUtils.createAccessTokenCookie(accessToken, jwtProperties.expirationSeconds());
    response.setHeader("Set-Cookie", responseCookie.toString());

    String redirectUrl = state != null && !state.trim().isEmpty() ? state : frontendUrl;
    response.sendRedirect(redirectUrl);
}
```
- **JWT 토큰 생성**: 회원 ID와 이메일로 `UserPrincipal` 생성 후 토큰 발급
- **쿠키 설정**: 액세스 토큰을 HTTP 쿠키로 설정
- **리다이렉트**: `state` 파라미터 또는 기본 프론트엔드 URL로 리다이렉트

## 데이터 모델

### OAuth 계정 모델
```java
// OAuthAccount.java:3-8
public record OAuthAccount(
    Long id,
    OAuthProvider provider,    // KAKAO
    String providerId,         // 카카오 사용자 ID
    Long memberId             // 내부 회원 ID
) {}
```

### 카카오 사용자 정보 응답 구조
```java
// KakaoUserInfoResponse.java:7-24
public record KakaoUserInfoResponse(
    Long id,                                    // 카카오 사용자 고유 ID
    String connectedAt,                         // 연결 시간
    Map<String, Object> properties,             // 프로필 정보 (nickname 등)
    KakaoAccount kakaoAccount                   // 계정 정보 (email 등)
) {
    public record KakaoAccount(
        Boolean profileNicknameNeedsAgreement,
        Profile profile,
        Boolean emailNeedsAgreement,
        Boolean isEmailValid,
        Boolean isEmailVerified,
        String email                            // 이메일 주소
    ) {}
    
    public record Profile(
        String nickname                         // 닉네임
    ) {}
}
```

## OAuth2 로그인 실패 시나리오

### 1. 카카오 OAuth 서버 에러

#### 1.1 인증 거부 또는 카카오 서버 오류 (OAUTH_ERROR_RECEIVED)
```java
ErrorType.OAUTH_ERROR_RECEIVED (HttpStatus.BAD_REQUEST, "OAuth 인증 과정에서 오류가 발생했습니다")
```
- **발생 시점**: 콜백 수신 시 `error` 파라미터 존재
- **원인**: 사용자가 인증 거부, 카카오 서버 오류, 잘못된 클라이언트 설정
- **HTTP 상태**: 400 Bad Request
- **처리**: 실패 핸들러로 즉시 리다이렉트

### 2. 토큰 교환 실패

#### 2.1 액세스 토큰 요청 실패 (OAUTH_TOKEN_REQUEST_FAILED)
```java
ErrorType.OAUTH_TOKEN_REQUEST_FAILED (HttpStatus.BAD_GATEWAY, "OAuth 토큰 요청에 실패했습니다")
```
- **발생 시점**: 카카오 토큰 API 호출 시
- **원인**: 
  - 잘못된 authorization code
  - 만료된 authorization code (10분 제한)
  - 잘못된 client_id/client_secret
  - 잘못된 redirect_uri
  - 카카오 토큰 서버 오류
- **HTTP 상태**: 502 Bad Gateway

#### 2.2 사용자 정보 요청 실패 (OAUTH_USER_INFO_REQUEST_FAILED)
```java
ErrorType.OAUTH_USER_INFO_REQUEST_FAILED (HttpStatus.BAD_GATEWAY, "OAuth 사용자 정보 요청에 실패했습니다")
```
- **발생 시점**: 카카오 사용자 정보 API 호출 시
- **원인**:
  - 잘못된 또는 만료된 액세스 토큰
  - 권한 범위(scope) 부족
  - 카카오 API 서버 오류
- **HTTP 상태**: 502 Bad Gateway

### 3. 내부 데이터 불일치

#### 3.1 OAuth 계정 연결 회원 미존재 (OAUTH_MEMBER_NOT_FOUND)
```java
ErrorType.OAUTH_MEMBER_NOT_FOUND (HttpStatus.NOT_FOUND, "OAuth 계정과 연결된 회원을 찾을 수 없습니다")
```
- **발생 시점**: 기존 OAuth 계정 처리 시
- **원인**: 
  - OAuth 계정은 존재하지만 연결된 회원 정보가 삭제됨
  - 데이터 무결성 문제
  - 회원 탈퇴 후 OAuth 계정 정리 누락
- **HTTP 상태**: 404 Not Found

### 4. 네트워크 및 시스템 오류

#### 4.1 외부 API 통신 실패
- **발생 시점**: RestTemplate 호출 시
- **원인**: 네트워크 오류, 타임아웃, 카카오 서버 장애
- **처리**: `HttpClientErrorException` 또는 기타 HTTP 오류로 분류

#### 4.2 데이터베이스 저장 실패
- **발생 시점**: 신규 회원 생성 시
- **원인**: DB 연결 오류, 제약조건 위반, 트랜잭션 롤백
- **처리**: RuntimeException으로 전파, 트랜잭션 롤백

## 실패 처리 메커니즘

### OAuth2FailureHandler
```java
// OAuth2FailureHandler.java:18-27
public void handleFailure(HttpServletResponse response, Exception e) throws IOException {
    String reasonCode = resolveReasonCode(e);
    redirectToFail(response, reasonCode);
}

private String resolveReasonCode(Exception e) {
    if (e instanceof HttpClientErrorException || e instanceof IllegalStateException) {
        return "token_exchange_failed";
    }
    return "server_error";
}
```

**실패 처리 흐름**:
1. 예외 타입에 따른 reason code 결정
2. 프론트엔드 실패 페이지로 리다이렉트: `/login/fail?reason={reasonCode}`

**Reason Code 분류**:
- `token_exchange_failed`: 토큰 교환 관련 오류
- `server_error`: 서버 내부 오류

## 성공 조건

OAuth2 로그인이 성공하려면 다음 조건들이 모두 만족되어야 합니다:

### 1. 외부 서비스 정상 동작
- ✅ 카카오 OAuth 서버 정상 동작
- ✅ 카카오 API 서버 정상 동작
- ✅ 네트워크 통신 정상

### 2. 인증 정보 유효성
- ✅ 유효한 authorization code (10분 이내)
- ✅ 올바른 client_id/client_secret 설정
- ✅ 정확한 redirect_uri 설정
- ✅ 유효한 액세스 토큰

### 3. 사용자 정보 접근 권한
- ✅ 필수 권한 동의 (이메일, 프로필)
- ✅ 카카오 계정 정상 상태
- ✅ API 호출 권한 유지

### 4. 시스템 정상 동작
- ✅ 데이터베이스 정상 동작
- ✅ 트랜잭션 성공적 처리
- ✅ JWT 토큰 생성 정상

## 로그인 결과

### 기존 회원 로그인 성공
1. **OAuth 계정 조회**: `OAuthAccount` 테이블에서 기존 연결 확인
2. **회원 정보 조회**: 연결된 `Member` 정보 반환
3. **JWT 토큰 발급**: 기존 회원 정보로 토큰 생성
4. **쿠키 설정**: 액세스 토큰 쿠키 설정
5. **리다이렉트**: 원래 요청했던 페이지 또는 메인 페이지

### 신규 회원 자동 가입 성공
1. **회원 생성**: 카카오 닉네임, 이메일로 `Member` 생성
2. **OAuth 계정 연결**: `OAuthAccount`로 카카오 계정과 연결
3. **JWT 토큰 발급**: 신규 회원 정보로 토큰 생성
4. **쿠키 설정**: 액세스 토큰 쿠키 설정
5. **리다이렉트**: 원래 요청했던 페이지 또는 메인 페이지

### 생성되는 데이터
- **Member**: `id`, `nickname`(카카오), `email`(카카오)
- **OAuthAccount**: `provider`(KAKAO), `providerId`(카카오 ID), `memberId`
- **JWT Cookie**: 인증 정보가 담긴 HTTP-Only 쿠키

## 연관 기능

### 1. 인증 시스템과의 연동
- JWT 토큰을 통한 후속 API 인증
- Spring Security 필터 체인과 연동
- 세션 관리 및 토큰 갱신

### 2. 회원 관리와의 연동
- OAuth 계정과 내부 회원의 1:1 매핑
- 회원 탈퇴 시 OAuth 계정 정리
- 프로필 정보 동기화

### 3. 프론트엔드와의 연동
- State 파라미터를 통한 리다이렉트 URL 관리
- 쿠키 기반 인증 상태 유지
- 실패 시 구체적인 오류 정보 제공

이 비즈니스 로직을 통해 시스템은 안전하고 사용자 친화적인 소셜 로그인 서비스를 제공할 수 있습니다.