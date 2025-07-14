/**
 * 응용 계층으로, 유스케이스를 구현하는 패키지입니다.
 * 
 * <p>이 패키지는 비즈니스 로직의 조율자 역할을 하며, 도메인 모델을 사용하여 
 * 유스케이스를 구현합니다. 외부 시스템과의 상호작용을 조율하고 트랜잭션 경계를 관리합니다.</p>
 * 
 * <p>주요 역할:</p>
 * <ul>
 *   <li>비즈니스 로직의 조율자 역할</li>
 *   <li>도메인 모델을 사용하여 유스케이스를 구현</li>
 *   <li>외부 시스템과의 상호작용을 조율</li>
 *   <li>트랜잭션 경계 관리</li>
 * </ul>
 * 
 * <p>구조:</p>
 * <ul>
 *   <li>{@link com.example.demo.application.dto} - 외부와의 데이터 전송 객체</li>
 *   <li>{@link com.example.demo.application.mapper} - DTO <-> Domain Model 변환</li>
 *   <li>{@link com.example.demo.application.service} - 유스케이스를 실행하는 서비스</li>
 * </ul>
 */
package com.example.demo.application; 