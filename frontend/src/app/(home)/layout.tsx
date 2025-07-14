import { Navbar } from '@/widgets';
import BellIcon from '@/assets/icons/Normal/Icon_Bell.svg';

export default function HomeLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className={'w-full bg-main pt-7'}>
      <div className={'flex flex-col px-5 gap-y-4 mb-4'}>
        {/*로고*/}
        <div className={'flex items-center justify-between'}>
          <span className={'font-gangwon text-[17px] text-white font-regular'}>
            Spot it
          </span>
          {/*TODO : 알림기능 구현시 BellIcon을 제거*/}
          <BellIcon fill={'var(--color-main)'} width={28} height={28} />
        </div>
      </div>
      {children}
      <Navbar />
    </div>
  );
}
