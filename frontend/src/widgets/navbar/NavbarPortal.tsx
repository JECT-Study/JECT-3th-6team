'use client';

import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import { Navbar } from '@/widgets';

function NavbarPortal() {
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
    return () => setMounted(false);
  }, []);

  return mounted ? createPortal(<Navbar />, document.body) : null;
}
