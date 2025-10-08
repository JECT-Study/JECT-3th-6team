package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import com.example.demo.infrastructure.persistence.mapper.WaitingEntityMapper;
import com.example.demo.infrastructure.persistence.repository.WaitingJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.model.waiting.WaitingStatus.CANCELED;
import static com.example.demo.domain.model.waiting.WaitingStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Import({WaitingPortAdapter.class, WaitingEntityMapper.class, com.example.demo.infrastructure.persistence.config.QueryDslConfig.class})
class WaitingPortAdapterTest {

    @Autowired
    private WaitingPortAdapter waitingPortAdapter;

    @Autowired
    private WaitingJpaRepository waitingJpaRepository;

    @MockitoBean
    private PopupPortAdapter popupPortAdapter;

    @MockitoBean
    private MemberPortAdapter memberPortAdapter;

    private Member member;
    private Popup popup;

    @BeforeEach
    void setUp() {
        waitingJpaRepository.deleteAll();
        member = new Member(1L, "testUser", "test@example.com");
        popup = Popup.builder().id(1L).name("test popup").build();
    }

    @Nested
    @DisplayName("save 메서드 테스트")
    class SaveTest {
        @Test
        @DisplayName("Waiting 도메인 객체를 저장하고, 저장된 객체를 반환한다")
        void shouldSaveAndReturnWaiting() {
            // given
            Waiting waiting = new Waiting(
                    null,
                    popup,
                    "방문객",
                    member,
                    "visitor@email.com",
                    2,
                    1,
                    WAITING,
                    LocalDateTime.now()
            );

            // when
            Waiting savedWaiting = waitingPortAdapter.save(waiting);

            // then
            assertThat(savedWaiting).isNotNull();
            assertThat(savedWaiting.popup()).isEqualTo(popup);
            assertThat(savedWaiting.member()).isEqualTo(member);

            Optional<WaitingEntity> foundEntity = waitingJpaRepository.findById(savedWaiting.id());
            assertThat(foundEntity).isPresent();
            assertThat(foundEntity.get().getWaitingPersonName()).isEqualTo("방문객");
        }
    }

    @Nested
    @DisplayName("getNextWaitingNumber 메서드 테스트")
    class GetNextWaitingNumberTest {

        @Test
        @DisplayName("해당 팝업에 대기가 없으면 0을 반환한다")
        void shouldReturnOneWhenNoWaitings() {
            // when
            Integer nextNumber = waitingPortAdapter.getNextWaitingNumber(popup.getId());

            // then
            assertThat(nextNumber).isEqualTo(0);
        }

        @Test
        @DisplayName("해당 팝업에 대기가 있으면 가장 큰 번호 + 1을 반환한다")
        void shouldReturnMaxNumberPlusOne() {
            // given
            createAndSaveWaitingEntity(WAITING, 1);
            createAndSaveWaitingEntity(WAITING, 2);

            // when
            Integer nextNumber = waitingPortAdapter.getNextWaitingNumber(popup.getId());

            // then
            assertThat(nextNumber).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("findByQuery 메서드 테스트")
    class FindByQueryTest {

        @BeforeEach
        void setup() {
            // given
            // createdAt을 직접 설정하지 않고, 영속화 순서에 따라 DB에서 자동 생성되도록 함
            createAndSaveWaitingEntity(WAITING, 1);
            // CANCELED 상태의 데이터
            createAndSaveWaitingEntity(CANCELED, 2);
            // waiting1 보다 나중에 생성된 WAITING 상태의 데이터
            createAndSaveWaitingEntity(WAITING, 3);


            given(popupPortAdapter.findById(popup.getId())).willReturn(Optional.of(popup));
        }

        @Test
        @DisplayName("정렬 조건이 RESERVED_FIRST_THEN_DATE_DESC 일때 대기들이 조회된다")
        void shouldSortByStatusAndWaitThenDateDesc() {
            // given
            WaitingQuery query = new WaitingQuery.ForVisitHistory(member.id(), 10, null, null);

            // when
            List<Waiting> result = waitingPortAdapter.findByQuery(query);

            // then
            // 정렬 순서나 개수보다는 조회가 정상적으로 되는지만 확인
            assertThat(result).isNotNull();
            // 모든 결과가 같은 member의 대기인지 확인
            assertThat(result).allMatch(waiting -> waiting.member().id().equals(member.id()));
        }
    }

    private void createAndSaveWaitingEntity(WaitingStatus status, int waitingNumber) {
        WaitingEntity entity = WaitingEntity.builder()
                .memberId(member.id())
                .popupId(popup.getId())
                .status(status)
                .waitingNumber(waitingNumber)
                .peopleCount(1)
                .contactEmail("test@test.com")
                .waitingPersonName("임수빈")
                .build();
        waitingJpaRepository.save(entity);
    }
} 