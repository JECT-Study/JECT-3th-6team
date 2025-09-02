'use client';

import { useEffect } from 'react';
import TagManager from 'react-gtm-module';

export default function GTMInit() {
  useEffect(() => {
    TagManager.initialize({
      gtmId: process.env.NEXT_PUBLIC_GA_ID!,
    });
  }, []);
  return null;
}
