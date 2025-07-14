import React from "react"
import { Chip } from "./Chip"

export function ChipExample() {
  return (
    <div className="space-y-4 p-4">
      <h2 className="text-lg font-semibold">Chip 컴포넌트 예시</h2>  
      <div className="space-y-2">
        <h3 className="text-sm font-medium">1. 기본 Variants</h3>
        <div className="flex gap-2">
          <Chip variant="white">white</Chip>
          <Chip variant="mainLight">mainLight</Chip>
          <Chip variant="main">main</Chip>
        </div>
      </div>

      <div className="space-y-2">
        <h3 className="text-sm font-medium">3. 아이콘이 우측에 있는 경우</h3>
        <div className="flex gap-2">
          <Chip variant="mainLight" iconPosition="right">삭제</Chip>
        </div>
      </div>
    </div>
  )
}
