/**
 * API 요청/응답 DTO들을 포함하는 패키지입니다.
 * 
 * <p>이 패키지는 다음과 같은 DTO들을 포함합니다:</p>
 * 
 * <ul>
 *     <li>요청 DTO (예: {@code CreateReservationRequest}, {@code UpdateStoreRequest} 등)</li>
 *     <li>응답 DTO (예: {@code ReservationResponse}, {@code StoreResponse} 등)</li>
 *     <li>공통 응답 DTO (예: {@code ApiResponse}, {@code ErrorResponse} 등)</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>요청 DTO는 API 엔드포인트의 입력을 정의합니다.</li>
 *     <li>응답 DTO는 API 엔드포인트의 출력을 정의합니다.</li>
 *     <li>공통 응답 DTO는 일관된 API 응답 형식을 제공합니다.</li>
 *     <li>모든 DTO는 불변성을 가집니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>DTO는 도메인 모델과 분리되어야 합니다.</li>
 *     <li>DTO는 API에 특화된 데이터 구조를 정의합니다.</li>
 *     <li>DTO는 적절한 검증 로직을 포함해야 합니다.</li>
 *     <li>DTO는 명확한 네이밍 컨벤션을 따릅니다 (Request, Response).</li>
 *     <li>DTO는 필요한 최소한의 데이터만 포함해야 합니다.</li>
 *     <li>민감한 정보는 응답에서 제외해야 합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.presentation.dto; 