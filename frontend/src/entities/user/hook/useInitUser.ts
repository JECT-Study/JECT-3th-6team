'use client';

import { useUserStore } from '@/entities/user/lib/useUserStore';
import { useEffect } from 'react';

// cookie의 토큰과 전역 유저상태의 sync를 맞추기 위한 훅
export default function useInitUser() {
  const { isLoggedIn } = useUserStore(state => state.userState);
  //const setUser = useUserStore(state => state.setUser);

  useEffect(() => {
    // 토큰이 있는데 전역 로그인 상태가 아니라면
    if (!isLoggedIn) {
      // TODO : 서버에서 유저정보를 가져옵니다.
      // const user = getUserAPI();
      // if(user) setUser(user);
    }
  }, []);
}
