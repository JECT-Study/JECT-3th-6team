import React from 'react'
import { Tag } from './Tag'

export function TagExample() {
  return (
    <div className="space-y-4 p-4">
      <h2 className="text-lg font-semibold">Tag 컴포넌트 예시</h2>
      
      <div className="space-y-2">
        <h3 className="text-sm font-medium">기본 태그 사용</h3>
        <div className="flex flex-wrap">
          <Tag>수도권</Tag>
          <Tag>패션</Tag>
          <Tag>뷰티</Tag>
          <Tag>체험형</Tag>
        </div>
      </div>
    </div>
  )
} 