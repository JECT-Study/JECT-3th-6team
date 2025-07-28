'use client';

import useInitUser from '@/entities/user/hook/useInitUser';
import React from 'react';

export default function UserInitProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  useInitUser();
  return <>{children}</>;
}
