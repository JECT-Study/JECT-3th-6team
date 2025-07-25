'use client';

import NotificationType from '@/features/notification/type/Notification';
import IconX from '@/assets/icons/Normal/Icon_X.svg';
import { tv } from 'tailwind-variants';
import formatRelativeTime from '../lib/formatRelativeTime';
import { cn } from '@/lib/utils';
import { HTMLAttributes } from 'react';

interface NotificationCardProps extends HTMLAttributes<HTMLDivElement> {
  data: NotificationType;
  onClose: () => void;
  onClick?: () => void;
  className?: string;
}

export default function NotificationCard({
  data,
  className,
  onClose,
  onClick,
  ...rest
}: NotificationCardProps) {
  const { notificationTitle, message, isRead, createdAt } = data;

  const style = tv({
    base: 'relative rounded-[10px] flex flex-col gap-y-[10px] p-[16px] hover:bg-sub2/20',
    variants: {
      isRead: {
        true: 'border border-gray40',
        false: 'border border-sub shadow-card',
      },
    },
  });

  const renderRelativeTime = formatRelativeTime(createdAt);
  return (
    <div
      className={cn(style({ isRead }), className)}
      {...rest}
      onClick={onClick}
    >
      <div
        className={'absolute top-[16px] right-[16px] cursor-pointer'}
        onClick={e => {
          e.stopPropagation();
          onClose();
        }}
      >
        <IconX width={16} height={16} fill={'var(--color-gray80)'} />
      </div>
      <div className={'flex items-center gap-x-[4px]'}>
        <h3 className={'font-medium text-[14px] select-none'}>
          {notificationTitle}
        </h3>
        <span className={'font-light text-[14px] text-gray60 select-none'}>
          {renderRelativeTime}
        </span>
      </div>
      <p
        className={
          'font-light text-[14px] leading-[1.5] text-gray80 select-none'
        }
      >
        {message}
      </p>
    </div>
  );
}
