import { create } from 'zustand';
import { createJSONStorage, persist } from 'zustand/middleware';

export type User = {
  email: string;
  nickname: string;
  role: 'user' | 'admin';
};

type UserState = {
  isLoggedIn: boolean;
  user: User | null;
};

const initialUserState: UserState = {
  isLoggedIn: false,
  user: null,
};

type Store = {
  userState: UserState;
  clearUser: () => void;
  setUser: (user: User) => void;
  _hasHydrated: boolean; // client에서 hydration완료 여부 확인용
  _setHasHydrated: (v: boolean) => void;
};

export const useUserStore = create<Store>()(
  persist(
    set => ({
      userState: initialUserState,
      clearUser: () => set({ userState: initialUserState }),
      setUser: (user: User) => set({ userState: { isLoggedIn: true, user } }),
      _hasHydrated: false,
      _setHasHydrated: v => set({ _hasHydrated: v }),
    }),
    {
      name: 'user',
      storage:
        typeof window !== 'undefined'
          ? createJSONStorage(() => localStorage)
          : undefined,
      onRehydrateStorage: () => state => {
        state?._setHasHydrated(true);
      },
    }
  )
);

/**
 * CSR에서만 값이 로드되도록 보장하고 싶은 컴포넌트에서 사용.
 */
export function useUserHydrated() {
  return useUserStore(s => s._hasHydrated);
}
