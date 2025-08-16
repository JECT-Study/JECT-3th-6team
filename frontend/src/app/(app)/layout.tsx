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
          'fixed hidden [@media(min-width:1289px)]:flex flex-col gap-y-[25px] justify-center items-center top-1/2 left-[calc((100vw-430px)/4)] -translate-x-1/2  -translate-y-1/2 opacity-50 '
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
      <div
        className={
          'fixed hidden [@media(min-width:1289px)]:flex justify-center items-center top-1/2 right-[calc((100vw-430px)/4)] gap-x-[10px] translate-x-1/2  -translate-y-1/2'
        }
      >
        <Image
          src={'/qr/feedback.svg'}
          width={114}
          height={114}
          alt={'qr feedback'}
          className={'w-[114px] h-[114px]'}
        />
        <div className={'flex flex-col gap-y-[12px] px-[5px] select-none '}>
          <p className={'text-gray80 font-semibold text-[20px]'}>
            스팟잇 서비스에 대한 <br /> 의견을 제시해주세요
          </p>
          <span className={'font-semibold text-[16px] text-gray60'}>
            QR 스캔하기
          </span>
        </div>
      </div>
      <div className="w-full min-w-[320px] max-w-[430px] mx-auto bg-white min-h-screen shadow-main">
        <Toaster position="top-center" richColors />
        <main className="flex-1">{children}</main>
      </div>
    </div>
  );
}
