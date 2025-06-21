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
        {children}
      </body>
    </html>
  );
}
