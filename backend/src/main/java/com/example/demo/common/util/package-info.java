/**
 * 애플리케이션 전반에서 사용되는 유틸리티 클래스들을 포함하는 패키지입니다.
 * 
 * <p>이 패키지는 다음과 같은 유틸리티 클래스들을 포함합니다:</p>
 * 
 * <ul>
 *     <li>문자열 처리 유틸리티 ({@code StringUtils})</li>
 *     <li>날짜/시간 처리 유틸리티 ({@code DateTimeUtils})</li>
 *     <li>파일 처리 유틸리티 ({@code FileUtils})</li>
 *     <li>암호화/복호화 유틸리티 ({@code CryptoUtils})</li>
 *     <li>검증 유틸리티 ({@code ValidationUtils})</li>
 * </ul>
 * 
 * <p>특징:
 * <ul>
 *     <li>모든 유틸리티 클래스는 상태를 가지지 않는 순수 함수들로 구성됩니다.</li>
 *     <li>외부 라이브러리에 대한 의존성을 최소화합니다.</li>
 *     <li>모든 메서드는 null 안전성을 보장합니다.</li>
 *     <li>메서드는 가능한 한 불변성을 유지합니다.</li>
 * </ul>
 * </p>
 * 
 * <p>주의사항:
 * <ul>
 *     <li>유틸리티 클래스는 private 생성자를 가져야 합니다.</li>
 *     <li>모든 메서드는 적절한 예외 처리를 포함해야 합니다.</li>
 *     <li>성능이 중요한 메서드는 캐싱을 고려합니다.</li>
 *     <li>모든 메서드는 단위 테스트를 포함해야 합니다.</li>
 * </ul>
 * </p>
 */
package com.example.demo.common.util; 