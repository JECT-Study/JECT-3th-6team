'use client';

import { Map, MapMarker, MarkerClusterer } from 'react-kakao-maps-sdk';
import { MapPosition } from './types';

interface MarkerClusterMapProps {
  center: MapPosition;
  markers: MapPosition[];
  level?: number;
  style?: React.CSSProperties;
  clustererOptions?: {
    averageCenter?: boolean;
    minLevel?: number;
  };
}

export default function MarkerClusterMap({
  center,
  markers,
  level = 6,
  style = { width: '100%', height: '400px' },
  clustererOptions = { averageCenter: true, minLevel: 10 },
}: MarkerClusterMapProps) {
  return (
    <Map center={center} style={style} level={level}>
      <MarkerClusterer
        averageCenter={clustererOptions.averageCenter}
        minLevel={clustererOptions.minLevel}
      >
        {markers.map((pos, index) => (
          <MapMarker key={`${pos.lat}-${pos.lng}-${index}`} position={pos} />
        ))}
      </MarkerClusterer>
    </Map>
  );
}
