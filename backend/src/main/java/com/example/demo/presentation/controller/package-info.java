/**
 * REST API 컨트롤러들을 포함하는 패키지입니다.
 * 
 * <p>이 패키지는 다음과 같은 컨트롤러들을 포함합니다:</p>
 * 
 * <ul>
 *     <li>예약 관련 컨트롤러 (예: {@code ReservationController})</li>
 *     <li>팝업스토어 관련 컨트롤러 (예: {@code StoreController})</li>
 *     <li>사용자 관련 컨트롤러 (예: {@code UserController}, {@code AuthController} 등)</li>
 *     <li>결제 관련 컨트롤러 (예: {@code PaymentController})</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>RESTful API 엔드포인트를 정의합니다.</li>
 *     <li>HTTP 요청을 애플리케이션 계층의 유스케이스로 변환합니다.</li>
 *     <li>유스케이스의 결과를 HTTP 응답으로 변환합니다.</li>
 *     <li>API 버전 관리, 인증, 권한 검사 등을 처리합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>컨트롤러는 가능한 한 얇게 유지합니다.</li>
 *     <li>비즈니스 로직은 애플리케이션 계층에 위임합니다.</li>
 *     <li>적절한 HTTP 메서드와 상태 코드를 사용합니다.</li>
 *     <li>API 문서화(예: Swagger)를 통해 API 스펙을 명확히 합니다.</li>
 *     <li>보안 관련 설정(인증, 권한, CORS 등)을 적절히 구현합니다.</li>
 *     <li>입력값 검증과 예외 처리를 포함합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.presentation.controller; 