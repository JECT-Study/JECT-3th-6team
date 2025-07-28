/**
 * JPA 전용 엔티티를 정의하는 패키지입니다.
 * 
 * <p>이 패키지는 DB 테이블과 1:1로 매핑되는 JPA 전용 객체들을 정의합니다. 
 * @Entity, @Table, @Id와 같은 JPA 어노테이션을 포함하며, 오직 데이터베이스와의 
 * 통신만을 위해 존재합니다.</p>
 * 
 * <p>주요 역할:</p>
 * <ul>
 *   <li>DB 테이블과 1:1로 매핑되는 JPA 전용 객체</li>
 *   <li>JPA 어노테이션을 포함한 데이터베이스 매핑</li>
 *   <li>데이터베이스와의 통신을 위한 전용 객체</li>
 *   <li>도메인 모델과 분리된 데이터베이스 표현</li>
 * </ul>
 */
package com.example.demo.infrastructure.persistence.entity; 