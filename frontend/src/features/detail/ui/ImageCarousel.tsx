'use client';

import { useState, useEffect } from 'react';
import Image from 'next/image';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  type CarouselApi,
} from '@/components/ui/carousel';

interface ImageCarouselProps {
  images: string[];
  altText?: string;
  className?: string;
}

export function ImageCarousel({
  images,
  altText,
  className,
}: ImageCarouselProps) {
  // altText가 없거나 빈 문자열인 경우 기본값 사용
  const defaultAltText = altText || '팝업 스토어 이미지';
  const [api, setApi] = useState<CarouselApi>();
  const [current, setCurrent] = useState(0);

  useEffect(() => {
    if (!api) {
      return;
    }

    setCurrent(api.selectedScrollSnap() + 1);

    api.on('select', () => {
      setCurrent(api.selectedScrollSnap() + 1);
    });
  }, [api]);

  return (
    <div className="relative">
      <Carousel setApi={setApi} className="w-full">
        <CarouselContent>
          {images.map((image, index) => (
            <CarouselItem key={index}>
              <div
                className={`relative w-full h-[300px] overflow-hidden ${className || ''}`}
              >
                <Image
                  src={image}
                  alt={`${defaultAltText} ${index + 1}`}
                  fill
                  className="object-cover"
                />
              </div>
            </CarouselItem>
          ))}
        </CarouselContent>
      </Carousel>

      {/* Image Indicators */}
      <div className="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-1">
        {images.map((_, index) => (
          <div
            key={index}
            className={`w-2 h-2 rounded-full transition-colors ${
              index === current - 1 ? 'bg-main' : 'bg-white'
            }`}
          />
        ))}
      </div>
    </div>
  );
}
