import { useNotificationStore } from '@/features/notification/hook/useNotificationStore';
import { useEffect, useRef } from 'react';
import { useUserStore } from '@/entities/user/lib/useUserStore';

/**
 * 홈 페이지 진입시 단한번 백엔드로 연결 요청 (new EventSource('/api/notifications/stream))
 * 하트비트 이벤트 : event.type === 'ping' -> 연결 유지용
 * 실제 알림 이벤트 : event.type === 'notification' -> 전역 상태 업데이트 -> UI 업데이트
 */

export default function useNotificationListener() {
  const addNotification = useNotificationStore(state => state.add);
  const eventSourceRef = useRef<EventSource | null>(null);
  const isLoggedIn = useUserStore(state => state.userState.isLoggedIn);
  useEffect(() => {
    if (!isLoggedIn) return;
    if (eventSourceRef.current) return;

    const eventSource = new EventSource(
      `${process.env.NEXT_PUBLIC_API_URL}/notifications/stream`,
      { withCredentials: true }
    );
    eventSourceRef.current = eventSource;

    // 테스트용
    eventSource.onopen = () => {
      console.log('✅ SSE 연결 성공');
    };

    eventSource.addEventListener('ping', event =>
      console.log('💗 PING', event.data)
    );

    eventSource.addEventListener('notification', event => {
      try {
        const notification = JSON.parse(event.data);
        addNotification(notification);
      } catch (error) {
        console.warn('알림 메세지 데이터 파싱실패', error);
      }
    });

    eventSource.onerror = error => {
      if (eventSource.readyState === EventSource.CLOSED) {
        console.log('SSE 연결 정상 종료(페이지 이탈/새로고침)');
      } else {
        console.error('SSE 오류', error);
      }
    };

    // 새로고침시 정상 종료
    const handleUnload = () => eventSource.close();
    window.addEventListener('beforeunload', handleUnload);

    // 페이지 언마운트 시 연결 해제
    return () => {
      window.removeEventListener('beforeunload', handleUnload);
      eventSource.close();
      eventSourceRef.current = null;
      console.log('연결 해제');
    };
  }, []);
}
