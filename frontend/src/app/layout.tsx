import type { Metadata } from 'next';
import Script from 'next/script';
import './globals.css';
import localFont from 'next/font/local';
import { ReactQueryClientProvider } from '@/shared/lib';
import AuthProvider from '@/features/auth/lib/AuthProvider';

export const metadata: Metadata = {
  title: '스팟잇 Spot It!',
  description: '지금, 이 순간의 핫플을 스팟잇!',
  openGraph: {
    title: '스팟잇 Spot It!',
    description: '지금, 이 순간의 핫플을 스팟잇!',
    images: [
      {
        url: '/images/og.svg',
        width: 1200,
        height: 630,
        alt: '스팟잇 Spot It!',
      },
    ],
    type: 'website',
  },
  twitter: {
    card: 'summary_large_image',
    title: '스팟잇 Spot It!',
    description: '지금, 이 순간의 핫플을 스팟잇!',
    images: ['/images/open_graph.svg'],
  },
};

const pretendard = localFont({
  src: '../../public/fonts/PretendardVariable.woff2',
  display: 'swap',
  weight: '300 700',
  variable: '--font-pretendard',
  fallback: ['Arial', 'sans-serif'],
});

const GA_ID = process.env.NEXT_PUBLIC_GA_ID ?? '';

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  // 참고: https://apis.map.kakao.com/web/guide/#whatlibrary
  const KAKAO_SDK_URL = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.NEXT_PUBLIC_KAKAO_APP_JS_KEY}&libraries=services,clusterer,drawing&autoload=false`;

  return (
    <html lang="ko" className={`${pretendard.variable}`}>
      <head>
        {/* Google Tag Manager */}
        <Script id="gtm-loader" strategy="afterInteractive">
          {`
            (function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
            new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
            j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
            'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
            })(window,document,'script','dataLayer','${GA_ID}');
          `}
        </Script>
      </head>
      <body className={`${pretendard.className} `}>
        {/* Google Tag Manager (noscript) */}
        <noscript>
          <iframe
            src={`https://www.googletagmanager.com/ns.html?id=${GA_ID}`}
            height="0"
            width="0"
            style={{ display: 'none', visibility: 'hidden' }}
          />
        </noscript>
        <ReactQueryClientProvider>
          <Script
            type="text/javascript"
            src={KAKAO_SDK_URL}
            strategy="beforeInteractive"
          />

          <AuthProvider>{children}</AuthProvider>
        </ReactQueryClientProvider>
      </body>
    </html>
  );
}
