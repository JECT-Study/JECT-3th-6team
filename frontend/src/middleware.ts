// src/middleware.ts
import { type NextRequest, NextResponse } from 'next/server';
import { checkDeviceUA } from '@/shared/lib/checkDeviceUA';

export function middleware(req: NextRequest) {
  const { pathname } = req.nextUrl;

  // 루트("/")에서만 작동
  if (pathname === '/') {
    const seen = req.cookies.get('seenLanding')?.value === '1';
    const ua = req.headers.get('user-agent') || '';
    const device = checkDeviceUA(ua);

    if (!seen) {
      const url = req.nextUrl.clone();
      url.pathname = '/landing';
      url.searchParams.set('device', device.toUpperCase());
      return NextResponse.redirect(url);
    }
  }

  return NextResponse.next();
}

// 아래 경로에 미들웨어 적용
export const config = {
  matcher: ['/', '/landing'],
};
