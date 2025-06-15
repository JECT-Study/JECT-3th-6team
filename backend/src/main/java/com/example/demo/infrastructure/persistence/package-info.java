/**
 * 데이터베이스 영속성 관련 구현체들을 포함하는 패키지입니다.
 * 
 * <p>이 패키지는 다음과 같은 하위 패키지로 구성됩니다:</p>
 * 
 * <ul>
 *     <li>{@link com.example.demo.infrastructure.persistence.entity} - JPA 엔티티</li>
 *     <li>{@link com.example.demo.infrastructure.persistence.repository} - Spring Data JPA Repository</li>
 *     <li>{@link com.example.demo.infrastructure.persistence.adapter} - 영속성 포트 구현체</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>JPA 엔티티는 데이터베이스 테이블과 매핑됩니다.</li>
 *     <li>Repository는 데이터베이스 접근을 추상화합니다.</li>
 *     <li>Adapter는 애플리케이션 계층의 포트를 구현합니다.</li>
 *     <li>도메인 모델과 영속성 모델 간의 변환을 처리합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>엔티티는 JPA 어노테이션만 사용하고 도메인 로직은 포함하지 않습니다.</li>
 *     <li>Repository는 가능한 한 Spring Data JPA를 활용합니다.</li>
 *     <li>Adapter는 도메인 모델과 영속성 모델 간의 변환을 담당합니다.</li>
 *     <li>트랜잭션 관리는 애플리케이션 계층에서 수행합니다.</li>
 *     <li>성능 최적화(예: N+1 문제 해결)를 고려합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.infrastructure.persistence; 