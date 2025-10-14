export type MenuType = {
  title: string;
  link: string;
};

export const PublicSettingMenu: Array<MenuType> = [
  { title: '개선안 제안', link: '/setting/suggest' },
  { title: '공지사항', link: '/setting/announcement' },
  { title: '약관 및 정책', link: '/setting/agreement' },
  {
    title: '고객센터',
    link: process.env.NEXT_PUBLIC_KAKAO_CHANNEL as string,
  },
] as const;

export const PrivateSettingMenu: Array<MenuType> = [
  ...PublicSettingMenu,
  // { title: '알림 설정', link: '/setting/notification' },
] as const;
