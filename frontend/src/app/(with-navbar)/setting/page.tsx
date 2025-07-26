'use client';

import { useUserStore } from '@/entities/user/lib/useUserStore';
import UserStateCard from '@/entities/user/ui/UserStateCard';
import {
  PrivateSettingMenu,
  PublicSettingMenu,
} from '@/entities/settings/constants/menu';
import MenuList from '@/entities/settings/ui/MenuList';

export default function SettingPage() {
  const { isLoggedIn } = useUserStore(state => state.userState);

  const menu = isLoggedIn ? PrivateSettingMenu : PublicSettingMenu;
  return (
    <div className={'w-full h-full flex flex-col px-5 pt-[26px]'}>
      <UserStateCard />
      <MenuList menu={menu} />
    </div>
  );
}
