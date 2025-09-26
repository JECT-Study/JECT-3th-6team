package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.ban.GlobalBan;
import com.example.demo.infrastructure.persistence.entity.GlobalBanEntity;
import com.example.demo.infrastructure.persistence.mapper.GlobalBanEntityMapper;
import com.example.demo.infrastructure.persistence.repository.GlobalBanJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({GlobalBanPortAdapter.class, GlobalBanEntityMapper.class})
class GlobalBanPortAdapterTest {

    @Autowired
    private GlobalBanPortAdapter globalBanPortAdapter;

    @Autowired
    private GlobalBanJpaRepository globalBanJpaRepository;

    private GlobalBan globalBan;
    private LocalDate today;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        globalBanJpaRepository.deleteAll();
        
        today = LocalDate.now();
        startDate = today;
        endDate = today.plusDays(3);
        
        globalBan = new GlobalBan(null, 1L, startDate, endDate);
    }

    @Nested
    @DisplayName("글로벌 밴 저장 테스트")
    class SaveTest {

        @Test
        @DisplayName("글로벌 밴을 저장할 수 있다")
        void shouldSaveGlobalBan() {
            // when
            GlobalBan savedGlobalBan = globalBanPortAdapter.save(globalBan);

            // then
            assertThat(savedGlobalBan.id()).isNotNull();
            assertThat(savedGlobalBan.memberId()).isEqualTo(1L);
            assertThat(savedGlobalBan.startDate()).isEqualTo(startDate);
            assertThat(savedGlobalBan.endDate()).isEqualTo(endDate);

            List<GlobalBanEntity> entities = globalBanJpaRepository.findAll();
            assertThat(entities).hasSize(1);
            assertThat(entities.get(0).getMemberId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("활성 밴 조회 테스트")
    class FindActiveBanTest {

        @Test
        @DisplayName("활성 밴이 있는 경우 조회할 수 있다")
        void shouldFindActiveBan() {
            // given
            GlobalBan savedGlobalBan = globalBanPortAdapter.save(globalBan);

            // when
            Optional<GlobalBan> foundBan = globalBanPortAdapter.findActiveBanByMemberId(1L);

            // then
            assertThat(foundBan).isPresent();
            assertThat(foundBan.get().id()).isEqualTo(savedGlobalBan.id());
            assertThat(foundBan.get().memberId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("활성 밴이 없는 경우 빈 Optional을 반환한다")
        void shouldReturnEmptyWhenNoActiveBan() {
            // when
            Optional<GlobalBan> foundBan = globalBanPortAdapter.findActiveBanByMemberId(1L);

            // then
            assertThat(foundBan).isEmpty();
        }

        @Test
        @DisplayName("만료된 밴은 활성 밴으로 조회되지 않는다")
        void shouldNotFindExpiredBan() {
            // given - 만료된 밴 생성
            LocalDate expiredStartDate = today.minusDays(5);
            LocalDate expiredEndDate = today.minusDays(1);
            GlobalBan expiredBan = new GlobalBan(null, 1L, expiredStartDate, expiredEndDate);
            globalBanPortAdapter.save(expiredBan);

            // when
            Optional<GlobalBan> foundBan = globalBanPortAdapter.findActiveBanByMemberId(1L);

            // then
            assertThat(foundBan).isEmpty();
        }
    }

    @Nested
    @DisplayName("만료된 밴 조회 테스트")
    class FindExpiredBansTest {

        @Test
        @DisplayName("만료된 밴들을 조회할 수 있다")
        void shouldFindExpiredBans() {
            // given
            GlobalBan activeBan = new GlobalBan(null, 1L, today, today.plusDays(3));
            GlobalBan expiredBan1 = new GlobalBan(null, 2L, today.minusDays(5), today.minusDays(1));
            GlobalBan expiredBan2 = new GlobalBan(null, 3L, today.minusDays(3), today.minusDays(1));
            
            globalBanPortAdapter.save(activeBan);
            globalBanPortAdapter.save(expiredBan1);
            globalBanPortAdapter.save(expiredBan2);

            // when
            List<GlobalBan> expiredBans = globalBanPortAdapter.findExpiredBans();

            // then
            assertThat(expiredBans).hasSize(2);
            assertThat(expiredBans).extracting(GlobalBan::memberId)
                    .containsExactlyInAnyOrder(2L, 3L);
        }

        @Test
        @DisplayName("만료된 밴이 없는 경우 빈 리스트를 반환한다")
        void shouldReturnEmptyListWhenNoExpiredBans() {
            // given
            GlobalBan activeBan = new GlobalBan(null, 1L, today, today.plusDays(3));
            globalBanPortAdapter.save(activeBan);

            // when
            List<GlobalBan> expiredBans = globalBanPortAdapter.findExpiredBans();

            // then
            assertThat(expiredBans).isEmpty();
        }
    }

    @Nested
    @DisplayName("밴 삭제 테스트")
    class DeleteTest {

        @Test
        @DisplayName("밴들을 삭제할 수 있다")
        void shouldDeleteBans() {
            // given
            GlobalBan ban1 = globalBanPortAdapter.save(new GlobalBan(null, 1L, today, today.plusDays(3)));
            GlobalBan ban2 = globalBanPortAdapter.save(new GlobalBan(null, 2L, today, today.plusDays(3)));
            List<GlobalBan> bansToDelete = List.of(ban1, ban2);

            // when
            globalBanPortAdapter.delete(bansToDelete);

            // then
            List<GlobalBanEntity> remainingEntities = globalBanJpaRepository.findAll();
            assertThat(remainingEntities).isEmpty();
        }
    }

    @Nested
    @DisplayName("회원별 밴 조회 테스트")
    class FindByMemberIdTest {

        @Test
        @DisplayName("특정 회원의 모든 밴을 조회할 수 있다")
        void shouldFindBansByMemberId() {
            // given
            GlobalBan ban1 = globalBanPortAdapter.save(new GlobalBan(null, 1L, today, today.plusDays(3)));
            GlobalBan ban2 = globalBanPortAdapter.save(new GlobalBan(null, 1L, today.plusDays(5), today.plusDays(8)));
            GlobalBan otherBan = globalBanPortAdapter.save(new GlobalBan(null, 2L, today, today.plusDays(3)));

            // when
            List<GlobalBan> memberBans = globalBanPortAdapter.findByMemberId(1L);

            // then
            assertThat(memberBans).hasSize(2);
            assertThat(memberBans).extracting(GlobalBan::memberId)
                    .containsExactly(1L, 1L);
        }

        @Test
        @DisplayName("해당 회원의 밴이 없는 경우 빈 리스트를 반환한다")
        void shouldReturnEmptyListWhenNoBansForMember() {
            // when
            List<GlobalBan> memberBans = globalBanPortAdapter.findByMemberId(1L);

            // then
            assertThat(memberBans).isEmpty();
        }
    }
}
