/**
 * 사용자 인터페이스를 담당하는 프레젠테이션 계층입니다.
 * 
 * <p>이 패키지는 다음과 같은 하위 패키지로 구성됩니다:</p>
 * 
 * <ul>
 *     <li>{@link com.example.demo.presentation.controller} - REST API 컨트롤러</li>
 *     <li>{@link com.example.demo.presentation.dto} - API 요청/응답 DTO</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>외부 클라이언트(웹, 모바일 앱)와의 통신을 담당합니다.</li>
 *     <li>HTTP 요청을 애플리케이션 계층의 유스케이스로 변환합니다.</li>
 *     <li>유스케이스의 결과를 HTTP 응답으로 변환합니다.</li>
 *     <li>API 버전 관리, 인증, 권한 검사 등의 웹 관련 관심사를 처리합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>컨트롤러는 가능한 한 얇게 유지하고, 비즈니스 로직은 애플리케이션 계층에 위임합니다.</li>
 *     <li>API 요청/응답 DTO는 프레젠테이션 계층에 특화된 데이터 구조를 정의합니다.</li>
 *     <li>적절한 예외 처리와 에러 응답 형식을 정의합니다.</li>
 *     <li>API 문서화(예: Swagger)를 통해 API 스펙을 명확히 합니다.</li>
 *     <li>보안 관련 설정(인증, 권한, CORS 등)을 적절히 구현합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.presentation; 