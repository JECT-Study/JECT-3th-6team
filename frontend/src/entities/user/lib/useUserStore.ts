import { create } from 'zustand';

type User = {
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

export const useUserStore = create<{
  userState: UserState;
  clearUser: () => void;
  setUser: (user: User) => void;
}>(set => ({
  userState: initialUserState,
  clearUser: () => set({ userState: initialUserState }),
  setUser: (user: User) => set({ userState: { isLoggedIn: true, user } }),
}));
