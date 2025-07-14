package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.Member;
import com.example.demo.infrastructure.persistence.entity.MemberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberEntityMapperTest {

    @Nested
    @DisplayName("toDomain 테스트")
    class Test01 {

        @Test
        @DisplayName("정상적인 MemberEntity를 Member 도메인 모델로 변환 테스트")
        public void test01() {
            // given
            MemberEntity entity = MemberEntity.builder()
                    .id(1L)
                    .email("test@example.com")
                    .name("테스트 사용자")
                    .build();

            // when
            Member result = new MemberEntityMapper().toDomain(entity);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("test@example.com", result.email());
            assertEquals("테스트 사용자", result.name());
        }

        @Test
        @DisplayName("null 값이 포함된 MemberEntity 변환 테스트")
        public void test02() {
            // given
            MemberEntity entity = MemberEntity.builder()
                    .id(1L)
                    .email(null)
                    .name(null)
                    .build();

            // when
            Member result = new MemberEntityMapper().toDomain(entity);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertNull(result.email());
            assertNull(result.name());
        }

        @Test
        @DisplayName("빈 문자열이 포함된 MemberEntity 변환 테스트")
        public void test03() {
            // given
            MemberEntity entity = MemberEntity.builder()
                    .id(1L)
                    .email("")
                    .name("")
                    .build();

            // when
            Member result = new MemberEntityMapper().toDomain(entity);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("", result.email());
            assertEquals("", result.name());
        }
    }
} 