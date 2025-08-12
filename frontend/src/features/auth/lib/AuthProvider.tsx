'use client';

import API from '@/shared/lib/API';
import React, { useEffect } from 'react';
import { handleUnauthorized } from '@/shared/lib/handleUnauthorized';

export default function AuthProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  useEffect(() => {
    API.onUnauthorized = handleUnauthorized;
    return () => {
      API.onUnauthorized = undefined;
    };
  }, []);

  return <>{children}</>;
}
