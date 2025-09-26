package com.example.demo.application.service;

import com.example.demo.domain.model.ban.GlobalBan;
import com.example.demo.domain.port.GlobalBanPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledBanReleaseServiceTest {

    @Mock
    private GlobalBanPort globalBanPort;

    @InjectMocks
    private ScheduledBanReleaseService scheduledBanReleaseService;

    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
    }

    @Nested
    @DisplayName("밴 해제 스케줄러 테스트")
    class ReleaseExpiredBansTest {

        @Test
        @DisplayName("만료된 밴이 있는 경우 해제할 수 있다")
        void shouldReleaseExpiredBans() {
            // given
            GlobalBan expiredBan1 = new GlobalBan(1L, 1L, today.minusDays(5), today.minusDays(1));
            GlobalBan expiredBan2 = new GlobalBan(2L, 2L, today.minusDays(3), today.minusDays(1));
            List<GlobalBan> expiredBans = List.of(expiredBan1, expiredBan2);
            
            when(globalBanPort.findExpiredBans()).thenReturn(expiredBans);

            // when
            scheduledBanReleaseService.releaseExpiredBans();

            // then
            verify(globalBanPort).findExpiredBans();
            verify(globalBanPort).delete(expiredBans);
        }

        @Test
        @DisplayName("만료된 밴이 없는 경우 아무것도 하지 않는다")
        void shouldDoNothingWhenNoExpiredBans() {
            // given
            when(globalBanPort.findExpiredBans()).thenReturn(List.of());

            // when
            scheduledBanReleaseService.releaseExpiredBans();

            // then
            verify(globalBanPort).findExpiredBans();
            verify(globalBanPort, never()).delete(any());
        }

        @Test
        @DisplayName("예외 발생 시에도 처리 계속 진행한다")
        void shouldContinueProcessingWhenExceptionOccurs() {
            // given
            when(globalBanPort.findExpiredBans()).thenThrow(new RuntimeException("DB 오류"));

            // when & then - 예외가 발생해도 메서드가 정상 종료되어야 함
            scheduledBanReleaseService.releaseExpiredBans();

            // then
            verify(globalBanPort).findExpiredBans();
            verify(globalBanPort, never()).delete(any());
        }

        @Test
        @DisplayName("단일 만료된 밴도 정상적으로 해제한다")
        void shouldReleaseSingleExpiredBan() {
            // given
            GlobalBan expiredBan = new GlobalBan(1L, 1L, today.minusDays(5), today.minusDays(1));
            List<GlobalBan> expiredBans = List.of(expiredBan);
            
            when(globalBanPort.findExpiredBans()).thenReturn(expiredBans);

            // when
            scheduledBanReleaseService.releaseExpiredBans();

            // then
            verify(globalBanPort).findExpiredBans();
            verify(globalBanPort).delete(expiredBans);
        }
    }

    @Nested
    @DisplayName("밴 해제 로직 검증")
    class BanReleaseLogicTest {

        @Test
        @DisplayName("여러 개의 만료된 밴을 한 번에 삭제한다")
        void shouldDeleteMultipleExpiredBansAtOnce() {
            // given
            GlobalBan expiredBan1 = new GlobalBan(1L, 1L, today.minusDays(5), today.minusDays(1));
            GlobalBan expiredBan2 = new GlobalBan(2L, 2L, today.minusDays(3), today.minusDays(1));
            GlobalBan expiredBan3 = new GlobalBan(3L, 3L, today.minusDays(2), today.minusDays(1));
            List<GlobalBan> expiredBans = List.of(expiredBan1, expiredBan2, expiredBan3);
            
            when(globalBanPort.findExpiredBans()).thenReturn(expiredBans);

            // when
            scheduledBanReleaseService.releaseExpiredBans();

            // then
            verify(globalBanPort).delete(expiredBans);
            verify(globalBanPort, times(1)).delete(any());
        }

        @Test
        @DisplayName("밴 해제 시 정확한 밴 리스트가 전달된다")
        void shouldPassCorrectBanListToDelete() {
            // given
            GlobalBan expiredBan1 = new GlobalBan(1L, 1L, today.minusDays(5), today.minusDays(1));
            GlobalBan expiredBan2 = new GlobalBan(2L, 2L, today.minusDays(3), today.minusDays(1));
            List<GlobalBan> expectedBans = List.of(expiredBan1, expiredBan2);
            
            when(globalBanPort.findExpiredBans()).thenReturn(expectedBans);

            // when
            scheduledBanReleaseService.releaseExpiredBans();

            // then
            verify(globalBanPort).delete(expectedBans);
        }
    }
}
