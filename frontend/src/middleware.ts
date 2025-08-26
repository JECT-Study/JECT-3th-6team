// src/middleware.ts
import { type NextRequest, NextResponse } from 'next/server';
import { isDesktopUA } from '@/shared/lib/isDesktopUA';

export function middleware(req: NextRequest) {
  const { pathname } = req.nextUrl;

  // 루트("/")에서만 작동
  if (pathname === '/') {
    const seen = req.cookies.get('seenLanding')?.value === '1';
    const ua = req.headers.get('user-agent') || '';
    const isDesktop = isDesktopUA(ua);

    if (!seen && isDesktop) {
      const url = req.nextUrl.clone();
      url.pathname = '/landing';
      return NextResponse.redirect(url);
    }
  }

  // 모바일/태블릿은 랜딩 접근 금지 (직접 접근 방어)
  if (pathname === '/landing') {
    const ua = req.headers.get('user-agent') || '';
    const isDesktop = isDesktopUA(ua);
    if (!isDesktop) {
      const url = req.nextUrl.clone();
      url.pathname = '/';
      return NextResponse.redirect(url);
    }
  }

  return NextResponse.next();
}

// 아래 경로에 미들웨어 적용
export const config = {
  matcher: ['/', '/landing'],
};
