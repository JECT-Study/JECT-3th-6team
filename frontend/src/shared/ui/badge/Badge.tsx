import * as React from "react"
import { Slot } from "@radix-ui/react-slot"
import { cva, type VariantProps } from "class-variance-authority"
import Image from "next/image"

import { cn } from "@/lib/utils"

export const badgeVariants = cva(
  "inline-flex items-center justify-center gap-1 px-2 py-1 text-xs font-medium w-fit whitespace-nowrap shrink-0 text-white rounded-md leading-none",
  {
    variants: {
      variant: {
        main: "bg-main",
        gray: "bg-gray80",
      },
    },
    defaultVariants: {
      variant: "main",
    },
  }
)

export interface BadgeProps extends React.ComponentProps<"span">,
  VariantProps<typeof badgeVariants> {
  asChild?: boolean
  icon?: React.ReactNode
  iconPosition?: 'none' | 'left'
}

export function Badge({
  className,
  variant,
  asChild = false,
  icon,
  iconPosition = 'none',
  children,
  ...props
}: BadgeProps) {
  const Comp = asChild ? Slot : "span"

  const renderContent = () => {
    if (!icon && iconPosition === 'none') {
      return children
    }

    if (!icon && iconPosition === 'left') {
      const clockIcon = (
        <Image
          src="/icons/Normal/Icon_Clock.svg"
          alt="clock"
          width={12}
          height={12}
          className="w-3 h-3 brightness-0 invert"
        />
      )

      return (
        <>
          {clockIcon}
          {children}
        </>
      )
    }

    return children
  }

  return (
    <Comp
      data-slot="chip"
      className={cn(badgeVariants({ variant }), className)}
      {...props}
    >
      {renderContent()}
    </Comp>
  )
}