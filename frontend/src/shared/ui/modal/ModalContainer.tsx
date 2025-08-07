'use client';
import { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import { cn } from '@/lib/utils';

interface ModalProps extends React.PropsWithChildren {
  open: boolean;
  className?: string;
}

export default function ModalContainer({
  open,
  className,
  children,
}: ModalProps) {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) return null;

  return ReactDOM.createPortal(
    <>
      {open ? (
        // 모달 컨테이너
        <div
          className={cn(
            'fixed top-0 left-0 w-full h-full flex justify-center items-center bg-modal-overlay z-100 backdrop-blur-[2px]',
            className
          )}
        >
          {/* 모달 내용 */}
          <div>{children}</div>
        </div>
      ) : null}
    </>,
    document.body
  );
}
