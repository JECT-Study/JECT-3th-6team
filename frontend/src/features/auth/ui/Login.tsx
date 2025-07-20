'use client';

import { useKakaoLoginUrl } from '@/features/auth/hook/useKakaoLoginUrl';
import KakaoLoginButton from '@/features/auth/ui/KakaoLoginButton';

export default function LoginPage() {
  const kakaoLoginUrl = useKakaoLoginUrl({
    clientId: process.env.NEXT_PUBLIC_CLIENT_ID!,
    redirectUri: process.env.NEXT_PUBLIC_REDIRECT_URL!,
  });

  const handleLogin = () => {
    if (kakaoLoginUrl) {
      window.location.href = kakaoLoginUrl;
    }
  };

  return (
    <div className={'w-full h-full mt-[30%]'}>
      <div className={'flex flex-col justify-center items-center gap-y-8'}>
        <h1 className={'flex flex-col gap-y-[1px]'}>
          <span className="text-main text-center font-gangwon font-bold text-3xl">
            스팟잇
          </span>
          <span className="font-gangwon text-center text-gray80 font-medium text-base">
            spot it!
          </span>
        </h1>

        <p className="text-center text-[20px] font-regular text-gray80 select-none mb-4">
          간편하게 로그인 하고 <br /> 다양한 서비스를 이용해보세요!
        </p>

        <KakaoLoginButton onClick={handleLogin} disabled={!kakaoLoginUrl} />
      </div>
    </div>
  );
}
