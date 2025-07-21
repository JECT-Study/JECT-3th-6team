import { useUserStore } from '@/entities/user/lib/useUserStore';
import Image from 'next/image';
import LogoImage from '/public/images/spotit-logo.png';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import Link from 'next/link';
import { LogoutButton } from '@/entities/user/ui/LogoutButton';

const LogoutStateCard = () => {
  return (
    <Link
      href={'/login'}
      className={
        'bg-sub2 rounded-[10px] px-4 py-6 flex items-center justify-between hover:bg-sub transition-all duration-350'
      }
    >
      <div className={'flex items-center'}>
        <div
          className={
            'w-[48px] h-[48px] mr-3 flex justify-center items-center bg-white rounded-full'
          }
        >
          <Image src={LogoImage} width={26} height={22} alt={'spotit logo'} />
        </div>
        <span className={'font-medium'}>로그인이 필요합니다.</span>
      </div>
      <IconBracketRight width={20} height={20} fill={'var(--color-gray80)'} />
    </Link>
  );
};

const LoginStateCard = () => {
  const { user } = useUserStore(state => state.userState);

  return (
    <div className={'flex flex-col gap-y-3'}>
      <Link
        href={'/setting/mypage'}
        className={
          'bg-white border border-main rounded-[10px] px-4 py-6 shadow-card flex items-center justify-between hover:bg-sub2 transition-all duration-350'
        }
      >
        <div className={'flex items-center'}>
          <div
            className={
              'w-[48px] h-[48px] mr-3 flex justify-center items-center bg-white rounded-full'
            }
          >
            <Image src={LogoImage} width={26} height={22} alt={'spotit logo'} />
          </div>
          <div className={'flex flex-col '}>
            <span className={'font-medium text-base'}>{user?.nickname}</span>
            <span className={'font-light text-sm text-gray60'}>
              {user?.email}
            </span>
          </div>
        </div>
        <IconBracketRight width={20} height={20} fill={'var(--color-gray80)'} />
      </Link>
      <LogoutButton />
    </div>
  );
};

export default function UserStateCard() {
  const { isLoggedIn } = useUserStore(state => state.userState);

  return isLoggedIn ? <LoginStateCard /> : <LogoutStateCard />;
}
