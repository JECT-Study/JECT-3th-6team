/**
 * Spring Data JPA Repository 인터페이스들을 포함하는 패키지입니다.
 * 
 * <p>이 패키지는 다음과 같은 유형의 Repository들을 포함합니다:</p>
 * 
 * <ul>
 *     <li>도메인 모델의 영속성을 위한 Repository</li>
 *     <li>데이터베이스 CRUD 작업을 위한 Repository</li>
 *     <li>복잡한 쿼리를 위한 Repository</li>
 *     <li>데이터 접근을 추상화하는 Repository</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>Spring Data JPA의 기능을 활용하여 데이터베이스 접근을 추상화합니다.</li>
 *     <li>메서드 이름 기반의 쿼리 생성 기능을 제공합니다.</li>
 *     <li>커스텀 쿼리 메서드를 정의할 수 있습니다.</li>
 *     <li>페이징과 정렬 기능을 지원합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>Repository는 가능한 한 Spring Data JPA의 기본 기능을 활용합니다.</li>
 *     <li>복잡한 쿼리는 QueryDSL을 사용하는 것을 고려합니다.</li>
 *     <li>N+1 문제를 해결하기 위한 적절한 fetch join을 사용합니다.</li>
 *     <li>대용량 데이터 처리를 위한 페이징 처리를 구현합니다.</li>
 *     <li>성능이 중요한 쿼리는 적절한 인덱스를 활용합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.infrastructure.persistence.repository; 