'use client';

import { Map, Circle } from 'react-kakao-maps-sdk';
import { MapPosition } from './types';

interface CircleMapProps {
  center: MapPosition;
  radius?: number;
  level?: number;
  minLevel?: number;
  maxLevel?: number;
  style?: React.CSSProperties;
  onClick?: (map: kakao.maps.Map, mouseEvent: kakao.maps.MouseEvent) => void;
  circleOptions?: {
    strokeWeight?: number;
    strokeColor?: string;
    strokeOpacity?: number;
    strokeStyle?: kakao.maps.StrokeStyles;
    fillColor?: string;
    fillOpacity?: number;
  };
}

export default function CircleMap({
  center,
  radius = 200,
  level = 6,
  minLevel,
  maxLevel,
  style = { width: '100%', height: '120px', borderRadius: '10px' },
  onClick,
  circleOptions = {
    strokeWeight: 4,
    strokeColor: '#75B8FA',
    strokeOpacity: 2,
    strokeStyle: 'solid',
    fillColor: '#CFE7FF',
    fillOpacity: 0.7,
  },
}: CircleMapProps) {
  return (
    <Map
      center={center}
      style={style}
      level={level}
      maxLevel={maxLevel}
      minLevel={minLevel}
      draggable={false}
      onClick={onClick}
    >
      <Circle
        center={center}
        radius={radius}
        strokeWeight={circleOptions.strokeWeight}
        strokeColor={circleOptions.strokeColor}
        strokeOpacity={circleOptions.strokeOpacity}
        strokeStyle={circleOptions.strokeStyle}
        fillColor={circleOptions.fillColor}
        fillOpacity={circleOptions.fillOpacity}
      />
    </Map>
  );
}
