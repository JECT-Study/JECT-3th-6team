import React from 'react'
import { Badge } from './Badge'

const StarIcon = () => (
  <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
  </svg>
)

const HeartIcon = () => (
  <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
  </svg>
)

export function BadgeExample() {
  return (
    <div className="space-y-4 p-4">
      <h2 className="text-lg font-semibold">Badge 컴포넌트 예시</h2>
      
      <div className="space-y-2">
        <h3 className="text-sm font-medium">1. 아이콘이 없고 텍스트만 있는 경우</h3>
        <div className="flex gap-2">
          <Badge variant="main">예약중</Badge>
          <Badge variant="gray">방문 완료</Badge>
        </div>
      </div>

      <div className="space-y-2">
        <h3 className="text-sm font-medium">2. 아이콘이 좌측에 있는 경우</h3>
        <div className="flex gap-2">
          <Badge icon={<StarIcon />} iconPosition="left" variant="main">10일 남음</Badge>
        </div>
      </div>
    </div>
  )
} 