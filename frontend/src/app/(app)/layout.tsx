import { Toaster } from '@/shared/ui';
import Image from 'next/image';

export default function AppShellLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-gray40 relative ">
      <Image
        src={'/images/landing/LANDING_LOGO_SMALL.svg'}
        width={106}
        height={36}
        alt={'small-logo'}
        className={'fixed top-[30px] left-[30px] hidden md:block '}
      />
      <div
        className={
          'fixed hidden [@media(min-width:1289px)]:flex flex-col gap-y-[25px] justify-center items-center top-1/2 left-[calc((100vw-320px)/4)] -translate-x-1/2  -translate-y-1/2 opacity-50 '
        }
      >
        <Image
          src={'/images/landing/LANDING_LOGO_BIG.svg'}
          width={130}
          height={170}
          alt={'logo-big '}
        />
        <p className={'text-[20px] text-gray60 font-semibold'}>
          지금, 이 순간의 핫플을 스팟잇! &quot; Spot it! &quot;
        </p>
      </div>
      <div className="w-full min-w-[320px] max-w-[430px] mx-auto bg-white min-h-screen">
        <Toaster position="top-center" richColors />
        <main className="flex-1">{children}</main>
      </div>
    </div>
  );
}
