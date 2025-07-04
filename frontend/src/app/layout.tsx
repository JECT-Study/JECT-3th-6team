import type { Metadata } from "next";
import "./globals.css";
import localFont from 'next/font/local';

export const metadata: Metadata = {
  title: "Popup App",
  description: "Popup store reservation app",
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
  return (
      <html lang="kr" className={`${pretendard.variable}`}>
      <body className={`${pretendard.className} `}>
        <div className="min-h-screen bg-gray40">
          <div className="w-full min-w-[320px] max-w-[430px] mx-auto bg-white min-h-screen">
            {/* <Header /> */}
            
            {/* 메인 콘텐츠 영역 */}
            <main className="flex-1">
              {children}
            </main>

            {/* <BottomNav /> */}
          </div>
        </div>
      </body>
    </html>
  );
}
