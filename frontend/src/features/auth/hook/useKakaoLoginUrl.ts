import { useEffect, useState } from 'react';

export function useKakaoLoginUrl({
  clientId,
  redirectUri,
}: {
  clientId: string;
  redirectUri: string;
}) {
  const [url, setUrl] = useState('');

  useEffect(() => {
    if (typeof window === 'undefined') return;

    const params = new URLSearchParams(window.location.search);
    const redirectPathAfterLogin = params.get('redirect_path') ?? '/';

    const kakaoUrl =
      `https://kauth.kakao.com/oauth/authorize` +
      `?client_id=${clientId}` +
      `&redirect_uri=${encodeURIComponent(redirectUri)}` +
      `&response_type=code` +
      `&state=${encodeURIComponent(redirectPathAfterLogin)}`;

    setUrl(kakaoUrl);
  }, [clientId, redirectUri]);

  return url;
}
