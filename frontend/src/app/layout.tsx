import type { Metadata } from 'next';
import Script from 'next/script';
import './globals.css';
import localFont from 'next/font/local';
import { Toaster } from '@/shared/ui';
import { ReactQueryClientProvider } from '@/shared/lib';
import AuthInitializer from '@/entities/user/lib/AuthInitializer';

export const metadata: Metadata = {
  title: 'Popup App',
  description: 'Popup store reservation app',
};

const pretendard = localFont({
  src: '../../public/fonts/PretendardVariable.woff2',
  display: 'swap',
  weight: '300 700',
  variable: '--font-pretendard',
  fallback: ['Arial', 'sans-serif'],
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  // 참고: https://apis.map.kakao.com/web/guide/#whatlibrary
  const KAKAO_SDK_URL = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.NEXT_PUBLIC_KAKAO_APP_JS_KEY}&libraries=services,clusterer,drawing&autoload=false`;

  return (
    <html lang="kr" className={`${pretendard.variable}`}>
      <body className={`${pretendard.className} `}>
        <ReactQueryClientProvider>
          <Script
            type="text/javascript"
            src={KAKAO_SDK_URL}
            strategy="beforeInteractive"
          />
          <AuthInitializer />
          {/*<AuthInitializer>*/}
          <div className="min-h-screen bg-gray40">
            <div className="w-full min-w-[320px] max-w-[430px] mx-auto bg-white min-h-screen">
              {/* <Header /> */}

              {/* 메인 콘텐츠 영역 */}
              <Toaster position="top-center" richColors />
              <main className="flex-1">{children}</main>

              {/* <BottomNav /> */}
            </div>
          </div>
          {/*</AuthInitializer>*/}
        </ReactQueryClientProvider>
      </body>
    </html>
  );
}
