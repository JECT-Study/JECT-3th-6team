'use client';

import { Map, MapMarker } from 'react-kakao-maps-sdk';

export default function KakaoMap() {
  return (
    <>
      <Map
        center={{ lat: 37.5353, lng: 127.008 }} // 한남동 61-2
        style={{ width: '100%', height: '180px', borderRadius: '10px' }}
        level={3}
      >
        <MapMarker position={{ lat: 37.5353, lng: 127.008 }} />
      </Map>
    </>
  );
}
