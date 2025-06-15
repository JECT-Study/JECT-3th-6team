/**
 * 애플리케이션에서 사용되는 예외와 에러 코드를 정의하는 패키지입니다.
 * 
 * <p>이 패키지는 다음과 같은 구성 요소를 포함합니다:</p>
 * 
 * <ul>
 *     <li>비즈니스 예외 클래스들 (예: {@code BusinessException}, {@code ResourceNotFoundException} 등)</li>
 *     <li>에러 코드 열거형 (예: {@code ErrorCode})</li>
 *     <li>글로벌 예외 핸들러 ({@code GlobalExceptionHandler})</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>모든 비즈니스 예외는 명확한 에러 코드와 메시지를 가집니다.</li>
 *     <li>예외는 적절한 HTTP 상태 코드로 매핑됩니다.</li>
 *     <li>클라이언트에게 일관된 형식의 에러 응답을 제공합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>예외는 가능한 한 구체적이고 명확한 메시지를 포함해야 합니다.</li>
 *     <li>민감한 정보가 에러 메시지에 포함되지 않도록 주의합니다.</li>
 *     <li>예외 처리는 가능한 한 상위 계층에서 일괄적으로 처리합니다.</li>
 *     <li>각 예외는 적절한 로깅을 포함해야 합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.common.exception; 