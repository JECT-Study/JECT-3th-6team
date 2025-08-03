import { useEffect, useRef } from 'react';

export function useIntersectionObserver(
  onIntersect: () => void,
  options?: IntersectionObserverInit
) {
  const ref = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!ref.current) return;

    const observer = new IntersectionObserver(([entry]) => {
      if (entry.isIntersecting) {
        onIntersect();
      }
    }, options);

    observer.observe(ref.current);

    return () => {
      observer.disconnect();
    };
  }, [onIntersect, options]);

  return ref;
}
