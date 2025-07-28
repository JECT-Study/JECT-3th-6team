'use client';

import { useSearchParams } from 'next/navigation';

import PageHeader from '@/shared/ui/header/PageHeader';
import KakaoMap from '@/shared/ui/map/KaKaoMap';

export default function DetailMapPage() {
  const searchParams = useSearchParams();
  const lat = Number(searchParams.get('lat'));
  const lng = Number(searchParams.get('lng'));

  return (
    <>
      <div className="w-full h-full">
        <PageHeader title="상세 정보" />
        <KakaoMap
          center={{ lat: lat, lng: lng }}
          level={3}
          className="w-full h-[calc(100vh-55px)]"
        />
      </div>
    </>
  );
}
