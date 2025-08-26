import React from 'react';
import { Badge } from './Badge';

const StarIcon = () => (
  <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
  </svg>
);

export function BadgeExample() {
  return (
    <div className="space-y-4 p-4">
      <h2 className="text-lg font-semibold">Badge 컴포넌트 예시</h2>

      <div className="space-y-2">
        <h3 className="text-sm font-medium">
          1. 아이콘이 없고 텍스트만 있는 경우
        </h3>
        <div className="flex gap-2">
          <Badge variant="main">예약중</Badge>
          <Badge variant="gray">방문 완료</Badge>
        </div>
      </div>

      <div className="space-y-2">
        <h3 className="text-sm font-medium">2. 아이콘이 좌측에 있는 경우</h3>
        <div className="flex gap-2">
          <Badge icon={<StarIcon />} iconPosition="left" variant="main">
            10일 남음
          </Badge>
        </div>
      </div>
    </div>
  );
}
