import { NextResponse } from 'next/server';

export const runtime = 'edge';

export async function GET() {
  const target =
    process.env.NEXT_PUBLIC_FEEDBACK_TARGET_URL ??
    'https://forms.gle/placeholder';

  const res = NextResponse.redirect(target, { status: 302 });
  res.headers.set('Cache-Control', 'no-store');
  return res;
}
