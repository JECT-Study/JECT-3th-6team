/**
 * Domain Model <-> JPA Entity 변환을 담당하는 패키지입니다.
 * 
 * <p>이 패키지는 domain.model의 순수 도메인 객체와 persistence.entity의 JPA 엔티티 
 * 객체 사이의 변환을 담당합니다. 도메인 모델의 순수성을 유지하면서 JPA 엔티티와의 
 * 매핑 처리를 제공합니다.</p>
 * 
 * <p>주요 역할:</p>
 * <ul>
 *   <li>domain.model의 순수 도메인 객체와 persistence.entity의 JPA 엔티티 객체 사이의 변환</li>
 *   <li>도메인 모델의 순수성을 유지하면서 JPA 엔티티와의 매핑 처리</li>
 *   <li>양방향 변환 로직 제공</li>
 * </ul>
 */
package com.example.demo.infrastructure.persistence.mapper; 