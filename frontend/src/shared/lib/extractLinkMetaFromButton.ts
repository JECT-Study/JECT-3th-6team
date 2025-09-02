'use client';

import React from 'react';

/** 버튼 클릭 이벤트에서 link_text / link_url 추출 */
export default function extractLinkMetaFromButton(
  e: React.MouseEvent<HTMLButtonElement>
): { text: string; url: string } {
  const btn = e.currentTarget; // HTMLButtonElement

  // 라벨 추출: data-gtm-text > aria-label > textContent > value > title
  const text =
    btn.getAttribute('data-gtm-text') ??
    btn.getAttribute('aria-label') ??
    (btn.textContent?.trim().replace(/\s+/g, ' ') || undefined) ??
    (btn.value?.trim() || undefined) ??
    btn.getAttribute('title') ??
    '';

  // URL 추출: data-gtm-url > 가장 가까운 a[href] > 현재 페이지
  const explicit = btn.getAttribute('data-gtm-url') ?? undefined;
  const closestA = btn.closest('a[href]') as HTMLAnchorElement | null;
  const url =
    explicit ??
    closestA?.href ??
    (typeof window !== 'undefined' ? window.location.href : '');

  return { text, url };
}
