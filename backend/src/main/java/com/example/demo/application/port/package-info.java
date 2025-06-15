/**
 * 애플리케이션 계층에서 정의하는 포트(인터페이스)들을 포함하는 패키지입니다.
 * 
 * <p>이 패키지는 다음과 같은 포트들을 포함합니다:</p>
 * 
 * <ul>
 *     <li>입력 포트 (예: {@code ReservationUseCase}, {@code StoreUseCase} 등)</li>
 *     <li>출력 포트 (예: {@code ReservationPersistencePort}, {@code PaymentPort} 등)</li>
 *     <li>이벤트 포트 (예: {@code EventPublisher}, {@code NotificationPort} 등)</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>입력 포트는 애플리케이션 서비스가 구현하는 유스케이스 인터페이스입니다.</li>
 *     <li>출력 포트는 인프라 계층이 구현하는 외부 시스템 연동 인터페이스입니다.</li>
 *     <li>이벤트 포트는 도메인 이벤트 발행을 위한 인터페이스입니다.</li>
 *     <li>모든 포트는 도메인 모델을 사용합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>포트는 가능한 한 도메인 모델에 가깝게 정의합니다.</li>
 *     <li>포트는 기술적인 구현 세부사항을 포함하지 않습니다.</li>
 *     <li>포트는 단일 책임 원칙을 따릅니다.</li>
 *     <li>포트는 명확한 네이밍 컨벤션을 따릅니다 (UseCase, Port).</li>
 *     <li>포트는 적절한 예외를 정의합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.application.port; 