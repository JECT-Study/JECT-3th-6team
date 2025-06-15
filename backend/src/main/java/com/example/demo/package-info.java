/**
 * 팝업 스토어 예약 서비스의 메인 패키지입니다.
 * 
 * <p>이 패키지는 헥사고날 아키텍처(Hexagonal Architecture)와 도메인 주도 설계(DDD) 원칙을 따르며,
 * 다음과 같은 계층으로 구성되어 있습니다:</p>
 * 
 * <ul>
 *     <li>{@link com.example.demo.config} - 애플리케이션 설정 및 빈 정의</li>
 *     <li>{@link com.example.demo.common} - 공통 유틸리티 및 예외</li>
 *     <li>{@link com.example.demo.domain} - 핵심 비즈니스 로직</li>
 *     <li>{@link com.example.demo.application} - 유스케이스 구현 및 도메인 조정</li>
 *     <li>{@link com.example.demo.infrastructure} - 외부 기술 의존성 구현</li>
 *     <li>{@link com.example.demo.presentation} - 사용자 인터페이스</li>
 * </ul>
 * 
 * <p>의존성 규칙:
 * <ul>
 *     <li>바깥쪽 계층은 안쪽 계층에 의존할 수 있습니다.</li>
 *     <li>안쪽 계층은 바깥쪽 계층에 직접 의존할 수 없습니다.</li>
 *     <li>계층 간 통신은 인터페이스(포트)를 통해 이루어집니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo; 