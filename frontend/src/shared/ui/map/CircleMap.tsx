'use client';

import { Map, Circle } from 'react-kakao-maps-sdk';
import type { CircleProps, _MapProps } from 'react-kakao-maps-sdk';

interface CircleMapProps extends _MapProps {
  circleOptions?: CircleProps;
}

export default function CircleMap({
  center,
  level = 6,
  minLevel,
  maxLevel,
  onClick,
  circleOptions = {
    center: center as { lat: number; lng: number },
    radius: 200,
    strokeWeight: 4,
    strokeColor: '#75B8FA',
    strokeOpacity: 2,
    strokeStyle: 'solid',
    fillColor: '#CFE7FF',
    fillOpacity: 0.7,
  },
  ...props
}: CircleMapProps) {
  return (
    <Map
      center={center}
      level={level}
      maxLevel={maxLevel}
      minLevel={minLevel}
      draggable={false}
      onClick={onClick}
      className="w-full h-30 rounded-[10px]"
      {...props}
    >
      <Circle {...circleOptions} />
    </Map>
  );
}
